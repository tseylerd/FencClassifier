package ru.spbstu.neer2015.data;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import static ru.spbstu.neer2015.data.GeneratorSetter.*;

public class Generator {
    private ArrayList<Sportsmen> sportsmens;
    private HashMap<String, Integer> counties;
    private HashMap<String, Integer> hands;

    public Generator() throws IOException {
        initCountries();
        initHands();
    }

    private static int getClassesNumber() {
        return 6;
    }

    private static List<String> formCountriesList() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(COUNTRIES_PATH));
        String line;
        List<String> cs = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            cs.add(line);
        }
        return cs;
    }

    public static String[] getCountries() throws IOException {
        List<String> cs = formCountriesList();
        String[] result = new String[cs.size()];
        for (int i = 0; i < cs.size(); i++) {
            result[i] = cs.get(i);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Generator generator = new Generator();
        generator.generateTrainSet();
        generator.saveSportsmens();
    }

    private void initCountries() throws IOException {
        counties = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(COUNTRIES_PATH));
        String country;
        while ((country = bufferedReader.readLine()) != null) {
            counties.put(country, country.hashCode());
        }
        bufferedReader.close();
    }

    private void initHands() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PATH_TO_HANDS));
        String line;
        hands = new HashMap<>();
        while ((line = bufferedReader.readLine()) != null) {
            String[] arr = line.split("\t");
            String name = arr[0];
            int hand = Integer.parseInt(arr[1]);
            hands.put(name, hand);
        }
    }

    private void filterSet() {
        HashSet<Sportsmen> setFiltered = new HashSet<Sportsmen>();
        for (Sportsmen sportsmen : sportsmens) {
            if (sportsmen.defaultsNumber() > DEFAULTS_MAX) {
                setFiltered.add(sportsmen);
            }
        }
        for (Sportsmen sportsmen : setFiltered) {
            sportsmens.remove(sportsmen);
        }
    }

    public void generateTrainSet() throws IOException, ParseException {
        sportsmens = new ArrayList<>();
        File file = new File(PATH_TO_RESULTS);
        File[] files = file.listFiles();
        int numberOfTrainingFolders = files.length;
        for (int i = 1; i < numberOfTrainingFolders; i++) {
            extractFromFolder(Integer.toString(i) + "/");
        }
        filterSet();
    }

    public ArrayList<Sportsmen> getTrainSet() throws IOException {
        return sportsmens;
    }

    private void writeAttributes(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("\n@attribute Name string\n");
        int start = 0;
        int end = getLastAttribute();
        for (int i = start; i < end; i++) {
            bufferedWriter.write("@attribute " + i + " numeric\n");
        }
        bufferedWriter.write("@attribute Class {");
        int classes = getClassesNumber();
        for (int i = 1; i < classes; i++) {
            bufferedWriter.write(Integer.toString(i) + ",");
        }
        bufferedWriter.write(Integer.toString(classes) + "}\n" + "\n");
        bufferedWriter.write("@data\n");
    }

    private int getLastAttribute() {
        return 6;
    }

    private void writeHeader(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("@relation Sportsmens\n");
        writeAttributes(bufferedWriter);
    }

    public void saveSportsmens() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(PATH_TO_TRAIN_SET));
        writeHeader(bufferedWriter);
        for (Sportsmen sportsmen : sportsmens) {
            if (sportsmen.checkCorrect()) {
                bufferedWriter.write(sportsmen.getText());
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.close();
    }

    private int getYearsOld(final String date) {
        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt("19" + date.substring(6, 8));
        GregorianCalendar birthday = new GregorianCalendar(year, month, day);
        Calendar today = Calendar.getInstance();
        double years = today.get(Calendar.YEAR) - birthday.get(GregorianCalendar.YEAR);
        return (int) years;
    }

    private int getNumberOfCountry(String country) {
        return counties.get(country);
    }

    private HashMap<String, Integer> getRating(final String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String s;
        HashMap<String, Integer> result = new HashMap<>();
        while ((s = bufferedReader.readLine()) != null) {
            String[] arr = s.split("\t");
            int pos = Integer.parseInt(arr[0]);
            String name = arr[1];
            result.put(name, pos);
        }
        return result;
    }

    private HashMap<String, Integer> getTeamRating(final String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        String s;
        HashMap<String, Integer> result = new HashMap<>();
        while ((s = bufferedReader.readLine()) != null) {
            String[] arr = s.split("\t");
            int pos = Integer.parseInt(arr[0]);
            String country = arr[2];
            result.put(country, pos);
        }
        return result;
    }

    private void addFinalRating(Sportsmen sportsmen, HashMap<String, Integer> rat) {
        Integer pos = rat.get(sportsmen.getName());
        if (pos == null) {
            pos = sportsmen.getPlace().getSportsmenClass();
        }
        pos *= 10;
        sportsmen.setRating(pos);
    }

    private void addTeamRating(Sportsmen sportsmen, String country, HashMap<String, Integer> rat) {
        Integer pos = rat.get(country);
        if (pos == null) {
            pos = sportsmen.getPlace().getSportsmenClass();
        }
        sportsmen.setCountryRating(pos);
    }

    private void addHand(Sportsmen sportsmen) {
        Integer hand = hands.get(sportsmen.getName());
        if (hand == null) {
            hand = (LEFT_SEL + RIGHT_SEL) / 2;
        } else {
            hand = hand == 1 ? LEFT_SEL : RIGHT_SEL;
        }
        sportsmen.setHand(hand);
    }

    private void extractFromFolder(final String folder) throws ParseException, IOException {
        String pathToFolder = PATH_TO_RESULTS + folder;
        Scanner scanner = new Scanner(new FileReader(pathToFolder + WORLD));
        HashMap<String, Sportsmen> hashMap = new HashMap<>();
        HashMap<String, Integer> rat = getRating(pathToFolder + RATING);
        HashMap<String, Integer> teamRat = getTeamRating(pathToFolder + TEAM_RATING);
        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split("\t");
            int place = Integer.parseInt(line[0]);
            String date = line[4];
            String name = line[2];
            String country = line[3];
            Sportsmen sportsmen = new Sportsmen(name, place, getNumberOfCountry(country));
            sportsmen.setYearsOld(getYearsOld(date));
            addFinalRating(sportsmen, rat);
            addHand(sportsmen);
            addTeamRating(sportsmen, country, teamRat);
            hashMap.put(name, sportsmen);
        }
        for (int i = 1; i < COMPETITIONS_NUMBER; i++) {
            addResults(hashMap, pathToFolder + Integer.toString(i));
        }
        for (Sportsmen sportsmen : hashMap.values()) {
            sportsmens.add(sportsmen);
        }
    }

    private void addResults(final HashMap<String, Sportsmen> hashMap, final String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(path));
        HashSet<String> hashSet = new HashSet<String>();
        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split("\t");
            String name = line[2];
            if (hashMap.containsKey(name)) {
                double place = Integer.parseInt(line[0]);
                if (!hashMap.get(name).ifFull()) {
                    hashMap.get(name).pushResult(place);
                    hashSet.add(name);
                }
            }
        }
        for (Sportsmen sportsmen : hashMap.values()) {
            if (!hashSet.contains(sportsmen.getName())) {
                sportsmen.pushResult(DEFAULT_RESULT);
            }
        }
    }
}