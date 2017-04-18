package com.inetelligencemodule.dao;

import com.inetelligencemodule.models.AbstractStageModel;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDataDao {

    @Autowired
    SessionFactory sessionFactory;

    Session session = null;
    Transaction tx = null;

    public AbstractDataDao() {
        session = sessionFactory.openSession();
    }

    public abstract AbstractStageModel getEntityById(long id) throws Exception;

   
    /*
	public boolean addEntity(Employee employee) throws Exception;
	public Employee getEntityById(long id) throws Exception;
	public List<Employee> getEntityList() throws Exception;
	public boolean deleteEntity(long id) throws Exception;*/
}
