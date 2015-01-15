/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.DocumentBean;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public interface DocumentDAO {
    
    public void createDocument(DocumentBean document);
    
    public void createDocuments(List<DocumentBean> documents);
    
    public DocumentBean readDocumentByName(String name);
    
    public int getCorpusSize();
    
    public void deleteDocumentByName(String name);
    
    public void deleteTable();
    
}
