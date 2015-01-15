/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.IndexationBean;
import com.mycompany.googlebis.controller.GoogleController;
import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marjorie
 */
public class GoogleControllerTest {
    
    private GoogleController googleController;
     
    @Before
    public void setUp() {
        googleController = new GoogleController();
    }
    
    @After
    public void tearDown() {
    }

    //@Test
    public void storeCorpusTest() {
        googleController.storeCorpus();
        //TODO
    }
    
    @Test
    public void request() {
        googleController.storeCorpus();
        SortedSet<IndexationBean> results = googleController.recoverRequestDocument("personnes Intouchables",false);
        for(IndexationBean index: results) {
            System.out.println("document name: " + index.getDocumentName());
            System.out.println("document link: " + index.getDocumentLink());
            System.out.println("document weight: " + index.getWeight());
        }
    }
    
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
