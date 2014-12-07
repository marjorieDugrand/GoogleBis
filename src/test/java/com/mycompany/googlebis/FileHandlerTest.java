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
import org.junit.Test;

/**
 *
 * @author david
 */
public class FileHandlerTest {
    
    private final FileHandler fileHandler = new FileHandler();
    
    
    @Test
    public void parseDocumentTest () {
        
        File[] documents = fileHandler.getCorpusList() ;
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>() ;
        
        System.out.println("parse document test :");
        
        for (File document : documents) {
            wordMap = (HashMap<String, Integer>) fileHandler.parseDocument(document) ;
        }
        
        System.out.println(wordMap.toString());
    }
    
    @Test
    public void parseFileRequestTest () {
        ArrayList requests = new ArrayList() ;
        
        //System.out.println("\nTest parse file request :\n");
        
        requests = fileHandler.parseFileRequest() ;
    }
    
    
    @Test
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
            String[] keyWordsRequest = fileHandler.parseRequest(keyWords.get(i)) ;
            
            for (int j=0 ; j<keyWordsRequest.length ; j++) {
                System.out.println(keyWordsRequest[j]);
            }
        }
    }
    
    @Test
    public void parseQRelsTest () {
        
        File[] qRelsFiles = fileHandler.getQRels();
        
        System.out.println("\nParse qrels test :\n");
        
        List<PertinenceBean> qrels = fileHandler.parseQRelsForRequest(qRelsFiles[0]) ;
        
        for (PertinenceBean qrel : qrels) {
            System.out.println(qrel.getRequest() + " : " + qrel.getDocumentName() + "->" + qrel.getPertinence());
        }
    }
    
}