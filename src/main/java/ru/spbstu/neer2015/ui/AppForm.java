package ru.spbstu.neer2015.ui;


import ru.spbstu.neer2015.classification.UserClassifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static ru.spbstu.neer2015.data.GeneratorSetter.*;

/**
 * Created by tseyler on 11.05.15.
 */
public class AppForm extends JFrame {
    private ArrayList<JTextField> competits;
    private UserClassifier classifier;
    private JLabel predict;
    private JRadioButton right;
    private JRadioButton left;
    private JTextField individualRating;
    private JTextField yearsOld;
    private JTextField teamRating;
    private JComboBox countries;
    private JLabel message;

    public AppForm() throws Exception {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        classifier = new UserClassifier();
        classifier.loadModel();
        getContentPane().setLayout(new GridLayout(1, 2, 2, 2));
        setSize(500, 500);
        JPanel panel = new JPanel(new GridLayout(7, 1, 4, 4));
        final JPanel description = new JPanel(new GridLayout(7, 1, 4, 4));
        final JPanel comps = new JPanel(new GridLayout(1, 15, 4, 4));
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
                JTextField field = competits.get(competits.size() - 1);
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
        leftRight.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        individualRating = new JTextField();
        individualRating.setVisible(true);
        individualRating.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(individualRating);
        /** add individual rating area */
        /** add team rating area */
        teamRating = new JTextField();
        teamRating.setVisible(true);
        teamRating.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(teamRating);
        /** add team rating area */
        /** add country area */
        String[] cs = getCountries();
        countries = new JComboBox(cs);
        countries.setVisible(true);
        countries.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(countries);
        /** add country area */
        /** add years*/
        yearsOld = new JTextField();
        yearsOld.setVisible(true);
        yearsOld.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(yearsOld);
        /** add country area */
        /** add button */
        JButton go = new JButton("Прогноз");
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double mid = getMiddle();
                    int years = Integer.parseInt(yearsOld.getText());
                    int rating = Integer.parseInt(individualRating.getText());
                    rating *= 10;
                    int teamrating = Integer.parseInt(teamRating.getText());
                    int country = ((String) countries.getSelectedItem()).hashCode();
                    int leftr = left.isSelected() ? leftSel : rightSel;
                    double place = classifier.classifySportsmen(years, mid, country, rating, leftr, teamrating) + 1;
                    try {
                        description.remove(message);
                    } catch (Exception ex) {

                    }
                    message = new JLabel("Вероятнее всего, спортсмен займет место " + classToText((int) place) + ".");
                    description.add(message);
                    description.updateUI();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Данные введены неверно. Проверьте, пожалуйста, ввод.");
                }
            }
        });
        go.setVisible(true);
        panel.add(go);
        /** add button */
        JLabel label1 = new JLabel(" - результаты");
        JLabel label2 = new JLabel(" - активная рука");
        JLabel label3 = new JLabel(" - индивидуальный рейтинг");
        JLabel label4 = new JLabel(" - командный рейтинг");
        JLabel label5 = new JLabel(" - страна");
        JLabel label6 = new JLabel(" - возраст");
        description.add(label1);
        description.add(label2);
        description.add(label3);
        description.add(label4);
        description.add(label5);
        description.add(label6);
        JPanel res = new JPanel(new GridLayout(2, 1));
        predict = new JLabel("");
        res.add(predict);
        add(panel);
        add(description);
    }

    private double getMiddle() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < competits.size(); i++) {
            arrayList.add(Integer.parseInt(competits.get(i).getText()));
        }
        double mid = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            mid += arrayList.get(i);
        }
        mid /= arrayList.size();
        return mid;
    }

    private String[] getCountries() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Data/Countries.txt"));
        String line;
        ArrayList<String> cs = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            cs.add(line);
        }
        String[] result = new String[cs.size()];
        for (int i = 0; i < cs.size(); i++) {
            result[i] = cs.get(i);
        }
        return result;
    }

    private String classToText(int clazz) {
        switch (clazz) {
            case 1:
                return "c 1 по 4";
            case 2:
                return "c 5 по 8";
            case 3:
                return "c 9 по 16";
            case 4:
                return "c 17 по 32";
            case 5:
                return "с 33 по 64";
            case 6:
                return "выше 64";
            default:
                return "непонятно какое";
        }
    }
}
