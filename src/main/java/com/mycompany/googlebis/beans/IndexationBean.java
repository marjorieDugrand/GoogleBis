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
public class IndexationBean implements Comparable {
    
    private String documentName;
    private String documentLink;
    private int weight;

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int compareTo(Object o) {
        if(o.getClass() == IndexationBean.class) {
            IndexationBean bean = (IndexationBean)o;
            if(this.weight >= bean.getWeight()) {
                return documentName.compareTo(bean.getDocumentName());
            } else {
                return 1;
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o.getClass() == IndexationBean.class) {
            IndexationBean aux = (IndexationBean)o;
            return documentName.equals(aux.getDocumentName())
                && documentLink.equals(aux.getDocumentLink())
                && weight == aux.getWeight();
        } else {
            return false;
        }
    }
}
