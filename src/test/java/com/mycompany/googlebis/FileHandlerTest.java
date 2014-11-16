/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.RequestBean;
import com.mycompany.googlebis.controller.FileHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author david
 */
public class FileHandlerTest {
    
    private FileHandler fileHandler = new FileHandler();
    
    
    @Test
    public void parseDocumentTest () {
        
        File[] documents = fileHandler.getCorpusList() ;
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>() ;
        
        for (File document : documents) {
            wordMap = (HashMap<String, Integer>) fileHandler.parseDocument(document) ;
        }
        
        // System.out.println(wordMap.toString());
    }
    
    @Test
    public void parseFileRequestTest () {
        ArrayList requests = new ArrayList() ;
        
        System.out.println("\nTest parse file request :\n");
        
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
            List<String> keyWordsRequest = fileHandler.parseRequest(keyWords.get(i)) ;
            
            for (int j=0 ; j<keyWordsRequest.size() ; j++) {
                System.out.println(keyWordsRequest.get(j));
            }
        }
    }
    
}