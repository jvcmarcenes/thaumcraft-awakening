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
    }

    public class Builder {

        private Research research;

        public Builder(String name) {
            research = new Research(name);
        }

        public Builder addPage(Page page) {
            research.pages.add(page);
            return this;
        }

        public Builder finalPage(Page page) {
            research.finalPage = page;
            return this;
        }

    }

}
