package ru.spbstu.neer2015.Data;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

import static ru.spbstu.neer2015.Data.Setter.*;

public class Generator {
    private ArrayList<Sportsmen> sportsmens;
    public void saveTrainSetToFile() throws IOException{
        saveSportsmens();
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
        sportsmens = new ArrayList<Sportsmen>();
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
    private void writeHeader(BufferedWriter bufferedWriter) throws IOException{
        bufferedWriter.write("@relation Sportsmens\n" +
                "\n" +
                "@attribute Name string\n");
        if (mean){
            bufferedWriter.write("@attribute 0 numeric\n" +
                "@attribute 1 numeric\n");
        }else {
            for (int i = 0; i < numberBests + 1; i++) {
                bufferedWriter.write("@attribute " + i + " numeric\n");
            }
        }
        bufferedWriter.write("@attribute Class {");
        for (int i = 0; i < classesNumber; i++) {
            bufferedWriter.write(Integer.toString(i)+",");
        }
        bufferedWriter.write(Integer.toString(classesNumber)+"}\n" + "\n");
        bufferedWriter.write("@data\n");
    }
    private void saveSportsmens() throws IOException{
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
    private void extractFromFolder(final String folder) throws FileNotFoundException, ParseException{
        String pathToFolder = pathToResults + folder;
        Scanner scanner = new Scanner(new FileReader(pathToFolder+world));
        HashMap<String, Sportsmen> hashMap = new HashMap<String, Sportsmen>();
        int k = 0;
        while (scanner.hasNext()){
            String[] line = scanner.nextLine().split("\t");

            double place = Integer.parseInt(line[0]);
            //+Double.toString(place);
            String date = line[4];
            String name = line[2] + date;
            Sportsmen sportsmen = new Sportsmen(name, (int)placeToClass(place));
            sportsmen.setYearsOld(getYearsOld(date));
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
            String date = line[4];
            String name = line[2] + date;
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
}