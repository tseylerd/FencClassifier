package ru.spbstu.neer2015.ui;

/**
 * Created by tseyler on 11.05.15.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainForm extends JFrame {
    private JFrame app;
    private JFrame set;
    private JButton openSeter;
    private JButton openPredictor;

    public MainForm() throws Exception {
        setSize(400, 100);
        initComponents();
        componeLayout();
        setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        MainForm m = new MainForm();
        m.setVisible(true);
    }

    private void initComponents() throws Exception {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app = new SettingForm(this);
        set = new AppForm(this);
        set.setVisible(false);
        app.setVisible(false);
        openSeter = new JButton("Настройка");
        openPredictor = new JButton("Прогнозирование");
        openSeter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                app.setVisible(true);
            }
        });
        openPredictor.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                set.setVisible(true);
            }
        });
    }

    private void componeLayout() {
        setLayout(new GridLayout(1, 2, 4, 4));
        add(openSeter);
        add(openPredictor);
    }
}
