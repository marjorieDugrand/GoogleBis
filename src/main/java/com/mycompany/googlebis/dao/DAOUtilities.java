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
    
    public static Connection getConnection() throws SQLException {
        String driver = "org.apache.derby.jdbc.ClientDriver";
        try {
                    Class.forName(driver).newInstance();
            } catch (Exception ex) {
                    System.err.println("fuuuuuuuuuuuck");
                }
        Connection con = DriverManager.getConnection(urlBase,username,password);
        System.out.println("connected"); 
        return con;
    }
    
    public static PreparedStatement initializePreparedStatement(Connection connection,
                                                                String sqlRequest,
                                                                boolean returnGeneratedKeys,
                                                                Object... objects )
            throws SQLException {
        PreparedStatement preparedStatement
                = connection.prepareStatement(sqlRequest,
                                              getGeneratedKeyParameter(returnGeneratedKeys));
        for ( int i = 0; i < objects.length; i++ ) {
            preparedStatement.setObject( i + 1, objects[i] );
        }
        return preparedStatement;
    }

    private static int getGeneratedKeyParameter(boolean returnGeneratedKeys) {
        if (returnGeneratedKeys) {
            return Statement.RETURN_GENERATED_KEYS;
        } else {
            return Statement.NO_GENERATED_KEYS;
        }
    }
    /**
     * Close a ResultSet
     * @param resultSet ResultSet that has to be closed
     */
    public static void silentClose(ResultSet resultSet) {
        if ( resultSet != null ) {
            try {
                resultSet.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture du ResultSet : " + e.getMessage() );
            }
        }
    }

    /**
     * Close a Statement
     * @param statement Statement that has to be closed
     */
    public static void silentClose( Statement statement ) {
        if ( statement != null ) {
            try {
                statement.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture du Statement : " + e.getMessage() );
            }
        }
    }

    /**
     * Close a Connection
     * @param connection Connection that has to be closed
     */
    public static void silentClose( Connection connection ) {
        if ( connection != null ) {
            try {
                connection.close();
            } catch ( SQLException e ) {
                System.out.println( "Échec de la fermeture de la connexion : " + e.getMessage() );
            }
        }
    }

    /**
     * Silent closing of a Statement and a Connection
     * @param statement Statement that has to be closed
     * @param connection Connection that has to be closed
     */
    public static void silentClose( Statement statement, Connection connection ) {
        silentClose( statement );
        silentClose( connection );
    }

    /**
     * Silent closing of a ResultSet, a Statement and a Connection
     * @param resultSet ResultSet that has to be closed
     * @param statement Statement that has to be closed
     * @param connection Connection that has to be closed
     */
    public static void silentClose( ResultSet resultSet, Statement statement, Connection connection ) {
        silentClose( resultSet );
        silentClose( statement );
        silentClose( connection );
    }
    
    public static ResultSet executeQuery(String sqlQuery, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement =
                    initializePreparedStatement(connection,sqlQuery,false,parameters);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
                Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            silentClose(preparedStatement, connection);
        }
        return resultSet;
    }
    
    public static void executeUpdate(String sqlUpdate, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement =
                initializePreparedStatement(connection,sqlUpdate,true,parameters);
            int statut = preparedStatement.executeUpdate();
            if ( statut == 0 ) {
                throw new DAOException( "Update " + sqlUpdate + " failed, no line added in database." );
            }
        } catch (SQLException ex) {
                Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(preparedStatement, connection);
        }
    }
}