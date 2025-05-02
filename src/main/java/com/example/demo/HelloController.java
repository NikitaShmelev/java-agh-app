package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Comparator;

public class HelloController {

    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, String> firstNameCol;
    @FXML private TableColumn<Teacher, String> lastNameCol;
    @FXML private TableColumn<Teacher, TeacherCondition> conditionCol;
    @FXML private TableColumn<Teacher, Integer> birthYearCol;
    @FXML private TableColumn<Teacher, Double> salaryCol;

    @FXML private TextField filterField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button sortButton;

    private final ObservableList<Teacher> masterData = FXCollections.observableArrayList();
    private FilteredList<Teacher> filteredData;

    @FXML
    public void initialize() {
        masterData.addAll(TeacherSeed.generateTeachers(10));
        filteredData = new FilteredList<>(masterData, p -> true);

        setupColumns();
        teacherTable.setItems(filteredData);

        filterField.setOnAction(e -> {
            String text = filterField.getText().trim().toLowerCase();
            filteredData.setPredicate(t ->
                    t.getLastName().toLowerCase().contains(text)
            );
        });
    }

    private void setupColumns() {
        // First Name
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFirstName()));
        firstNameCol.setOnEditCommit(e ->
                e.getRowValue().setFirstName(e.getNewValue()));

        // Last Name
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getLastName()));
        lastNameCol.setOnEditCommit(e ->
                e.getRowValue().setLastName(e.getNewValue()));

        // Condition
        conditionCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(TeacherCondition.values())
        ));
        conditionCol.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCondition()));
        conditionCol.setOnEditCommit(e ->
                e.getRowValue().setCondition(e.getNewValue()));

        // Birth Year
        birthYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        birthYearCol.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getBirthYear()).asObject());
        birthYearCol.setOnEditCommit(e ->
                e.getRowValue().setBirthYear(e.getNewValue()));

        // Salary
        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        salaryCol.setCellValueFactory(cell ->
                new SimpleDoubleProperty(cell.getValue().getSalary()).asObject());
        salaryCol.setOnEditCommit(e ->
                e.getRowValue().setSalary(e.getNewValue()));
    }

    @FXML
    private void onAddTeacher() {
        Teacher newT = new Teacher("First", "Last",
                TeacherCondition.OBECNY, 1980, 3000.0);
        masterData.add(newT);
        int row = masterData.size() - 1;
        teacherTable.scrollTo(row);
        teacherTable.layout();
        teacherTable.edit(row, firstNameCol);
    }

    @FXML
    private void onDeleteTeacher() {
        Teacher sel = teacherTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            masterData.remove(sel);
        } else {
            new Alert(Alert.AlertType.WARNING, "No teacher selected").showAndWait();
        }
    }

    @FXML
    private void onSortTeachers() {
        FXCollections.sort(masterData, Comparator.comparing(Teacher::getLastName));
    }
}
