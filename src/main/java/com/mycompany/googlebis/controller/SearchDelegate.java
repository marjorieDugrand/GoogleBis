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
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Marjorie
 */
public class SearchDelegate {
    
    private SortedSet<IndexationBean> results;
    private String requestText;
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
        requestText = requestDAO.readRequestByName(requestName).getText();
        String[] importantWords = fileHandler.parseRequest(requestName);
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
    
    public double evaluateResultsPrecision(Integer precisionLevel) {
        Iterator<IndexationBean> iterator = results.iterator();
        double precision = 0;
        for(int i=0; i < precisionLevel && iterator.hasNext(); i++) {
            IndexationBean result = iterator.next();
            PertinenceBean bean = pertinenceDAO.readPertinence(requestText, result.getDocumentName());
            precision += bean.getPertinence();
        }
        return precision;
    }
    
}
