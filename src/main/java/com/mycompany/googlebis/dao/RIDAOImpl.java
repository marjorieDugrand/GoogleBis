/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.beans.WordBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public class RIDAOImpl implements RIDAO {

    public void storeDocumentsReferences(List<DocumentBean> documents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void storeWordReferences(List<WordBean> wordReferences) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<DocumentBean> getDocumentCorrespondingToWords(String[] words) {
        List<DocumentBean> documents = new ArrayList<DocumentBean>();
        return documents;
    }
    
}
