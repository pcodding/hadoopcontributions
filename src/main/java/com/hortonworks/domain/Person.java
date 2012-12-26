package com.hortonworks.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.hortonworks.utils.Util;

@NodeEntity
public class Person {
	@GraphId
	Long nodeId;
	public String firstName;
	public String middleInitial;
	public String lastName;
	@Indexed
	public String email;
	public Employer employer;
	@RelatedTo(type = "committed", direction = Direction.BOTH)
	private Set<Commit> commits = new HashSet<Commit>();
	@Indexed
	protected String aliases = new String();
	@Indexed
	protected String name;

	public Person() {
	}

	public Person(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = firstName + " " + lastName;
		this.email = Util.normalizeEmail(email);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null)
			this.email = Util.normalizeEmail(email);
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public Set<Commit> getCommits() {
		return commits;
	}

	public void setCommits(Set<Commit> commits) {
		this.commits = commits;
	}

	public String[] getAliases() {
		if (aliases != null)
			return aliases.split("|");
		else
			return new String[0];
	}

	public void addAlias(String alias) {
		this.aliases += (alias + "|");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
