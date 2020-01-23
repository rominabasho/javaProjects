package com.gpch.login.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.gpch.login.model.Application;
import com.gpch.login.dao.*;

@Service("applicationService")
public class ApplicationService {

	@Autowired
    @Qualifier("applicationDao")
    private ApplicationDao applicationDao;
 
    @Transactional
    public Application getApplicationById(int applicationId) {
 
        return applicationDao.selectApplicationById(applicationId);
    }
 
    @Transactional
    public void addApplication(Application application) {
    	applicationDao.insertApplication(application);
    }
 
    @Transactional
    public void modifyApplication(Application application) {
    	applicationDao.updateApplication(application);
    }
    
    @Transactional
    public void approveApplication(int applicationId) {
    	applicationDao.approveApplication(applicationId);
    }
 
    @Transactional
    public List<Application> getAllApplications() {
        return applicationDao.selectAllApplications();
 
    }
    
    @Transactional
    public List<Application> getUserApplications(int userId) {
        return applicationDao.selectUserApplications(userId);
 
    }
 
    @Transactional
    public void removeApplication(int applicationId) {
    	applicationDao.deleteApplication(applicationId);
 
    }
 
}
