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

import static junit.framework.Assert.*;
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
        assertEquals(13, wordMap.size());
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
        mapTest.put("6", 1); 
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
    
    @Test
    public void parseFileRequestTest () {
        List<RequestBean> requests = fileHandler.parseFileRequest();
        List<RequestBean> testRequests = createExpectedRequests();
        assertEquals(testRequests.size(), requests.size());
        for(int i=0; i < testRequests.size(); i++) {
           assertEquals(testRequests.get(i).getName(), requests.get(i).getName());
           assertEquals(testRequests.get(i).getText(), requests.get(i).getText());
        }
    }
    
    private List<RequestBean> createExpectedRequests() {
        List<RequestBean> req = new ArrayList<RequestBean>();
        req.add(createRequestBean("Q1", "personnes, intouchables"));
        req.add(createRequestBean("Q2", "lieu, naissance, omar sy"));
        req.add(createRequestBean("Q3", "personnes, recompensées, intouchables"));
        req.add(createRequestBean("Q4", "palmarès, globes de cristal, 2012"));
        req.add(createRequestBean("Q5", "membre, jury, globes de cristal, 2012"));
        req.add(createRequestBean("Q6", "prix, omar sy, globes de cristal, 2012"));  
        req.add(createRequestBean("Q7", "lieu, globes cristal, 2012"));
        req.add(createRequestBean("Q8", "prix, omar sy"));
        req.add(createRequestBean("Q9", "acteurs, joué avec, omar sy"));
        return req;
    }
    
    private RequestBean createRequestBean(String name, String text) {
        RequestBean bean = new RequestBean();
        bean.setName(name);
        bean.setText(text);
        return bean;
    }
    
    @Test
    public void parseRequestTest() {
        List<RequestBean> requests = fileHandler.parseFileRequest();
        List<String> keyWords = fileHandler.parseRequest(requests.get(5).getText()) ;
        List<String> expectedKeywords = new ArrayList<String>();
        expectedKeywords.add("prix");
        expectedKeywords.add("omar");
        expectedKeywords.add("sy");
        expectedKeywords.add("globe");
        expectedKeywords.add("crist");
        expectedKeywords.add("2012");
        assertEquals(expectedKeywords, keyWords);
    }
    
    @Test
    public void parseQRelsTest () {       

        List<PertinenceBean> qrels = fileHandler.parseQRelsForRequest(new File("D:\\RI\\qrels\\qrel_testQ1.txt")) ;
        List<PertinenceBean> expectedQrels = new ArrayList<PertinenceBean>();
        
        expectedQrels.add(createPertinenceBean("Q1", "D1.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D2.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D3.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D4.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D5.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D6.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D7.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D8.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D9.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D10.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D11.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D12.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D13.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D14.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D15.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D16.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D17.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D18.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D19.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D20.html", 0.5));
        expectedQrels.add(createPertinenceBean("Q1", "D21.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D22.html", 1));
        expectedQrels.add(createPertinenceBean("Q1", "D23.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D24.html", 0));
        expectedQrels.add(createPertinenceBean("Q1", "D25.html", 0));

        assertEquals(expectedQrels.size(), qrels.size());
        for (int i=0; i < qrels.size(); i++) {
            assertTrue(expectedQrels.get(i).equals(qrels.get(i)));
        }
    }
    
    private PertinenceBean createPertinenceBean(String request, String file, double pertinence) {
        PertinenceBean bean = new PertinenceBean();
        bean.setDocumentName(file);
        bean.setRequest(request);
        bean.setPertinence(pertinence);
        return bean;
    }
    
}