package com.zylman.protein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zylman.protein.DIP.Field;

public class FeatureVector {
	String sequence;
	ArrayList<Double> hydrophobicityArr = new ArrayList<Double>();
	ArrayList<Double> hydrophocilityArr = new ArrayList<Double>();
	ArrayList<Double> volumeArr = new ArrayList<Double>();
	ArrayList<Double> polarityArr = new ArrayList<Double>();
	ArrayList<Double> polarizabilityArr = new ArrayList<Double>();
	ArrayList<Double> sasaArr = new ArrayList<Double>();
	ArrayList<Double> nciArr = new ArrayList<Double>();
	
	Map<Character, Double> hydrophobicity = new HashMap<Character, Double>(){{
		put('A',0.62);
		put('C',0.29);
		put('D',-0.9);
		put('E',-0.74);
		put('F',1.19);
		put('G',0.48);
		put('H',-0.4);
		put('I',1.38);
		put('K',-1.5);
		put('L',1.06);
		put('M',0.64);
		put('N',-0.78);
		put('P',0.12);
		put('Q',-0.85);
		put('R',-2.53);
		put('S',-0.18);
		put('T',-0.05);
		put('V',1.08);
		put('W',0.81);
		put('Y',0.26);		
	}};
	Map<Character, Double> hydrophocility = new HashMap<Character, Double>(){{
		put('A',-0.5);
		put('C',-1.0);
		put('D',3.0);
		put('E',3.0);
		put('F',-2.5);
		put('G',0.0);
		put('H',-0.5);
		put('I',-1.8);
		put('K',3.0);
		put('L',-1.8);
		put('M',-1.3);
		put('N',2.0);
		put('P',0.0);
		put('Q',0.2);
		put('R',3.0);
		put('S',0.3);
		put('T',-0.4);
		put('V',-1.5);
		put('W',-3.4);
		put('Y',-2.3);
	}};
	Map<Character, Double> volume = new HashMap<Character, Double>(){{
		put('A',27.5);
		put('C',44.6);
		put('D',40.0);
		put('E',62.0);
		put('F',115.5);
		put('G',0.0);
		put('H',79.0);
		put('I',93.5);
		put('K',100.0);
		put('L',93.5);
		put('M',94.1);
		put('N',58.7);
		put('P',41.9);
		put('Q',80.7);
		put('R',105.0);
		put('S',29.3);
		put('T',51.3);
		put('V',71.5);
		put('W',145.5);
		put('Y',117.3);
	}};
	Map<Character, Double> polarity = new HashMap<Character, Double>(){{
		put('A',8.1);
		put('C',5.5);
		put('D',13.0);
		put('E',12.3);
		put('F',5.2);
		put('G',9.0);
		put('H',10.4);
		put('I',5.2);
		put('K',11.3);
		put('L',4.9);
		put('M',5.7);
		put('N',11.6);
		put('P',8.0);
		put('Q',10.5);
		put('R',10.5);
		put('S',9.2);
		put('T',8.6);
		put('V',5.9);
		put('W',5.4);
		put('Y',6.2);
	}};
	Map<Character, Double> polarizability = new HashMap<Character, Double>(){{
		put('A',0.046);
		put('C',0.128);
		put('D',0.105);
		put('E',0.151);
		put('F',0.29);
		put('G',0.0);
		put('H',0.23);
		put('I',0.186);
		put('K',0.219);
		put('L',0.186);
		put('M',0.221);
		put('N',0.134);
		put('P',0.131);
		put('Q',0.18);
		put('R',0.291);
		put('S',0.062);
		put('T',0.108);
		put('V',0.14);
		put('W',0.409);
		put('Y',0.298);
	}};
	Map<Character, Double> SASA = new HashMap<Character, Double>(){{
		put('A',1.181);
		put('C',1.461);
		put('D',1.587);
		put('E',1.862);
		put('F',2.228);
		put('G',0.881);
		put('H',2.025);
		put('I',1.81);
		put('K',2.258);
		put('L',1.931);
		put('M',2.034);
		put('N',1.655);
		put('P',1.468);
		put('Q',1.932);
		put('R',2.56);
		put('S',1.298);
		put('T',1.525);
		put('V',1.645);
		put('W',2.663);
		put('Y',2.368);
	}};
	Map<Character, Double> NCI = new HashMap<Character, Double>(){{
		put('A',0.007187);
		put('C',-0.03661);
		put('D',-0.02382);
		put('E',0.006802);
		put('F',0.037552);
		put('G',0.179052);
		put('H',-0.01069);
		put('I',0.021631);
		put('K',0.017708);
		put('L',0.051672);
		put('M',0.002683);
		put('N',0.005392);
		put('P',0.239531);
		put('Q',0.049211);
		put('R',0.043587);
		put('S',0.004627);
		put('T',0.003352);
		put('V',0.057004);
		put('W',0.037977);
		put('Y',0.023599);
	}};

	FeatureVector(String sequence) {
		// Store the sequence.
		this.sequence = sequence;
		
		int lg = 30;								// Can change this -- optimize!
		int sequenceLength = sequence.length();
		
		// Keep track of where we are in the feature array.
		int featureArrayCurrIndex;
		featureArrayCurrIndex = 0;
		
		// Calculate the feature vector values
		
		
		/* Hydrophobicity */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averageHydrophobicity;
				averageHydrophobicity = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averageHydrophobicity += hydrophobicity.get(sequence.charAt(aa));
				}
				averageHydrophobicity /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = hydrophobicity.get(sequence.charAt(i)) - averageHydrophobicity;
				
				// Right term inside the sum
				double rightTerm = hydrophobicity.get(sequence.charAt(i + lag)) - averageHydrophobicity;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.hydrophobicityArr.add(outsideVal * sum);
			
		}
		
		
		/* Hydrophocility */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averageHydrophocility;
				averageHydrophocility = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averageHydrophocility += hydrophocility.get(sequence.charAt(aa));
				}
				averageHydrophocility /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = hydrophocility.get(sequence.charAt(i)) - averageHydrophocility;
				
				// Right term inside the sum
				double rightTerm = hydrophocility.get(sequence.charAt(i + lag)) - averageHydrophocility;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.hydrophocilityArr.add(outsideVal * sum);
			
		}
		
		
		/* Volume */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averageVolume;
				averageVolume = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averageVolume += volume.get(sequence.charAt(aa));
				}
				averageVolume /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = volume.get(sequence.charAt(i)) - averageVolume;
				
				// Right term inside the sum
				double rightTerm = volume.get(sequence.charAt(i + lag)) - averageVolume;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.volumeArr.add(outsideVal * sum);
			
		}
		
		
		/* Polarity */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averagePolarity;
				averagePolarity = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averagePolarity += polarity.get(sequence.charAt(aa));
				}
				averagePolarity /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = polarity.get(sequence.charAt(i)) - averagePolarity;
				
				// Right term inside the sum
				double rightTerm = polarity.get(sequence.charAt(i + lag)) - averagePolarity;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.polarityArr.add(outsideVal * sum);
			
		}
		
		
		/* Polarizability */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averagePolarizability;
				averagePolarizability = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averagePolarizability += polarizability.get(sequence.charAt(aa));
				}
				averagePolarizability /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = polarizability.get(sequence.charAt(i)) - averagePolarizability;
				
				// Right term inside the sum
				double rightTerm = polarizability.get(sequence.charAt(i + lag)) - averagePolarizability;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.polarizabilityArr.add(outsideVal * sum);
			
		}
		
		
		/* SASA */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averageSASA;
				averageSASA = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averageSASA += SASA.get(sequence.charAt(aa));
				}
				averageSASA /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = SASA.get(sequence.charAt(i)) - averageSASA;
				
				// Right term inside the sum
				double rightTerm = SASA.get(sequence.charAt(i + lag)) - averageSASA;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.sasaArr.add(outsideVal * sum);
			
		}
		
		
		/* NCI */
		
		// Go through each of the lag values
		for(int lag=1; lag<=lg; lag++) {
			double outsideVal = 1/(sequenceLength - lag);
			double sum = 0;
			
			// Go through each of amino acids
			for(int i=1; i<=sequenceLength-lag; i++) {
				
				// Average value
				double averageNCI;
				averageNCI = 0;
				for(int aa=0; aa<sequenceLength; aa++) {
					averageNCI += NCI.get(sequence.charAt(aa));
				}
				averageNCI /= sequenceLength;
				
				// Left term inside the sum
				double leftTerm = NCI.get(sequence.charAt(i)) - averageNCI;
				
				// Right term inside the sum
				double rightTerm = NCI.get(sequence.charAt(i + lag)) - averageNCI;
				
				// Add to sum
				sum += leftTerm * rightTerm;
				
			}
			
			// Add the calculated value to the feature vector.
			this.nciArr.add(outsideVal * sum);
			
		}
	}
	
	String getSequence() {
		return sequence;
	}
}
