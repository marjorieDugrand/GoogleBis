package com.mycompany.googlebis.sparqlclient;

import com.mycompany.googlebis.controller.FileHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SparqlQuery {
    
    // private static final String DIRECTORY = "D:\\RI\\";
    private static final String DIRECTORY = "/media/data/RI/";
    private static final File STOPLISTFILE = new File(DIRECTORY + "stopliste.txt") ;
    private static final String PREFIX = 
          "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
        + "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
        + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
        + "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"
        + "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n"
        + "PREFIX filmo: <http://www.irit.fr/recherches/MELODI/ontologies/FilmographieV1.owl#>" ;
    
   private static SparqlClient sparqlClient = new SparqlClient("localhost:3030/space");
   private static List<String> stopList = new ArrayList<String>(); // to fill

    public SparqlQuery() {
        String query = "ASK WHERE { ?s ?p ?o }";
        boolean serverIsUp = sparqlClient.ask(query);
        parseStopList();
        
        if (serverIsUp) {
            System.out.println("server is UP");
        } else {
            System.out.println("service is DOWN");
        }
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
    
    public List<String> getAllSynonymes(List<String> words) {
        List<String> synonymes = new ArrayList<String>();
        for(String word: words) {
            synonymes.addAll(getWordSynonymes(word));
        }
        return synonymes;
    }
    
    public List<String> getWordSynonymes(String word) {
        List<String> subClasses;
        List<String> synonymes = new ArrayList<String>() ;
        List<String> results;

        subClasses = getSubClassOf(word);
        for (String subclass : subClasses) {
            List<String> subclassSyn = getSynonymesFromURI(subclass);
            for (String synonyme : subclassSyn) {
                synonymes.add(synonyme) ;
            }
        }
        
        results = splitList(synonymes);
        results.add(word);
        
       return results ;
    }
    
    public static List<String> splitList (List<String> synonymes) {
        List<String> results = new ArrayList<String>() ;
        String regexp = " |'|-" ;
        
        
        
        for (String synonyme : synonymes) {
            String[] words = synonyme.split(regexp);
            for (String word : words) {
                if (!(stopList.contains(word)) && !(results.contains(word))) {
                    results.add(word) ;
                }
            }
        }
        return results ;
    }
    
    private static String getURI(String word) {
        String uri = null;
        String query = 
                PREFIX + "\n"
                + "SELECT ?res\n"
                + "WHERE {\n"
                + "     ?res rdfs:label \""+word+"\"@fr."
                + "}\n" ;
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            
            for (Map<String, String> result : results) {
                //System.out.println(result.get("res"));
                uri = result.get("res");
            }
        return uri;
    }
    
    private static void getLieu() {
        String query = 
                PREFIX + "\n"
                + "SELECT ?lieu\n"
                + "WHERE {\n"
                + "     ?lieu a filmo:Lieu."
                + "}\n" ;
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            
            for (Map<String, String> result : results) {
                //System.out.println(result.get("lieu"));
            }
    }
    
    private static List<String> getSubClassOf(String word) {
        
        List<String> subClass = new ArrayList<String>() ;
        String query = 
                PREFIX + "\n"
                + "SELECT ?res WHERE\n" 
                +"{\n" 
                +"    ?uri rdfs:label \"" + word + "\"@fr.\n"
                +"    ?res rdfs:subClassOf ?uri.\n" 
                +"}" ;
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            
            for (Map<String, String> result : results) {
                //System.out.println(result.get("res"));
                subClass.add(result.get("res")); 
            }
            
        return subClass ;
    }
    
    private static List<String> getSynonymes(String word) {
        
        List<String> synonymes = new ArrayList<String>() ;
        String query = 
                PREFIX + "\n"
                + "SELECT ?res WHERE\n" 
                +"{\n" 
                +"    ?uri rdfs:label \"" + word + "\"@fr.\n"
                +"     ?uri rdfs:label ?res .\n" 
                +"}" ;
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            
            for (Map<String, String> result : results) {
                //System.out.println(result.get("res"));
                synonymes.add(result.get("res")); 
            }
            
        return synonymes ;
    }
    
    private static List<String> getSynonymesFromURI(String uri) {
        
        List<String> synonymes = new ArrayList<String>() ;
        String query = 
                PREFIX + "\n"
                + "SELECT ?res WHERE\n" 
                +"{\n" 
                +"     <" + uri + "> rdfs:label ?res .\n" 
                +"}" ;
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            
            for (Map<String, String> result : results) {
                //System.out.println(result.get("res"));
                synonymes.add(result.get("res")); 
            }
            
        return synonymes ;
    }
    
}
