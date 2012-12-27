package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Employer;
import com.hortonworks.repository.EmployerRepository;

@Service
public class EmployerService {
	@Autowired
	EmployerRepository repo;

	public Iterable<Employer> findAll() {
		Iterable<Employer> commiters = repo.findAll();
		return commiters;
	}

	public void save(Employer employer) {
		repo.save(employer);
	}

	public Employer findByOrgName(String name) {
		Employer employer = repo.findByPropertyValue("orgName", name);
		return employer;
	}
}
