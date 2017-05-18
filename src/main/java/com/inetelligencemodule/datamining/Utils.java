/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.datamining;

import com.inetelligencemodule.controller.RestController;
import com.inetelligencemodule.database.DBConnector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author nikita
 */
public class Utils {

    public File prepareFileToTraining(String tableName) {
        DBConnector db = new DBConnector();
        BufferedWriter bw = null;
        File tmpFile = new File("tmp_file");

        try {
            tmpFile.createNewFile();
            bw = new BufferedWriter(new FileWriter(tmpFile, true));
            List<HashMap<String, Object>> tableData = db.getTableData(tableName);
            for (HashMap<String, Object> rowData : tableData) {
                String[] strArr = new String[rowData.keySet().size()];
                int i=0;
                for (String columnName : rowData.keySet()) {
                    if (!columnName.equals("carid")) {
                        strArr[i++] = String.valueOf(rowData.get(columnName));
                    }
                }         
                strArr[i++] = String.valueOf(rowData.get("carid"));
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

        return tmpFile;
    }
}
