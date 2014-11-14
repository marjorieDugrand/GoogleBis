package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.beans.IndexationBean;
import com.mycompany.googlebis.beans.RelationBean;
import com.mycompany.googlebis.beans.WordBean;
import com.mycompany.googlebis.dao.*;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Marjorie
 */
public class GoogleController {
    private final DocumentDAO documentDAO;
    private final WordDAO wordDAO;
    private final IndexationDAO indexationDAO;
    private final FileHandler fileHandler;
    private SortedSet<IndexationBean> results;
    
    
    public GoogleController() {
        documentDAO = new DocumentDAOImpl();
        wordDAO = new WordDAOImpl();
        indexationDAO = new IndexationDAOImpl();
        fileHandler = new FileHandler();
    }
    
    public void storeCorpus() {
        File[] documents = fileHandler.getCorpusList();
        for(File document: documents) {
            storeDocument(document);
            Map<String,Integer> words = fileHandler.parseDocument(document);
            for(String word: words.keySet()) {
                storeWord(word);
                storeRelation(word, document.getName(),words.get(word));
            }
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
    
    public SortedSet<IndexationBean> recoverRequestDocument(String request)  {
        //String[] importantWords = fileHandler.parseRequest(request);
        String[] importantWords = request.split(" ");
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(importantWords);
        results = new TreeSet<IndexationBean>();
        for(String docName: documents.keySet()) {
            RelationBean relations = documents.get(docName);
            IndexationBean indexation = relations.getWordIndexations().get(0);
            int weightSum = 0;
            for(IndexationBean index: relations.getWordIndexations()) {
                weightSum += index.getWeight();
            }
            weightSum = (new Double(Math.pow(weightSum, relations.getIndexationsSize()))).intValue();
            indexation.setWeight(weightSum);
            results.add(indexation);
        }
        return results;
    }
    
    public Integer evaluateResultsPrecision(Integer precisionLevel) {
        Iterator<IndexationBean> iterator = results.iterator();
        for(int i=0; i < precisionLevel && iterator.hasNext(); i++) {
            IndexationBean result = iterator.next();
            
        }
        for(IndexationBean result: results){
            
        }
        return 0;
    }
}
