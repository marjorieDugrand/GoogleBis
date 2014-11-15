/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.dao;

/**
 *
 * @author Marjorie
 */
public class DAOFactory {
    
    private static DAOFactory instance;
    
    private DAOFactory() {};
    
    public static DAOFactory getInstance() {
        if(instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    public DocumentDAO getDocumentDAO() {
        return new DocumentDAOImpl();
    }
    
    public WordDAO getWordDAO() {
        return new WordDAOImpl();
    }
    
    public IndexationDAO getIndexationDAO() {
        return new IndexationDAOImpl();
    }
    
    public PertinenceDAO getPertinenceDAO() {
        return new PertinenceDAOImpl();
    }
    
    public RequestDAO getRequestDAO() {
        return new RequestDAOImpl();
    }
}
