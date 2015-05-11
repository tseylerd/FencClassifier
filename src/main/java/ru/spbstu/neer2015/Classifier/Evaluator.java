package ru.spbstu.neer2015.classifier;

import ru.spbstu.neer2015.data.Setter;

public class Evaluator {
    private int bestKernel;
    private double bestParam;
    private double bestC;
    private double correct;
    Setter setter;

    public Evaluator(Setter setter) {
        this.setter = setter;
    }

    public void evaluate() throws Exception {
        double[] cParamGrid = getGrid(setter.getStartC(), setter.getStepC(), setter.getEndC());
        double[] kernelParamGrid = getGrid(setter.getStartKernel(), setter.getStepKernel(), setter.getEndKernel());
        int cLen = cParamGrid.length;
        int kLen = kernelParamGrid.length;
        correct = 0;
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < cLen; j++) {
                for (int k = 0; k < kLen; k++) {
                    Classifier classifier = new Classifier();
                    classifier.buildClassifier(i, (int) cParamGrid[j], kernelParamGrid[k]);
                    double estimator = classifier.getEstimator();
                    System.out.println(getNowResults(cParamGrid[j], kernelParamGrid[k], i, estimator));
                    if (estimator > correct) {
                        correct = estimator;
                        bestKernel = i;
                        bestParam = kernelParamGrid[k];
                        bestC = cParamGrid[j];
                    }
                    classifier.finalize();
                }
            }
        }

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
        stringBuilder.append("------------------------------------------------------\n");
        return stringBuilder.toString();
    }

    private double[] getGrid(double start, double step, double end) {
        int steps = (int) Math.round((end - start) / step);
        double[] grid = new double[steps];
        double current = start;
        for (int i = 0; i < steps; i++) {
            grid[i] = current;
            current += step;
        }
        return grid;
    }

    public static void main(String[] args) throws Exception {
        Setter setter1 = new Setter(1, 100, 1, 1, 4, 0.01);
        Evaluator evaluator = new Evaluator(setter1);
        evaluator.evaluate();
        System.out.println(evaluator.getStringResults());
    }
}
