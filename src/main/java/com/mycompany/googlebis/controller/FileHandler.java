/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    private static final String repository = "/media/data/RI/corpus_test";
    private static final File stopListFile = new File("/media/data/RI/stopliste.txt") ;
    
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
        
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>() ;
        ArrayList stopList = new ArrayList() ;
        Integer i ;
        
        FileInputStream stopListFileInput = new FileInputStream(stopListFile) ;
	BufferedReader stopListBufferedReader = new BufferedReader(new InputStreamReader(stopListFileInput));
        String stopWord ;
        
        while ((stopWord = stopListBufferedReader.readLine()) != null) {
            stopList.add(stopWord) ;
        }
        stopListFileInput.close();
        
        System.out.println(stopList.toString());
        
        Document doc = Jsoup.parse(document, "UTF-8");
        String text = doc.body().text() ;
        String[] words = text.split(" ");
        
        for (String word : words) {
            String wordLC = word.toLowerCase() ;
            if (!(stopList.contains(wordLC))) {
                if (!(wordMap.containsKey(wordLC))) {
                    wordMap.put(wordLC, 1) ;
                } else {
                    i = (Integer) wordMap.get(wordLC) + 1 ;
                    wordMap.put(wordLC, i) ;
                }
            }
        }
        System.out.println("nb mots :" + wordMap.size()) ;
        return wordMap;
    }

    public String[] parseRequest(String request) { 
        return request.split(" ");
    }

}
