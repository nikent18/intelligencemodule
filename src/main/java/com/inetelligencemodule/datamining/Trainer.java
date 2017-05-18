/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.filesworking.ClassifierModelFiles;
import com.inetelligencemodule.models.AbstractStageModel;
import com.inetelligencemodule.models.LoanApprovalStage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author nikita
 */
public abstract class Trainer {

    private List<AbstractStageModel> trainingDataset;
    public abstract void trainModel(String tableName);
/*
    public Trainer(List<AbstractStageModel> stageModels) {
        trainingDataset = stageModels;
    }

    public void train(String stageName) throws Exception {
        LoanApprovalStage.setWekaAttributes(trainingDataset);
        Instances trainingData = new Instances("name", trainingDataset.get(0).getWekaAttrsList(), 0);
        for (AbstractStageModel stage : trainingDataset) {
            trainingData.add(stage.getWekaInstance());
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        Classifier classifier = new MultilayerPerceptron();
        classifier.buildClassifier(trainingData);
        ClassifierModelFiles.saveClassifierModel(classifier, stageName);
        /*  Instance testInstance = new DenseInstance(trainingDataset.get(0).getWekaAttrsList().size());
        testInstance.setValue(new Attribute("sepallength", 0), 7.7);
        testInstance.setValue(new Attribute("sepalwidth", 1), 0.94);
        testInstance.setValue(new Attribute("petallength", 2), 2.60);
        testInstance.setValue(new Attribute("petalwidth", 3), 0.25);    
        classifier.classifyInstance(testInstance);
        double prediction = classifier.classifyInstance(testInstance);

        double distribution[] =classifier.distributionForInstance(testInstance);
       System.out.println("---------------------------------------------");
        System.out.println(prediction);
       double max = distribution[0];
       int j =0;
       for (int i = 0; i < distribution.length; i++) {
           //  System.out.println(distribution[i]);
           if (distribution[i] > max) {
               max = distribution[i];
               j = i;
           }
       }     
    }
*/
    

}
