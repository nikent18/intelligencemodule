package com.inetelligencemodule.controller;

import com.inetelligencemodule.database.DBConnector;
import com.inetelligencemodule.datamining.Classification;
import com.inetelligencemodule.datamining.ClassifierAccurancy;
import com.inetelligencemodule.datamining.DeepClassification;
import com.inetelligencemodule.datamining.DeepLearningTrainer;
import com.inetelligencemodule.datamining.Trainer;
import com.inetelligencemodule.models.AbstractStageModel;
import java.util.List;

import org.apache.log4j.Logger;
import com.inetelligencemodule.models.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inetelligencemodule.models.Employee;
import com.inetelligencemodule.models.LoanApprovalStage;
import com.inetelligencemodule.services.InterfaceDataServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

@Controller
@RequestMapping("/loan_stage")
public class RestController {

    @Autowired
    InterfaceDataServices dataServices;

    static final Logger logger = Logger.getLogger(RestController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String getLoanApprovalStageById(@PathVariable("id") long id) {
        LoanApprovalStage stage = null;
        try {
            stage = (LoanApprovalStage) dataServices.getEntityById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stage.getStageClass();
    }

    @RequestMapping(value = "/qwetest/{tableName}", method = RequestMethod.GET)
    public @ResponseBody
    String test(@PathVariable("tableName") String tableName) {
        DBConnector db = new DBConnector();
        try {
            List tmp = db.getTableData(tableName);
            System.out.println(tmp.size());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Testok";
    }
    
    @RequestMapping(value = "/addEntity/{tableName}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String addEntity(@PathVariable("tableName") String tableName, @RequestBody String entity) {
        DBConnector db = new DBConnector();
        try {
            db.insertEntity(tableName, entity);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Testok";
    }
    
    @RequestMapping(value = "/updateEntity/{tableName}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String updateEntity(@PathVariable("tableName") String tableName, @RequestBody String entity) {
        DBConnector db = new DBConnector();
        try {
            db.updateEntity(tableName, entity);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Testok";
    }
    
    @RequestMapping(value = "/classifyMode/{tableName}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String classifyEntity(@PathVariable("tableName") String tableName, @RequestBody String entity) {
        try {
            DeepClassification dc = new DeepClassification();
            return dc.classify(tableName, entity);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
 
    @RequestMapping(value = "/addLoanApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String addLoanApproval(@RequestBody LoanApprovalStage stage) {
        try {
            System.out.println(123);
            dataServices.addEntity(stage);
            return "ok";
        } catch (Exception e) {
            // e.printStackTrace();
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "/updateLoanApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String updateLoanApproval(@RequestBody LoanApprovalStage stage) {
        try {
            dataServices.updateEntity(stage);
            return "ok";
        } catch (Exception e) {
            // e.printStackTrace();
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = "/trainModel/{tableName}", method = RequestMethod.GET)
    public @ResponseBody
    String trainModel(@PathVariable("tableName") String tableName) {       
        DeepLearningTrainer dlt = new DeepLearningTrainer();
        dlt.trainModel(tableName);
        return "done";
    }
/*
    @RequestMapping(value = "/classifyLoanApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String classifyLoanApproval(@RequestBody LoanApprovalStage stage) {
        try {
            List<AbstractStageModel> stageList = dataServices.getEntityList();
            LoanApprovalStage.setWekaAttributes(stageList);
            Classification cl = new Classification();
            return cl.classify(stage, "dwqd");
        } catch (Exception e) {
            return e.getMessage();
        }
    }*/
    
    /*
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Status addEmployee(@RequestBody Employee employee) {
        try {
            dataServices.addEntity(employee);
            return new Status(1, "Employee added Successfully !");
        } catch (Exception e) {
            // e.printStackTrace();
            return new Status(0, e.toString());
        }

    }
    
    String getEmployee(@PathVariable("id") long id) {
        Employee employee = null;
        try {
            employee = dataServices.getEntityById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee.getEmail();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public @ResponseBody
    List<Employee> getEmployee() {

        List<Employee> employeeList = null;
        try {
            employeeList = dataServices.getEntityList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public @ResponseBody
    Status deleteEmployee(@PathVariable("id") long id) {

        try {
            dataServices.deleteEntity(id);
            return new Status(1, "Employee deleted Successfully !");
        } catch (Exception e) {
            return new Status(0, e.toString());
        }

    }*/
}
