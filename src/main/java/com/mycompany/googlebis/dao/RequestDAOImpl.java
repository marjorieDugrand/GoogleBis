/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.RequestBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 */
public class RequestDAOImpl implements RequestDAO{

    private static final String REQUEST_CREATE =
            "INSERT INTO Requests (req_name, text) "
          + "VALUES (?, ?)";
    
    private static final String READ_REQUEST_BY_NAME =
            "SELECT request_id,req_name,text "
          + "FROM Requests "
          + "WHERE req_name=?";
    
    private static final String DELETE_TABLE = 
            "DELETE FROM REQUESTS";
    
    private static final String DELETE_FROM_NAME = 
            "DELETE FROM REQUESTS "
          + "WHERE req_name=?";
    
    public void createRequest(RequestBean request) {
        DAOUtilities.executeCreate(REQUEST_CREATE, request.getName(), request.getText());
    }

    public RequestBean readRequestByName(String name) {
        
        ResultSet rs = DAOUtilities.executeQuery(READ_REQUEST_BY_NAME, name);
        RequestBean request = null;
        try {
            if(rs.next()) {
                request = map(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(rs);
        }
        return request;
    }
    
    private RequestBean map(ResultSet rs)throws SQLException{
        RequestBean request = new RequestBean();
        request.setId(rs.getInt("request_id"));
        request.setName(rs.getString("req_name"));
        request.setText(rs.getString("text"));
        return request;
    }

    public void deleteTable() {
        DAOUtilities.executeDelete(DELETE_TABLE);
    }

    public void deleteRequestByName(String requestName) {
        DAOUtilities.executeDelete(DELETE_FROM_NAME, requestName);
    }
}
