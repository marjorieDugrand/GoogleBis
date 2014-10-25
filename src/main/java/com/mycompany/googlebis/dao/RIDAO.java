/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.*;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public interface RIDAO {
    
    public void storeDocumentsReferences(List<DocumentBean> documents);
    
    public void storeWordReferences(List<WordBean> wordReferences);
    
    public List<DocumentBean> getDocumentCorrespondingToWords(String[] words);
}
