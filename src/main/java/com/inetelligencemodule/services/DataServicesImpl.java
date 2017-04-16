package com.inetelligencemodule.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.inetelligencemodule.models.Employee;
import com.inetelligencemodule.dao.InterfaceDataDao;
import com.inetelligencemodule.models.AbstractStageModel;

public class DataServicesImpl implements InterfaceDataServices {

	@Autowired
	InterfaceDataDao dataDao;
        
        public AbstractStageModel getEntityById(long id) throws Exception {
		return dataDao.getEntityById(id);
	}
        
	/*
	@Override
	public boolean addEntity(Employee employee) throws Exception {
		return dataDao.addEntity(employee);
	}

	@Override
	public Employee getEntityById(long id) throws Exception {
		return dataDao.getEntityById(id);
	}

	@Override
	public List<Employee> getEntityList() throws Exception {
		return dataDao.getEntityList();
	}

	@Override
	public boolean deleteEntity(long id) throws Exception {
		return dataDao.deleteEntity(id);
	}*/

}
