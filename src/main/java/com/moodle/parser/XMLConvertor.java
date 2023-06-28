package com.moodle.parser;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.*;
import java.util.*;

public class XMLConvertor {

    /** Собирает в questionsInfo инфу про вопрос в формате List<Map<String, Map<String, ArrayList<String>>>>
     * (Тип вопроса (Формулирова, Список ответов (где правильные отметка (-true)))
     * @param inputXMLFile - входной файл moodleXML
     */
    public static void collectXMLData(String inputXMLFile) {

        List<Map<String, Map<String, ArrayList<String>>>> questionsInfo = new ArrayList<>(); // <Тип вопроса, <Формулировка, ответы>>

        try {
            // где-то тут барагоз жоский
            Document document = XMLParser.parse(inputXMLFile); //  уверен, что прямо сразу где-то здесь
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

                            if (Objects.equals(detailElement.getTagName(), "questiontext")) {
                                if (detailElement.getNodeValue() != null) {
                                    questionText = detailElement.getNodeValue();
                                }
                            }

                            /* Правильные ответы выделяю для мультивыбора и вставить пропущенное слово, остальное
                            пока не пониманию точно, как должно выглядеть в DOCX и как выделить их тоже ососбо пока не разобрался,
                             особенно с перетаскиванием это КРИНЖ.
                            */
                            if (Objects.equals(detailElement.getTagName(), "answer")) {
                                if (detailElement.getNodeValue() != null) {
                                    String isCorrect = String.valueOf((!detailElement.getAttribute("fraction").equals("0")));
                                    if (isCorrect.equals("true")) {
                                        answers.add(detailElement.getNodeValue() + isCorrect);
                                    } else {
                                        answers.add(detailElement.getNodeValue());
                                    }
                                }
                            }
                            if (Objects.equals(detailElement.getTagName(), "dragbox")) {
                                if (detailElement.getNodeValue() != null) {
                                    answers.add(detailElement.getNodeValue());
                                }
                            }
                            if (Objects.equals(detailElement.getTagName(), "selectoption")) {
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println(questionsInfo);
    }

    /**
     * Нормализует ответы
     * @param questionType - тип вопроса
     * @param answers - ответы
     * @param questionText - формулировка
     * @return Map<String, ArrayList<String>> - Нормализованные (формулировка, список ответов).
     */
    public static Map<String, ArrayList<String>> normalizeXMLData(String questionType, ArrayList<String> answers, String questionText) {
        Map<String, ArrayList<String>> normalizedAnswers = new HashMap<String, ArrayList<String>>(){};
        String newQuestionText = questionText.replaceAll("<[^>]*>", "").trim(); //тупо все теги убирает, это тупо, но я пока ничего лучше не придумал В(
        ArrayList<String> newAnswers = new ArrayList<>();
        StringBuilder answer = new StringBuilder();

        System.out.println(newQuestionText);

        if (!Objects.equals(questionType, "essay")) {
            for (int i = 0; i < answers.toArray().length; i++) {

                String currentAnswer = answers.toArray()[i].toString().replaceAll("<[^>]*>", "").trim();
                String[] splitted = (currentAnswer.split("\n"));
                for (int j = 0; j < splitted.length; j++) {
                    currentAnswer = splitted[j].trim();
                    if (!currentAnswer.equals("")) {
                            if (Objects.equals(questionType, "multichoice") || Objects.equals(questionType,"shortanswer") ||  Objects.equals(questionType,"multichoiceset")) { //Множественный выбор, короткий ответ,
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
                            if (Objects.equals(questionType,"numerical")) { //Числовой
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
                            if  (Objects.equals(questionType,"ddwtos") || Objects.equals(questionType,"gapselect")) { //Перетаскивание в текст и выбор пропущенных слов
                                if (j == 0) {
                                    answer = new StringBuilder(currentAnswer);
                                }
                                if (j == 1) {
                                    answer.append(" (Группа ответа): ").append(currentAnswer);
                                }
                            }
                    }
                }
                System.out.println(answer);
                newAnswers.add(answer.toString());
                normalizedAnswers.put(newQuestionText, newAnswers);
            }
        } else {
            normalizedAnswers.put(newQuestionText, answers);
        }
        return normalizedAnswers;
    }
}

