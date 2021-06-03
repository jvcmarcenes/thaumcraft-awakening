package io.github.jvcmarcenes.tca.data.server;

import net.minecraft.data.DataGenerator;

public class ResearchProvider /* implements IDataProvider */ {
  
  /*
    Need better understandment of data providers,
    this possibly isn't the best solution for this use case
  */

  public ResearchProvider(DataGenerator dataGenerator) {

  }

  protected void registerResearches() {

    /*
    
    ResearchBuilder.create("nitor")
      .page()
        .write("Lorem ipsum dolor sit amet").next()
        .recipe(modLoc("nitor"))
        .requirements()
          .mustCraft(modLoc("nitor"))
          .cost(Knowledge.THEORY_ALCHEMY, 1)
          .cost(Knowledge.OBSERVATION_FUNDAMENTALS, 2)
          .end()
        .end()
      .finalPage()
        .write("Lorem ipsum dolor sit amet")
        .bookmark(modLoc("nitor"))
        .end()
      .build(consumer)

    */

  }
}
