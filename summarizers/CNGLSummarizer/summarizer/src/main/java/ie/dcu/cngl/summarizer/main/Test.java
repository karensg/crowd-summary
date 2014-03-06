package ie.dcu.cngl.summarizer.main;

import ie.dcu.cngl.summarizer.Aggregator;
import ie.dcu.cngl.summarizer.Summarizer;
import ie.dcu.cngl.summarizer.Weighter;
import ie.dcu.cngl.tokenizer.Structurer;

import java.io.File;

import org.apache.commons.io.FileUtils;


public class Test {
	public static void main(String [] args) throws Exception {
		String testFileLoc = "C:\\Users\\Friso\\Desktop\\crowd-summary\\summarizers\\testfiles\\test1.txt";
		
		String text = FileUtils.readFileToString(new File(testFileLoc), "UTF-8");
		Structurer structurer = new Structurer();
		Weighter weighter = new Weighter();
		Aggregator aggregator = new Aggregator();		
		Summarizer summarizer = new Summarizer(structurer, weighter, aggregator);
		summarizer.setNumSentences(10);
		String summary = summarizer.summarize(text);
		System.out.println(summary);
	}
}
