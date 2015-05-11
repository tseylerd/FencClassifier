package ru.spbstu.neer2015.Classifier;

import ru.spbstu.neer2015.Data.Setter;
import ru.spbstu.neer2015.Data.Generator;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Normalize;

import java.io.*;
import java.util.Random;

import static ru.spbstu.neer2015.Data.Setter.*;

/**
 * Created by tseyler on 11.03.15.
 */
public class Classifier {
    private FilteredClassifier classifier;
    private Instances train;
    public Classifier() throws Exception{
        train = new Instances(new BufferedReader(new FileReader(pathToTrainSet)));
        if (Setter.normalize){
            Normalize normalize = new Normalize();
            normalize.setInputFormat(train);
            train = Filter.useFilter(train, normalize);
        }
        train.setClassIndex(train.numAttributes()-1);
        classifier = new FilteredClassifier();
    }
    public void buildClassifier(final int kernelType, final int c, double param) throws Exception{
        SMO smo = new SMO();
        Kernel kernel1 = getKernel(kernelType, param);
        String[] opt = kernel1.getOptions();
        smo.setKernel(kernel1);
        smo.setC(c);
        for (int i = 0; i < opt.length; i++) {
            System.out.println(opt[i]);
        }
        Remove remove = new Remove();
        remove.setAttributeIndices("1");
        classifier.setFilter(remove);
        classifier.setClassifier(smo);
        classifier.buildClassifier(train);
    }

    public void crossValidateToConsole() throws Exception{
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(classifier, train, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());
    }
    public double getEstimator() throws Exception{
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(classifier, train, 10, new Random(1));
        return evaluation.pctCorrect();
    }
    public void classifyAllFromFile(final String path) throws Exception{
        Instances unlabeled = new Instances(
                new BufferedReader(
                        new FileReader(path)));
        if (Setter.normalize) {
            Normalize normalize = new Normalize();
            unlabeled = weka.filters.Filter.useFilter(unlabeled, normalize);
        }
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = classifier.classifyInstance(unlabeled.instance(i));
            unlabeled.instance(i).setClassValue(clsLabel);
        }
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("path"));
        writer.write(unlabeled.toString());
        writer.newLine();
        writer.close();
    }
    public void finalize(){
        train.delete();
    }
    public static void main(String[] args) throws Exception{
        Generator generator = new Generator();
        generator.generateTrainSet();
        generator.saveSportsmens();
        Classifier classifier1 = new Classifier();
        classifier1.buildClassifier(4, 10000, 0.01);
        classifier1.crossValidateToConsole();
    }
}
