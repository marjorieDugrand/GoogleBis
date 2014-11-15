/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.controller.FileHandler;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.junit.Test;

/**
 *
 * @author david
 */
public class fileHandlerTest {
    
    private FileHandler fileHandler = new FileHandler();
    
    
    @Test
    public void parseDocumentTest () throws IOException {
        
        File[] documents = fileHandler.getCorpusList() ;
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>() ;
        
        for (File document : documents) {
            wordMap = (HashMap<String, Integer>) fileHandler.parseDocument(document) ;
        }
        
        System.out.println(wordMap.toString());
    }
    
}
