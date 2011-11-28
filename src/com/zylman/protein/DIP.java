package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DIP {
	
	public enum Field {
		INTERACTOR_A ("ID interactor A"),
		INTERACTOR_B ("ID interactor B"),
		ALT_INTERACTOR_A ("Alt. ID interactor A"),
		ALT_INTERACTOR_B ("Alt. ID interactor B"),
		INTERACTOR_A_ALIASES ("Alias(es) interactor A"),
		INTERACTOR_B_ALIASES ("Alias(es) interactor B"),
		DETECTION_METHODS ("Interaction detection method(s)"),
		FIRST_AUTHOR ("Publication 1st author(s)"),
		PUB_ID ("Publication Identifier(s)"),
		INTERACTOR_A_TAXID ("Taxid interactor A"),
		INTERACTOR_B_TAXID ("Taxid interactor B"),
		INTERACTION_TYPES ("Interaction type(s)"),
		SOURCE_DB ("Source database(s)"),
		INTERACTION_ID ("Interaction identifier(s)"),
		CONFIDENCE ("Confidence value(s)"),
		PROCESSING_STATUS ("Processing Status");
		
		final String name;
		Field(String name) {
			this.name = name;
		}	
	}
	
	public class Interaction {
		Map<Field, String> interaction = new HashMap<Field, String>();
		Interaction(String interaction) {
			String[] fields = interaction.split("\t");
			this.interaction.put(Field.INTERACTOR_A, fields[0]);
			this.interaction.put(Field.INTERACTOR_B, fields[1]);
			this.interaction.put(Field.ALT_INTERACTOR_A, fields[2]);
			this.interaction.put(Field.ALT_INTERACTOR_B, fields[3]);
			this.interaction.put(Field.INTERACTOR_A_ALIASES, fields[4]);
			this.interaction.put(Field.INTERACTOR_B_ALIASES, fields[5]);
			this.interaction.put(Field.DETECTION_METHODS, fields[6]);
			this.interaction.put(Field.FIRST_AUTHOR, fields[7]);
			this.interaction.put(Field.PUB_ID, fields[8]);
			this.interaction.put(Field.INTERACTOR_A_TAXID, fields[9]);
			this.interaction.put(Field.INTERACTOR_B_TAXID, fields[10]);
			this.interaction.put(Field.INTERACTION_TYPES, fields[11]);
			this.interaction.put(Field.SOURCE_DB, fields[12]);
			this.interaction.put(Field.INTERACTION_ID, fields[13]);
			this.interaction.put(Field.CONFIDENCE, fields[14]);
			this.interaction.put(Field.PROCESSING_STATUS, fields[15]);
		}
	}
	
	List<Interaction> interactions = new ArrayList<Interaction>(); 
	
	DIP(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = br.readLine(); // Remove column headings
		while ((line = br.readLine()) != null) {
			interactions.add(new Interaction(line));
		}
	}
}
