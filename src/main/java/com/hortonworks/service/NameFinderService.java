package com.hortonworks.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class NameFinderService implements InitializingBean {
	private Logger logger = Logger.getLogger(this.getClass());
	private String personModelPath = new String("en-ner-person.bin");
	private String tokenModelPath = new String("en-token.bin");
	private String excludeRegex = new String(".*(Upgrade|FsUrlStreamHandlerFactory|Fix|Resource|Add|Remove|Setup|Change|Mac OS|Generalize|NPE|Refactor|Replace|Test|FSCK|FSData|Allow|Data|Service|NullPointer|JobTracker|Respsect|FSName|Forgot|Context|FileUtil|Improve|DFS|GridMix|Provide|DBInputFormat|DataNode|Sqoop|Updating|NameNode|FileSystem|Balancer|API|Distinguish|Marks|RPC|DistC|Removes|Datanode|InputStream|Copy|Disable|Copy|SequenceFile|TextOuputFormat|Deprecate|SocketIO|DFS|HMaster|NetworkTopology|ArrayList|ChecksumFile|Iterable|Ensure|INodeDirectory|Hadoop|CRC|FsShell|CHANGES|AlreadyBeing|MapReduce|EC2|GenericWriteable).*");
	Tokenizer tokenizer;
	NameFinderME nameFinder;

	public Collection<String> findNames(String description) {
		String tokens[] = tokenizer.tokenize(description);
		Span[] names = nameFinder.find(tokens);
		LinkedList<String> detectedNames = new LinkedList<String>();

		for (int spanIndex = 0; spanIndex < names.length; spanIndex++) {
			String name = new String();
			for (int arrayIndex = names[spanIndex].getStart(); arrayIndex < names[spanIndex]
					.getEnd(); arrayIndex++)
				name += tokens[arrayIndex] + " ";
			logger.debug("Identified name: " + name);
			if (!name.matches(excludeRegex))
				detectedNames.add(name.trim());
			else
				logger.warn("Found excluded name: " + name);
		}
		nameFinder.clearAdaptiveData();
		return detectedNames;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			TokenizerModel model = new TokenizerModel(new FileInputStream(
					tokenModelPath));
			tokenizer = new TokenizerME(model);

			TokenNameFinderModel personModel = new TokenNameFinderModel(
					new FileInputStream(personModelPath));
			nameFinder = new NameFinderME(personModel);
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}