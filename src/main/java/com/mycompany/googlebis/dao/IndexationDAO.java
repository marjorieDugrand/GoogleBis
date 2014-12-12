/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.IndexationBean;
import com.mycompany.googlebis.beans.RelationBean;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Marjorie
 */
public interface IndexationDAO {
    
    public void storeIndexations(String word, List<IndexationBean> indexationBeans);
    
    public void storeIndexations(String word, IndexationBean indexationBeans);
    
    public Map<String, RelationBean> getDocumentCorrespondingToWords(List<String> words);
    
    public void deleteIndexation(String word, String document);
    
    public void deleteIndexationByDocument(String document);
    
    public void deleteIndexationByWord(String word);
    
    public void deleteTable();
}
