/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.DocumentBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 */
public class DocumentDAOImpl implements DocumentDAO {

    private static final String DOCUMENT_CREATE =
            "INSERT INTO Documents (doc_name, link) "
          + "VALUES (?, ?)";
    
    private static final String READ_DOCUMENT_BY_NAME =
            "SELECT doc_id,doc_name,link "
          + "FROM documents "
          + "WHERE doc_name = ?";
    
    private static final String DOCUMENT_DELETE = 
            "DELETE FROM DOCUMENTS "
          + "WHERE doc_name = ?";
    
    private static final String DELETE_TABLE =
            "DELETE FROM DOCUMENTS";
        
    private static final String DOCUMENTS_COUNT = 
            "SELECT COUNT(*) as DOCCOUNT FROM DOCUMENTS";
    
    public void createDocument(DocumentBean document) {
        DAOUtilities.executeCreate(DOCUMENT_CREATE,
                                   document.getName(),
                                   document.getLink());
    }

    public void createDocuments(List<DocumentBean> documents) {
        for(DocumentBean document: documents) {
            createDocument(document);
        }
    }

    public DocumentBean readDocumentByName(String name) {
        DocumentBean document = null;
        ResultSet resultSet = DAOUtilities.executeQuery(READ_DOCUMENT_BY_NAME, name);
        try {
            if(resultSet.next() ) {
                document = map(resultSet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(resultSet);
        }
        return document;
    }
    
    public List<DocumentBean> recoverDocuments(ResultSet rs) {
        List<DocumentBean> documents = new ArrayList<DocumentBean>();
        try {
            while(rs.next()) {
                DocumentBean document = map(rs);
                documents.add(document);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DocumentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
    private DocumentBean map(ResultSet rs) throws SQLException {     
        DocumentBean document = new DocumentBean();
        document.setId(rs.getInt("doc_id"));
        document.setName(rs.getString("doc_name"));
        document.setLink(rs.getString("link"));
        return document;
    }

    public void deleteDocumentByName(String name) {
        DAOUtilities.executeDelete(DOCUMENT_DELETE, name);
    }

    public void deleteTable() {
        DAOUtilities.executeDelete(DELETE_TABLE);
    }
    
    public int getCorpusSize() {
        int size = 0;
        ResultSet resultSet = DAOUtilities.executeQuery(DOCUMENTS_COUNT);
        try {
            if(resultSet.next() ) {
                size = resultSet.getInt("DOCCOUNT");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(resultSet);
        }
        return size;
    }
}
