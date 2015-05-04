package ru.spbstu.neer2015.Data;

import java.util.Arrays;

import static ru.spbstu.neer2015.Data.Setter.*;
/**
 * Created by tseyler on 30.01.15.
 */
public class Sportsmen {
    private String name;
    private double[] results;
    private int i;
    private int place;
    public Sportsmen(String name, int place){
        this.name = name;
        this.results = new double[dimensions];
        i = 0;
        this.place = place;
    }

    public Sportsmen(Sportsmen sportsmen){
        name = sportsmen.name;
        results = new double[dimensions];
        double[] oldVector = sportsmen.getResults();
        for (int j = 0; j < dimensions; j++) {
            results[j] = oldVector[j];
        }
        place = sportsmen.getPlace();
        i = dimensions;
    }
    public void normalize(){
        double sum = 0;
        for (int j = 0; j < dimensions; j++) {
            sum += results[j]* results[j];
        }
        sum = Math.sqrt(sum);
        for (int j = 0; j < dimensions; j++) {
            results[j] /= sum;
        }
    }
    public boolean ifFull(){
        return i == dimensions;
    }
    public void compareAndChangeClass(double place){
        if (this.place != place){
            this.place = -1;
        }else {
            this.place = 1;
        }
    }
    public double[] getResults() {
        return results;
    }
    public int defaultsNumber(){
        int c = 0;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] == defaultResult){
                c++;
            }
        }
        return c;
    }

    public String getName() {
        return name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
    public void pushResult(double result){
        try {
            results[i] = result;
            i++;
        }catch (Exception e){
            System.out.print(name);
        }
    }
    public void setResults(){
        results[0] /= dimensions;
    }
    public void setYearsOld(double yearsOld){
        results[i] = yearsOld;
        ++i;
    }
    private void doThatResultsAreTrue(){
        int r = 0;
        int k = 0;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] != defaultResult){
                r += results[j];
                k++;
            }
        }
        r /= k;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] == defaultResult){
               results[j] = r;
            }
        }
        double[] temp = new double[dimensions-1];
        for (int j = 1; j < dimensions; j++) {
            temp[j-1] = results[j];
        }
        Arrays.sort(temp);
        for (int j = 1; j < dimensions; j++) {
            results[j] = temp[j-1];
        }
    }
    public boolean checkCorrect(){
        if (filterSmaller) {
            int sum = 0;
            for (int j = 1; j < dimensions; j++) {
                sum += results[j];
            }
            sum /= (dimensions-1);
            if (placeToClass(sum) > place) {
                return false;
            } else {
                return defaultsNumber() <= defaultsMax && place < filteredPlace;
            }
        }else {
            return defaultsNumber() <= defaultsMax;
        }
    }

    public String getText(){
        doThatResultsAreTrue();
        String result = "";
        result += "'" + name + "',";
        double mid = 0;
        for (int j = 1; j < numberBests+1; j++) {
            mid += results[j];
        }
        if (mean){
            mid = Math.pow(mid/numberBests, power);
           result += Double.toString(results[0]);
            result+= ",";
            result += Double.toString(mid);
            result+= ",";
        }else if(bests) {
            for (int j = 0; j < numberBests + 1; j++) {
                result += Integer.toString((int)Math.pow(results[j], power)) + ",";
            }
        }else {
            result += Integer.toString((int)Math.pow(results[0], power)) + ",";
            for (int j = dimensions-1; j > dimensions-numberBests-1; j--) {
                result += Integer.toString((int)Math.pow(results[j], power)) + ",";
            }
        }
        result += Integer.toString(place);
        return result;
    }
}
