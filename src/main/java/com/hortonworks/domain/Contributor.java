package com.hortonworks.domain;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Contributor extends Person {

	public Contributor() {
	}

	public Contributor(String name) {
		super();
		this.name = name;
	}

	public Contributor(String firstName, String lastName, String email) {
		super(firstName, lastName, email);
	}
}
