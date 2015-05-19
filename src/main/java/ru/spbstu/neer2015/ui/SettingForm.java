package ru.spbstu.neer2015.ui;

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
    private JLabel[] jLabels;
    private JLabel[] jLabelsP;
    private JTextPane forCurRes;
    private JTextPane forFinalRes;
    private JProgressBar progressBar;
    private ArrayList<Thread> threads;
    private Runnable action;
    private JFrame back;
    private JButton backButton;
    private JButton set;
    private Controller controller;

    public SettingForm(final JFrame back) throws Exception {
        super();
        this.back = back;
        initComponents();
        componeComponents();
        setActions();
    }

    private void initComponents() throws Exception {
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1, 4, 4));
        backButton = new JButton("Назад");
        set = new JButton("Настроить");
        jLabels = new JLabel[3];
        jLabelsP = new JLabel[3];
        jTextFields = new JTextField[3];
        jTextFieldsP = new JTextField[3];
        forCurRes = new JTextPane();
        forCurRes.setEditable(false);
        forFinalRes = new JTextPane();
        forFinalRes.setEditable(false);
        String[] texts = new String[]{"Начальное значение ", "Конечное значение ", "Шаг "};
        for (int i = 0; i < 3; i++) {
            String cText = new String(texts[i] + "C");
            String pText = new String(texts[i] + "параметра");
            jLabels[i] = new JLabel(cText);
            jLabelsP[i] = new JLabel(pText);
            jTextFields[i] = new JTextField(cText);
            jTextFieldsP[i] = new JTextField(pText);
        }
        progressBar = new JProgressBar();
        threads = new ArrayList<>();
        controller = new Controller();
        action = new Runnable() {
            @Override
            public void run() {
                try {
                    controller.setBestClassifier(jTextFields[0].getText(), jTextFields[1].getText(), jTextFields[2].getText(), jTextFieldsP[0].getText(), jTextFieldsP[1].getText(), jTextFieldsP[2].getText(), progressBar, forCurRes, forFinalRes);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private void componeComponents() {
        JPanel forC = new MyJPanel();
        JPanel forCLabels = new JPanel(new GridLayout(1, 3, 4, 4));
        JPanel forP = new MyJPanel();
        JPanel forPLabels = new JPanel(new GridLayout(1, 3, 4, 4));
        JPanel forButtonAndRes1 = new MyJPanel();
        JPanel forButtonAndRes2 = new MyJPanel();
        for (int i = 0; i < 3; i++) {
            forCLabels.add(jLabels[i]);
            forPLabels.add(jLabelsP[i]);
            forC.add(jTextFields[i]);
            forP.add(jTextFieldsP[i]);
        }
        forButtonAndRes1.add(set);
        forButtonAndRes1.add(forCurRes);
        forButtonAndRes2.add(backButton);
        forButtonAndRes2.add(forFinalRes);
        add(forCLabels);
        add(forC);
        add(forPLabels);
        add(forP);
        add(progressBar);
        add(forButtonAndRes1);
        add(forButtonAndRes2);
    }

    private void setActions() {
        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (threads.size() == 0 || !threads.get(threads.size() - 1).isAlive()) {
                    Thread thread = new Thread(action);
                    thread.start();
                } else {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Подождите, настройка не завершена");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                back.setVisible(true);
            }
        });
    }
}
