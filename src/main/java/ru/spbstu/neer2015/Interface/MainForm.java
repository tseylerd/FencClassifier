package ru.spbstu.neer2015.Interface;

/**
 * Created by tseyler on 11.05.15.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class MainForm extends JFrame {
    private JFrame app;
    private JFrame set;

    public MainForm() throws IOException {
        GridLayout layout = new GridLayout(1, 2);
        setSize(400, 100);
        JButton setting = new JButton("Настройка");
        setting.setVisible(true);
        getContentPane().setLayout(layout);
        add(setting);
        JButton run = new JButton("Прогнозирование");
        run.setVisible(true);
        add(run);
        setting.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app = new SettingForm();
                app.setVisible(true);
                setVisible(false);
            }
        });
        run.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    set = new AppForm();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                set.setVisible(true);
                setVisible(false);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MainForm m = new MainForm();
        m.setVisible(true);
    }
}
