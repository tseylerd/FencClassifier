package ru.spbstu.neer2015.data;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Normalize;

import java.io.BufferedReader;
import java.io.FileReader;

import static ru.spbstu.neer2015.data.GeneratorSetter.PATH_TO_TRAIN_SET;

/**
 * Created by tseyler on 18.05.15.
 */
public class DataReader {
    private static DataReader data;
    Instances train;

    public DataReader() throws Exception {

        train = new Instances(new BufferedReader(new FileReader(PATH_TO_TRAIN_SET)));
        if (GeneratorSetter.NORMALIZE) {
            Normalize normalize = new Normalize();
            normalize.setInputFormat(train);
            train = Filter.useFilter(train, normalize);
        }
        Remove remove = new Remove();
        remove.setAttributeIndices("1");
        remove.setInputFormat(train);
        train = Filter.useFilter(train, remove);
        train.setClassIndex(train.numAttributes() - 1);

    }

    public static Instances getTrain() throws Exception {
        if (data == null) {
            data = new DataReader();
        }
        return data.train;
    }
}
