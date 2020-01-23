package com.gpch.login.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;

import com.gpch.login.model.Application;

@Repository("applicationDao")
public class ApplicationDao {
	
	@PersistenceContext
    private EntityManager entityManager;
 
    public Application selectApplicationById(int applicationId) {
        return entityManager.find(Application.class, applicationId);
    }
 
    public void insertApplication(Application application) {
        entityManager.persist(application);
    }
 
    public void updateApplication(Application application) {
 
        Application appToUpdate = selectApplicationById(application.getId());
 
        appToUpdate.setUser(application.getUser());
        appToUpdate.setFromDate(application.getFromDate());
        appToUpdate.setToDate(application.getToDate());
        appToUpdate.setNoOfTakenDays(application.getNoOfTakenDays());
        appToUpdate.setNoOfLeftDays(application.getNoOfLeftDays());
        appToUpdate.setDescription(application.getDescription());
        entityManager.flush();
    }
    
    public void approveApplication(int applicationId) {
    	 
        Application appToApprove = selectApplicationById(applicationId);
 
        appToApprove.setApproved(true);
        entityManager.flush();
    }
 
    public void deleteApplication(int applicationId) {
        entityManager.remove(selectApplicationById(applicationId));
    }
 
    @SuppressWarnings("unchecked")
	public List<Application> selectAllApplications() {
        Query query = (Query) entityManager.createQuery("from Application app order by app.fromDate");
        return (List<Application>) query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
   	public List<Application> selectUserApplications(int userId) {
           Query query = (Query) entityManager.createQuery("from Application app where app.user.id = " + userId);
           return (List<Application>) query.getResultList();
       }

}
