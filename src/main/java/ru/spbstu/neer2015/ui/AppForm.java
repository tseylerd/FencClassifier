package ru.spbstu.neer2015.ui;

import ru.spbstu.neer2015.data.Generator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by tseyler on 11.05.15.
 */
public class AppForm extends JFrame {
    private final JFrame backFrame;
    private ArrayList<JTextField> competits;
    private JRadioButton right;
    private JRadioButton left;
    private JTextField individualRating;
    private JComboBox yearsOld;
    private JTextField teamRating;
    private JComboBox countries;
    private JLabel message;
    private JButton plus;
    private JButton minus;
    private JButton go;
    private JButton back;
    private JPanel competitions;
    private Controller controller;

    public AppForm(JFrame back) throws Exception {
        super();
        this.backFrame = back;
        initComponents();
        componeComponents();
        addActions();
    }

    private void initComponents() throws Exception {
        setSize(500, 500);
        controller = new Controller();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ButtonGroup hand = new ButtonGroup();
        right = new JRadioButton("Левая");
        left = new JRadioButton("Правая");
        go = new JButton("Прогноз");
        individualRating = new MyJTextField();
        yearsOld = new MyJComboBox();
        back = new JButton("Назад");
        for (int i = 10; i < 51; i++) {
            yearsOld.addItem(i);
        }
        teamRating = new MyJTextField();
        competitions = new MyJPanel();
        countries = new MyJComboBox();
        String[] count = Generator.getCountries();
        for (int i = 0; i < count.length; i++) {
            countries.addItem(count[i]);
        }
        message = new JLabel();
        plus = new JButton("+");
        minus = new JButton("-");
        hand = new ButtonGroup();
        hand.add(left);
        hand.add(right);
        competits = new ArrayList<>();
        competits.add(new JTextField());
    }

    private void componeComponents() {
        setLayout(new GridLayout(1, 2, 2, 2));
        JPanel leftColumn = new JPanel(new GridLayout(8, 1, 4, 4));
        JPanel rightColumn = new JPanel(new GridLayout(8, 1, 4, 4));
        MyJPanel leftOrRightHandPanel = new MyJPanel();
        competitions.add(plus);
        competitions.add(minus);
        competitions.add(competits.get(0));
        leftOrRightHandPanel.add(left);
        leftOrRightHandPanel.add(right);
        leftColumn.add(competitions);
        leftColumn.add(leftOrRightHandPanel);
        leftColumn.add(individualRating);
        leftColumn.add(teamRating);
        leftColumn.add(countries);
        leftColumn.add(yearsOld);
        leftColumn.add(go);
        leftColumn.add(back);
        rightColumn.add(new JLabel(" - результаты"));
        rightColumn.add(new JLabel(" - активная рука"));
        rightColumn.add(new JLabel(" - индивидуальный рейтинг"));
        rightColumn.add(new JLabel(" - командный рейтинг"));
        rightColumn.add(new JLabel(" - страна"));
        rightColumn.add(new JLabel(" - возраст"));
        rightColumn.add(message);
        add(leftColumn);
        add(rightColumn);
    }

    private void addActions() {
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                backFrame.setVisible(true);
            }
        });
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField field = new JTextField();
                competitions.add(field);
                competits.add(field);
                competitions.updateUI();
            }
        });
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField field = competits.get(competits.size() - 1);
                competits.remove(field);
                competitions.remove(field);
                competitions.updateUI();
            }
        });
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    message.setText(controller.getResult(competits, left.isSelected(), individualRating.getText(), teamRating.getText(), (String) countries.getSelectedItem(), (int) yearsOld.getSelectedItem()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Данные введены неверно. Проверьте, пожалуйста, ввод.");
                }
            }
        });
    }
}
