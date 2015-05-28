package ru.spbstu.neer2015.classification;

import ru.spbstu.neer2015.data.ParametrSelectionSetter;

import javax.swing.*;

public class ParametrSelection {
    private final ParametrSelectionSetter setter;
    private final boolean paramSet;
    private int bestKernel;
    private double bestParam;
    private int bestC;
    private double correct;

    public ParametrSelection(ParametrSelectionSetter setter, boolean paramSet) {
        this.setter = setter;
        this.paramSet = paramSet;
    }

    public static void main(String[] args) throws Exception {
        ParametrSelectionSetter setter1 = new ParametrSelectionSetter(200, 300, 1, 1, 1, 1);
        ParametrSelection parametrSelection = new ParametrSelection(setter1, false);
        parametrSelection.evaluate(new JProgressBar(), new JTextPane());
    }

    private int getStartKernel() {
        return 1;
    }

    private int getEndKernel() {
        return 4;
    }

    public void evaluate(JProgressBar progressBar, JTextPane label) throws Exception {
        double[] cParamGrid = getGrid(setter.getStartC(), setter.getStepC(), setter.getEndC());
        double[] kernelParamGrid = getGrid(setter.getStartKernel(), setter.getStepKernel(), setter.getEndKernel());
        int cLen = cParamGrid.length;
        int kLen = kernelParamGrid.length;
        progressBar.setValue(0);
        correct = 0;
        int count = (getEndKernel() - getStartKernel()) * cLen * kLen;
        progressBar.setMinimum(0);
        progressBar.setMaximum(count);
        for (int i = getStartKernel(); i < getEndKernel(); i++) {
            for (int j = 0; j < cLen; j++) {
                for (int k = 0; k < kLen; k++) {
                    UserClassifier classifier = new UserClassifier();
                    classifier.buildClassifier(i, (int) cParamGrid[j], kernelParamGrid[k]);
                    double estimator = classifier.getEstimator();
                    label.setText(getStringResults((int)cParamGrid[j], kernelParamGrid[k], i, estimator));
                    if (estimator > correct) {
                        correct = estimator;
                        bestKernel = i;
                        bestParam = kernelParamGrid[k];
                        bestC = (int)cParamGrid[j];
                    }
                    progressBar.setValue(progressBar.getValue() + 1);
                }
            }
        }

    }

    public UserClassifier getBestClassifier() throws Exception {
        UserClassifier classifier = new UserClassifier();
        classifier.buildClassifier(bestKernel, bestC, bestParam);
        return classifier;
    }

    public String getStringResults(int c, double param, int kernel, double correct) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Parametrs for classifier:\n");
        stringBuilder.append("\tC: " + Double.toString(c) + "\n");
        stringBuilder.append("\tKernel parametr: " + Double.toString(param) + "\n");
        stringBuilder.append("\tKernel: ");
        stringBuilder.append(MyKernel.getMyKernel(kernel).getName());
        stringBuilder.append("\tCorrect: " + Double.toString(correct) + "\n");
        return stringBuilder.toString();
    }
    public String getStringResults() {
        return getStringResults(bestC, bestParam, bestKernel, correct);
    }

    private double[] getGrid(double start, double step, double end) {
        int steps = (int) Math.round((end - start) / step);
        double[] grid = new double[steps + 1];
        double current = start;
        for (int i = -1; i < steps; i++) {
            grid[i + 1] = current;
            current += step;
        }
        return grid;
    }
}
