package ru.spbstu.neer2015.classification;

import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;

/**
 * Created by tseyler on 28.05.15.
 */
public enum MyKernel {
    MY_POLY_KERNEL(1) {
        @Override
        public Kernel getKernel(double param) {
            PolyKernel polyKernel = new PolyKernel();
            polyKernel.setExponent(param);
            return polyKernel;
        }

        @Override
        public String getName() {
            return "poly kernel\n";
        }
    },
    MY_NORM_KERNEL(2) {
        @Override
        public Kernel getKernel(double param) {
            NormalizedPolyKernel normalizedPolyKernel = new NormalizedPolyKernel();
            normalizedPolyKernel.setExponent(param);
            return normalizedPolyKernel;
        }

        @Override
        public String getName() {
            return "normalzed poly kernel\n";
        }
    },
    MY_RBF_KERNEL(3) {
        @Override
        public Kernel getKernel(double param) {
            RBFKernel rbfKernel = new RBFKernel();
            rbfKernel.setGamma(param);
            return rbfKernel;
        }

        @Override
        public String getName() {
            return "RBF kernel\n";
        }
    };
    private int number;
    private Kernel kernel;

    private MyKernel(int number) {
        this.number = number;
    }

    public static MyKernel getMyKernel(int number) {
        for (MyKernel myKernel : MyKernel.values()) {
            if (myKernel.number == number) {
                return myKernel;
            }
        }
        return null;
    }

    public abstract Kernel getKernel(double param);

    public abstract String getName();
}
