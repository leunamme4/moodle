package com.moodle.parser;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.*;

import java.util.*;

public class XMLConvertor {

    /**
     * Собирает в questionsInfo инфу про вопрос в формате List<Map<String, Map<String, ArrayList<String>>>>
     * (Тип вопроса (Формулирова, Список ответов (где правильные отметка (-true)))
     *
     * @param inputXMLFile - входной файл moodleXML
     * @return List<Map<String, Map<String, ArrayList<String>>>> <Тип вопроса, <Формулировка, ответы>>
     */
    public static List<Map<String, Map<String, ArrayList<String>>>> collectXMLData(String inputXMLFile) {

        List<Map<String, Map<String, ArrayList<String>>>> questionsInfo = new ArrayList<>(); // <Тип вопроса, <Формулировка, ответы>>

        try {
            Document document = XMLParser.parse(inputXMLFile);

            document.getDocumentElement().normalize();

            NodeList questionList = document.getElementsByTagName("question");
            for (int i = 0; i < questionList.getLength(); i++) {

                Node question = questionList.item(i);

                if (question.getNodeType() == Node.ELEMENT_NODE) {

                    Element questionElement = (Element) question;
                    String questionType = questionElement.getAttribute("type");
                    String questionText = "";
                    ArrayList<String> answers = new ArrayList<>();

                    NodeList questionDetails = question.getChildNodes();
                    for (int j = 0; j < questionDetails.getLength(); j++) {

                        Node detail = questionDetails.item(j);
                        if (detail.getNodeType() == Node.ELEMENT_NODE) {
                            Element detailElement = (Element) detail;

                            if (detailElement.getTagName().equals("questiontext")) {
                                if (detailElement.getNodeValue() != null) {
                                    questionText = detailElement.getNodeValue();
                                }
                            }

                            /* Правильные ответы выделяю для мультивыбора и вставить пропущенное слово, остальное
                            пока не пониманию точно, как должно выглядеть в DOCX и как выделить их тоже ососбо пока не разобрался,
                             особенно с перетаскиванием это КРИНЖ.
                            */
                            if (detailElement.getTagName().equals("answer")) {
                                if (detailElement.getNodeValue() != null) { // ПОЧЕМУ-ТО РАВНО null
                                    String isCorrect = String.valueOf((!detailElement.getAttribute("fraction").equals("0")));
                                    if (isCorrect.equals("true")) {
                                        answers.add(detailElement.getNodeValue() + isCorrect);
                                    } else {
                                        answers.add(detailElement.getNodeValue());
                                    }
                                }
                            }
                            if (detailElement.getTagName().equals("dragbox")) {
                                if (detailElement.getNodeValue() != null) { // ПОЧЕМУ-ТО РАВНО null
                                    answers.add(detailElement.getNodeValue());
                                }
                            }
                            if (detailElement.getTagName().equals("selectoption")) { // ПОЧЕМУ-ТО РАВНО null
                                if (detailElement.getNodeValue() != null) {
                                    answers.add(detailElement.getNodeValue());
                                }
                            }
                        }
                    }
                    Map<String, Map<String, ArrayList<String>>> questions = new HashMap<>();
                    questions.put(questionType, normalizeXMLData(questionType, answers, questionText));
                    questionsInfo.add(questions);
                }
            }

        } catch (DOMException e) {
            Window.alert("Could not parse XML document.");
            throw new RuntimeException(e);
        }

        return questionsInfo;
    }

    /**
     * Нормализует ответы
     * @param questionType - тип вопроса
     * @param answers - ответы
     * @param questionText - формулировка
     * @return Map<String, ArrayList<String>> - Нормализованные (формулировка, список ответов).
     */
    public static Map<String, ArrayList<String>> normalizeXMLData(String questionType, ArrayList<String> answers, String questionText) {
       // Window.alert(questionText);
        Map<String, ArrayList<String>> normalizedAnswers = new HashMap<>(){};
        String newQuestionText = questionText.replaceAll("<[^>]*>", "").trim(); //тупо все теги убирает, это тупо, но я пока ничего лучше не придумал В(
        ArrayList<String> newAnswers = new ArrayList<>();
        StringBuilder answer = new StringBuilder();

        System.out.println(newQuestionText);

        if (!questionType.equals("essay")) {
            for (int i = 0; i < answers.toArray().length; i++) {

                String currentAnswer = answers.toArray()[i].toString().replaceAll("<[^>]*>", "").trim();
                String[] splitted = (currentAnswer.split("\n"));
                for (int j = 0; j < splitted.length; j++) {
                    currentAnswer = splitted[j].trim();
                    if (!currentAnswer.equals("")) {
                        if ((questionType.equals("multichoice")) || (questionType.equals("shortanswer")) || (questionType.equals("multichoiceset"))) { //Множественный выбор, короткий ответ,
                            if (j == 0) {
                                answer = new StringBuilder(currentAnswer);
                            }
                            if (j == 2) {
                                answer.append(" (Комментарий к вопросу при просмотре результата): ").append(currentAnswer);
                            }
                            if (j == splitted.length - 1 && splitted[splitted.length - 1].trim().equals("true") && !questionType.equals("shortanswer")) {
                                answer.append(" true");
                            }
                        }
                        if (questionType.equals("numerical")) { //Числовой
                            if (j == 0) {
                                answer = new StringBuilder(currentAnswer);
                            }
                            if (j == 2) {
                                answer.append(" (Комментарий к вопросу при просмотре результата): ").append(currentAnswer);
                            }
                            if (j == 4) {
                                answer.append(" Допустимая погрешность = +-").append(currentAnswer);
                            }
                        }
                        if  ((questionType.equals("ddwtos")) || (questionType.equals("gapselect"))) { //Перетаскивание в текст и выбор пропущенных слов
                            if (j == 0) {
                                answer = new StringBuilder(currentAnswer);
                            }
                            if (j == 1) {
                                answer.append(" (Группа ответа): ").append(currentAnswer);
                            }
                        }
                    }
                }
                newAnswers.add(answer.toString());
                normalizedAnswers.put(newQuestionText, newAnswers);
            }
        } else {
            normalizedAnswers.put(newQuestionText, answers);
        }
        return normalizedAnswers;
    }
}
