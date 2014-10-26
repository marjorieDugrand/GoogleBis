/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.WordBean;

/**
 *
 * @author Marjorie
 */
public interface WordDAO {
    
    public void createWord(WordBean word);
    
    public WordBean readWordByName(String name);
}
