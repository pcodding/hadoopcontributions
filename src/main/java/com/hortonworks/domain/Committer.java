package com.hortonworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.hortonworks.utils.Util;

@NodeEntity
public class Committer extends Person {
	@RelatedTo(type = "committed", direction = Direction.BOTH)
	private Set<Commit> commits = new HashSet<Commit>();
	private Employer employer;
	@Indexed
	private String userId;

	public Committer() {
	}

	public Committer(String firstName, String lastName, String email) {
		super(firstName, lastName, email);
		this.userId = Util.normalizeEmail(email).split("@")[0];
	}

	public Committer(String name, String email) {
		super(null, null, email);
		this.firstName = name.split(" ")[0];
		try {
			this.lastName = name.split(" ")[1];
		} catch (Exception e) {
		}
		this.name = name;
		this.userId = Util.normalizeEmail(email).split("@")[0];
	}

	public Set<Commit> getCommits() {
		return commits;
	}

	public void setCommits(Set<Commit> commits) {
		this.commits = commits;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
