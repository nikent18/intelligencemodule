/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.controller.RestController;
import com.inetelligencemodule.database.DBConnector;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 *
 * @author nikita
 */
public class DeepLearningTrainer extends Trainer {

    @Override
    public void trainModel(String tableName) {
        File tmpFile = null;
        try {
            Utils utils = new Utils();
            tmpFile = utils.prepareFileToTraining(tableName);
            RecordReader recordReader = new CSVRecordReader(0, ",");
            recordReader.initialize(new FileSplit(tmpFile));
            //Second: the RecordReaderDataSetIterator handles conversion to DataSet objects, ready for use in neural network
            int labelIndex = 10;     //5 values in each row of the iris.txt CSV: 4 input features followed by an integer label (class) index. Labels are the 5th value (index 4) in each row
            int numClasses = 6;     //3 classes (types of iris flowers) in the iris data set. Classes have integer values 0, 1 or 2
            int batchSize = 25500;    //Iris data set: 150 examples total. We are loading all of them into one DataSet (not recommended for large data sets)
            final int numInputs = 10;
            int numHiddenNodes = 30;
            int outputNum = 6;
            int iterations = 1000;
            long seed = 123;
            int N = 0;
            System.out.println("Build model....");
            MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                    .seed(seed)
                    .iterations(iterations)
                    .activation(Activation.TANH)
                    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                    .weightInit(WeightInit.XAVIER)
                    .learningRate(0.1)
                    .regularization(true).l2(1e-4)
                    .list()
                    .layer(N++, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.RELU)
                            .build())
                    .layer(N++, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.RELU)
                            .build())
                    .layer(N++, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.RELU)
                            .build())
                    .layer(N++, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.RELU)
                            .build())
                    .layer(N++, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                            .weightInit(WeightInit.XAVIER)
                            .activation(Activation.SOFTMAX).weightInit(WeightInit.XAVIER)
                            .nIn(numHiddenNodes).nOut(outputNum).build())
                    .pretrain(true).backprop(true).build();
            
            //run the model
            MultiLayerNetwork model = new MultiLayerNetwork(conf);
            model.init();
            model.setListeners(new ScoreIterationListener(100));
            
            
            
            DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, batchSize, labelIndex, numClasses);
            DataSet allData = iterator.next();
            DataSet trainingData = null;
            DataSet testData = null;
            double accuracy = 0;
            //     for (int i=0; i < 10; i++) {
            allData.shuffle();
            SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.90); //Use 85% of data for training
            trainingData = testAndTrain.getTrain();
            testData = testAndTrain.getTest();
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(trainingData);           //Collect the statistics (mean/stdev) from the training data. This does not modify the input data
            normalizer.transform(trainingData);     //Apply normalization to the training data
            normalizer.transform(testData);         //Apply normalization to the test data. This is using statistics calculated from the *training* set
            model.fit(trainingData);

            //evaluate the model on the test set
            Evaluation eval = new Evaluation(outputNum);
            INDArray output = model.output(testData.getFeatureMatrix());
            eval.eval(testData.getLabels(), output);
            System.out.println(output);
            System.out.println(eval.stats());
            accuracy+= eval.accuracy();
            //     }
            //      System.out.println(accuracy/10.0);
        } catch (IOException ex) {
            Logger.getLogger(DeepLearningTrainer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DeepLearningTrainer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //tmpFile.delete();
        }
    }
    
    
    
}
