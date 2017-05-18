package com.inetelligencemodule.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.inetelligencemodule.models.Employee;
import com.inetelligencemodule.dao.InterfaceDataDao;
import com.inetelligencemodule.models.AbstractStageModel;
import com.inetelligencemodule.models.LoanApprovalStage;

public class DataServicesImpl implements InterfaceDataServices {

    @Autowired
    InterfaceDataDao dataDao;

    public AbstractStageModel getEntityById(long id) throws Exception {
        return dataDao.getEntityById(id);
    }

    @Override
    public List<AbstractStageModel> getEntityList() throws Exception {
        return dataDao.getEntityList();
    }

    @Override
    public boolean addEntity(AbstractStageModel stageModel) throws Exception {
        return dataDao.addEntity(stageModel);
    }

    @Override
    public boolean updateEntity(AbstractStageModel stageModel) throws Exception {
        return dataDao.updateEntity(stageModel);
    }
}
