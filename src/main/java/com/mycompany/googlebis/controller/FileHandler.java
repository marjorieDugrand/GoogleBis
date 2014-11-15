/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Marjorie
 */
public class FileHandler {
    
    private static final String REPOSITORY = "/media/data/RI/corpus_test";
    private static final File STOPLISTFILE = new File("/media/data/RI/stopliste.txt") ;
    private static final String REQUESTFILE = "/media/data/RI/requests.html" ;
    
    public File[] getCorpusList() {
        File directory = new File(REPOSITORY);
        return directory.listFiles();
    }
    
    /**
     * Parse the document to recover the important words that define it
     * @author David
     * @param document 
     * @return 
     */
    public Map<String,Integer> parseDocument(File document) {
        
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>() ;
        ArrayList stopList = new ArrayList() ;
        Integer i ;

        try {
            FileInputStream stopListFileInput ;
            stopListFileInput = new FileInputStream(STOPLISTFILE);
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

        
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return wordMap;
    }

    public void parseFileRequest() { 
        
        // RequestBean[] requestBeans = new RequestBean ;
        
        Document doc = Jsoup.parse(REQUESTFILE, "UTF-8");
        
        Elements requestNames = doc.select("h2") ;
        Elements requestTexts = doc.select("dl") ;
        Elements requestKeyWords = new Elements();
        
        for (int i = 0 ; i<requestTexts.size() ; i++) {
            Element e=requestTexts.get(i);
            requestKeyWords.add(e.select("dd").first());
        }
        
        /*
        for (int i=0 ; i<requestNames.size() ; i++) {
            RequestBean request = new RequesBean() ;
            request.setName(requestNames.get(i).text()) ;
            request.setText(requestKeyWords.get(i).text()) ;
            
            requestBeans.add(request) ;
        }*/
        
        
    }
    
    public String[] parseRequest(String request){
        return request.split(", ") ;
    }
    
    public void parseQRels() {
        
    }
    
    private int compareTwoWords (String a, String b) {
        a = a.substring(0, 5) ;
        b = b.substring(0, 5) ;
        
        return a.compareTo(b) ;
    }
    
    // Parse qrels et parse request

}