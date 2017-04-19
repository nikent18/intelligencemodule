/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

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
    protected long stageId;

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
    
    public void setStageId(int approvalId) {
        this.stageId = approvalId;
    }

    public long getStageId() {
        return stageId;
    }

}
