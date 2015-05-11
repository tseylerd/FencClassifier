package ru.spbstu.neer2015.data;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

import static ru.spbstu.neer2015.data.Setter.*;
import static ru.spbstu.neer2015.data.Setter.rating;

public class Generator {
    private ArrayList<Sportsmen> sportsmens;
    private HashMap<String, Double> counties;
    private HashMap<String, Integer> hands;
    public Generator() throws IOException{
        counties = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(countiesPath));
        String country;
        while ((country = bufferedReader.readLine())!=null){
            counties.put(country, (double)country.hashCode());
        }
        bufferedReader.close();
        if (addHand) {
            bufferedReader = new BufferedReader(new FileReader(pathToHands));
            String line;
            hands = new HashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] arr = line.split("\t");
                String name = arr[0];
                int hand = Integer.parseInt(arr[1]);
                hands.put(name, hand);
            }
        }
    }
    private void filterSet(){
        HashSet<Sportsmen> setFiltered = new HashSet<Sportsmen>();
        for(Sportsmen sportsmen : sportsmens){
            if (sportsmen.defaultsNumber() > defaultsMax){
                setFiltered.add(sportsmen);
            }
        }
        for (Sportsmen sportsmen: setFiltered){
            sportsmens.remove(sportsmen);
        }
    }
    public void generateTrainSet() throws IOException, ParseException{
        sportsmens = new ArrayList<>();
        for (int i = 1; i < numberOfTrainingFolders; i++) {
            extractFromFolder(Integer.toString(i) + "/");
        }
        filterSet();
    }
    public ArrayList<Sportsmen> getTrainSet() throws IOException{
        return sportsmens;
    }

    private ArrayList<Sportsmen> getFilteredSet(final double filteredClass){
        ArrayList<Sportsmen> filteredSet = new ArrayList<Sportsmen>();
        for(Sportsmen sportsmen : sportsmens){
            filteredSet.add(new Sportsmen(sportsmen));
        }
        for(Sportsmen sportsmen : filteredSet){
            sportsmen.compareAndChangeClass(filteredClass);
        }
        return filteredSet;
    }
    private void writeAttributes(BufferedWriter bufferedWriter) throws IOException{
        bufferedWriter.write("\n@attribute Name string\n");
        int start = 0;
        int end = getLastAttribute();
        for (int i = start; i < end; i++) {
            bufferedWriter.write("@attribute " + i + " numeric\n");
        }
        bufferedWriter.write("@attribute Class {");
        int classes = getClassesNumber();
        for (int i = 1; i < classes; i++) {
            bufferedWriter.write(Integer.toString(i)+",");
        }
        bufferedWriter.write(Integer.toString(classes)+"}\n" + "\n");
        bufferedWriter.write("@data\n");
    }
    private int getLastAttribute(){
        int result;
        if (mean){
            result = 2;
        }else {
            result = numberBests + 1;
        }
        if (addCountry){
            result++;
        }
        if (addRating){
            result++;
        }
        if (addHand){
            result++;
        }
        if (addCountryRating){
            result++;
        }
        return result;
    }
    private void writeHeader(BufferedWriter bufferedWriter) throws IOException{
        bufferedWriter.write("@relation Sportsmens\n");
        writeAttributes(bufferedWriter);
    }
    public void saveSportsmens() throws IOException{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathToTrainSet));
        writeHeader(bufferedWriter);
        for (Sportsmen sportsmen : sportsmens){
            if (sportsmen.checkCorrect()){
                bufferedWriter.write(sportsmen.getText());
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }
    private int getYearsOld(final String date){
        if (yearsOld){
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt("19" + date.substring(6, 8));
            GregorianCalendar birthday = new GregorianCalendar(year, month, day);
            GregorianCalendar today = new GregorianCalendar(2015, 04, 27);
            double years = today.get(GregorianCalendar.YEAR) - birthday.get(GregorianCalendar.YEAR);
            return (int)years;
        }else{
            return defaultYears;
        }
    }
    private double getNumberOfCountry(String country){
        return counties.get(country);
    }
    private HashMap<String, Integer> getRating(final String path) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String s;
        HashMap<String, Integer> result = new HashMap<>();
        while ((s = bufferedReader.readLine()) != null){
            String[] arr = s.split("\t");
            int pos = Integer.parseInt(arr[0]);
            String name = arr[1];
            result.put(name, pos);
        }
        return result;
    }
    private HashMap<String, Integer> getTeamRating(final String path) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String s;
        HashMap<String, Integer> result = new HashMap<>();
        while ((s = bufferedReader.readLine()) != null){
            String[] arr = s.split("\t");
            int pos = Integer.parseInt(arr[0]);
            String country = arr[2];
            result.put(country, pos);
        }
        return result;
    }
    private void addFinalRating(Sportsmen sportsmen, HashMap<String, Integer> rat){
        if (addRating){
            Integer pos = rat.get(sportsmen.getName());
            if (pos == null){
                pos = sportsmen.getPlace();
            }
            pos *= 10;
            sportsmen.setRating(pos);
        }
    }
    private void addTeamRating(Sportsmen sportsmen, String country, HashMap<String, Integer> rat){
        if (addCountryRating){
            Integer pos = rat.get(country);
            if (pos == null){
                pos = sportsmen.getPlace();
            }
            sportsmen.setCountryRating(pos);
        }
    }
    private void addHand(Sportsmen sportsmen){
        if (addHand){
            Integer hand = hands.get(sportsmen.getName());
            if (hand == null){
                hand = 5000;
            }
            sportsmen.setHand(hand);
        }
    }
    private void extractFromFolder(final String folder) throws ParseException, IOException{
        String pathToFolder = pathToResults + folder;
        Scanner scanner = new Scanner(new FileReader(pathToFolder+world));
        HashMap<String, Sportsmen> hashMap = new HashMap<>();
        HashMap<String, Integer> rat = getRating(pathToFolder + rating);
        HashMap<String, Integer> teamRat = getTeamRating(pathToFolder + teamRating);
        while (scanner.hasNext()){
            String[] line = scanner.nextLine().split("\t");
            double place = Integer.parseInt(line[0]);
            String date = line[4];
            String name = line[2];
            String country = line[3];
            Sportsmen sportsmen = new Sportsmen(name, (int)placeToClass(place), getNumberOfCountry(country));
            sportsmen.setYearsOld(getYearsOld(date));
            addFinalRating(sportsmen, rat);
            addHand(sportsmen);
            addTeamRating(sportsmen, country, teamRat);
            hashMap.put(name, sportsmen);
        }
        for (int i = 1; i < competitionsNumber; i++) {
            addResults(hashMap, pathToFolder+Integer.toString(i), folder);
        }
        for (Sportsmen sportsmen : hashMap.values()){
            sportsmens.add(sportsmen);
        }
    }
    private void addResults(final HashMap<String, Sportsmen> hashMap, final String path, final String folder) throws FileNotFoundException{
        Scanner scanner = new Scanner(new FileReader(path));
        HashSet<String> hashSet = new HashSet<String>();
        while (scanner.hasNext()){
            String[] line = scanner.nextLine().split("\t");
            String name = line[2];
            if (hashMap.containsKey(name)){
                double place = Integer.parseInt(line[0]);
                if (!hashMap.get(name).ifFull()) {
                        hashMap.get(name).pushResult(place);
                        hashSet.add(name);
                }
            }
        }
        for (Sportsmen sportsmen : hashMap.values()){
            if (!hashSet.contains(sportsmen.getName())){
                sportsmen.pushResult(defaultResult);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Generator generator = new Generator();
        generator.generateTrainSet();
        generator.saveSportsmens();
    }
}