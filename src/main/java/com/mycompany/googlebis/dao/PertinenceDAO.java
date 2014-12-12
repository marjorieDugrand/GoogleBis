/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.PertinenceBean;

/**
 *
 * @author Marjorie
 */
public interface PertinenceDAO {
    
    public void createPertinence(PertinenceBean pertinence);
    
    public PertinenceBean readPertinence(String requestName, String filename);
    
    public void deleteTable();
    
}
