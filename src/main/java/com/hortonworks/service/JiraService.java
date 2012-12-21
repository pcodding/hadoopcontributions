package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Committer;
import com.hortonworks.domain.Jira;
import com.hortonworks.repository.JiraRepository;

@Service
public class JiraService {
	@Autowired
	JiraRepository repo;

	public Iterable<Jira> findAll() {
		Iterable<Jira> jiras = repo.findAll();
		return jiras;
	}

	public void save(Jira jira) {
		repo.save(jira);
	}

	public Jira findByJiraId(String jiraId) {
		Jira jira = repo.findByPropertyValue("jiraId", jiraId);
		return jira;
	}
}

