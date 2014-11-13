/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
     * @author David
     * @param document 
     * @return 
     * @throws java.io.IOException 
     */
    public Map<String,Integer> parseDocument(File document) throws IOException {
        //TODO
        
        HashMap wordMap = new HashMap<String, Integer>() ;
        Integer i ;
        
        Document doc = Jsoup.parse(document, "UTF-8");
        String text = doc.body().text() ;
        String[] words = text.split(" ");
        
        for (String word : words) {
            if (!(wordMap.containsKey(word))) {
                wordMap.put(word, 1) ;
            } else {
                i = (Integer) wordMap.get(word) + 1 ;
                wordMap.put(word, i) ;
            }
        }
        return wordMap;
    }

    String[] parseRequest(String request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
