package ru.spbstu.neer2015.data;

import java.util.Arrays;
import java.util.Comparator;

import static ru.spbstu.neer2015.data.GeneratorSetter.*;

public class Sportsmen {
    private String name;
    private double[] results;
    private int i;
    private int yearsOld;
    private SportsmenClass place;
    private int country;
    private int rating;
    private int hand;
    private int countryRating;
    private final String DELIMITER = ",";
    private final String STRING_END_OR_BEGIN = "'";
    public Sportsmen(String name, int place, int country) {
        this.name = name;
        this.results = new double[dimensions];
        i = 0;
        this.place = SportsmenClass.getClass(place);
        this.country = country;
    }

    public Sportsmen(Sportsmen sportsmen) {
        name = sportsmen.name;
        results = new double[dimensions];
        System.arraycopy(sportsmen.getResults(), 0, results, 0, dimensions);
        place = sportsmen.getPlace();
        i = dimensions;
    }

    public void setCountryRating(int countryRating) {
        this.countryRating = countryRating;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public void setRating(int pos) {
        this.rating = pos*2;
    }

    public boolean ifFull() {
        return i == dimensions;
    }

    public double[] getResults() {
        return results;
    }

    public int defaultsNumber() {
        int c = 0;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] == defaultResult) {
                c++;
            }
        }
        return c;
    }

    public String getName() {
        return name;
    }

    public SportsmenClass getPlace() {
        return place;
    }

    public void pushResult(double result) {
        if (i < results.length) {
            results[i] = result;
            i++;
        }
    }

    public void setYearsOld(int yearsOld) {
        this.yearsOld = yearsOld;
    }

    private double getMeanRealResult(){
        double sum = 0;
        double count = 0;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] != defaultResult) {
                sum += results[j];
                count++;
            }
        }
        sum /= count;
        return (sum);
    }
    private void setDefaultResultsAsMean(double mean){
        for (int j = 0; j < dimensions; j++) {
            if (results[j] == defaultResult) {
                results[j] = mean;
            }
        }
    }
    private void doThatResultsAreTrue() {
        double mean = getMeanRealResult();
        setDefaultResultsAsMean(mean);
        Arrays.sort(results);
    }
    public boolean checkCorrect() {
        return defaultsNumber() <= defaultsMax && !isItEmmision();
    }

    private boolean isItEmmision(){
        if(filterSmaller){
            double sum = getMeanRealResult();
            if (Math.abs(SportsmenClass.getClass(sum).getSportsmenClass() - place.getSportsmenClass()) > 2) {
                return true;
            }
        }
        return false;
    }
    private double getBestsMean() {
        double mid = 0;
        for (int j = 0; j < numberBests; j++) {
            mid += results[j];
        }
        mid = Math.pow(mid / numberBests, power);
        return mid;
    }

    private void appendFactor(StringBuilder stringBuilder, Number whatToAppend){
        stringBuilder.append(whatToAppend);
        stringBuilder.append(DELIMITER);
    }

    private void appendName(StringBuilder stringBuilder, String name){
        stringBuilder.append(STRING_END_OR_BEGIN);
        stringBuilder.append(name);
        stringBuilder.append(STRING_END_OR_BEGIN);
        stringBuilder.append(DELIMITER);
    }

    public String getText() {
        doThatResultsAreTrue();
        StringBuilder result = new StringBuilder();
        double mid = getBestsMean();
        appendName(result, name);
        appendFactor(result, yearsOld);
        appendFactor(result, mid);
        appendFactor(result, country);
        appendFactor(result, rating);
        appendFactor(result, hand);
        appendFactor(result, countryRating);
        result.append(place.getSportsmenClass());
        return result.toString();
    }
}
