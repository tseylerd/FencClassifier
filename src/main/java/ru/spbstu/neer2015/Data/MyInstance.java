package ru.spbstu.neer2015.data;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by tseyler on 28.05.15.
 */
public class MyInstance extends Instance {
    public MyInstance(Instances dataSet, int count, int years, double mid, int country, int rating, int hand, int teamRating) {
        super(count);
        setDataset(dataSet);
        setValue(0, years);
        setValue(1, mid);
        setValue(2, country);
        setValue(3, rating);
        setValue(4, hand);
        setValue(5, teamRating);
    }
}
