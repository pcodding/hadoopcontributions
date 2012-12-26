package com.hortonworks.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.hortonworks.domain.Commit;

@Service
public class NumStatParser {
	public static Logger logger = Logger.getLogger("NumStatParser");
	private String filePath;
	String authorsFilePath;
	String employersFilePath;

	@Autowired
	private CommitParser parser;

	@Autowired
	private AuthorParser authorParser;

	public NumStatParser() {

	}

	public NumStatParser(String filePath) {
		this.filePath = filePath;
	}

	public void parseData() throws IOException {
		logger.info("Parsing numstat data from file: " + filePath);
		BufferedReader input = new BufferedReader(new FileReader(filePath));
		String currentLine = null;
		while ((currentLine = input.readLine()) != null) {
			// logger.debug(currentLine);
			parser.parseCommit(currentLine);
		}
		LinkedList<Commit> commits = parser.completeParse();
		logger.info("Processed " + parser.rowCount + " stored "
				+ parser.project.getCommits().size());
		// System.out.println(parser.project.getCommits());
		// System.out.println(commits);
	}

	public void parseAuthors() throws IOException {
		logger.info("Parsing author data from file: " + authorsFilePath);
		BufferedReader input = new BufferedReader(new FileReader(
				authorsFilePath));
		String currentLine = null;
		while ((currentLine = input.readLine()) != null) {
			// logger.debug(currentLine);
			authorParser.parseAuthor(currentLine);
		}
		authorParser.completeParse();
	}

	public static void main(String[] args) {
		if (args.length == 0 || args.length < 2)
			printUsage();
		else {
			String numstatFilePath = args[0];
			String authorsFilePath = args[1];
			// String employersFilePath = args[2];
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"applicationContext.xml");
			NumStatParser parser = (NumStatParser) context
					.getBean("numStatParser");
			parser.setFilePath(numstatFilePath);
			parser.setAuthorsFilePath(authorsFilePath);
			// parser.setEmployersFilePath(employersFilePath);
			try {
				parser.parseData();
				parser.parseAuthors();
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

	public String getAuthorsFilePath() {
		return authorsFilePath;
	}

	public void setAuthorsFilePath(String authorsFilePath) {
		this.authorsFilePath = authorsFilePath;
	}

	public String getEmployersFilePath() {
		return employersFilePath;
	}

	public void setEmployersFilePath(String employersFilePath) {
		this.employersFilePath = employersFilePath;
	}

	public CommitParser getParser() {
		return parser;
	}

	public void setParser(CommitParser parser) {
		this.parser = parser;
	}

	public AuthorParser getAuthorParser() {
		return authorParser;
	}

	public void setAuthorParser(AuthorParser authorParser) {
		this.authorParser = authorParser;
	}

	public static void printUsage() {
		System.err
				.println("com.hortonworks.parser.NumStatParser <path to numstat> <path to authors>");
	}
}
