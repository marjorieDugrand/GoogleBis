/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 */
public class DAOUtilities {
    
    private static final String urlBase = "jdbc:derby://localhost:1527/googleBis";
    private static final String username = "google";
    private static final String password = "google";
    private static final String driver = "org.apache.derby.jdbc.ClientDriver";
    
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    
    private static void initializeConnection(){
        if(connection == null) {
            try {
                Class.forName(driver).newInstance();
                connection = DriverManager.getConnection(urlBase,username,password);
            } catch (SQLException e1) {
                throw new RuntimeException("connection problem", e1);
            } catch (Exception e2) {
                throw new RuntimeException("driver problem", e2);
            }
        }
    }
    
    private static void initializePreparedStatement(String sqlRequest,
                                                    boolean returnGeneratedKeys,
                                                    Object... objects ) throws SQLException {
        preparedStatement
                = connection.prepareStatement(sqlRequest,
                                              getGeneratedKeyParameter(returnGeneratedKeys));
        for ( int i = 0; i < objects.length; i++ ) {
            preparedStatement.setObject( i + 1, objects[i] );
        }
    }

    private static int getGeneratedKeyParameter(boolean returnGeneratedKeys) {
        if (returnGeneratedKeys) {
            return Statement.RETURN_GENERATED_KEYS;
        } else {
            return Statement.NO_GENERATED_KEYS;
        }
    }

    public static void silentClose(ResultSet resultSet) {
        if ( resultSet != null ) {
            try {
                preparedStatement.close();
                resultSet.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture : " + e.getMessage() );
            }
        }
    }

    private static void silentClose() {
        try {
            preparedStatement.close();
        } catch ( SQLException e ) {
            System.out.println( "Échec de la fermeture : " + e.getMessage() );
        }
    }
    
    public static ResultSet executeQuery(String sqlQuery, Object... parameters) {
        ResultSet resultSet = null;
        try {
            initializeConnection();
            initializePreparedStatement(sqlQuery,false,parameters);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
                Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
    
    private static void executeUpdate(String sqlUpdate, boolean keyGenerated, Object... parameters) {
        try {
            initializeConnection();
            initializePreparedStatement(sqlUpdate,keyGenerated,parameters);
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Update " + sqlUpdate + " failed, no line added in database." );
            }
        } catch (SQLException ex) {
                throw new DAOException(ex);
        } finally {
            silentClose();
        }
    }
    
    public static void executeDelete(String sqlDelete, Object... parameters) {
        try {
            executeUpdate(sqlDelete, false, parameters);
        } catch(DAOException exc) {
            System.out.println("object already deleted");
        }
    }
    
    public static void executeCreate(String sqlCreate, Object... parameters) {
        executeUpdate(sqlCreate, true, parameters);
    }
}
