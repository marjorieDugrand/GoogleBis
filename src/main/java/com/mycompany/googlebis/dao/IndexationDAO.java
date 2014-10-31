/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.IndexationBean;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public interface IndexationDAO {
    
    public void storeIndexations(String word, List<IndexationBean> indexationBeans);
    
    public void storeIndexations(String word, IndexationBean indexationBeans);
    
    public List<IndexationBean> getDocumentCorrespondingToWords(String[] words);
    
    public void deleteIndexation(String word, String document);
    
    public void deleteIndexationByDocument(String document);
    
    public void deleteIndexationByWord(String word);
}
