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
            if(this.weight < bean.getWeight()) {
                return 1;
            } else if(this.weight > bean.getWeight()) {
                return -1;
            } else {
                return documentName.compareTo(bean.getDocumentName());
            }
        }
        return 0;
    }
    
    /*The value 0 if the argument is a string lexicographically equal to this string;
    a value less than 0 if the argument is a string lexicographically greater than this string;
    and a value greater than 0 if the argument is a string lexicographically less than this string*/
    
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
