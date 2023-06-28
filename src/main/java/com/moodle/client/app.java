package com.moodle.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.moodle.parser.XMLConvertor;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */


public class app implements EntryPoint {
  /**
   * This is the entry point method.
   */

  private static final String PATH = "src/main/resources/moodleXML/example-1.xml";
  public void onModuleLoad() {

    final FileUpload uploadFile = new FileUpload();
    final Button convertButton = new Button("Конвертировать");

    uploadFile.addStyleName("input-file");
    convertButton.addStyleName("convert");

    convertButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        // Не падает, но DomException ловит В(((((( ЗА ШО?
         XMLConvertor.collectXMLData(PATH);
      }
    });

    RootPanel.get("inputFileContainer").add(uploadFile);
    RootPanel.get("convertContainer").add(convertButton);
  }
}
