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

	public void save(Commit commit) {
		repo.save(commit);
	}

	public Commit findCommitByCommitId(String commitId) {
		return repo.findByPropertyValue("commitId", commitId);
	}

	public Commit findByJiraId(String jiraId) {
		return repo.findByJiraId(jiraId);
	}

	public Iterable<Commit> findAllByJiraId(String jiraId) {
		return repo.findAllByJiraId(jiraId);
	}
}
