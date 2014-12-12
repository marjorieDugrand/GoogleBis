/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.controller;

import com.mycompany.googlebis.beans.PertinenceBean;
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
    
    private static final String DIRECTORY = "D:\\RI\\";
   // private static final String DIRECTORY = "/media/data/RI/";
    private static final String DOCUMENTSREPOSITORY = DIRECTORY + "corpus_test";
    private static final File STOPLISTFILE = new File(DIRECTORY + "stopliste.txt") ;
    private static final File REQUESTFILE = new File (DIRECTORY + "requests.html") ;
    private static final String QRELSREPOSITORY = DIRECTORY + "qrels" ;
    
    public File[] getCorpusList() {
        return getDirectoryFiles(DOCUMENTSREPOSITORY);
    }
    
    public File[] getQRels() {
        return getDirectoryFiles(QRELSREPOSITORY);
    }
    
    private File[] getDirectoryFiles(String repositoryName) {
        File directory = new File(repositoryName);
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
        int tailleMots = 5 ;
        String regexp;
        regexp = "\\s+|'|\\(|\\.|&|[0-9]|\"|\\-|\\:|\\?|\\||\\)|\\,|\\/|\\«|@|\\!|\\+|\\>";

        try {
            FileInputStream stopListFileInput ;
            stopListFileInput = new FileInputStream(STOPLISTFILE);
            BufferedReader stopListBufferedReader = new BufferedReader(new InputStreamReader(stopListFileInput));
            String stopWord ;

            while ((stopWord = stopListBufferedReader.readLine()) != null) {
                stopList.add(stopWord) ;
            }
            stopListFileInput.close();

            //System.out.println(stopList.toString());

            Document doc = Jsoup.parse(document, "UTF-8");
            String text = doc.body().text() ;
            String[] words = text.split(regexp);

            for (String word : words) {

                String wordLC = word.toLowerCase() ;
                if (!(stopList.contains(wordLC))) {
                    
                    if (wordLC.length() >= tailleMots)
                        wordLC = wordLC.substring(0, tailleMots);
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
    
    /**
     * Parse the requests file to recover the important words that define each request
     * @author David 
     * @return ArrayListRequestBean
     */
    public ArrayList<RequestBean> parseFileRequest() { 
        
        ArrayList<RequestBean> requestBeans = new ArrayList<RequestBean>() ;
        String baliseH2 = "h2" ;
        String baliseDL = "dl" ;
        String baliseDD = "dd" ;

        try {
            Document doc;
                doc = Jsoup.parse(REQUESTFILE, "UTF-8");

            Elements requestNames = doc.select(baliseH2) ;
            Elements requestTexts = doc.select(baliseDL) ;
            Elements requestKeyWords = new Elements();

            /*
            System.out.println("nb h2 element : " + requestNames.size()) ;
            System.out.println("nb dl element : " + requestTexts.size()) ;
            */
            
            for (int i = 0 ; i<requestTexts.size() ; i++) {
                RequestBean request = new RequestBean() ;
                
                Element e=requestTexts.get(i);
                requestKeyWords.add(e.select(baliseDD).first());
                
                request.setId(i);
                request.setName(requestNames.get(i).text()) ;
                request.setText(requestKeyWords.get(i).text().toLowerCase()) ;
                
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
     * @param request
     * @return String list
     */
    public List<String> parseRequest(String request){
        String regexp = ", " ;
        List<String> importantWords = new ArrayList<String>();
        for(String word: request.split(regexp)) {
            System.out.println("word " + word);
            if(word.length() > 5) {
                importantWords.add(word.substring(0,5));
            } else {
                importantWords.add(word);
            }
        }
        return importantWords;
    }
    
    
    public List<PertinenceBean> parseQRels() {
        List<PertinenceBean> pertinence = new ArrayList<PertinenceBean>();
        File[] qrels = getQRels();
        for(File qrel: qrels) {
            pertinence.addAll(parseQRelsForRequest(qrel));
        }
        return pertinence;
    }
    
    /**
     * Parse the qrel files 
     * @author David 
     * @param qrels
     * @return hashmap
     */
    public List<PertinenceBean> parseQRelsForRequest(File qrels) {
        List<PertinenceBean> qRels = new ArrayList<PertinenceBean>();
        
        try {
            FileInputStream qRelsFileInput;
            qRelsFileInput = new FileInputStream(qrels);
            BufferedReader qRelsBufferedReader = new BufferedReader(new InputStreamReader(qRelsFileInput));
            String qRelsLine;
            String regexp = "\\s+" ;

            while ((qRelsLine = qRelsBufferedReader.readLine()) != null) {
                //qRelsLines.add(qRelsLine) ;
                List<String> qRelsLines = Arrays.asList(qRelsLine.split(regexp)) ;
                String name = qrels.getName() ;
                String requestName = Arrays.asList(name.split("\\.")).get(0);
                requestName = requestName.substring(requestName.indexOf("Q"));
                System.out.println(qRelsLines.toString()) ;
                PertinenceBean bean = new PertinenceBean();
                
                bean.setRequest(requestName);
                bean.setDocumentName(qRelsLines.get(0));
                bean.setPertinence(StringToFloat(qRelsLines.get(1)));
                qRels.add(bean);
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
        if (("1").equals(number))
            numberFloat = (float) 1;
        if (("0,5").equals(number))
            numberFloat = (float) 0.5 ;
        if (("0").equals(number))
            numberFloat = (float) 0 ;
        
        return (Float) numberFloat ;
    }
}