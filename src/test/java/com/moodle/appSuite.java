package com.moodle;

import com.moodle.client.appTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class appSuite extends GWTTestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite("Tests for app");
    suite.addTestSuite(appTest.class);
    return suite;
  }
}
