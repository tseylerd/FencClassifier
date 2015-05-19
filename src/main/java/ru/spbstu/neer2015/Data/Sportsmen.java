package ru.spbstu.neer2015.data;

import java.util.Arrays;

import static ru.spbstu.neer2015.data.GeneratorSetter.*;

public class Sportsmen {
    private String name;
    private double[] results;
    private int i;
    private int place;
    private double country;
    private int rating;
    private int hand;
    private int countryRating;

    public Sportsmen(String name, int place, double country) {
        this.name = name;
        this.results = new double[dimensions];
        i = 0;
        this.place = placeToClass(place);
        this.country = country;
    }

    public Sportsmen(Sportsmen sportsmen) {
        name = sportsmen.name;
        results = new double[dimensions];
        double[] oldVector = sportsmen.getResults();
        for (int j = 0; j < dimensions; j++) {
            results[j] = oldVector[j];
        }
        place = sportsmen.getPlace();
        i = dimensions;
    }

    private static int placeToClass(final int place) {
        if (sayThatGood) {
            if (place < 17) {
                return 1;
            } else {
                return 2;
            }
        }
        if (exponentClasses) {
            if (place >= 1 && place < 5) {
                return 1;
            }
            if (place > 4 && place < 9) {
                return 2;
            }
            if (place > 8 && place < 17) {
                return 3;
            }
            if (place > 16 && place < 33) {
                return 4;
            }
            if (place > 32 && place < 65) {
                return 5;
            }
            return 6;
        } else {
            int result = place / 4;
            if (result > maxClasses)
                result = maxClasses;
            return result;
        }
    }

    public void setCountryRating(int countryRating) {
        this.countryRating = countryRating;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public void setRating(int pos) {
        this.rating = pos;
    }

    public void normalize() {
        double sum = 0;
        for (int j = 0; j < dimensions; j++) {
            sum += results[j] * results[j];
        }
        sum = Math.sqrt(sum);
        for (int j = 0; j < dimensions; j++) {
            results[j] /= sum;
        }
    }

    public boolean ifFull() {
        return i == dimensions;
    }

    public void compareAndChangeClass(double place) {
        if (this.place != place) {
            this.place = -1;
        } else {
            this.place = 1;
        }
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

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void pushResult(double result) {
        if (i < results.length) {
            results[i] = result;
            i++;
        }
    }

    public void setResults() {
        results[0] /= dimensions;
    }

    public void setYearsOld(double yearsOld) {
        results[i] = yearsOld;
        ++i;
    }

    private void doThatResultsAreTrue() {
        int r = 0;
        int k = 0;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] != defaultResult) {
                r += results[j];
                k++;
            }
        }
        r /= k;
        for (int j = 0; j < dimensions; j++) {
            if (results[j] == defaultResult) {
                results[j] = r;
            }
        }
        double[] temp = new double[dimensions - 1];
        for (int j = 1; j < dimensions; j++) {
            temp[j - 1] = results[j];
        }
        Arrays.sort(temp);
        for (int j = 1; j < dimensions; j++) {
            results[j] = temp[j - 1];
        }
    }

    public boolean checkCorrect() {
        if (filterSmaller) {
            int sum = 0;
            for (int j = 1; j < dimensions; j++) {
                sum += results[j];
            }
            sum /= (dimensions - 1);
            if (placeToClass(sum) > place) {
                return false;
            }
        }
        return defaultsNumber() <= defaultsMax;
    }

    private double getMean() {
        double mid = 0;
        for (int j = 1; j < numberBests + 1; j++) {
            mid += results[j];
        }
        mid = Math.pow(mid / numberBests, power);
        return mid;
    }

    public String getText() {
        doThatResultsAreTrue();
        String result = "";
        result += "'" + name + "',";
        if (mean) {
            double mid = getMean();
            result += Double.toString(results[0]);
            result += ",";
            result += Double.toString(mid);
            result += ",";
        } else if (bests) {
            for (int j = 0; j < numberBests + 1; j++) {
                result += Integer.toString((int) Math.pow(results[j], power)) + ",";
            }
        } else {
            result += Integer.toString((int) Math.pow(results[0], power)) + ",";
            for (int j = dimensions - 1; j > dimensions - numberBests - 1; j--) {
                result += Integer.toString((int) Math.pow(results[j], power)) + ",";
            }
        }
        if (addCountry) {
            result += Double.toString(country) + ",";
        }
        if (addRating) {
            result += Integer.toString(rating * 2) + ",";
        }
        if (addHand) {
            result += Integer.toString(hand) + ",";
        }
        if (addCountryRating) {
            result += Integer.toString(countryRating) + ",";
        }
        result += Integer.toString(place);
        //result += "{" + Double.toString((double)(place)) + "}";
        return result;
    }
}
