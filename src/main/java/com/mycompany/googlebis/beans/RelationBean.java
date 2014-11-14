/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marjorie
 */
public class RelationBean {

    private List<IndexationBean> wordIndexations;
    
    public RelationBean() {
        wordIndexations = new ArrayList<IndexationBean>();
    }
    
    public int getIndexationsSize() {
        return wordIndexations.size();
    }
    
    public List<IndexationBean> getWordIndexations() {
        return wordIndexations;
    }

    public void setWordIndexations(List<IndexationBean> wordIndexations) {
        this.wordIndexations = wordIndexations;
    }
    
    public void addWordIndexation(IndexationBean wordIndexation) {
        this.wordIndexations.add(wordIndexation);
    }
}
