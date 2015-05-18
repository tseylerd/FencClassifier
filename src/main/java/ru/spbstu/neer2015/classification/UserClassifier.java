package ru.spbstu.neer2015.classification;

import ru.spbstu.neer2015.data.DataReader;
import ru.spbstu.neer2015.data.EvaluationSetter;
import ru.spbstu.neer2015.data.GeneratorSetter;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.*;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Normalize;

import java.io.*;
import java.util.Random;

import static ru.spbstu.neer2015.data.GeneratorSetter.*;

/**
 * Created by tseyler on 11.03.15.
 */
public class UserClassifier {
    private MultiBoostAB smo;
    private Instances train;
    public UserClassifier() throws Exception {
        train = DataReader.getTrain();
    }
    public double classifySportsmen(int years, double mid, int country, int rating, int hand, int teamRating) throws Exception {
        Instance instance = new Instance(7);
        instance.setDataset(train);
        instance.setValue(0, "Name");
        instance.setValue(1, years);
        instance.setValue(2, mid);
        instance.setValue(3, country);
        instance.setValue(4, rating);
        instance.setValue(5, hand);
        instance.setValue(6, teamRating);
        instance.setClassMissing();
        Normalize normalize1 = new Normalize();
        normalize1.setInputFormat(train);
        normalize1.input(instance);
        instance = normalize1.output();
        double clazz = smo.classifyInstance(instance);
        return clazz;
    }
    private Kernel getKernel(int type, double param) {
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
    public void buildClassifier(final int kernelType, final int c, double param) throws Exception {
        SMO smo1 = new SMO();
        Kernel kernel1 = getKernel(kernelType, param);
        smo1.setKernel(kernel1);
        smo1.setC(c);
        Remove remove = new Remove();
        remove.setAttributeIndices("1");
        remove.setInputFormat(train);
        train = Filter.useFilter(train, remove);
        smo = new MultiBoostAB();
        smo.setClassifier(smo1);
        smo.setNumSubCmtys(6);
        smo.buildClassifier(train);
    }
    public void crossValidateToConsole() throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(smo, train, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());
    }

    public double getEstimator() throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(smo, train, 10, new Random(1));
        return evaluation.pctCorrect();
    }

    public void classifyAllFromFile(final String path) throws Exception {
        Instances unlabeled = new Instances(
                new BufferedReader(
                        new FileReader(path)));
        if (GeneratorSetter.normalize) {
            Normalize normalize = new Normalize();
            unlabeled = weka.filters.Filter.useFilter(unlabeled, normalize);
        }
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = smo.classifyInstance(unlabeled.instance(i));
            unlabeled.instance(i).setClassValue(clsLabel);
        }
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("path"));
        writer.write(unlabeled.toString());
        writer.newLine();
        writer.close();
    }

    public void finalize() {
        train.delete();
    }

    public static void main(String[] args) throws Exception {
/*        Generator generator = new Generator();
        generator.generateTrainSet();
        generator.saveSportsmens();*/
        Evaluator evaluator = new Evaluator(new EvaluationSetter(200, 300, 1, 1, 1, 1), false);
        evaluator.evaluate();
        UserClassifier classifier1 = evaluator.getBestClassifier();
        classifier1.crossValidateToConsole();
    }
}
