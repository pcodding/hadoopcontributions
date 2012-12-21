package com.hortonworks.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Change {
	@GraphId
	Long nodeId;
	public int added;
	public int deleted;
	public String path;

	public Change() {

	}

	public Change(int added, int deleted, String path) {
		super();
		this.added = added;
		this.deleted = deleted;
		this.path = path;
	}

	public Change(String line) {
		String[] pieces = line.split("\t");
		added = Integer.parseInt(pieces[0]);
		deleted = Integer.parseInt(pieces[1]);
		path = pieces[2];
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
