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
    
    private static final String documentRegexp = "[\\s!\"?;()&\':\\.,’_@\\\\«»-]+";
    private static final String requestRegexp = "[\\s,]+";
    
    private static final int tailleMots = 5 ;
    
    private List<String> stopList;
    
    public FileHandler() {
        parseStopList();
    }
    
    private void parseStopList() {
        stopList = new ArrayList<String>();
        try {
            FileInputStream stopListFileInput = new FileInputStream(STOPLISTFILE);
            BufferedReader stopListBufferedReader = new BufferedReader(new InputStreamReader(stopListFileInput));
            String stopWord ;
            while ((stopWord = stopListBufferedReader.readLine()) != null) {
                stopList.add(stopWord) ;
            }
            stopListFileInput.close();
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
        Map<String, Integer> wordMap = new HashMap<String, Integer>() ;
        try {
            String[] words = getDocumentImportantWords(document);
            for (String word : words) {
                System.out.println("word recovered : " + word);
                String wordLC = word.toLowerCase() ;
                if (!(stopList.contains(wordLC))) {
                    wordLC = cutWordAtAppropriateSize(wordLC);
                    addWordInMap(wordMap, wordLC);
                }
            }
            System.out.println("nb mots :" + wordMap.size()) ;
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wordMap;
    }
    
    private String cutWordAtAppropriateSize(String word) {
        String cutWord;
        if(word.length() >= tailleMots) {
            cutWord= word.substring(0, tailleMots);
        } else {
            cutWord = word;
        }
        return cutWord;
    }
    
    private void addWordInMap(Map<String, Integer> map, String word) {
         if (!(map.containsKey(word))) {
             map.put(word, 1) ;
         } else {
             Integer weight = map.get(word) + 1 ;
             map.put(word, weight) ;
         }
    }
    
    private String[] getDocumentImportantWords(File document) throws IOException {
        //String regexp = "\\s+|('\\s*)|\\(|\\.|&|[0-9]|\"|\\-|\\:|\\?|\\||\\)|\\,|\\/|\\«|@|\\!|\\+|\\>";
        Document doc = Jsoup.parse(document, "UTF-8");
        String text = doc.select("meta[name=description]").attr("content");
        text += " " + doc.select("meta[name=keywords]").attr("content");
        //text += doc.select("title").text();
        System.out.println("text recovered : " + text);
        return text.split(documentRegexp);
    }
    
    /**
     * Parse the requests file to recover the important words that define each request
     * @author David 
     * @return ListRequestBean
     */
    public List<RequestBean> parseFileRequest() { 
        
        List<RequestBean> requestBeans = new ArrayList<RequestBean>() ;

        try {
            Document doc = Jsoup.parse(REQUESTFILE, "UTF-8");
            Elements requestNames = getRequestsName(doc) ;
            Elements requestTexts = getRequestsText(doc);
            
            for (int i = 0 ; i<requestTexts.size() ; i++) {
                RequestBean request = new RequestBean() ;
                request.setName(requestNames.get(i).text()) ;
                request.setText(requestTexts.get(i).text().toLowerCase()) ;
                requestBeans.add(request) ;
            }
        
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return requestBeans ;
        
    }
    
    private Elements getRequestsName(Document doc) {
       String baliseNames = "h2" ;
       return doc.select(baliseNames); 
    }
    
    private Elements getRequestsText(Document doc) {
        String baliseDL = "dl" ;
        String baliseDD = "dd" ;
        Elements requestTexts = doc.select(baliseDL) ;
        Elements requestKeyWords = new Elements();
            
        for (Element element: requestTexts) {
            requestKeyWords.add(element.select(baliseDD).first());
        }
        return requestKeyWords;
    }
    
    /**
     * Parse the request key words to recover the important words that define the request
     * @author David 
     * @param request
     * @return String list
     */
    public List<String> parseRequest(String request){
        List<String> importantWords = new ArrayList<String>();
        for(String word: request.split(requestRegexp)) {
            if (!(stopList.contains(word))) {
                    word = cutWordAtAppropriateSize(word);
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