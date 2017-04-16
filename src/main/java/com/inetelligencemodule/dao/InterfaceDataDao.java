package com.inetelligencemodule.dao;

import com.inetelligencemodule.models.AbstractStageModel;
import java.util.List;

import com.inetelligencemodule.models.Employee;

public interface InterfaceDataDao {

    public AbstractStageModel getEntityById(long id) throws Exception;

    /*
	public boolean addEntity(Employee employee) throws Exception;
	public Employee getEntityById(long id) throws Exception;
	public List<Employee> getEntityList() throws Exception;
	public boolean deleteEntity(long id) throws Exception;*/
}
