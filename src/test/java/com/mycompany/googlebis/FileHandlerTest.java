/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.PertinenceBean;
import com.mycompany.googlebis.beans.RequestBean;
import com.mycompany.googlebis.controller.FileHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import org.junit.Ignore;

/**
 *
 * @author david
 */
public class FileHandlerTest {
    
    private final FileHandler fileHandler = new FileHandler();
    
    
    @Test
    public void parseDocument1Test () {
        File[] documents = fileHandler.getCorpusList() ;
        Map<String, Integer> wordMap = fileHandler.parseDocument(documents[0]) ;      
        Map<String,Integer> mapTest = createMapTestForDoc1();
        assertEquals(mapTest,wordMap);
    }
    
    private Map<String,Integer> createMapTestForDoc1() {
        Map<String, Integer> docMap = new HashMap<String, Integer> ();
        docMap.put("intou", 3);
        docMap.put("casti", 2);
        docMap.put("com", 2);
        docMap.put("toutl", 2);
        docMap.put("acteu", 2);
        docMap.put("techn", 1);
        docMap.put("équip", 1);
        docMap.put("ensem", 1);
        docMap.put("réali", 1);
        docMap.put("détai", 1);
        docMap.put("consu", 1);
        return docMap;
    }

    @Test
    public void parseDocument10Test() {
        File document = new File("D:\\RI\\corpus_test\\D10.html");
        Map<String, Integer> wordMap = fileHandler.parseDocument(document) ;
        Map<String, Integer> mapTest = createMapForDocument10();
        assertEquals(mapTest,wordMap);
        assertEquals(12, wordMap.size());
    }

    @Test
    public void parseDocument3Test() {
        File document = new File("D:\\RI\\corpus_test\\D3.html");
        Map<String, Integer> wordMap = fileHandler.parseDocument(document) ;
        Map<String, Integer> mapTest = createMapForDocument3();
        assertEquals(mapTest,wordMap);
    }
    
    
    private Map<String,Integer> createMapForDocument10() {
        Map<String,Integer> mapTest = new HashMap<String, Integer>();
        mapTest.put("noire", 1);
        mapTest.put("condé", 1);
        mapTest.put("dossi", 1);
        mapTest.put("génér", 1);
        mapTest.put("avoca", 1);
        mapTest.put("avis", 1);
        mapTest.put("paris", 1);
        mapTest.put("novem", 1);
        mapTest.put("prote", 1);
        mapTest.put("enten", 1);
        mapTest.put("amian", 1);
        mapTest.put("victi", 1);
        return mapTest;
    }
    
    private Map<String,Integer> createMapForDocument3() {
        Map<String,Integer> map3 = new HashMap<String,Integer>();
        map3.put("intou",1);
        map3.put("film",1);
        map3.put("triom",1);
        map3.put("porté",1);
        map3.put("franç",1);
        map3.put("ciném",1);
        map3.put("compt",1);
        map3.put("acteu",1);
        map3.put("parti",1);
        map3.put("sy",1);
        map3.put("omar",1);
        map3.put("testo",1);
        map3.put("fred",1);
        map3.put("tande",1);
        map3.put("conna",1);
        return map3;
    }
    
    @Test @Ignore
    public void parseFileRequestTest () {
        ArrayList requests = new ArrayList() ;
        
        //System.out.println("\nTest parse file request :\n");
        
        requests = fileHandler.parseFileRequest() ;
    }
    
    
    @Test @Ignore
    public void parseRequestTest() {
        
        System.out.println("\nTest parse request :\n");
        
        ArrayList<RequestBean> requestBeans = new ArrayList<RequestBean>() ;
        ArrayList<String> keyWords = new ArrayList<String> () ;
        
        requestBeans = fileHandler.parseFileRequest() ;
        
        for (int i=0 ; i<requestBeans.size() ; i++) {
            keyWords.add(requestBeans.get(i).getText()) ;
            
            System.out.println(requestBeans.get(i).getText()) ;
        }
        
        for (int i=0 ; i<keyWords.size() ; i++) {
            List<String> keyWordsRequest = fileHandler.parseRequest(keyWords.get(i)) ;
            
            for (String keyWord:keyWordsRequest) {
                System.out.println(keyWord);
            }
        }
    }
    
    @Test @Ignore
    public void parseQRelsTest () {
        
        File[] qRelsFiles = fileHandler.getQRels();
        
        System.out.println("\nParse qrels test :\n");
        
        List<PertinenceBean> qrels = fileHandler.parseQRelsForRequest(qRelsFiles[0]) ;
        
        for (PertinenceBean qrel : qrels) {
            System.out.println(qrel.getRequest() + " : " + qrel.getDocumentName() + "->" + qrel.getPertinence());
        }
    }
    
}