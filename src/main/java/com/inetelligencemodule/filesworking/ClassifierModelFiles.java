/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.filesworking;

import com.inetelligencemodule.datamining.Trainer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;

/**
 *
 * @author nikita
 */
public class ClassifierModelFiles {
    private static String modelsPath = "/home/nikita/NetBeansProjects/IntelligenceModule/files/ClassifierModels/";
    
    public static void saveClassifierModel(Classifier model, String stageName) {
        ObjectOutputStream oos = null;        
        try {
            weka.core.SerializationHelper.write(modelsPath + stageName + ".model", model);
        } catch (Exception ex) {
            Logger.getLogger(ClassifierModelFiles.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
    
    public static Classifier getClassifier(String stageName) {
        try {
            return (Classifier) weka.core.SerializationHelper.read(modelsPath + stageName + ".model");
        } catch (Exception ex) {
            Logger.getLogger(ClassifierModelFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
