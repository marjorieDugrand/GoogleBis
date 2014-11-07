/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import java.io.File;

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
    public String[] parseDocument(File document) {
        //TODO
        return null;
    }

}
