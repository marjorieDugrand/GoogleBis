/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.*;
import com.mycompany.googlebis.dao.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Marjorie
 */
public class CorpusStoringDelegate {
    
    private final FileHandler fileHandler;
    private final DocumentDAO documentDAO;
    private final WordDAO wordDAO;
    private final IndexationDAO indexationDAO;
    private final RequestDAO requestDAO;
    private final PertinenceDAO pertinenceDAO;
    
    public CorpusStoringDelegate() {
        DAOFactory factory = DAOFactory.getInstance();
        documentDAO = factory.getDocumentDAO();
        wordDAO = factory.getWordDAO();
        indexationDAO = factory.getIndexationDAO();
        requestDAO = factory.getRequestDAO();
        pertinenceDAO = factory.getPertinenceDAO();
        fileHandler = new FileHandler();
    }
    
    public void storeCorpus() {
        clearDatabase();
        storeDocumentCorpus();
        storeRequests();
        storePertinence();
    }
    
    private void clearDatabase() {
        pertinenceDAO.deleteTable();
        indexationDAO.deleteTable();
        wordDAO.deleteTable();
        requestDAO.deleteTable();
        documentDAO.deleteTable();
    }
    
    private void storeDocumentCorpus() {
        File[] documents = fileHandler.getCorpusList();
        for(File document: documents) {
            storeDocument(document);
            storeDocumentIndexation(document);
        }
    }
    
    private void storeDocumentIndexation(File document) {
        Map<String,Integer> words = fileHandler.parseDocument(document);
        for(String word: words.keySet()) {
            storeWord(word);
            storeRelation(word, document.getName(),words.get(word));
        }
    }
   
    private void storeDocument(File document) {
       DocumentBean bean = new DocumentBean();
       bean.setName(document.getName());
       bean.setLink(document.getParent());
       documentDAO.createDocument(bean); 
    }
    
    private void storeWord(String word) {
        WordBean bean = new WordBean();
        bean.setWord(word);
        if(wordDAO.readWordByName(word) == null) {
            wordDAO.createWord(bean);
        }
    }
    
    private void storeRelation(String word, String documentName, int weight) {
        IndexationBean bean = new IndexationBean();
        bean.setWeight(weight);
        bean.setDocumentName(documentName);
        indexationDAO.storeIndexations(word, bean);
    }
    
    private void storeRequests() {
        List<RequestBean> requests = fileHandler.parseFileRequest();
        for(RequestBean request: requests) {
            requestDAO.createRequest(request);
        }
    }
    
    private void storePertinence() {
        List<PertinenceBean> pertinences = fileHandler.parseQRels();
        for(PertinenceBean pertinence: pertinences) {
            pertinenceDAO.createPertinence(pertinence);
        }
    }
    
}
