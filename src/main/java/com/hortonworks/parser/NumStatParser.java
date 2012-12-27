package com.hortonworks.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Commit;
import com.hortonworks.domain.Project;
import com.hortonworks.service.ProjectService;

@Service
public class NumStatParser {
	public static Logger logger = Logger.getLogger("NumStatParser");
	private String projectName;
	private String filePath;
	private String contributorsMetadataPath;

	@Autowired
	private CommitParser parser;

	@Autowired
	private ContributorMetadataParser contributorMetadataParser;

	@Autowired
	ProjectService projectService;

	public NumStatParser() {

	}

	public NumStatParser(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @throws IOException
	 */
	public void parseData() throws IOException {
		logger.debug("Resolving project by name: " + projectName);
		Project project = projectService.findByName(projectName);
		if (project == null)
			project = new Project(projectName);
		parser.setProject(project);
		logger.info("Parsing numstat data from file: " + filePath);
		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath), "UTF8"));
		String currentLine = null;
		while ((currentLine = input.readLine()) != null) {
			parser.parseCommit(currentLine);
		}
		LinkedList<Commit> commits = parser.completeParse();
		logger.info("Processed " + parser.rowCount + " stored "
				+ parser.project.getCommits().size());
	}

	/**
	 * @throws IOException
	 */
	public void parseContributorMetadata() throws IOException {
		logger.info("Parsing contributor meta data from file: "
				+ contributorsMetadataPath);
		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(contributorsMetadataPath), "UTF8"));
		String currentLine = null;
		while ((currentLine = input.readLine()) != null) {
			contributorMetadataParser.parseMetaData(currentLine);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0 || args.length < 3)
			printUsage();
		else {
			String projectName = args[0];
			String numstatFilePath = args[1];
			String contributorMetadataPath = args[2];
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			NumStatParser parser = (NumStatParser) context
					.getBean("numStatParser");
			parser.setProjectName(projectName);
			parser.setFilePath(numstatFilePath);
			parser.setContributorsMetadataPath(contributorMetadataPath);
			try {
				parser.parseContributorMetadata();
				parser.parseData();
				((ClassPathXmlApplicationContext) context).close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContributorsMetadataPath() {
		return contributorsMetadataPath;
	}

	public void setContributorsMetadataPath(String contributorsMetadataPath) {
		this.contributorsMetadataPath = contributorsMetadataPath;
	}

	public CommitParser getParser() {
		return parser;
	}

	public void setParser(CommitParser parser) {
		this.parser = parser;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public static void printUsage() {
		System.err
				.println("com.hortonworks.parser.NumStatParser <path to numstat> <path to authors>");
	}
}
