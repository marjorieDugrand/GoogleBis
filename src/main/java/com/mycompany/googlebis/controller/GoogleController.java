package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.dao.*;

/**
 *
 * @author Marjorie
 */
public class GoogleController {
    private DocumentDAO documentDAO;
    private WordDAO wordDAO;
    private IndexationDAO indexationDAO;
    
    private static final String repository = "C:\\Users\\Marjorie\\Desktop\\corpus_test";
    
    public GoogleController() {
        documentDAO = new DocumentDAOImpl();
        wordDAO = new WordDAOImpl();
        indexationDAO = new IndexationDAOImpl();
    }
    
    public void StoreCorpus() {
        //get number of documents
        for () {
            
            //parse document
        }
    }
}
