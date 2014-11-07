package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.dao.*;
import java.io.File;

/**
 *
 * @author Marjorie
 */
public class GoogleController {
    private DocumentDAO documentDAO;
    private WordDAO wordDAO;
    private IndexationDAO indexationDAO;
    private FileHandler fileHandler;
    
    
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
            //parse document
        }
    }
    
    private void storeDocument(File document) {
       DocumentBean bean = new DocumentBean();
       bean.setName(document.getName());
       bean.setLink(document.getParent());
       documentDAO.createDocument(bean); 
    }
}
