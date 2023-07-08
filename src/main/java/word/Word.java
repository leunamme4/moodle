package word;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

public class Word {

    public static void printTextAndType(XWPFDocument document, Integer number, String questionText, String typeText, Map<String, String> types){
        XWPFParagraph newLine = document.createParagraph();
        XWPFRun run = newLine.createRun();
        run.setText(" ");

        XWPFParagraph question = document.createParagraph();
        XWPFRun questionSettings = question.createRun(); //для текста вопроса
        questionSettings.setFontFamily("Times New Roman");
        questionSettings.setFontSize(14);
        questionSettings.setBold(true);
        questionSettings.setText("Question №" + number.toString() + ": " + questionText);

        XWPFParagraph type = document.createParagraph();
        XWPFRun typeSettings = type.createRun();
        typeSettings.setFontFamily("Times New Roman");
        typeSettings.setFontSize(14);
        typeSettings.setItalic(true);
        typeSettings.setText("Тип вопроса: " + types.get(typeText));
    }

    public static void multichoicesAndGap(XWPFDocument document, Integer number, String questionText, List<String> answersList, String textType, Map<String, String> types){
        printTextAndType(document, number, questionText, textType, types);
        int count = 1;

        for (String s : answersList) {
            XWPFParagraph answers = document.createParagraph();
            XWPFRun tab = answers.createRun();
            tab.setFontFamily("Times New Roman");
            tab.setFontSize(14);
            tab.setText("   ");

            XWPFRun answersSettings = answers.createRun();
            answersSettings.setFontFamily("Times New Roman");
            answersSettings.setFontSize(14);

            if (s.contains("Правильный ответ")) {
                answersSettings.setTextHighlightColor("yellow");
            }
            answersSettings.setText(count + ". " + s);
            count++;
        }
    }

    public static void essay(XWPFDocument document, Integer number, String questionText, String textType, Map<String, String> types){
        printTextAndType(document, number, questionText, textType, types);
    }

    public static void ddwtos(XWPFDocument document, Integer number, String questionText, List<String> answersList, String textType, Map<String, String> types){ //3
        printTextAndType(document, number, questionText, textType, types);

        XWPFParagraph answersTitle = document.createParagraph();
        XWPFRun answersPrint = answersTitle.createRun();
        answersPrint.setFontFamily("Times New Roman");
        answersPrint.setFontSize(14);
        answersPrint.setText("Варианты ответов:");

        XWPFParagraph answers = document.createParagraph();
        XWPFRun answersSettings = answers.createRun();
        answersSettings.setFontFamily("Times New Roman");
        answersSettings.setFontSize(14);

        int answersSize = answersList.size();
        for (int k = 0; k < answersSize; k++) {
            if (k == answersSize - 1) {
                answersSettings.setText(answersList.get(k));
                break;
            }
            answersSettings.setText(answersList.get(k) + ", ");
        }
    }

    public static void numericalAndShort(XWPFDocument document, Integer number, String questionText, List<String> answersList, String textType,Map<String, String> types){ // 2
        printTextAndType(document, number, questionText, textType, types);

        XWPFParagraph answersTitle = document.createParagraph();
        XWPFRun answersPrint = answersTitle.createRun();
        answersPrint.setFontFamily("Times New Roman");
        answersPrint.setFontSize(14);
        answersPrint.setText("Правильные варианты ответов:");

        int count = 1;

        for (String s : answersList) {
            XWPFParagraph answers = document.createParagraph();
            XWPFRun answersSettings = answers.createRun();
            answersSettings.setFontFamily("Times New Roman");
            answersSettings.setFontSize(14);
            answersSettings.setText("   " + count + ". " + s);
            count++;
        }
    }


    public static void main(String[] args) {
        //главный List
        List<Map<String, Map<String, ArrayList<String>>>> questionsInfo = new ArrayList<>();

        //инициализация тестового массива
        //вопрос 1
        String[] answerspr1 = {"rrr", "sss", "sdsds"};
        ArrayList<String> answers1 = new ArrayList<>(Arrays.asList(answerspr1));
        Map<String, ArrayList<String>> quAns1 = new HashMap<>();
        quAns1.put("Вопрос 1?", answers1);
        Map<String, Map<String, ArrayList<String>>> firstQ = new HashMap<>();
        firstQ.put("ddwtos", quAns1);
        questionsInfo.add(firstQ);


        //вопрос 2
        ArrayList<String> answers2 = new ArrayList<>();
        Map<String, ArrayList<String>> quAns2 = new HashMap<>();
        quAns2.put("Вопрос 2?", answers2);
        Map<String, Map<String, ArrayList<String>>> secondQ = new HashMap<>();
        secondQ.put("essay", quAns2);
        questionsInfo.add(secondQ);

        //вопрос 3
        String[] answerspr3 = {"rrr3", "sss3", "sdsds3 - Правильный ответ"};
        ArrayList<String> answers3 = new ArrayList<>(Arrays.asList(answerspr3));
        Map<String, ArrayList<String>> quAns3 = new HashMap<>();
        quAns3.put("Вопрос 3?", answers3);
        Map<String, Map<String, ArrayList<String>>> thirdQ = new HashMap<>();
        thirdQ.put("gapselect", quAns3);
        questionsInfo.add(thirdQ);

        //вопрос 4
        String[] answerspr4 = {"rrr4", "sss4", "sdsds4 - Правильный ответ"};
        ArrayList<String> answers4 = new ArrayList<>(Arrays.asList(answerspr4));
        Map<String, ArrayList<String>> quAns4 = new HashMap<>();
        quAns4.put("Вопрос 4?", answers4);
        Map<String, Map<String, ArrayList<String>>> fourthQ = new HashMap<>();
        fourthQ.put("multichoice", quAns4);
        questionsInfo.add(fourthQ);

        //вопрос 5
        String[] answerspr5 = {"rrr5", "sss5 - Правильный ответ", "sdsds5 - Правильный ответ"};
        ArrayList<String> answers5 = new ArrayList<>(Arrays.asList(answerspr5));
        Map<String, ArrayList<String>> quAns5 = new HashMap<>();
        quAns5.put("Вопрос 5?", answers5);
        Map<String, Map<String, ArrayList<String>>> fifthQ = new HashMap<>();
        fifthQ.put("multichoiceset", quAns5);
        questionsInfo.add(fifthQ);

        //вопрос 6
        String[] answerspr6 = {"1312421", "228"};
        ArrayList<String> answers6 = new ArrayList<>(Arrays.asList(answerspr6));
        Map<String, ArrayList<String>> quAns6 = new HashMap<>();
        quAns6.put("Вопрос 6?", answers6);
        Map<String, Map<String, ArrayList<String>>> sixthQ = new HashMap<>();
        sixthQ.put("numerical", quAns6);
        questionsInfo.add(sixthQ);

        //вопрос 7
        String[] answerspr7 = {"rrr7", "sss7", "sdsds7"};
        ArrayList<String> answers7 = new ArrayList<>(Arrays.asList(answerspr7));
        Map<String, ArrayList<String>> quAns7 = new HashMap<>();
        quAns7.put("Вопрос 7?", answers7);
        Map<String, Map<String, ArrayList<String>>> seventhQ = new HashMap<>();
        seventhQ.put("shortanswer", quAns7);
        questionsInfo.add(seventhQ);

        Map<String, String> types = new HashMap<>();
        types.put("ddwtos", "Перетаскивание в текст");
        types.put("essay", "Эссе");
        types.put("gapselect", "Выбор пропущенных слов");
        types.put("multichoice", "Множественный выбор и выбор пропущенных слов");
        types.put("multichoiceset", "Все или ничего");
        types.put("numerical", "Числовой ответ");
        types.put("shortanswer", "Короткий ответ");

        try {
            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File("D:\\projetdocx\\abcde.docx")); //надо поменять

            //основа
            for (int i = 0; i < questionsInfo.size(); i++) {
                Map<String, Map<String, ArrayList<String>>> question = questionsInfo.get(i);
                int questionNumber = i + 1; //надо поменять с учетом что первый элемент это категория
                String questionType = "";
                String questionText = "";
                List<String> answers = new ArrayList<>();
                for (Map.Entry<String, Map<String, ArrayList<String>>> entry : question.entrySet()) {
                    questionType = entry.getKey();
                    Map<String, ArrayList<String>> value = entry.getValue();
                    System.out.println("Question № " + questionNumber + "\n" + "Type: " + questionType);
                    for (Map.Entry<String, ArrayList<String>> entry1 : value.entrySet()){
                        questionText = entry1.getKey();
                        answers = entry1.getValue();
                        System.out.println("Text: " + questionText + "\nAnswers: " + answers +"\n\n");
                    }
                }

                //запись в файл
                if (types.containsKey(questionType)){
                    switch (questionType) {
                        case ("ddwtos") -> ddwtos(document, questionNumber, questionText, answers, questionType, types);
                        case ("essay") -> essay(document, questionNumber, questionText, questionType, types);
                        case ("gapselect"), ("multichoice"), ("multichoiceset") -> multichoicesAndGap(document, questionNumber, questionText, answers, questionType, types);
                        case ("numerical"), ("shortanswer") -> numericalAndShort(document, questionNumber, questionText, answers, questionType, types);
                        default -> System.out.println("Такого типа вопроса нет");
                    }
                }
            }

            //записываем все в док и закрываем его
            document.write(out);
            out.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
