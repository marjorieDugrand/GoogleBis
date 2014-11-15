/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.googlebis;

import com.mycompany.googlebis.controller.GoogleController;
import com.mycompany.googlebis.view.GoogleFrame;

/**
 *
 * @author Marjorie
 */
public class GoogleBis {
    
    public static void main(String args[]) {
        GoogleController googleController = new GoogleController();
        GoogleFrame googleFrame = new GoogleFrame(googleController);
    }
    
}
