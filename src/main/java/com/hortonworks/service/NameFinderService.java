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
	private String excludeRegex = new String(".*Fix.*|Resource");
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
				detectedNames.add(name);
			else
				logger.debug("Found excluded name: " + name);
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