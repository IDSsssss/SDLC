package com.example.employeevalue.view;

import com.example.employeevalue.model.EmployeeData;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class InputDialog extends JDialog {
    private final JTextField salaryField;
    private final JTextField workingHoursField;
    private final JTextField actualHoursField;

    private EmployeeData result;

    public InputDialog(MainView parent, EmployeeData previousData) {
        super(parent, "Ввод данных", true);

        setSize(400, 250);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Зарплата:"));
        salaryField = new JTextField();
        panel.add(salaryField);

        panel.add(new JLabel("Часы работы:"));
        workingHoursField = new JTextField();
        panel.add(workingHoursField);

        panel.add(new JLabel("Реально отработано:"));
        actualHoursField = new JTextField();
        panel.add(actualHoursField);

        JButton saveButton = new JButton("Рассчитать");
        JButton cancelButton = new JButton("Отмена");

        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);

        /*
         * Если данные уже вводились,
         * восстанавливаем их.
         */
        if (previousData != null) {
            salaryField.setText(String.valueOf(previousData.salary()));

            workingHoursField.setText(String.valueOf(previousData.workingHours()));

            actualHoursField.setText(String.valueOf(previousData.actualHours()));
        }

        saveButton.addActionListener(_ -> saveData());

        cancelButton.addActionListener(_ -> dispose());
    }

    private void saveData() {
        try {
            double salary = parseNumber(salaryField.getText());
            double workingHours = parseNumber(workingHoursField.getText());
            double actualHours = parseNumber(actualHoursField.getText());

            result = new EmployeeData(salary, workingHours, actualHours);

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректные числовые значения.", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseNumber(String text) throws NumberFormatException {
        text = text.trim().replace(',', '.');

        if (text.isEmpty()) {
            throw new NumberFormatException();
        }

        return Double.parseDouble(text);
    }

    public EmployeeData getResult() {
        return result;
    }
}