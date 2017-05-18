/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.filesworking.ClassifierModelFiles;
import com.inetelligencemodule.models.AbstractStageModel;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author nikita
 */
public class Classification {
    
    public String classify(AbstractStageModel toClassifyModel, String stageName) throws Exception {
        Instance stageInstance = toClassifyModel.getWekaInstance();
        Classifier classifier = ClassifierModelFiles.getClassifier(stageName);
        Instances trainingData = new Instances("name", toClassifyModel.getWekaAttrsList(), 0);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        stageInstance.setDataset(trainingData);
        return toClassifyModel.getClassValues().get((int)classifier.classifyInstance(stageInstance));
    }
}
