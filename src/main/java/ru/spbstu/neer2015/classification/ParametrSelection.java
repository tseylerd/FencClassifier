package ru.spbstu.neer2015.classification;

import ru.spbstu.neer2015.data.ParametrSelectionSetter;

import javax.swing.*;

public class ParametrSelection {
    private int bestKernel;
    private double bestParam;
    private double bestC;
    private double correct;
    private final ParametrSelectionSetter setter;
    private final boolean paramSet;

    public ParametrSelection(ParametrSelectionSetter setter, boolean paramSet) {
        this.setter = setter;
        this.paramSet = paramSet;
    }

    private int getStartKernel(){
        int result = 1;
        if (!paramSet){
            result +=3;
        }
        return result;
    }
    private int getEndKernel(){
        int result = 4;
        if (!paramSet){
            result +=3;
        }
        return result;
    }
    public void evaluate(JProgressBar progressBar, JTextPane label) throws Exception {
        double[] cParamGrid = getGrid(setter.getStartC(), setter.getStepC(), setter.getEndC());
        double[] kernelParamGrid = getGrid(setter.getStartKernel(), setter.getStepKernel(), setter.getEndKernel());
        int cLen = cParamGrid.length;
        int kLen = kernelParamGrid.length;
        progressBar.setValue(0);
        correct = 0;
        int count = (getEndKernel() - getStartKernel())*cLen*kLen;
        progressBar.setMinimum(0);
        progressBar.setMaximum(count);
        for (int i = getStartKernel(); i < getEndKernel(); i++) {
            for (int j = 0; j < cLen; j++) {
                for (int k = 0; k < kLen; k++) {
                    UserClassifier classifier = new UserClassifier();
                    classifier.buildClassifier(i, (int) cParamGrid[j], kernelParamGrid[k]);
                    double estimator = classifier.getEstimator();
                    label.setText(getNowResults(cParamGrid[j], kernelParamGrid[k], i, estimator));
                    if (estimator > correct) {
                        correct = estimator;
                        bestKernel = i;
                        bestParam = kernelParamGrid[k];
                        bestC = cParamGrid[j];
                    }
                    progressBar.setValue(progressBar.getValue()+1);
                }
            }
        }

    }
    public UserClassifier getBestClassifier() throws Exception{
        UserClassifier classifier = new UserClassifier();
        classifier.buildClassifier(bestKernel, (int)bestC, bestParam);
        return classifier;
    }
    public String getStringResults() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Best parametrs for classifier:\n");
        stringBuilder.append("\tC: " + Double.toString(bestC) + "\n");
        stringBuilder.append("\tKernel parametr: " + Double.toString(bestParam) + "\n");
        stringBuilder.append("\tKernel: ");
        if (bestKernel == 1) {
            stringBuilder.append("polynomial kernel\n");
        } else if (bestKernel == 2) {
            stringBuilder.append("normalized kernel\n");
        } else {
            stringBuilder.append("RBF kernel\n");
        }
        stringBuilder.append("\tCorrect: " + Double.toString(correct) + "\n");
        return stringBuilder.toString();
    }

    public String getNowResults(double c, double kernelP, int kernelType, double correct) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Parametrs:\n");
        stringBuilder.append("\tC: " + Double.toString(c) + "\n");
        stringBuilder.append("\tKernel parametr: " + Double.toString(kernelP) + "\n");
        stringBuilder.append("\tKernel: ");
        if (kernelType == 1) {
            stringBuilder.append("polynomial kernel\n");
        } else if (kernelType == 2) {
            stringBuilder.append("normalized kernel\n");
        } else {
            stringBuilder.append("RBF kernel\n");
        }
        stringBuilder.append("\tCorrect: " + Double.toString(correct) + "\n");
        //stringBuilder.append("------------------------------------------------------\n");
        return stringBuilder.toString();
    }

    private double[] getGrid(double start, double step, double end) {
        int steps = (int) Math.round((end - start) / step);
        double[] grid = new double[steps+1];
        double current = start;
        for (int i = -1; i < steps; i++) {
            grid[i+1] = current;
            current += step;
        }
        return grid;
    }

    public static void main(String[] args) throws Exception {
        ParametrSelectionSetter setter1 = new ParametrSelectionSetter(200, 300, 1, 1, 1, 1);
        ParametrSelection parametrSelection = new ParametrSelection(setter1, false);
        parametrSelection.evaluate(new JProgressBar(), new JTextPane());
        System.out.println(parametrSelection.getStringResults());
    }
}
