package com.example.employeevalue.model;

import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private static final double CLICKS_PER_HOUR = 300.0;
    private static final double CODE_LINES_PER_HOUR = 25.0;
    private static final double SIGHS_PER_HOUR = 900.0;

    private final List<ModelListener> listeners = new ArrayList<>();

    private EmployeeData data;
    private EmployeeResult result;

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public EmployeeData getData() {
        return data;
    }

    public void setData(EmployeeData data) {
        validate(data);
        this.data = data;

        double hourlyCost = data.salary() / data.actualHours();
        double clickCost = hourlyCost / CLICKS_PER_HOUR;
        double codeLineCost = hourlyCost / CODE_LINES_PER_HOUR;
        double sighCost = hourlyCost / SIGHS_PER_HOUR;

        this.result = new EmployeeResult(hourlyCost, clickCost, codeLineCost, sighCost);

        notifyListeners();
    }

    private void validate(EmployeeData data) {
        if (data == null) {
            throw new IllegalArgumentException("Данные не могут быть пустыми.");
        }

        if (data.salary() <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше нуля.");
        }

        if (data.workingHours() <= 0) {
            throw new IllegalArgumentException("Количество рабочих часов должно быть больше нуля.");
        }

        if (data.actualHours() <= 0) {
            throw new IllegalArgumentException("Количество реально отработанных часов " + "должно быть больше нуля.");
        }
    }

    private void notifyListeners() {
        for (ModelListener listener : listeners) {
            listener.modelChanged(data, result);
        }
    }
}