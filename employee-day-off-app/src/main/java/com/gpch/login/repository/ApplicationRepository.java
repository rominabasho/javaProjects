package com.gpch.login.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gpch.login.model.Application;

@Repository("applicationRepository")
public interface ApplicationRepository extends CrudRepository<Application, Long> {
      Application findByUserLastName(String lastName);
}
