/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.*;
import com.mycompany.googlebis.dao.*;
import java.util.List;
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
       // List<IndexationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
        //assertEquals(documents.size(),0);
    }
    
    @Test
    public void oneRelationRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        
        String[] keywords = {"apple","peach"};
       // List<IndexationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
      //  assertEquals(1, documents.size());
       // assertEquals("doc1", documents.get(0).getDocumentName());
    }
    
    @Test
    public void twoRelationsTwoRetrieved() {
        createDocument("doc1", "c:documents/doc1");
        createDocument("doc2", "c:documents/doc2");
        createWord("apple");
        createIndexation("apple", "doc1", 1);
        createIndexation("apple", "doc2", 3);
        
        String[] keywords = {"apple","peach"};
    //    List<IndexationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
      //  assertEquals(2, documents.size());
      //  assertEquals("doc2", documents.get(0).getDocumentName()); 
    }
    
    @Test
    public void twoWordsContainedByDocument() {
        createDocument("doc1", "c:documents/doc1");
        createWord("apple");
        createWord("peach");
        createIndexation("apple", "doc1", 2);
        createIndexation("peach", "doc1", 1);
        
        String[] keywords = {"apple","peach"};
    //    List<IndexationBean> documents = indexationDAO.getDocumentCorrespondingToWords(keywords);
       // assertEquals(2, documents.size());
     //   assertEquals("doc1", documents.get(0).getDocumentName());
      //  assertEquals(2, documents.get(0).getWeight()); 
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
      //  List<IndexationBean> documents1 = indexationDAO.getDocumentCorrespondingToWords(keywords1);
  //      List<IndexationBean> documents2 = indexationDAO.getDocumentCorrespondingToWords(keywords2);
    //    assertEquals(4, documents1.size());
        //assertEquals("doc1", documents1.get(0).getDocumentName());
      //  assertEquals(1, documents2.size());
      //  assertEquals("doc2", documents2.get(0).getDocumentName());
        
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