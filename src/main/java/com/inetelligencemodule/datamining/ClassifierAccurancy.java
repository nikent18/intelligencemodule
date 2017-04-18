/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.models.LoanApprovalStage;
import com.inetelligencemodule.services.DataServicesImpl;
import com.inetelligencemodule.services.InterfaceDataServices;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

/**
 *
 * @author Никита Кенть
 */
public class ClassifierAccurancy {

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    public static Evaluation classify(Classifier model,
            Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation evaluation = new Evaluation(trainingSet);

        model.buildClassifier(trainingSet);
        evaluation.evaluateModel(model, testingSet);

        return evaluation;
    }

    public static double calculateAccuracy(ArrayList<Prediction> predictions) {
        double correct = 0;

        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.get(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }

        return 100 * correct / predictions.size();
    }

    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];

        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }

    public void process() throws Exception {
        BufferedReader datafile = readDataFile("/home/nikita/NetBeansProjects/IntelligenceModule/files/train.txt");

        Instances data = new Instances(datafile);
        data.setClassIndex(data.numAttributes() - 1);

        // Do 10-split cross validation
        Instances[][] split = crossValidationSplit(data, 50);

        // Separate split into training and testing arrays
        Instances[] trainingSplits = split[0];
        Instances[] testingSplits = split[1];

        // Use a set of classifiers
        Classifier[] models = {
            new J48(), // a decision tree
            new RandomForest(),//decision table majority classifier
            new NaiveBayes(),
            new MultilayerPerceptron()
        };

        // Run for each model
        for (Classifier model : models) {
            ArrayList<Prediction> predictions = new ArrayList<>();
            for (int i = 0; i < trainingSplits.length; i++) {
                Evaluation validation = classify(model, trainingSplits[i], testingSplits[i]);
                predictions.addAll(validation.predictions());
                
            }
            double accuracy = calculateAccuracy(predictions);
            System.out.println(model.getClass().getSimpleName() + " accuracy: " + String.format("%.2f%%", accuracy) + "\n---------------------------------");

           /* ArffLoader testLoader = new ArffLoader();
            testLoader.setFile(new File("files/test.txt"));
            Instances testStructure = testLoader.getStructure();
            testStructure.setClassIndex(testStructure.numAttributes() - 1);
            Instance toClassify;
            while ((toClassify = testLoader.getNextInstance(testStructure)) != null) {
                double prediction = model.classifyInstance(toClassify);
              //  System.out.println(prediction);
            }*/

        }
        tests();
    }
    @Autowired
    InterfaceDataServices dataServices;
    public void tests () {
    /*    List<LoanApprovalStage> records = dataServices.getAllRecords();
    //    List<LoanApprovalStage> records = tmp.getAllRecords();
        for (LoanApprovalStage stage : records) {
            System.out.println(123);
        }*/
    }
    
}
