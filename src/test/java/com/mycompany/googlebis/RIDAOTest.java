/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.*;
import com.mycompany.googlebis.dao.*;
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
    
    @Before
    public void setUp() {
        indexationDAO = new IndexationDAOImpl();
        documentDAO = new DocumentDAOImpl();
        wordDAO = new WordDAOImpl();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void nothingInDatabaseNothingRetrieved() {
        deleteWordReference("apple");
        deleteWordReference("peach");
        String[] keywords = {"apple","peach"};
        Map<String,RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(documents.size(),0);
    }
    
    @Test
    public void oneRelationRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        
        String[] keywords = {"apple","peach"};
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        assertEquals(1, documents.size());
        assertEquals(1, documents.get("doc1").getIndexationsSize());
    }
    
    @Test
    public void twoRelationsTwoRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createDocument("doc2", "c:documents/doc2");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        createIndexation("apple", "doc2", 3);
        
        String[] keywords = {"apple","peach"};
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
        
        String[] keywords = {"apple","peach"};
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
        
        String[] keywords1 = {"élève","fenêtre", "noël"};
        String[] keywords2 = {"ça"};
        Map<String, RelationBean> documents1 = indexationDAO.getDocumentCorrespondingToWords(keywords1);
        Map<String, RelationBean> documents2 = indexationDAO.getDocumentCorrespondingToWords(keywords2);
        assertEquals(2, documents1.size());
        assertEquals(3, documents1.get("doc1").getIndexationsSize());
        assertEquals(1, documents2.size());
        assertEquals(1, documents2.get("doc2").getIndexationsSize());
        
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