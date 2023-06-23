package com.moodle.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class XMLParser {

// Недодела пока, потом доделаю, это легче чем казалось В)
    public static void getDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File("src/main/resources/moodleXML/example-1.xml"));

            document.getDocumentElement().normalize();

            NodeList questionList = document.getElementsByTagName("question");
            for (int i = 0; i < questionList.getLength(); i++) {
                Node question = questionList.item(i);

                if (question.getNodeType() == Node.ELEMENT_NODE) {

                    Element questionElement = (Element) question;
                    System.out.println("Question type - " + questionElement.getAttribute("type"));

                    NodeList questionDetails = question.getChildNodes();
                    for (int j = 0; j < questionDetails.getLength(); j++) {
                        Node detail = questionDetails.item(j);
                        if (detail.getNodeType() == Node.ELEMENT_NODE) {
                            Element detailElement = (Element) detail;
                            if (Objects.equals(detailElement.getTagName(), "questiontext")) {
                                System.out.println("    Текст вопроса- " + detailElement.getTextContent());
                            }
                            //  System.out.println("    " + detailElement.getTagName());
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

}
