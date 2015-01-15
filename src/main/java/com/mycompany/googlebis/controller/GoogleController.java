package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.IndexationBean;
import java.util.SortedSet;

/**
 *
 * @author Marjorie
 */
public class GoogleController {
    
    private final CorpusStoringDelegate corpusDelegate;
    private final SearchDelegate searchDelegate;
    
    
    public GoogleController() {
        corpusDelegate = new CorpusStoringDelegate();
        searchDelegate = new SearchDelegate();
    }
    
    public void storeCorpus() {
        corpusDelegate.storeCorpus();
    }
   
    
    public SortedSet<IndexationBean> recoverRequestDocument(String requestName, boolean useSemanticVersion)  {
        return searchDelegate.recoverRequestDocument(requestName, useSemanticVersion);
    }
    
    public double evaluateResultsPrecision(Integer precisionLevel) {
        return searchDelegate.evaluateResultsPrecision(precisionLevel);
    }
}
