package com.mycompany.googlebis;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.mycompany.googlebis.sparqlclient.*;

/**
 *
 * @author david
 */
public class SparqlTest {
    
    @Test
    public void splitListTest() {
        List<String> words = new ArrayList<String>() ;
        SparqlQuery query = new SparqlQuery();
        
        words.add("role");
        words.add("role d'animal");
        words.add("long-metrage");
        words.add("film long");
        words.add("personnage de film");
        
        List<String> results = query.splitList(words) ;
        System.out.println(results.toString()) ;
        
    }
}
