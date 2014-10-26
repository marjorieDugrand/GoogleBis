/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.beans.DocumentBean;
import com.mycompany.googlebis.dao.RIDAO;
import com.mycompany.googlebis.dao.RIDAOImpl;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Marjorie
 */
public class RIDAOTest {
    
    private RIDAO ridao;
    
    @Before
    public void setUp() {
        ridao = new RIDAOImpl();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void nothingInDatabaseNothingRetrieved() {
        String[] keywords = {"apple","peach"};
        List<DocumentBean> documents = ridao.getDocumentCorrespondingToWords(keywords);
        assertEquals(documents.size(),0);
    }
}
