package tests;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.classify.LinearClassifier;
import edu.stanford.nlp.classify.LinearClassifierFactory;
import edu.stanford.nlp.ling.BasicDatum;
import edu.stanford.nlp.ling.Datum;

public class stanfordClassifierTest {

	/**
	 * @author Matthijs Brouns
	 * 
	 * 
	 * Test to see how Stanford NLP Classifier works
	 */
	protected static final String GREEN = "green";
	protected static final String RED = "red";
	protected static final String WORKING = "working";
	protected static final String BROKEN = "broken";

	private stanfordClassifierTest() {
	} // not instantiable

	protected static Datum<String, String> makeStopLights(String ns, String ew) {
		List<String> features = new ArrayList<String>();
		// Create the north-south light feature
		features.add("NS=" + ns);
		// Create the east-west light feature
		features.add("EW=" + ew);
		// Create the label
		String label = (ns.equals(ew) ? BROKEN : WORKING);
		return new BasicDatum<String, String>(features, label);
	}

	public static void main(String[] args) {
		// Create a training set
		List<Datum<String, String>> trainingData = new ArrayList<Datum<String, String>>();
		trainingData.add(makeStopLights(GREEN, RED));
		trainingData.add(makeStopLights(GREEN, RED));
		trainingData.add(makeStopLights(GREEN, RED));
		trainingData.add(makeStopLights(RED, GREEN));
		trainingData.add(makeStopLights(RED, GREEN));
		trainingData.add(makeStopLights(RED, GREEN));
		trainingData.add(makeStopLights(RED, RED));
		// Create a test set
		Datum<String, String> workingLights = makeStopLights(GREEN, RED);
		Datum<String, String> brokenLights = makeStopLights(RED, RED);
		// Build a classifier factory
		LinearClassifierFactory<String, String> factory = new LinearClassifierFactory<String, String>();
		factory.useConjugateGradientAscent();
		// Turn on per-iteration convergence updates
		factory.setVerbose(true);
		// Small amount of smoothing
		factory.setSigma(10.0);
		// Build a classifier
		LinearClassifier<String, String> classifier = factory
				.trainClassifier(trainingData);
		// Check out the learned weights
		classifier.dump();
		// Test the classifier
		System.out.println("Working instance got: "
				+ classifier.scoresOf(workingLights));
		classifier.justificationOf(workingLights);
		System.out.println("Broken instance got: "
				+ classifier.scoresOf(brokenLights));
		classifier.justificationOf(brokenLights);

	}

}
