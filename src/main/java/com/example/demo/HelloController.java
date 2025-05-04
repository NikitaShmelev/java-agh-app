package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HelloController {

    // Left panel: groups
    @FXML private ListView<ClassTeacher> groupList;
    @FXML private Button addGroupButton;
    @FXML private Button deleteGroupButton;
    @FXML private Button modifyGroupButton;
    @FXML private Button sortGroupButton;

    // Top panel: filter and teacher buttons
    @FXML private TextField filterField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button modifyButton;
    @FXML private Button sortTeacherButton;

    // Teachers table
    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, String> firstNameCol;
    @FXML private TableColumn<Teacher, String> lastNameCol;
    @FXML private TableColumn<Teacher, TeacherCondition> conditionCol;
    @FXML private TableColumn<Teacher, Integer> birthYearCol;
    @FXML private TableColumn<Teacher, Double> salaryCol;

    // Data
    private final ObservableList<ClassTeacher> groupData = FXCollections.observableArrayList();
    private FilteredList<Teacher> filteredTeachers;

    @FXML
    public void initialize() {
        // — Seeds
        ClassTeacher g1 = new ClassTeacher("Math Dept", 5);
        ClassTeacher g2 = new ClassTeacher("Science Dept", 8);

        List<Teacher> seed = TeacherSeed.generateTeachers(4);
        g1.addTeacher(seed.get(0));
        g1.addTeacher(seed.get(1));
        g2.addTeacher(seed.get(2));
        g2.addTeacher(seed.get(3));

        groupData.addAll(g1, g2);
        groupList.setItems(groupData);

        setupTable();

        // onGroupSelect: update teachers table
        groupList.getSelectionModel().selectedItemProperty().addListener((obs, oldG, newG) -> {
            if (newG != null) {
                updateTableForGroup(newG);
            } else {
                teacherTable.setItems(FXCollections.emptyObservableList());
            }
        });

        // filter on Enter
        filterField.setOnAction(e -> onFilter());
    }

    /** Настройка колонок таблицы для inline‑редактирования */
    private void setupTable() {
        // First Name
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFirstName()));
        firstNameCol.setOnEditCommit(e ->
                e.getRowValue().setFirstName(e.getNewValue()));

        // Last Name
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getLastName()));
        lastNameCol.setOnEditCommit(e ->
                e.getRowValue().setLastName(e.getNewValue()));

        // Condition
        conditionCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(TeacherCondition.values())
        ));
        conditionCol.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue().getCondition()));
        conditionCol.setOnEditCommit(e ->
                e.getRowValue().setCondition(e.getNewValue()));

        // Birth Year
        birthYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        birthYearCol.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getBirthYear()).asObject());
        birthYearCol.setOnEditCommit(e ->
                e.getRowValue().setBirthYear(e.getNewValue()));

        // Salary
        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        salaryCol.setCellValueFactory(c ->
                new SimpleDoubleProperty(c.getValue().getSalary()).asObject());
        salaryCol.setOnEditCommit(e ->
                e.getRowValue().setSalary(e.getNewValue()));
    }


    private void updateTableForGroup(ClassTeacher selG) {
        List<Teacher> plainList = selG.getTeachers();

        // wrap with ObservableList
        ObservableList<Teacher> obsList = FXCollections.observableArrayList(plainList);

        // wrap with FilteredList
        String txt = filterField.getText().trim().toLowerCase();
        filteredTeachers = new FilteredList<>(obsList, t ->
                txt.isEmpty() || t.getLastName().toLowerCase().contains(txt)
        );
        teacherTable.setItems(filteredTeachers);
    }

    // ==== Groups handlers ====

    @FXML
    private void onAddGroup() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Add Group");
        dlg.setHeaderText("Enter group name:");
        Optional<String> name = dlg.showAndWait();
        name.ifPresent(n -> {
            ClassTeacher gt = new ClassTeacher(n, 10);
            groupData.add(gt);
            groupList.getSelectionModel().select(gt);
        });
    }

    @FXML
    private void onDeleteGroup() {
        ClassTeacher sel = groupList.getSelectionModel().getSelectedItem();
        if (sel != null) {
            groupData.remove(sel);
            teacherTable.setItems(FXCollections.emptyObservableList());
        } else {
            new Alert(Alert.AlertType.WARNING, "No group selected").showAndWait();
        }
    }

    @FXML
    private void onModifyGroup() {
        ClassTeacher sel = groupList.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING, "No group selected").showAndWait();
            return;
        }
        TextInputDialog dlg = new TextInputDialog(sel.getGroupName());
        dlg.setTitle("Modify Group");
        dlg.setHeaderText("Enter new group name:");
        dlg.showAndWait().ifPresent(newName -> {
            sel.setGroupName(newName);
            groupList.refresh();
        });
    }

    @FXML
    private void onSortGroups() {
        FXCollections.sort(groupData, Comparator.comparingDouble(
                g -> - (double) g.getCurrentSize() / g.getMaxTeachers()
        ));
    }

    // ==== Teachers handlers ====

    @FXML
    private void onAddTeacher() {
        ClassTeacher selG = groupList.getSelectionModel().getSelectedItem();
        if (selG == null) {
            new Alert(Alert.AlertType.WARNING, "Select a group first").showAndWait();
            return;
        }
        Teacher t = new Teacher("First", "Last", TeacherCondition.OBECNY, 1980, 3000.0);
        selG.addTeacher(t);
        updateTableForGroup(selG);

        // open edit panel
        int row = teacherTable.getItems().size() - 1;
        teacherTable.scrollTo(row);
        teacherTable.layout();
        teacherTable.edit(row, firstNameCol);
    }

    @FXML
    private void onDeleteTeacher() {
        ClassTeacher selG = groupList.getSelectionModel().getSelectedItem();
        Teacher sel = teacherTable.getSelectionModel().getSelectedItem();
        if (selG != null && sel != null) {
            selG.removeTeacher(sel);
            updateTableForGroup(selG);
        } else {
            new Alert(Alert.AlertType.WARNING, "No teacher selected").showAndWait();
        }
    }

    @FXML
    private void onModifyTeacher() {
        int row = teacherTable.getSelectionModel().getSelectedIndex();
        if (row >= 0) {
            teacherTable.edit(row, firstNameCol);
        } else {
            new Alert(Alert.AlertType.WARNING, "No teacher selected").showAndWait();
        }
    }

    @FXML
    private void onSortTeachers() {
        ClassTeacher selG = groupList.getSelectionModel().getSelectedItem();
        if (selG != null) {
            List<Teacher> sorted = selG.sortByName();
            // update model and table

            selG.getTeachers().clear();
            selG.getTeachers().addAll(sorted);
            updateTableForGroup(selG);
        }
    }

    @FXML
    private void onFilter() {
        ClassTeacher selG = groupList.getSelectionModel().getSelectedItem();
        if (selG != null) {
            updateTableForGroup(selG);
        }
    }
}
