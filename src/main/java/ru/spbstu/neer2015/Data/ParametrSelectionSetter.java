package ru.spbstu.neer2015.data;

/**
 * Created by tseyler on 18.05.15.
 */
public class ParametrSelectionSetter {
    private int startC = 1;
    private int endC = 100;
    private int stepC = 1;
    private double startKernel = 1;
    private double endKernel = 4;
    private double stepKernel = 0.1;
    public ParametrSelectionSetter( int startC, int  endC, int stepC, double startKernel, double endKernel, double stepKernel) {
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
