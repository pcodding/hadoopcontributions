package com.hortonworks.domain;

import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Contributor extends Person {
	@RelatedTo(type = "committed")
	private Set<Commit> commits;

	public Contributor() {
	}

	public Contributor(String firstName, String lastName, String email) {
		super(firstName, lastName, email);
	}

	public Set<Commit> getCommits() {
		return commits;
	}

	public void setCommits(Set<Commit> commits) {
		this.commits = commits;
	}
}
