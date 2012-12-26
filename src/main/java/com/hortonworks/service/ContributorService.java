package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Contributor;
import com.hortonworks.repository.ContributorRepository;

@Service
public class ContributorService {
	@Autowired
	ContributorRepository repo;

	public Iterable<Contributor> findAll() {
		Iterable<Contributor> commiters = repo.findAll();
		return commiters;
	}

	public void save(Contributor contributor) {
		repo.save(contributor);
	}

	public Contributor findByname(String name) {
		Contributor contributor = repo.findByPropertyValue("name", name);
		return contributor;
	}
}
