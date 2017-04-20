package com.inetelligencemodule.dao;

import com.inetelligencemodule.models.AbstractStageModel;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import com.inetelligencemodule.models.Employee;
import com.inetelligencemodule.models.LoanApprovalStage;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanApprovalDao implements InterfaceDataDao {

    @Autowired
    SessionFactory sessionFactory;

    Session session = null;
    Transaction tx = null;

    @Override
    public AbstractStageModel getEntityById(long id) throws Exception {
        session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(LoanApprovalStage.class);
                
        AbstractStageModel stage = (LoanApprovalStage)criteria
                .add(Restrictions.eq("stageId", id)).uniqueResult();
        tx = session.getTransaction();

        session.beginTransaction();
        tx.commit();
        return stage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AbstractStageModel> getEntityList() throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        List<AbstractStageModel> loanApprovalList = session.createCriteria(LoanApprovalStage.class)
                .list();
        tx.commit();
        session.close();
        return loanApprovalList;
    }

    @Override
    public boolean addEntity(AbstractStageModel stageModel) throws Exception {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        session.save(stageModel);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean updateEntity(AbstractStageModel stageModel) throws Exception {
        long stageId = stageModel.getStageId();
        String stageClass = stageModel.getStageClass();
        
        
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(LoanApprovalStage.class);
                
        AbstractStageModel existingStageModel = (LoanApprovalStage)criteria
                .add(Restrictions.eq("stageId", stageId)).uniqueResult();
        existingStageModel.setStageClass(stageClass);
        session.update(existingStageModel);
        tx.commit();
        session.close();
        return true;
    }

    /*
	@Override
	public boolean addEntity(Employee employee) throws Exception {

		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		session.save(employee);
		tx.commit();
		session.close();

		return false;
	}

	@Override
	public Employee getEntityById(long id) throws Exception {
		session = sessionFactory.openSession();
		Employee employee = (Employee) session.load(Employee.class,
				new Long(id));
		tx = session.getTransaction();
               
                
		session.beginTransaction();
		tx.commit();
		return employee;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getEntityList() throws Exception {
		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		List<Employee> employeeList = session.createCriteria(Employee.class)
				.list();
		tx.commit();
		session.close();
		return employeeList;
	}
	
	@Override
	public boolean deleteEntity(long id)
			throws Exception {
		session = sessionFactory.openSession();
		Object o = session.load(Employee.class, id);
		tx = session.getTransaction();
		session.beginTransaction();
		session.delete(o);
		tx.commit();
		return false;
	}
     */
}
