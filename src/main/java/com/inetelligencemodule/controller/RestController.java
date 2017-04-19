package com.inetelligencemodule.controller;

import com.inetelligencemodule.datamining.ClassifierAccurancy;
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

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    String test() {
        
 
        List<LoanApprovalStage> stageList = null;
        try {
            stageList = dataServices.getEntityList();

        } catch (Exception e) {
            e.printStackTrace();
        }

  //      return stageList;
        
        ClassifierAccurancy ca = new ClassifierAccurancy();
        try {
            ca.process();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(RestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "wqeqw";
    }
 
    @RequestMapping(value = "/addLoanApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String addLoanApproval(@RequestBody LoanApprovalStage stage) {
        try {
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
