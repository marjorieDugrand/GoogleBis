/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

import com.mycompany.googlebis.beans.RequestBean;

/**
 *
 * @author Marjorie
 */
public interface RequestDAO {
    
    public void createRequest(RequestBean request);
    
    public RequestBean readRequestByName(String name);
    
    public void deleteTable();
    
    public void deleteRequestByName(String requestName);
    
}
