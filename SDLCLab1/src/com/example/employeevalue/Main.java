package com.example.employeevalue;

import com.example.employeevalue.controller.EmployeeController;
import com.example.employeevalue.model.EmployeeModel;
import com.example.employeevalue.view.MainView;

import javax.swing.SwingUtilities;

public class Main {
    static void main() {
        SwingUtilities.invokeLater(() -> {
            EmployeeModel model = new EmployeeModel();
            MainView view = new MainView();
            new EmployeeController(model, view);

            view.setVisible(true);
        });
    }
}