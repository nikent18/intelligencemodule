/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.database.DBConnector;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;

/**
 *
 * @author nikita
 */
public class DeepClassification {
    public String classify(String tableName, String entity) throws InterruptedException, SQLException {
        File tmpFile = null;
        Utils utils = new Utils();
        try {
            int labelIndex = 8;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
            int numClasses = 4;
            tmpFile = utils.prepareFileForClassify(tableName, entity);
            RecordReader recordReader = new CSVRecordReader(0, ",");
            recordReader.initialize(new FileSplit(tmpFile));
            DBConnector db = new DBConnector();       
            DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 1, labelIndex, numClasses);
            DataSet testData = iterator.next();
            
            RecordReader normalizeReader = new CSVRecordReader(0, ",");
            normalizeReader.initialize(new FileSplit(new File(Utils.getStatisticPath()+"_"+tableName)));
            
            DataSetIterator normalizeIterator = new RecordReaderDataSetIterator(normalizeReader, 15000, labelIndex, numClasses);
            DataSet normalizeData = normalizeIterator.next();
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(normalizeData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
            normalizer.transform(testData);     

            File modelPath = new File(Utils.getModelPath()+"_"+tableName);
            MultiLayerNetwork restored = ModelSerializer.restoreMultiLayerNetwork(modelPath);
            Evaluation eval = new Evaluation(3);
            INDArray output = restored.output(testData.getFeatureMatrix());
            eval.eval(testData.getLabels(), output);
            System.out.println("ARR " + eval.stats());
            INDArray ar = output.getRow(0);
            System.out.println("HELLO *(*(*(*(*(*");
            System.out.println("ARR " + ar);
            
            double max = 0;
            int classId = 0;
            for (int i = 0; i < ar.length(); i++) {
                if (ar.getDouble(i) > max) {
                    max = ar.getDouble(i);
                    classId = i;
                }
            }
            return db.getClassLabelById(classId, tableName);
       
        } catch (IOException ex) {
            Logger.getLogger(DeepClassification.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            tmpFile.delete();
        }
        return "";
    }
}
