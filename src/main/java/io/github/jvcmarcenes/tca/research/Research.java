package io.github.jvcmarcenes.tca.research;

import java.util.ArrayList;
import java.util.List;

public class Research {
	
	private final String name;
	private List<Page> pages;
	private Page finalPage;
	
	public Research(String name) {
		this.name = name;
		pages = new ArrayList<>();
	}
	
	public class Page {
		private String text;
		private String[] knowledgeCondition;
		private Stage requirements;
	}

	public class Stage {

	}
}
