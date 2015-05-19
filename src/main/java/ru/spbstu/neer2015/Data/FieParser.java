package ru.spbstu.neer2015.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by tseyler on 11.05.15.
 */
public class FieParser {
    public static void main(String[] args) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("SportsmensHands"));
        String url = "http://fie.org/fencers/fencer/";
        for (int i = 1; i < 60000; i++) {
            System.out.println("----------------");
            System.out.println(Integer.toString(i));
            System.out.println("----------------");
            try {
                String u = url + Integer.toString(i);
                Document document = Jsoup.connect(u).timeout(10 * 1000).get();
                Elements elementsHand = document.getElementsByTag("li");
                Elements elementsName = document.getElementsByTag("h2");
                String name = elementsName.get(0).text();
                System.out.println(name);
                for (Element element : elementsHand) {
                    if (element.text().equals("Right-handed")) {
                        System.out.println("2");
                        bufferedWriter.write(name);
                        bufferedWriter.write("\t");
                        bufferedWriter.write("2\n");
                    }
                    if (element.text().equals("Left-handed")) {
                        System.out.println("1");
                        bufferedWriter.write(name);
                        bufferedWriter.write("\t");
                        bufferedWriter.write("1\n");
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        bufferedWriter.close();
    }
}
