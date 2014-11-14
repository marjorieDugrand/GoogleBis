/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis.beans;

/**
 *
 * @author Marjorie
 */
public class PertinenceBean {
    
    private String documentName;
    private String request;
    private double pertinence;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public double getPertinence() {
        return pertinence;
    }

    public void setPertinence(double pertinence) {
        this.pertinence = pertinence;
    }
    
}
