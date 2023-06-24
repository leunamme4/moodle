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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class XMLParser {
    private static final String PATH = "src/main/resources/moodleXML/example-1.xml";

    /** Все-таки это дольше, чем я думал **/

    //Собирает в questionsInfo инфу про вопрос с ответами в плохом формате
    public static void collectXMLData(String inputXMLFile) {

        List<Map<String, Map<ArrayList<String>, ArrayList<String>>>> questionsInfo = new ArrayList<>(); // <Тип вопроса, <Формулировка, ответы>>

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(inputXMLFile));

            document.getDocumentElement().normalize();

            NodeList questionList = document.getElementsByTagName("question");
            for (int i = 0; i < questionList.getLength(); i++) {

                Node question = questionList.item(i);

                if (question.getNodeType() == Node.ELEMENT_NODE) {

                    Element questionElement = (Element) question;
                    String questionType = questionElement.getAttribute("type");
                    ArrayList<String> questionText = new ArrayList<>(List.of());
                    ArrayList<String> answers = new ArrayList<>(List.of());

                    NodeList questionDetails = question.getChildNodes();
                    for (int j = 0; j < questionDetails.getLength(); j++) {

                        Node detail = questionDetails.item(j);
                        if (detail.getNodeType() == Node.ELEMENT_NODE) {
                            Element detailElement = (Element) detail;

                            if (Objects.equals(detailElement.getTagName(), "questiontext")) {
                                if (detailElement.getTextContent() != null) {
                                    questionText.add(detailElement.getTextContent());
                                }
                            }

                            if (Objects.equals(detailElement.getTagName(), "answer")) {
                                if (detailElement.getTextContent() != null) {
                                    answers.add(detailElement.getTextContent());
                                }
                            }
                        }
                    }
                    answers = normalizeXMLData(questionType, answers);
                    // System.out.println(answers);
                    Map<String, Map<ArrayList<String>, ArrayList<String>>> questions =Map.of(questionType, Map.of(questionText, answers));
                    questionsInfo.add(questions);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        System.out.println(questionsInfo);
        /**
         ** Выдает:
         *
         * [{category={[]=[]}}, {ddwtos={[
         *       <p><span>Текст вопроса: [[1]], [[2]], [[3]], [[4]], [[5]], [[6]]</span></p>
         *     ]=[]}}, {essay={[
         *       Что такое хорошо и что такое плохо?
         *     ]=[]}}, {multichoice={[
         *       Устинов _____ Михайлович.
         *     ]=[Олег, Сергей, Андрей]}}, {multichoice={[
         *       Уберите лишний (лишние)
         *     ]=[Москва (Комментарий к вопросу при просмотре результата): Столица России, Пекин (Комментарий к вопросу при просмотре результата): Столица Китая, Дакка (Комментарий к вопросу при просмотре результата): Столица Бангладеш, Джакарта (Комментарий к вопросу при просмотре результата): Столица Индонезии, Сидней (Комментарий к вопросу при просмотре результата): Правильно, Пермь (Комментарий к вопросу при просмотре результата): Правильно]}}, {multichoiceset={[
         *       <p>Текст вопроса</p>
         *     ]=[
         *       <p><span>Вариант ответа 1</span></p>
         *
         *
         *
         *     ,
         *       <p><span>Вариант ответа 2</span></p>
         *
         *
         *
         *     ,
         *       <p><span>Вариант ответа 3</span></p>
         *
         *
         *
         *     ,
         *       <p><span>Вариант ответа 4</span></p>
         *
         *
         *
         *     ,
         *       <p><span>Вариант ответа 5</span></p>
         *
         *
         *
         *     ]}}, {numerical={[
         *       Число Пи (4 цифры после запятой)
         *     ]=[
         *       3.1415
         *
         *
         *
         *       0.0005
         *     ]}}, {shortanswer={[
         *       Сколько дней в году?
         *     ]=[
         *       365
         *
         *
         *
         *     ,
         *       366
         *
         *
         *
         *     ]}}]
         **/


    }
    //нормализует ответы
    public static ArrayList<String> normalizeXMLData(String questionType, ArrayList<String> answers) {
        ArrayList<String> normalizedAnswers = new ArrayList<>();
        String answer = "";
        switch (questionType) {
            // Только мультивыбор пока только нормализовал B)
            case "multichoice":
                for (int i = 0; i < answers.toArray().length; i++) {
                    String currentAnswer = answers.toArray()[i].toString().trim();
                    List<String> splitted = List.of(currentAnswer.split("\n"));
                    for (int j = 0; j < splitted.size(); j ++) {
                        currentAnswer = splitted.get(j).trim();
                        if (!currentAnswer.equals("")) {
                            if (j == 0) {
                                answer = currentAnswer;
                            }
                            if (j == 2) {
                                answer += " (Комментарий к вопросу при просмотре результата): " + currentAnswer;
                            }
                        }
                    }
                    normalizedAnswers.add(answer);
                }
                return normalizedAnswers;

            case "asd":
                break;
            default:
                break;
        }
        return answers;
    }

}
