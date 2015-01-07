/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.PertinenceBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 */
public class PertinenceDAOImpl implements PertinenceDAO{

    private static final String PERTINENCE_CREATE =
            "INSERT INTO Pertinence (doc_id, request_id, pertinence) "
          + "VALUES (?, ?, ?)";
    
    private static final String READ_PERTINENCE =
            "SELECT p.id,d.doc_name,r.req_name,p.pertinence "
          + "FROM REQUESTS r, DOCUMENTS d, PERTINENCE p "
          + "WHERE r.req_name=? AND d.doc_name=? "
          + "AND r.request_id = p.request_id AND p.doc_id = d.doc_id";
    
    private static final String DELETE_TABLE = 
            "DELETE FROM PERTINENCE";
    
    private static final String DELETE_PERTINENCE = 
            "DELETE FROM PERTINENCE "
          + "WHERE request_id=? AND doc_id=?";
    
    private final DocumentDAO documentDAO;
    private final RequestDAO requestDAO;
    
    public PertinenceDAOImpl() {
        documentDAO = new DocumentDAOImpl();
        requestDAO = new RequestDAOImpl();
    }
    
    public void createPertinence(PertinenceBean pertinence) {
        int docID = documentDAO.readDocumentByName(pertinence.getDocumentName()).getId();
        int requestID = requestDAO.readRequestByName(pertinence.getRequest()).getId();
        DAOUtilities.executeCreate(PERTINENCE_CREATE,
                                   docID,
                                   requestID,
                                   pertinence.getPertinence());
    }

    public PertinenceBean readPertinence(String requestName, String filename) {
        System.out.println("request : " + requestName + " filename : " + filename);
        ResultSet rs = DAOUtilities.executeQuery(READ_PERTINENCE, requestName, filename);
        PertinenceBean pertinence = null;
        try {
            if(rs.next()) {
                pertinence = map(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(rs);
        }
        return pertinence;
    }
    
    private PertinenceBean map(ResultSet rs)throws SQLException{
        PertinenceBean pertinence = new PertinenceBean();
        pertinence.setDocumentName(rs.getString("doc_name"));
        pertinence.setRequest(rs.getString("req_name"));
        pertinence.setPertinence(rs.getDouble("pertinence"));
        return pertinence;
    }
    
    public void deleteTable() {
        DAOUtilities.executeDelete(DELETE_TABLE);
    }
    
    public void deletePertinence(String requestName, String filename) {
        int docId = documentDAO.readDocumentByName(filename).getId();
        int requestId = requestDAO.readRequestByName(requestName).getId();
        DAOUtilities.executeDelete(DELETE_PERTINENCE, requestId, docId);
    }

    
}
