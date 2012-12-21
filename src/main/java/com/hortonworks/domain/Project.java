package com.hortonworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Project {
	@GraphId
	Long nodeId;
	private String name;
	@RelatedTo(type = "commits")
	private Set<Commit> commits = new HashSet<Commit>();

	public Project() {
	}

	public Project(String name) {
		this.name = name;
	}

	public Set<Commit> getCommits() {
		return commits;
	}

	public void setCommits(Set<Commit> commits) {
		this.commits = commits;
	}	
}

