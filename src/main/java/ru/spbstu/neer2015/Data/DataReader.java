package ru.spbstu.neer2015.data;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Normalize;

import java.io.BufferedReader;
import java.io.FileReader;

import static ru.spbstu.neer2015.data.GeneratorSetter.pathToTrainSet;

/**
 * Created by tseyler on 18.05.15.
 */
public class DataReader {
    private static DataReader data = new DataReader();
    Instances train;

    public DataReader() {
        try {
            train = new Instances(new BufferedReader(new FileReader(pathToTrainSet)));
            if (GeneratorSetter.normalize) {
                Normalize normalize = new Normalize();
                normalize.setInputFormat(train);
                train = Filter.useFilter(train, normalize);
            }
            Remove remove = new Remove();
            remove.setAttributeIndices("1");
            remove.setInputFormat(train);
            train = Filter.useFilter(train, remove);
            train.setClassIndex(train.numAttributes() - 1);
        } catch (Exception e) {
            System.out.print("Data error.");
        }
    }

    public static Instances getTrain() {
        return data.train;
    }
}
