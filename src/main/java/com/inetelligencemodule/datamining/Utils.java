/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetelligencemodule.controller.RestController;
import com.inetelligencemodule.database.DBConnector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author nikita
 */
public class Utils {
    
    private static final String modelPath = "/home/nikita/NetBeansProjects/IntelligenceModule/files/ClassifierModels/deepModel";
    private static final String statisticPath = "/home/nikita/NetBeansProjects/IntelligenceModule/files/Statistic/deepStatistic";
    public static String getModelPath() {
        return modelPath;
    }
    
    public static String getStatisticPath() {
        return statisticPath;
    }

    public File prepareFileToTraining(String tableName) {
        DBConnector db = new DBConnector();
        BufferedWriter bw = null;
        File statisticFile = new File(statisticPath + "_" + tableName);

        try {
            statisticFile.createNewFile();
            ResultSetMetaData rsmd = db.getTableMeta(tableName);
            bw = new BufferedWriter(new FileWriter(statisticFile, true));
            List<HashMap<String, Object>> tableData = db.getTableData(tableName);
            for (HashMap<String, Object> rowData : tableData) {
                String[] strArr = new String[rsmd.getColumnCount() - 2];
                int i;
                int k=0;
                for (i=1; i < rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i+1);
                    if (!columnName.equals("task_id") && !columnName.equals("id")
                            && !columnName.equals("stage_class")) {
                        strArr[k++] = String.valueOf(rowData.get(columnName));
                    }
                }         
                strArr[k++] = String.valueOf(rowData.get("stage_class"));
                bw.write(String.join(",",  strArr)+"\n");
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return statisticFile;
    }
    
    public File prepareFileForClassify(String tableName, String jsonEntity) throws IOException, SQLException {
        DBConnector db = new DBConnector();       
        BufferedWriter bw = null;
        File tmpFile = new File("tmp_file_classify");
        HashMap<String,String> insertMap =
            new ObjectMapper().readValue(jsonEntity, HashMap.class);
               
        try {
            ResultSetMetaData rsmd = db.getTableMeta(tableName);
            tmpFile.createNewFile();
            HashMap<String, Object> rawInfo = db.getRowById(tableName,getTaskId(insertMap));
            bw = new BufferedWriter(new FileWriter(tmpFile));
            int i;
            int k=0;
            String[] strArr = new String[rsmd.getColumnCount() - 2];
            for (i=0; i < rsmd.getColumnCount(); i++) {                
                String columnName = rsmd.getColumnName(i+1);
                    if (!columnName.equals("stage_class") && !columnName.equals("id")
                            && !columnName.equals("task_id")) {
                        strArr[k++] = String.valueOf(rawInfo.get(columnName));
                    }                                       
            }
            strArr[k] = String.valueOf("0");
            bw.write(String.join(",",  strArr)+"\n");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return tmpFile;
    }    
    private String getTaskId(HashMap<String,String> insertMap) {
        String taskId = "";
        for (Map.Entry<String, String> entry : insertMap.entrySet()) {
            if (entry.getKey() == "task_id") {
                taskId = String.valueOf(entry.getValue());
            }
        }
        return taskId;
    }
}
