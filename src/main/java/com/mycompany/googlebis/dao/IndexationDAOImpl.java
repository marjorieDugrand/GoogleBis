/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.IndexationBean;
import com.mycompany.googlebis.beans.RelationBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjorie
 */
public class IndexationDAOImpl implements IndexationDAO{
    
    private static final String INDEXATION_CREATE = 
            "INSERT INTO ContainedIn (word_id,doc_id,weight) "
          + "VALUES (?,?,?)";
    
    private static final String READ_DOCUMENTS_BY_WORD_CONTAINED = 
            "SELECT d.doc_name, d.link, c.weight "
          + "FROM DOCUMENTS d, WORDS w, CONTAINEDIN c "
          + "WHERE w.word = ? "
          + "AND c.word_id = w.word_id "
          + "AND c.doc_id = d.doc_id "
          + "ORDER BY c.weight DESC";
    
    private static final String INDEXATION_DELETE = 
            "DELETE FROM CONTAINEDIN "
          + "WHERE word_id = ? AND doc_id = ?";
    
    private static final String INDEXATION_DELETE_BY_DOCUMENT = 
            "DELETE FROM CONTAINEDIN "
          + "WHERE doc_id = ?";
    
    private static final String INDEXATION_DELETE_BY_WORD = 
            "DELETE FROM CONTAINEDIN "
          + "WHERE word_id = ?";
    
    private static final String DELETE_TABLE = 
            "DELETE FROM CONTAINEDIN";
       
    private static final String WORD_FREQUENCY = 
            "SELECT COUNT(c.id) as FREQUENCY "
          + "FROM CONTAINEDIN c, WORDS w "
          + "WHERE w.word = ? AND w.word_id = c.word_id ";
    
    private final WordDAO wordDAO;
    private final DocumentDAO documentDAO;
    
    public IndexationDAOImpl() {
        wordDAO = new WordDAOImpl();
        documentDAO = new DocumentDAOImpl();
    }
    public Map<String,RelationBean> getDocumentCorrespondingToWords(List<String> words) {
        Map<String, RelationBean> results = new HashMap<String, RelationBean>();
        int corpusSize = documentDAO.getCorpusSize();
        for(String word: words) {
            ResultSet rs = DAOUtilities.executeQuery(READ_DOCUMENTS_BY_WORD_CONTAINED, word);   
            int frequency = getWordFrequencyInCorpus(word);
            double ponderation = Math.log((double)((double)corpusSize/(double)frequency));
            System.out.println("corpus size : " + corpusSize);
            System.out.println("frequency : " + frequency);
            System.out.println("ponderation : " + ponderation);
            try {
                while(rs.next()) {
                    String documentName = rs.getString("doc_name");
                    IndexationBean wordIndexation = new IndexationBean();
                    wordIndexation.setDocumentName(documentName);
                    wordIndexation.setDocumentLink(rs.getString("link"));
                    double weight = rs.getInt("weight") * ponderation;
                    wordIndexation.setWeight(weight);
                    if(!results.containsKey(documentName)) {
                        results.put(documentName, new RelationBean());
                    }
                    results.get(documentName).addWordIndexation(wordIndexation);
                }
            } catch (SQLException ex) {
                Logger.getLogger(IndexationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                DAOUtilities.silentClose(rs);
            }
        }
        return results;
    }

    public void storeIndexations(String word, IndexationBean indexationBean) {
        int wordID = getWordID(word);
        storeWordDocumentRelation(wordID, indexationBean);
    }

    public void storeIndexations(String word, List<IndexationBean> indexationBeans) {
        int wordID = getWordID(word);
        for(IndexationBean index: indexationBeans) {
            storeWordDocumentRelation(wordID, index);
        }
    }
    
    private int getWordID(String word) {
       return wordDAO.readWordByName(word).getId(); 
    }
    
    private int getDocumentID(String document) {
        return documentDAO.readDocumentByName(document).getId();
    }
    
    private void storeWordDocumentRelation(int wordID, IndexationBean indexationBean) {
        int docID = getDocumentID(indexationBean.getDocumentName());
        DAOUtilities.executeCreate(INDEXATION_CREATE,
                                   wordID,
                                   docID,
                                   indexationBean.getWeight());
    }

    public void deleteIndexation(String word, String document) {
        try {
            int wordID = getWordID(word);
            int docID = getDocumentID(document);
            DAOUtilities.executeDelete(INDEXATION_DELETE, wordID, docID);
        } catch(NullPointerException exc) {
            
        }
    }

    public void deleteIndexationByDocument(String document) {
        try {
            int docID = getDocumentID(document);
            DAOUtilities.executeDelete(INDEXATION_DELETE_BY_DOCUMENT, docID);
        } catch(NullPointerException exc) {
            
        }
    }

    public void deleteIndexationByWord(String word) {
        try {
            int wordID = getWordID(word);
            DAOUtilities.executeDelete(INDEXATION_DELETE_BY_WORD, wordID);
        } catch(NullPointerException exc) {
            
        }
    }

    public void deleteTable() {
        DAOUtilities.executeDelete(DELETE_TABLE);
    }
    
    public int getWordFrequencyInCorpus(String word) {
        int frequency = 1;
        ResultSet resultSet = DAOUtilities.executeQuery(WORD_FREQUENCY, word);
        try {
            if(resultSet.next() ) {
                frequency = resultSet.getInt("FREQUENCY");
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DAOUtilities.silentClose(resultSet);
        }
        return frequency;
    }
}
