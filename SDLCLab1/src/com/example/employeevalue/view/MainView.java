package com.example.employeevalue.view;

import com.example.employeevalue.model.EmployeeData;
import com.example.employeevalue.model.EmployeeResult;
import com.example.employeevalue.model.ModelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;

public class MainView extends JFrame implements ModelListener {
    private final JLabel salaryLabel;
    private final JLabel workingHoursLabel;
    private final JLabel actualHoursLabel;

    private final JLabel hourlyCostLabel;
    private final JLabel clickCostLabel;
    private final JLabel codeLineCostLabel;
    private final JLabel sighCostLabel;

    private final JButton inputButton;

    private final DecimalFormat format = new DecimalFormat("0.0000");

    public MainView() {
        setTitle("Сколько ты стоишь как сотрудник");
        setSize(650, 450);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Сколько ты стоишь как сотрудник", JLabel.CENTER);

        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        centerPanel.setBorder(BorderFactory.createTitledBorder("Информация"));

        centerPanel.add(new JLabel("Зарплата:"));
        salaryLabel = new JLabel("—");
        centerPanel.add(salaryLabel);

        centerPanel.add(new JLabel("Часы работы:"));
        workingHoursLabel = new JLabel("—");
        centerPanel.add(workingHoursLabel);

        centerPanel.add(new JLabel("Реально отработано:"));
        actualHoursLabel = new JLabel("—");
        centerPanel.add(actualHoursLabel);

        centerPanel.add(new JLabel("Стоимость часа:"));
        hourlyCostLabel = new JLabel("—");
        centerPanel.add(hourlyCostLabel);

        centerPanel.add(new JLabel("Стоимость одного клика:"));
        clickCostLabel = new JLabel("—");
        centerPanel.add(clickCostLabel);

        centerPanel.add(new JLabel("Стоимость одной строки кода:"));
        codeLineCostLabel = new JLabel("—");
        centerPanel.add(codeLineCostLabel);

        centerPanel.add(new JLabel("Стоимость рабочего вздоха:"));
        sighCostLabel = new JLabel("—");
        centerPanel.add(sighCostLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        inputButton = new JButton("Ввести данные");

        inputButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        bottomPanel.add(inputButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public JButton getInputButton() {
        return inputButton;
    }

    @Override
    public void modelChanged(EmployeeData data, EmployeeResult result) {
        salaryLabel.setText(format.format(data.salary()));

        workingHoursLabel.setText(format.format(data.workingHours()));

        actualHoursLabel.setText(format.format(data.actualHours()));

        hourlyCostLabel.setText(format.format(result.hourlyCost()));

        clickCostLabel.setText(format.format(result.clickCost()));

        codeLineCostLabel.setText(format.format(result.codeLineCost()));

        sighCostLabel.setText(format.format(result.sighCost()));
    }
}