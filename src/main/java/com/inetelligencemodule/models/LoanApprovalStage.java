/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

@Entity
@Table(name = "loan_approval_stage")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanApprovalStage extends AbstractStageModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected long id;
    
    @Column(name = "sepallength")
    private double sepallength;

    @Column(name = "sepalwidth")
    private double sepalwidth;

    @Column(name = "petallength")
    private double petallength;

    @Column(name = "petalwidth")
    private double petalwidth;

    @Column(name = "stage_class")
    protected String stageClass;
    
    @Column(name = "approval_id")
    protected Long stageId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStageClass() {
        return stageClass;
    }

    public void setStageClass(String stageClass) {
        this.stageClass = stageClass;
    }

    public void setSepallength(double sepallength) {
        this.sepallength = sepallength;
    }

    public void setSepalwidth(double sepalwidth) {
        this.sepalwidth = sepalwidth;
    }

    public void setPetallength(double petallength) {
        this.petallength = petallength;
    }

    public void setPetalwidth(double petalwidth) {
        this.petalwidth = petalwidth;
    }

    public double getSepallength() {
        return sepallength;
    }

    public double getSepalwidth() {
        return sepalwidth;
    }

    public double getPetallength() {
        return petallength;
    }

    public double getPetalwidth() {
        return petalwidth;
    }
    
    public void setStageId(Long approvalId) {
        this.stageId = approvalId;
    }

    public long getStageId() {
        return stageId;
    }
    
    public Instance getWekaInstance() {
        Map<String, Attribute> attrs = getAttributes();
        Instance inst = new DenseInstance(attrs.size());
        inst.setValue(attrs.get("sepallength"), this.sepallength);
        inst.setValue(attrs.get("sepalwidth"), this.sepalwidth);
        inst.setValue(attrs.get("petallength"), this.petallength);
        inst.setValue(attrs.get("petalwidth"), this.petalwidth);
        inst.setValue(attrs.get("stageClass"), this.stageClass);
        return inst;
    }
    
    public ArrayList<Attribute> getWekaAttrsList() {
        return new ArrayList<>(getAttributes().values());
    }
    
    protected Map<String, Attribute> getAttributes() {
        Map<String, Attribute> attrs = new HashMap<>();
        attrs.put("sepallength", new Attribute("sepallength", 0));
        attrs.put("sepalwidth", new Attribute("sepalwidth", 1));
        attrs.put("petallength", new Attribute("petallength", 2));
        attrs.put("petalwidth", new Attribute("petalwidth", 3));
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("setosa");
        classValues.add("versicolor");
        classValues.add("virginica");
        attrs.put("stageClass", new Attribute("stageClass", classValues, 4));
        return attrs;
    }
    
    

}
