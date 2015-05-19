package ru.spbstu.neer2015.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by tseyler on 19.05.15.
 */
public class MyJPanel extends JPanel {
    public MyJPanel() {
        super(new GridLayout(1, 15, 4, 4));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
