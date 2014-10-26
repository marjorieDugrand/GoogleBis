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
            "INSERT INTO Documents (name, link) "
          + "VALUES (?, ?)";
    
    private static final String READ_DOCUMENT_BY_NAME =
            "SELECT doc_id,name,link "
          + "FROM documents "
          + "WHERE name = ?";
        
    public void createDocument(DocumentBean document) {
        DAOUtilities.executeUpdate(DOCUMENT_CREATE,
                                   document.getName(),
                                   document.getLink());
    }

    public void createDocuments(List<DocumentBean> documents) {
        for(DocumentBean document: documents) {
            createDocument(document);
        }
    }

    public List<DocumentBean> readDocumentByName(String name) {
        List<DocumentBean> documents = new ArrayList<DocumentBean>();
        ResultSet resultSet = DAOUtilities.executeQuery(READ_DOCUMENT_BY_NAME, name);
        try {
            while(resultSet.next() ) {
                DocumentBean document = map(resultSet);
                documents.add(document);
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
    
    private DocumentBean map(ResultSet rs) throws SQLException {     
        DocumentBean document = new DocumentBean();
        document.setId(rs.getInt("doc_id"));
        document.setName(rs.getString("name"));
        document.setLink(rs.getString("link"));
        return document;
    }
}
