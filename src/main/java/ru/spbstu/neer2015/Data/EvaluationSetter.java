package ru.spbstu.neer2015.data;

/**
 * Created by tseyler on 18.05.15.
 */
public class EvaluationSetter {
    private double startC = 1;
    private double endC = 100;
    private double stepC = 1;
    private double startKernel = 1;
    private double endKernel = 4;
    private double stepKernel = 0.1;
    public EvaluationSetter(double startC, double endC, double stepC, double startKernel, double endKernel, double stepKernel) {
        this.startC = startC;
        this.endC = endC;
        this.stepC = stepC;
        this.stepKernel = stepKernel;
        this.startKernel = startKernel;
        this.endKernel = endKernel;
    }
    public double getStartC() {
        return startC;
    }

    public double getEndC() {
        return endC;
    }

    public double getStepC() {
        return stepC;
    }

    public double getStartKernel() {
        return startKernel;
    }

    public double getEndKernel() {
        return endKernel;
    }

    public double getStepKernel() {
        return stepKernel;
    }
}
