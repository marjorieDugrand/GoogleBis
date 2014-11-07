/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import java.io.File;
import java.util.Map;

/**
 *
 * @author Marjorie
 */
public class FileHandler {
    
    private static final String repository = "D:\\corpus_test";
    
    public File[] getCorpusList() {
        File directory = new File(repository);
        return directory.listFiles();
    }
    
    /**
     * Parse the document to recover the important words that define it
     * @param document 
     * @return 
     */
    public Map<String,Integer> parseDocument(File document) {
        //TODO
        return null;
    }

    String[] parseRequest(String request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
