package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Commit;
import com.hortonworks.repository.CommitRepository;

@Service
public class CommitService {
	@Autowired
	CommitRepository repo;

	public Iterable<Commit> findAll() {
		Iterable<Commit> commits = repo.findAll();
		return commits;
	}
}
