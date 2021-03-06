/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inetelligencemodule.config.DatabaseConfigs;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.*;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Simple Java program to connect to MySQL database running on localhost and
 * running SELECT and INSERT query to retrieve and add data.
 *
 * @author Javin Paul
 */
public class DBConnector {

    // JDBC URL, username and password of MySQL server
    private static final String url = "jdbc:mysql://localhost:3306/int_module";
    private static final String user = "root";
    private static final String password = "pass";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public DBConnector() {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);
            // getting Statement object to execute query
            stmt = con.createStatement();
        } catch (SQLException sqlEx) {

        }
    }

    public void insertEntity(String tableName, String json) throws IOException, SQLException {
        HashMap<String, String> insertMap
                = new ObjectMapper().readValue(json, HashMap.class);
        String query = "INSERT INTO " + tableName + " (";

        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> columnValues = new ArrayList<>();
        ArrayList<String> questionMarks = new ArrayList<>();
        for (Map.Entry<String, String> entry : insertMap.entrySet()) {
            columnNames.add(entry.getKey().toString());
            columnValues.add(transformInput(entry.getValue(), tableName, entry.getKey()));
            questionMarks.add("?");
        }

        query += String.join(",", columnNames);
        query += ") VALUES (" + String.join(",", questionMarks) + " )";

        PreparedStatement preparedStatement = null;
        preparedStatement = con.prepareStatement(query);

        for (int i = 0; i < columnValues.size(); i++) {
            preparedStatement.setObject(i + 1, columnValues.get(i));
        }
        preparedStatement.execute();
    }

    public void updateEntity(String tableName, String json) throws IOException, SQLException {
        HashMap<String, String> insertMap
                = new ObjectMapper().readValue(json, HashMap.class);
        String query = "UPDATE " + tableName;
        String where = "";
        String setCol = "";
        Object stageId = null;
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<String> columnValues = new ArrayList<>();
        ArrayList<String> questionMarks = new ArrayList<>();
        for (Map.Entry<String, String> entry : insertMap.entrySet()) {
            String value = transformInput(entry.getValue(), tableName, entry.getKey());
            if (entry.getKey() == DatabaseConfigs.getStageIdColumnName()) {
                where = " WHERE " + DatabaseConfigs.getStageIdColumnName() + "=?";
                stageId = entry.getValue();
            } else {
                if (setCol.equals("")) {
                    setCol = " SET " + entry.getKey() + "=" + value;
                } else {
                    setCol += " , " + entry.getKey() + "=" + value;
                }
            }
        }
        query += setCol + where;
        PreparedStatement preparedStatement = null;
        preparedStatement = con.prepareStatement(query);
        preparedStatement.setObject(1, stageId);
        preparedStatement.execute();
    }

    public List<HashMap<String, Object>> getTableData(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        rs = stmt.executeQuery(query);
        List<HashMap<String, Object>> data = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            HashMap<String, Object> rowInfo = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                if (columnName.equals("id") || columnName.equals("task_id")) {
                    continue;
                }
                rowInfo.put(columnName, rs.getObject(columnName));
            }
            data.add(rowInfo);
        }
        return data;
    }

    public ResultSetMetaData getTableMeta(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        rs = stmt.executeQuery(query);
        List<HashMap<String, Object>> data = new ArrayList<>();
        return rs.getMetaData();
    }
    
    public HashMap<String, Object> getRowById(String tableName, String task_id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE task_id="+String.valueOf(task_id);
        rs = stmt.executeQuery(query);
        HashMap<String, Object> rowInfo = new HashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        if (rs.next()) {           
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                if (columnName.equals("id") || columnName.equals("task_id")) {
                    continue;
                }
                rowInfo.put(columnName, rs.getObject(columnName));
            }
        }
        return rowInfo;
    }

    public String transformInput(Object value, String tableName, String columnName) throws SQLException {
        ResultSetMetaData rsmd = getTableMeta(tableName);
        String returnValue = String.valueOf(value);
        if (!isNumeric(returnValue)) {
            returnValue = String.valueOf(getWordId(returnValue, tableName, columnName));
        }

        return returnValue;
    }
    
    private int getWordId(String word, String tableName, String columnName) throws SQLException {
        String query = "SELECT word_id FROM " + getWord2TableName(tableName, columnName) + " WHERE word='" + word + "'";
        rs = stmt.executeQuery(query);
        if (!rs.next()) {
            return addWord(word, tableName, columnName);
        } else {
            return rs.getInt("word_id");
        }
    }

    private int addWord(String word, String tableName, String columnName) throws SQLException {
        String query = "SELECT max(word_id) as word_id FROM " + getWord2TableName(tableName, columnName);
        rs = stmt.executeQuery(query);

        int id = 0;
        if (rs.next()) {
            id = rs.getInt("word_id") + 1;
        }
        String insertQuery = "INSERT INTO " + getWord2TableName(tableName, columnName)
                + " VALUES (" + id + ", '" + word + "')";
        stmt.executeUpdate(insertQuery);
        return id;
    }

    private String getWord2TableName(String tableName, String columnName) {
        return tableName + "_" + columnName;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public String getClassLabelById(int classId, String tableName) throws SQLException {
        String query = "SELECT word FROM " + getWord2TableName(tableName, "stage_class") + " WHERE word_id=" + String.valueOf(classId);
        rs = stmt.executeQuery(query);

        int id = 0;
        if (rs.next()) {
            return rs.getString("word");
        }
        return "Classification error";
    }
}
