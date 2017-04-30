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
@Table(name = "demo_stage")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LoanApprovalStage extends AbstractStageModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected long id;
    
    @Column(name = "age")
    private int age;

    @Column(name = "income")
    private int income;

    @Column(name = "demand")
    private int demand;

    @Column(name = "area")
    private String area;

    @Column(name = "stage_class")
    protected String stageClass;
    
    @Column(name = "stage_id")
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

    public void setAge(int age) {
        this.age = age;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAge() {
        return age;
    }

    public int getIncome() {
        return income;
    }

    public int getDemand() {
        return demand;
    }

    public String getArea() {
        return area;
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
        inst.setValue(attrs.get("age"), this.age);
        inst.setValue(attrs.get("income"), this.income);
        inst.setValue(attrs.get("demand"), this.demand);
        inst.setValue(attrs.get("area"), this.area);
        if (this.stageClass != null && this.stageClass != "") {
            inst.setValue(attrs.get("stageClass"), (this.stageClass));
        }
        return inst;
    }
    
    public ArrayList<Attribute> getWekaAttrsList() {
        String[] attributesList = {"age", "income", "demand", 
                                        "area", "stageClass"};
        ArrayList attrs = new ArrayList<>();
        Map attrsInfo = getAttributes();
        for (int i=0; i< attributesList.length; i++) {
            attrs.add(attrsInfo.get(attributesList[i]));
        }
        return attrs;
    }
    
    protected Map<String, Attribute> getAttributes() {
        Map<String, Attribute> attrs = new HashMap<>();
        attrs.put("age", new Attribute("age", 0));
        attrs.put("income", new Attribute("income", 1));
        attrs.put("demand", new Attribute("demand", 2));
        attrs.put("area", new Attribute("area", 3));
        List classValues = new ArrayList(3); 
        classValues.add("yes");
        classValues.add("no");
        attrs.put("stageClass", new Attribute("stageClass", classValues, 4));
        return attrs;
    }
    
    public ArrayList<String> getClassValues() {
        ArrayList classValues = new ArrayList(3); 
        classValues.add("yes");
        classValues.add("no");
        return  classValues;
    }    
}
