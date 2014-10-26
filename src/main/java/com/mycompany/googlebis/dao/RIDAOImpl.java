/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.beans.WordBean;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public class RIDAOImpl implements RIDAO {
    
    private final DAOUtilities daoUtilities;

    private static final String WORD_DOC_LINK_CREATE =
            "INSERT INTO ContainedIn (word_id, doc_id, weight) "
            + "VALUES (?, ?, ?)";
    
    private static final String READ_DOCUMENTS_BY_WORD_CONTAINED = 
            "SELECT d.name, d.link, c.weight "
          + "FROM DOCUMENTS d, WORDS w, CONTAINEDIN c "
          + "WHERE w.word = ? "
          + "AND c.word_id = w.word_id "
          + "AND c.doc_id = d.doc_id "
          + "ORDER BY c.weight DESC";
    
    public RIDAOImpl() {
        daoUtilities = new DAOUtilities();
    }
    

    
   /* public void storeWordReferences(List<WordBean> wordReferences) {
        Connection connection = null;
        try {
            connection = daoUtilities.getConnection();
            for (WordBean wordReference: wordReferences) {
                storeWordReference(connection, wordReference);
            }
        } catch ( SQLException e ) {
           System.err.println(e.getMessage());
        } finally {
            daoUtilities.silentClose(connection );
        }
    }
    */
   /* private void storeWordReference(Connection connection, WordBean wordReference) throws SQLException {
        PreparedStatement preparedStatement =
                daoUtilities.initializePreparedStatement(connection,
                                                         WORD_CREATE,
                                                         true,
                                                         wordReference.getWord());
        int statut = preparedStatement.executeUpdate();
        daoUtilities.silentClose(preparedStatement);
        if ( statut == 0 ) {
            throw new DAOException( "Word's creation failed, no line added in database." );
        } else {
            for (DocumentBean doc: )
        }
    }

    public List<DocumentBean> getDocumentCorrespondingToWords(String[] keywords) {
        List<DocumentBean> documents = new ArrayList<DocumentBean>();
        Connection connection = null;
        try {
            connection = daoUtilities.getConnection(urlBase, username, password);
            for (String word: keywords) {
                List<DocumentBean> wordAssociatedDocuments =
                        getDocumentsCorrespondingToWord(connection, word);
                documents = mergeFoundDocuments(documents, wordAssociatedDocuments);
            }
        } catch ( SQLException e ) {
           System.err.println(e.getMessage());
        } finally {
            daoUtilities.silentClose(connection );
        }
        return documents;
    }
    
    private List<DocumentBean> getDocumentsCorrespondingToWord(Connection connection,
            String word) throws SQLException {
        List<DocumentBean> documents = new ArrayList<DocumentBean>();
        PreparedStatement preparedStatement =
                daoUtilities.initializePreparedStatement(connection,
                                                         READ_DOCUMENTS_BY_WORD_CONTAINED,
                                                         false,
                                                         word);
        ResultSet resultSet = preparedStatement.executeQuery();
        while ( resultSet.next() ) {
           DocumentBean document = map( resultSet );
           documents.add(document);
        }
        return documents;
    }

    private List<DocumentBean> mergeFoundDocuments(List<DocumentBean> previousDocuments,
                                                   List<DocumentBean> newDocuments) {
        List<DocumentBean> mergedDocuments = new ArrayList<DocumentBean>(previousDocuments);
        for(DocumentBean newDoc: newDocuments) {
            boolean alreadyFound = false;
            for(DocumentBean previousDoc: previousDocuments) {
                if(previousDoc.getName().equals(newDoc.getName())) {
                    alreadyFound = true;
                    DocumentBean doc = new DocumentBean();
                    doc.setName(previousDoc.getName());
                    doc.setLink(previousDoc.getLink());
                    doc.setWeight(previousDoc.getWeight() + newDoc.getWeight());
                    mergedDocuments.add(doc);
                }
            }
            if(!alreadyFound) {
                mergedDocuments.add(newDoc);
            }
        }
        return mergedDocuments;
    }
    */

    public void storeWordReferences(List<WordBean> wordReferences) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<DocumentBean> getDocumentCorrespondingToWords(String[] words) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
