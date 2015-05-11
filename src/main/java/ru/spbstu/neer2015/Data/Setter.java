package ru.spbstu.neer2015.data;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;

public class Setter {
    public final static boolean yearsOld = true;
    public final static boolean addCountry = true;
    public final static boolean addCountryRating = true;
    public final static boolean addRating = true;
    public final static int defaultYears = -1;
    public final static int leftSel = 10000;
    public final static int rightSel = 1;
    public final static String pathToResults = "ForLearn/";
    public final static String pathToTrainSet = "ForLearn/Result/train";
    public final static String pathToHands = "Data/hands";
    public final static String rating = "rating";
    public final static String teamRating = "teamRating";
    public final static String world = "world";
    public final static int dimensions = 9;
    public final static int numberBests = 8;
    public final static double power = 1;
    public final static int competitionsNumber = 9;
    public final static int defaultsMax = 4;
    public final static double defaultResult = -1;
    public final static boolean normalize = true;
    public static boolean sayThatGood = false;
    public final static int maxClasses = 50;
    public final static boolean mean = true;
    public final static boolean filterSmaller = true;
    public final static boolean exponentClasses = true;
    public final static boolean bests = true;
    public final static boolean addHand = false;
    public double startC = 1;
    public double endC = 100;
    public double stepC = 1;
    public double startKernel = 1;
    public double endKernel = 4;
    public double stepKernel = 0.1;
    public final static String countiesPath = "data/countries.txt";

    public Setter(double startC, double endC, double stepC, double startKernel, double endKernel, double stepKernel) {
        this.startC = startC;
        this.endC = endC;
        this.stepC = stepC;
        this.stepKernel = stepKernel;
        this.startKernel = startKernel;
        this.endKernel = endKernel;
    }

    public Setter(boolean sayThatGood) {
        this.sayThatGood = sayThatGood;
    }

    public static int getClassesNumber() {
        if (exponentClasses) {
            return 5;
        }
        if (sayThatGood) {
            return 2;
        } else {
            return 50;
        }
    }

    public static double placeToClass(final double place) {
        if (sayThatGood) {
            if (place < 17) {
                return 1;
            } else {
                return 2;
            }
        }
        if (exponentClasses) {
            if (place >= 1 && place < 9) {
                return 1;
            }
            if (place > 8 && place < 17) {
                return 2;
            }
            if (place > 16 && place < 33) {
                return 3;
            }
            if (place > 32 && place < 65) {
                return 4;
            }
            return 5;
        } else {
            double result = (int) place / 4;
            if (result > maxClasses)
                result = maxClasses;
            return result;
        }
    }

    public static Kernel getKernel(int type, double param) {
        switch (type) {
            case 1: {
                PolyKernel polyKernel = new PolyKernel();
                polyKernel.setExponent(param);
                return polyKernel;
            }
            case 2: {
                NormalizedPolyKernel normalizedPolyKernel = new NormalizedPolyKernel();
                normalizedPolyKernel.setExponent(param);
                return normalizedPolyKernel;
            }
            case 3: {
                RBFKernel rbfKernel = new RBFKernel();
                rbfKernel.setGamma(param);
                return rbfKernel;
            }
            case 4: {
                PolyKernel polyKernel = new PolyKernel();
                return polyKernel;
            }
            case 5: {
                NormalizedPolyKernel normalizedPolyKernel = new NormalizedPolyKernel();
                return normalizedPolyKernel;
            }
            case 6: {
                RBFKernel rbfKernel = new RBFKernel();
                return rbfKernel;
            }
            default:
                return new PolyKernel();
        }
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
