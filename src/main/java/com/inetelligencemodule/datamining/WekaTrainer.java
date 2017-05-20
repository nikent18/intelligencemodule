/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.database.DBConnector;
import com.inetelligencemodule.filesworking.ClassifierModelFiles;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author nikita
 */
public class WekaTrainer extends Trainer{
    
    private String tableName;
    
    public WekaTrainer(String tableName) {
        this.tableName = tableName;
    }
    
    @Override
    public void trainModel(String tableName) {
        try {
            Instances trainingData = new Instances("name", getAttrsList(), 0);
            trainingData = fillInstances(trainingData);
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            Classifier classifier = new MultilayerPerceptron();
            classifier.buildClassifier(trainingData);
            ClassifierModelFiles.saveClassifierModel(classifier, "wqe");
            
            
            Instance testInstance = new DenseInstance(getAttrsList().size());
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
            
            
        } catch (SQLException ex) {
            Logger.getLogger(WekaTrainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WekaTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList<Attribute> getAttrsList() throws SQLException {
        DBConnector db = new DBConnector();
        ArrayList<Attribute> attrs = new ArrayList<>();
        ResultSetMetaData rsmd = db.getTableMeta(tableName);
        int i=0;
        for (i=0; i < rsmd.getColumnCount(); i++) {
            String columnName = rsmd.getColumnName(i+1);
            if (!columnName.equals("id") && !columnName.equals("task_id") && 
                    !columnName.equals("stage_class")) {
                 attrs.add(new Attribute(columnName, i));
            }           
        }
        attrs.add(new Attribute("stage_class", i));
        return attrs;
    }
    
    private Instances fillInstances(Instances instances) throws SQLException {
        DBConnector db = new DBConnector();
        ResultSetMetaData rsmd = db.getTableMeta(tableName);
        List<HashMap<String, Object>> tableData = db.getTableData(tableName);
            for (HashMap<String, Object> rowData : tableData) {
                instances.add(getWekaInstance(rowData));
        }
        return null;
    }
    
    private Instance getWekaInstance(HashMap<String, Object> rowData) throws SQLException{
        ArrayList<Attribute> attrs = getAttrsList();
        Instance inst = new DenseInstance(attrs.size());
        for (int i = 0; i < attrs.size(); i++) {
            double value = Double.parseDouble(rowData.get(attrs.get(i).name()).toString());
            inst.setValue(attrs.get(i), value);
        }
        return inst;
    }
}
/*
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
