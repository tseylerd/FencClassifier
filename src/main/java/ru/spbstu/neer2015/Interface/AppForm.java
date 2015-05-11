package ru.spbstu.neer2015.Interface;


import ru.spbstu.neer2015.classifier.Classifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by tseyler on 11.05.15.
 */
public class AppForm extends JFrame {
    ArrayList<JTextField> competits;
    Classifier classifier;
    JLabel predict;
    JRadioButton right;
    JRadioButton left;
    JTextArea individualRating;
    JTextArea teamRating;
    JComboBox countries;
    public AppForm() throws Exception{
        classifier = new Classifier();
        classifier.buildClassifier(4, 1000, 1);
        getContentPane().setLayout(new GridLayout(1, 3));
        setSize(500, 500);
        JPanel panel = new JPanel(new GridLayout(6,1,4,4));
        JPanel description = new JPanel(new GridLayout(6,1,4,4));
        final JPanel comps = new JPanel(new GridLayout(1, 15));
        final JButton plus = new JButton("+");
        final JButton minus = new JButton("-");
        JTextField result = new JTextField();
        comps.add(plus);
        comps.add(minus);
        comps.add(result);
        comps.setVisible(true);
        plus.setVisible(true);
        minus.setVisible(true);
        competits = new ArrayList<>();
        competits.add(result);
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField field = new JTextField();
                comps.add(field);
                competits.add(field);
                field.setVisible(true);
                comps.updateUI();
            }
        });
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField field = competits.get(competits.size()-1);
                competits.remove(field);
                field.setVisible(false);
                field.removeAll();
                comps.remove(field);
                comps.updateUI();
            }
        });
        panel.setVisible(true);
        panel.add(comps);
        /** add left/right radioButtons */
        GridLayout lr = new GridLayout(1, 2);
        JPanel leftRight = new JPanel(lr);
        ButtonGroup buttonGroup = new ButtonGroup();
        left = new JRadioButton("Left-handed");
        right = new JRadioButton("Right-handed");
        buttonGroup.add(left);
        buttonGroup.add(right);
        left.setLayout(lr);
        right.setLayout(lr);
        leftRight.add(left);
        leftRight.add(right);
        leftRight.setVisible(true);
        left.setVisible(true);
        right.setVisible(true);
        left.setSelected(true);
        panel.add(leftRight);
        /** add left/right radioButtons */
        /** add individual rating area */
        individualRating = new JTextArea();
        individualRating.setVisible(true);
        panel.add(individualRating);
        /** add individual rating area */
        /** add team rating area */
        teamRating = new JTextArea();
        teamRating.setVisible(true);
        panel.add(teamRating);
        /** add team rating area */
        /** add country area */
        String[] cs = getCountries();
        countries = new JComboBox(cs);
        countries.setVisible(true);
        panel.add(countries);
        /** add country area */
        /** add button */
        JButton go = new JButton("Прогноз");
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        go.setVisible(true);
        panel.add(go);
        /** add button */
        description.add(new JLabel(" - результаты"));
        description.add(new JLabel(" - активная рука"));
        description.add(new JLabel(" - индивидуальный рейтинг"));
        description.add(new JLabel(" - командный рейтинг"));
        description.add(new JLabel(" - страна"));
        JPanel res = new JPanel(new GridLayout(2, 1));
        res.add(new JLabel("Результат: "));
        predict = new JLabel("");
        add(panel);
        add(description);
    }

    private String[] getCountries() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Data/Countries.txt"));
        String line;
        ArrayList<String> cs = new ArrayList<>();
        while ((line = bufferedReader.readLine())!= null){
            cs.add(line);
        }
        String[] result = new String[cs.size()];
        for (int i = 0; i < cs.size(); i++) {
            result[i] = cs.get(i);
        }
        return result;
    }
}
