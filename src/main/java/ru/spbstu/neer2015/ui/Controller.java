package ru.spbstu.neer2015.ui;

import ru.spbstu.neer2015.classification.ParametrSelection;
import ru.spbstu.neer2015.classification.UserClassifier;
import ru.spbstu.neer2015.data.ParametrSelectionSetter;

import javax.swing.*;
import java.util.ArrayList;

import static ru.spbstu.neer2015.data.GeneratorSetter.leftSel;
import static ru.spbstu.neer2015.data.GeneratorSetter.rightSel;

/**
 * Created by tseyler on 19.05.15.
 */
public class Controller {
    UserClassifier classifier;

    public Controller() throws Exception {
        classifier = new UserClassifier();
        classifier.loadModel();
    }

    public String getResult(ArrayList<JTextField> competitions, boolean hand, String individualRating, String teamRating, String country, int yearsOld) throws Exception {
        double mean = getMeanResult(competitions);
        int indRating = Integer.parseInt(individualRating);
        int cRating = (Integer.parseInt(teamRating));
        int handVal = hand ? leftSel : rightSel;
        int countryVal = country.hashCode();
        int clazz = classifier.classifySportsmen(yearsOld, mean, countryVal, indRating, handVal, cRating) + 1;
        return "Вероятнее всего, спортсмен займет место " + classToText(clazz);
    }

    private double getMeanResult(ArrayList<JTextField> competitions) {
        double mean = 0;
        for (int i = 0; i < competitions.size(); i++) {
            mean += Integer.parseInt(competitions.get(i).getText());
        }
        mean /= competitions.size();
        return mean;
    }

    private String classToText(int clazz) {
        switch (clazz) {
            case 1:
                return "c 1 по 4";
            case 2:
                return "c 5 по 8";
            case 3:
                return "c 9 по 16";
            case 4:
                return "c 17 по 32";
            case 5:
                return "с 33 по 64";
            case 6:
                return "выше 64";
        }
        return "";
    }

    public void setBestClassifier(String startC, String endC, String stepC, String startP, String endP, String stepP, JProgressBar progressBar, JTextPane pane, JTextPane finalResult) throws Exception {
        int cStart = Integer.parseInt(startC);
        int cEnd = Integer.parseInt(endC);
        int cStep = Integer.parseInt(stepC);
        double pStart = Double.parseDouble(startP);
        double pEnd = Double.parseDouble(endP);
        double pStep = Double.parseDouble(stepC);
        ParametrSelectionSetter parametrSelectionSetter = new ParametrSelectionSetter(cStart, cEnd, cStep, pStart, pEnd, pStep);
        ParametrSelection parametrSelection = new ParametrSelection(parametrSelectionSetter, true);
        parametrSelection.evaluate(progressBar, pane);
        finalResult.setText(parametrSelection.getStringResults());
    }

}
