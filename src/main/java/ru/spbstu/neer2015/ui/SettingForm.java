package ru.spbstu.neer2015.ui;

import ru.spbstu.neer2015.classification.ParametrSelection;
import ru.spbstu.neer2015.data.ParametrSelectionSetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by tseyler on 11.05.15.
 */
public class SettingForm extends JFrame {
    private JTextField[] jTextFields;
    private JTextField[] jTextFieldsP;
    JLabel[] jLabels;
    JLabel[] jLabelsP;
    JProgressBar progressBar;
    ArrayList<Thread> threads;
    public SettingForm(){
        super();
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 4, 4));
        JPanel panelForCLabels = new JPanel();
        GridLayout gridForCLabels = new GridLayout(1,3,4,4);
        panelForCLabels.setLayout(gridForCLabels);
        jLabels = new JLabel[3];
        jLabels[0] = new JLabel("Начальное значение C");
        jLabels[1] = new JLabel("Конечное значение C");
        jLabels[2] = new JLabel("Шаг");
        for (JLabel label : jLabels){
            label.setVisible(true);
            panelForCLabels.add(label);
        }
        add(panelForCLabels);
        JPanel panelForCFields = new JPanel();
        GridLayout gridForCFields = new GridLayout(1,3,4,4);
        panelForCFields.setLayout(gridForCFields);
        jTextFields = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            jTextFields[i] = new JTextField();
            jTextFields[i].setVisible(true);
            panelForCFields.add(jTextFields[i]);
        }
        add(panelForCFields);
        JPanel panelForPLabels = new JPanel();
        GridLayout gridForPLabels = new GridLayout(1,3,4,4);
        panelForPLabels.setLayout(gridForPLabels);
        jLabelsP = new JLabel[3];
        jLabelsP[0] = new JLabel("Начальное значение параметра");
        jLabelsP[1] = new JLabel("Конечное значение параметра");
        jLabelsP[2] = new JLabel("Шаг");
        for (JLabel label : jLabelsP){
            label.setVisible(true);
            panelForPLabels.add(label);
        }
        add(panelForPLabels);
        JPanel panelForPFields = new JPanel();
        GridLayout gridForPFields = new GridLayout(1,3,4,4);
        panelForPFields.setLayout(gridForPFields);
        jTextFieldsP = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            jTextFieldsP[i] = new JTextField();
            jTextFieldsP[i].setVisible(true);
            panelForPFields.add(jTextFieldsP[i]);
        }
        add(panelForPFields);
        progressBar = new JProgressBar();
        add(progressBar);
        progressBar.setVisible(true);
        JPanel last = new JPanel(new GridLayout(1,2,4,4));
        final JTextPane label = new JTextPane();
        JButton go = new JButton("Настроить");
        threads = new ArrayList<>();
        final JTextPane textPane = new JTextPane();
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final int startC = Integer.parseInt(jTextFields[0].getText());
                    final int endC = Integer.parseInt(jTextFields[1].getText());
                    final int stepC = Integer.parseInt(jTextFields[2].getText());
                    final double startP = Double.parseDouble(jTextFieldsP[0].getText());
                    final double endP = Double.parseDouble(jTextFieldsP[1].getText());
                    final double stepP = Double.parseDouble(jTextFieldsP[2].getText());
                    if ((threads.size() == 0) || !threads.get(threads.size()-1).isAlive()) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ParametrSelectionSetter parametrSelectionSetter = new ParametrSelectionSetter(startC, endC, stepC, startP, endP, stepP);
                                    ParametrSelection parametrSelection = new ParametrSelection(parametrSelectionSetter, true);
                                    parametrSelection.evaluate(progressBar, label);
                                    parametrSelection.getBestClassifier().saveModel();
                                    textPane.setText(parametrSelection.getStringResults());
                                    progressBar.setValue(0);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        threads.add(thread);
                        thread.start();

                    }else {
                        JOptionPane.showMessageDialog((Component) e.getSource(), "Подождите, настройка не завершена");
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Данные введены неверно. Проверьте, пожалуйста, ввод.");
                }
            }
        });
        go.setVisible(true);
        last.add(go);
        label.setVisible(true);
        last.add(label);
        add(last);
        textPane.setVisible(true);
        add(textPane);
    }
}
