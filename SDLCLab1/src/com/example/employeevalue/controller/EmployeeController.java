package com.example.employeevalue.controller;

import com.example.employeevalue.model.EmployeeData;
import com.example.employeevalue.model.EmployeeModel;
import com.example.employeevalue.view.InputDialog;
import com.example.employeevalue.view.MainView;

public class EmployeeController {
    private final EmployeeModel model;
    private final MainView view;

    public EmployeeController(EmployeeModel model, MainView view) {
        this.model = model;
        this.view = view;

        model.addListener(view);

        view.getInputButton().addActionListener(_ -> openInputDialog());
    }

    private void openInputDialog() {
        InputDialog dialog = new InputDialog(view, model.getData());

        dialog.setVisible(true);

        EmployeeData data = dialog.getResult();

        if (data != null) {
            try {
                model.setData(data);
            } catch (IllegalArgumentException e) {
                javax.swing.JOptionPane.showMessageDialog(view, e.getMessage(), "Ошибка", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}