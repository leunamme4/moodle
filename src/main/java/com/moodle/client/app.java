package com.moodle.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class app implements EntryPoint {
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final FileUpload uploadFile = new FileUpload();
    final Button convertButton = new Button("Конвертировать");

    uploadFile.addStyleName("input-file");
    convertButton.addStyleName("convert");

    convertButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        // handle the click event
      }
    });

    RootPanel.get("inputFileContainer").add(uploadFile);
    RootPanel.get("convertContainer").add(convertButton);
  }
}
