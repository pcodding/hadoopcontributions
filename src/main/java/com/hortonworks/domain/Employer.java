package com.hortonworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Employer {
	@GraphId
	Long nodeId;
	@RelatedTo(type = "contributors", direction = Direction.INCOMING)
	private Set<Contributor> contributors = new HashSet<Contributor>();

	@RelatedTo(type = "committers", direction = Direction.INCOMING)
	private Set<Committer> committers = new HashSet<Committer>();

	@Indexed(indexName="orgName")
	public String orgName;

	public Employer() {

	}

	public Employer(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Set<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}

	public Set<Committer> getCommitters() {
		return committers;
	}

	public void setCommitters(Set<Committer> committers) {
		this.committers = committers;
	}

	public String toString() {
		return new ToStringBuilder(this).toString();
	}
}
