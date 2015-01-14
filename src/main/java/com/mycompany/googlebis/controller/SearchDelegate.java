/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.IndexationBean;
import com.mycompany.googlebis.beans.PertinenceBean;
import com.mycompany.googlebis.beans.RelationBean;
import com.mycompany.googlebis.dao.DAOFactory;
import com.mycompany.googlebis.dao.IndexationDAO;
import com.mycompany.googlebis.dao.PertinenceDAO;
import com.mycompany.googlebis.dao.RequestDAO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Marjorie
 */
public class SearchDelegate {
    
    private SortedSet<IndexationBean> results;
    private String request;
    private final IndexationDAO indexationDAO;
    private final RequestDAO requestDAO;
    private final PertinenceDAO pertinenceDAO;
    private final FileHandler fileHandler;
    
    public SearchDelegate() {
        DAOFactory factory = DAOFactory.getInstance();
        indexationDAO = factory.getIndexationDAO();
        requestDAO = factory.getRequestDAO();
        pertinenceDAO = factory.getPertinenceDAO();
        fileHandler = new FileHandler();
    }
    
    public SortedSet<IndexationBean> recoverRequestDocument(String requestName)  {
        System.out.println("delegate searching request " + requestName);
        request = requestName;
        String requestText = requestDAO.readRequestByName(request).getText();
        List<String> importantWords = fileHandler.parseRequest(requestText);
        return recoverDocumentsAssociatedWithWords(importantWords);
    }
    
    public SortedSet<IndexationBean> recoverSemanticRequestDocument(String requestName) {
        //TODO
        List<String> words = new ArrayList<String>();
        //TODO
        return recoverDocumentsAssociatedWithWords(words);
    }
    
    public SortedSet<IndexationBean> recoverDocumentsAssociatedWithWords(List<String> words) {
        Map<String, RelationBean> documents = indexationDAO.getDocumentCorrespondingToWords(words);
        results = new TreeSet<IndexationBean>();
        for(String docName: documents.keySet()) {
            IndexationBean indexation = analyzeDocumentWeight(documents.get(docName));
            results.add(indexation);
        }
        return results;
    }
    
    private IndexationBean analyzeDocumentWeight(RelationBean relations) {
        IndexationBean indexation = relations.getWordIndexations().get(0);
        int weightSum = 0;
        for(IndexationBean index: relations.getWordIndexations()) {
            weightSum += index.getWeight();
        }
        weightSum = (new Double(Math.pow(weightSum, relations.getIndexationsSize()))).intValue();
        indexation.setWeight(weightSum);
        return indexation;
    }
    
    public double evaluateResultsPrecision(Integer precisionLevel) {
        Iterator<IndexationBean> iterator = results.iterator();
        double precision = 0;
        for(int i=0; i < precisionLevel && iterator.hasNext(); i++) {
            IndexationBean result = iterator.next();
            System.out.println("doc : " + result.getDocumentName());
            PertinenceBean bean = pertinenceDAO.readPertinence(request, result.getDocumentName());
            precision += bean.getPertinence();
        }
        return precision/precisionLevel;
    }
    
}
