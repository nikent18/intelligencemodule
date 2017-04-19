/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inetelligencemodule.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
