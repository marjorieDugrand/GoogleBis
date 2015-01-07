/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.*;
import com.mycompany.googlebis.dao.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marjorie
 */
public class RIDAOTest {
    
    private IndexationDAO indexationDAO;
    private DocumentDAO documentDAO;
    private WordDAO wordDAO;
    private RequestDAO requestDAO;
    private PertinenceDAO pertinenceDAO;
    
    @Before
    public void setUp() {
        indexationDAO = new IndexationDAOImpl();
        documentDAO = new DocumentDAOImpl();
        wordDAO = new WordDAOImpl();
        requestDAO = new RequestDAOImpl();
        pertinenceDAO = new PertinenceDAOImpl();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void nothingInDatabaseNothingRetrieved() {
        deleteWordReference("apple");
        deleteWordReference("peach");
        List<String> keywords = createKeywordsList("apple","peach");
        Map<String,RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(documents.size(),0);
    }
    
    @Test
    public void oneRelationRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        List<String> keywords = createKeywordsList("apple","peach");
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(1, documents.size());
        assertEquals(1, documents.get("doc1").getIndexationsSize());
    }
    
    private List<String> createKeywordsList(String... keywords) {
        List<String> keywordsList = new ArrayList<String>();
        for(int i=0; i < keywords.length; i++) {
            keywordsList.add(keywords[i]);
        }
        return keywordsList;
    }
    
    @Test
    public void twoRelationsTwoRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createDocument("doc2", "c:documents/doc2");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        createIndexation("apple", "doc2", 3);
        
        List<String> keywords = createKeywordsList("apple","peach");
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(2, documents.size());
        assertEquals(1, documents.get("doc2").getIndexationsSize());
        assertEquals(1, documents.get("doc1").getIndexationsSize());
    }
    
    @Test
    public void twoWordsContainedByDocument() {
        createDocument("doc1", "c:documents/doc1");
        createWord("apple");
        createWord("peach");
        createIndexation("apple", "doc1", 2);
        createIndexation("peach", "doc1", 1);
        
        List<String> keywords = createKeywordsList("apple","peach");
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(1, documents.size());
        assertEquals(2, documents.get("doc1").getIndexationsSize());
        assertEquals(2, documents.get("doc1").getWordIndexations().get(0).getWeight()); 
    }
    
    @Test
    public void wordsWithSpecialCharacters() {
        createDocument("doc1", "c:/documents/doc1");
        createDocument("doc2", "c:/documents/doc2");
        createWords("élève","ça","fenêtre","noël");
        createIndexation("élève", "doc1", 2);
        createIndexation("ça", "doc2", 1);
        createIndexation("fenêtre", "doc1", 2);
        createIndexation("fenêtre", "doc2", 2);
        createIndexation("noël", "doc1", 1);
        
        List<String> keywords1 = createKeywordsList("élève","fenêtre", "noël");
        List<String> keywords2 = createKeywordsList("ça");
        Map<String, RelationBean> documents1 = indexationDAO.getDocumentCorrespondingToWords(keywords1);
        Map<String, RelationBean> documents2 = indexationDAO.getDocumentCorrespondingToWords(keywords2);
        assertEquals(2, documents1.size());
        assertEquals(3, documents1.get("doc1").getIndexationsSize());
        assertEquals(1, documents2.size());
        assertEquals(1, documents2.get("doc2").getIndexationsSize());
        
    }
    
    @Test
    public void retrievePertinence() {
        createDocument("doc1", "c:/documents/doc1");
        createDocument("doc2", "c:/documents/doc2");
        createDocument("doc3", "c:/documents/doc3");
        createWords("élève","ça","fenêtre","noël");
        createIndexation("élève", "doc1", 2);
        createIndexation("ça", "doc2", 1);
        createIndexation("fenêtre", "doc1", 2);
        createIndexation("fenêtre", "doc2", 2);
        createIndexation("noël", "doc1", 1);
        createRequest("r1","ça noël");
        createPertinence("r1", "doc1", 1.0);
        createPertinence("r1", "doc2", 0.5);
        assertEquals(1.0,pertinenceDAO.readPertinence("r1", "doc1").getPertinence()); 
        assertEquals(0.5,pertinenceDAO.readPertinence("r1", "doc2").getPertinence()); 
    }
    
    private void createRequest(String requestName, String request) {
        requestDAO.deleteRequestByName(requestName);
        RequestBean requestBean = new RequestBean();
        requestBean.setName(requestName);
        requestBean.setText(request);
        requestDAO.createRequest(requestBean);
    }
    
    private void createPertinence(String request, String document, double pertinence) {
       pertinenceDAO.deletePertinence(request, document);
       PertinenceBean pertinenceBean = new PertinenceBean();
       pertinenceBean.setDocumentName(document);
       pertinenceBean.setRequest(request);
       pertinenceBean.setPertinence(pertinence);
       pertinenceDAO.createPertinence(pertinenceBean);
    }
    
    private void deleteWordReference(String word) {
        indexationDAO.deleteIndexationByWord(word);
        wordDAO.deleteWordByName(word);
    }
    
    private void deleteDocumentReference(String name) {
        indexationDAO.deleteIndexationByDocument(name);
        documentDAO.deleteDocumentByName(name);
    }
    private void createDocument(String name, String link) {
        deleteDocumentReference(name);
        DocumentBean document = new DocumentBean();
        document.setName(name);
        document.setLink(link);
        documentDAO.createDocument(document);
    }
    
    private void createWord(String word) {
        deleteWordReference(word);
        WordBean bean = new WordBean();
        bean.setWord(word);
        wordDAO.createWord(bean);
    }
    
    private void createWords(String... words) {
        for(String word: words) {
            createWord(word);
        }
    }
    
    private void createIndexation(String word, String document, int weight) {
        indexationDAO.deleteIndexation(word, document);
        IndexationBean index = new IndexationBean();
        index.setDocumentName(document);
        index.setWeight(weight);
        indexationDAO.storeIndexations(word,index);
    }
}