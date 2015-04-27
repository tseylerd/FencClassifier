package ru.spbstu.neer2015.Data;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;

public class Setter {
    public final static boolean yearsOld = true;
    public final static int defaultYears = -1;
    public final static String pathToResults = "ForLearn/";
    public final static String pathToTrainSet = "ForLearn/Result/train";
    public final static String world = "world";
    public final static int dimensions = 9;
    public final static int numberBests = 6;
    public final static double power = (1);
    public final static int numberOfTrainingFolders = 7;
    public final static int competitionsNumber = 9;
    public final static int defaultsMax = 4;
    public final static int numberOfClasses = 10;
    public final static double defaultResult = -1;
    public final static int filteredPlace = 9;
    public final static boolean normalize = true;
    public final static boolean sayThatGood = false;
    public final static int classesNumber = 9;
    public final static int maxPasses = 100;
    public final static boolean mean = true;
    public final static boolean filterSmaller = true;
    public final static boolean exponentClasses = true;
    public final static boolean bests = true;
    public final static int kernel = 1;
    public static double placeToClass(double place){
        if (sayThatGood){
            if (place <= 16){
                return 0;
            }else {
                return 1;
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
        }else {
            double result = (int)place/4;
            if (result > classesNumber)
                result = classesNumber;
            return result;
        }
    }
    public static Kernel getKernel(){
        switch (kernel){
            case 1: return new PolyKernel();
            case 2: return new NormalizedPolyKernel();
            case 3: return new RBFKernel();
            default: return new PolyKernel();
        }
    }
}
