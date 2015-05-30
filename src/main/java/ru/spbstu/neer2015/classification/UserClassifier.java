package ru.spbstu.neer2015.classification;

import ru.spbstu.neer2015.data.DataReader;
import ru.spbstu.neer2015.data.Generator;
import ru.spbstu.neer2015.data.GeneratorSetter;
import ru.spbstu.neer2015.data.MyInstance;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.meta.MultiBoostAB;
import weka.core.Instances;
import weka.filters.unsupervised.instance.Normalize;

import java.io.*;
import java.util.Random;

import static ru.spbstu.neer2015.data.GeneratorSetter.MODEL_PATH;

/**
 * Created by tseyler on 11.03.15.
 */
public class UserClassifier {
    private MultiBoostAB multiBoostAB;
    private Instances train;

    public UserClassifier() throws Exception {
        train = new Instances(DataReader.getTrain());
    }

    public static void main(String[] args) throws Exception {
        Generator generator = new Generator();
        generator.generateTrainSet();
        generator.saveSportsmens();
        /*ParametrSelection parametrSelection = new ParametrSelection(new ParametrSelectionSetter(1, 500, 100, 0.01, 2, 0.5), false);
        parametrSelection.evaluate(new JProgressBar(), new JTextPane());
        System.out.print(parametrSelection.getStringResults());
        UserClassifier classifier1 = parametrSelection.getBestClassifier();*/
        UserClassifier classifier1 = new UserClassifier();
        classifier1.buildClassifier(2, 500, 4);
        classifier1.crossValidateToConsole();
    }

    public int classifySportsmen(int years, double mid, int country, int rating, int hand, int teamRating) throws Exception {
        MyInstance instance = new MyInstance(train, 7, years, mid, country, rating, hand, teamRating);
        Normalize normalize = new Normalize();
        normalize.setInputFormat(train);
        normalize.input(instance);
        instance = (MyInstance) normalize.output();
        int clazz = (int) multiBoostAB.classifyInstance(instance);
        return clazz;
    }

    public void buildClassifier(final int kernelType, final int c, double param) throws Exception {
        SMO smo1 = new SMO();
        Kernel kernel1 = MyKernel.getMyKernel(kernelType).getKernel(param);
        smo1.setKernel(kernel1);
        smo1.setC(c);
        multiBoostAB = new MultiBoostAB();
        multiBoostAB.setClassifier(smo1);
        multiBoostAB.setNumSubCmtys(6);
        multiBoostAB.buildClassifier(train);
    }

    public void saveModel() throws Exception {
        ObjectOutputStream modelOS = new ObjectOutputStream(new FileOutputStream(MODEL_PATH));
        modelOS.writeObject(multiBoostAB);
        modelOS.flush();
        modelOS.close();
    }

    public void loadModel() throws Exception {
        FileInputStream fis = new FileInputStream(MODEL_PATH);
        ObjectInputStream ois = new ObjectInputStream(fis);
        multiBoostAB = (MultiBoostAB) ois.readObject();
        ois.close();
    }

    public void crossValidateToConsole() throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(multiBoostAB, train, 10, new Random(1));
        System.out.println(evaluation.toSummaryString());
    }

    public double getEstimator() throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.crossValidateModel(multiBoostAB, train, 10, new Random(1));
        return evaluation.pctCorrect();
    }

    public void classifyAllFromFile(final String path) throws Exception {
        Instances unlabeled = new Instances(
                new BufferedReader(
                        new FileReader(path)));
        if (GeneratorSetter.NORMALIZE) {
            Normalize normalize = new Normalize();
            unlabeled = weka.filters.Filter.useFilter(unlabeled, normalize);
        }
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = multiBoostAB.classifyInstance(unlabeled.instance(i));
            unlabeled.instance(i).setClassValue(clsLabel);
        }
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("path"));
        writer.write(unlabeled.toString());
        writer.newLine();
        writer.close();
    }

}
