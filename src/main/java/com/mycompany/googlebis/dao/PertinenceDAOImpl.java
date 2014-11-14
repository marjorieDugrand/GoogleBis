/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.PertinenceBean;
import com.mycompany.googlebis.beans.RequestBean;
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
            "INSERT INTO Pertinence (doc_id, request_id) "
          + "VALUES (?, ?)";
    
    private static final String READ_PERTINENCE_BY_FILENAME =
            "SELECT p.id,d.name,r.name "
          + "FROM REQUESTS r, DOCUMENTS d, PERTINENCE p "
          + "WHERE r.name=? AND r.request_id = p.request_id AND p.doc_id = d.doc_id";
    
    private final DocumentDAO documentDAO;
    private final RequestDAO requestDAO;
    
    public PertinenceDAOImpl() {
        documentDAO = new DocumentDAOImpl();
        requestDAO = new RequestDAOImpl();
    }
    
    public void createPertinence(PertinenceBean pertinence) {
        int docID = documentDAO.readDocumentByName(pertinence.getDocumentName()).getId();
        int requestID = requestDAO.readRequestByName(pertinence.getRequest()).getId();
        DAOUtilities.executeCreate(PERTINENCE_CREATE, docID, requestID);
    }

    public PertinenceBean readPertinenceByFileName(String filename) {
        
        ResultSet rs = DAOUtilities.executeQuery(READ_PERTINENCE_BY_FILENAME, filename);
        PertinenceBean pertinence = null;
        try {
            if(rs.next()) {
                pertinence = map(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pertinence;
    }
    
    private PertinenceBean map(ResultSet rs)throws SQLException{
        PertinenceBean pertinence = new PertinenceBean();
        pertinence.setDocumentName(rs.getString("d.name"));
        pertinence.setRequest(rs.getString("r.name"));
        pertinence.setPertinence(rs.getInt("pertinence"));
        return pertinence;
    }
    
}
