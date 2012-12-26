package com.hortonworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Committer;
import com.hortonworks.repository.CommitterRepository;

@Service
public class CommitterService {
	@Autowired
	CommitterRepository repo;

	public Iterable<Committer> findAll() {
		Iterable<Committer> commiters = repo.findAll();
		return commiters;
	}

	public void save(Committer committer) {
		repo.save(committer);
	}

	public Committer findByEmail(String email) {
		if (email != null)
			email = email.replaceAll("[<,>]", "");
		Committer committer = repo.findByPropertyValue("email", email);
		return committer;
	}

	public Committer findByUserId(String userId) {
		Committer committer = repo.findByPropertyValue("userId", userId);
		if (committer != null)
			System.out.println(committer.getUserId());
		return committer;
	}

	public Committer findByAlias(String alias) {
		String query = "*" + alias + "*";
		return repo.findAllByQuery("aliases", query).single();
	}
}
