/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.models.AbstractStageModel;
import java.util.ArrayList;
import java.util.List;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author nikita
 */
public class Trainer {

    private List<AbstractStageModel> trainingDataset;

    public Trainer(List<AbstractStageModel> stageModels) {
        trainingDataset = stageModels;
    }

    public void train() throws Exception {
        //ArrayList<Instance> modelsInstances = new ArrayList<>();
        Instances instances = new Instances("name", trainingDataset.get(0).getWekaAttrsList(), trainingDataset.size());
        for (AbstractStageModel stage : trainingDataset) {
            instances.add(stage.getWekaInstance());
        }
        instances.setClassIndex(instances.numAttributes() - 1);
        Classifier classifier = new MultilayerPerceptron();
        //classifier.buildClassifier(instances);

        // Do 10-split cross validation
        Instances[][] split = crossValidationSplit(instances, 50);

        // Separate split into training and testing arrays
        Instances[] trainingSplits = split[0];
        Instances[] testingSplits = split[1];
         ArrayList<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < trainingSplits.length; i++) {
           
            classifier.buildClassifier(trainingSplits[i]);
            Evaluation evaluation = new Evaluation(trainingSplits[i]);
            evaluation.evaluateModel(classifier, testingSplits[i]);
            // Evaluation validation = classify(classifier, trainingSplits[i], testingSplits[i]);
            predictions.addAll(evaluation.predictions());

        }
        for (Prediction pr : predictions) {
            System.out.println("PRED " + pr.predicted());
            System.out.println("ACT " + pr.actual());
        }

    }

    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];

        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }
}
