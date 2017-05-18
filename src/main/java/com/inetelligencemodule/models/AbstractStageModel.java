/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import weka.core.Attribute;
import weka.core.Instance;

/**
 *
 * @author nikita
 */
public abstract class AbstractStageModel implements Serializable {

    private static final long serialVersionUID = 1L;
    public abstract long getId();
    public abstract void setStageClass(String stageClass);
    public abstract String getStageClass();
    public abstract long getStageId();
    public abstract Instance getWekaInstance();
    protected abstract Map<String, Attribute> getAttributes();
    public abstract ArrayList<Attribute> getWekaAttrsList();
    public abstract ArrayList<String> getClassValues();
    public abstract String getArea();
    
}
