/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.RequestBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private static final File REQUESTFILE = new File ("/media/data/RI/requests.html") ;
    private static final String QRELS = "/media/data/RI/qrels" ;
    
    public File[] getCorpusList() {
        File directory = new File(REPOSITORY);
        return directory.listFiles();
    }
    
    public File[] getQRels() {
        File qrelsFile = new File(QRELS) ;
        return qrelsFile.listFiles() ;
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
                    
                    if (wordLC.length() >= 5)
                        wordLC = wordLC.substring(0, 5);

                    if (!(wordMap.containsKey(wordLC))) {
                        wordMap.put(wordLC, 1) ;
                    } else {
                        i = (Integer) wordMap.get(wordLC) + 1 ;
                        wordMap.put(wordLC, i) ;
                    }
                }
            }
            //System.out.println("nb mots :" + wordMap.size()) ;

        
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return wordMap;
    }
    
    /**
     * Parse the requests file to recover the important words that define each request
     * @author David 
     * @return ArrayListRequestBean
     */
    public ArrayList<RequestBean> parseFileRequest() { 
        
        ArrayList<RequestBean> requestBeans = new ArrayList<RequestBean>() ;

        try {
            Document doc;
                doc = Jsoup.parse(REQUESTFILE, "UTF-8");

            Elements requestNames = doc.select("h2") ;
            Elements requestTexts = doc.select("dl") ;
            Elements requestKeyWords = new Elements();

            /*
            System.out.println("nb h2 element : " + requestNames.size()) ;
            System.out.println("nb dl element : " + requestTexts.size()) ;
            */
            
            for (int i = 0 ; i<requestTexts.size() ; i++) {
                RequestBean request = new RequestBean() ;
                
                Element e=requestTexts.get(i);
                requestKeyWords.add(e.select("dd").first());
                
                request.setId(i);
                request.setName(requestNames.get(i).text()) ;
                request.setText(requestKeyWords.get(i).text()) ;

                System.out.println(request.getName()) ;
                System.out.println(request.getText()) ; 
                
                requestBeans.add(request) ;
            }
        
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return requestBeans ;
        
    }
    
    /**
     * Parse the request key words to recover the important words that define the request
     * @author David 
     * @return String list
     */
    public List<String> parseRequest(String request){
        return Arrays.asList(request.split(", ")) ;
    }
    
    /**
     * Parse the qrel files 
     * @author David 
     * @param qrels
     * @return hashmap
     */
    public HashMap<String,Float> parseQRels(File qrels) {
        // TODO
        HashMap<String,Float> qRels = new HashMap<String,Float>() ;
        
        try {
            FileInputStream qRelsFileInput;
            qRelsFileInput = new FileInputStream(qrels);
            BufferedReader qRelsBufferedReader = new BufferedReader(new InputStreamReader(qRelsFileInput));
            String qRelsLine;

            while ((qRelsLine = qRelsBufferedReader.readLine()) != null) {
                //qRelsLines.add(qRelsLine) ;
                List<String> qRelsLines = Arrays.asList(qRelsLine.split("\\s+")) ;
                
                System.out.println(qRelsLines.toString()) ;
                
                qRels.put(qRelsLines.get(0), StringToFloat(qRelsLines.get(1))) ;
            }
            qRelsFileInput.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qRels ;
    }
    
    private Float StringToFloat(String number) {
        float numberFloat = 0 ;
        if (number.equals("1"))
            numberFloat = (float) 1;
        if (number.equals("0,5"))
            numberFloat = (float) 0.5 ;
        if (number.equals("0"))
            numberFloat = (float) 0 ;
        
        return (Float) numberFloat ;
    }

}