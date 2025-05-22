package com.example.demo.ui;

import com.example.demo.container.ClassContainer;
import com.example.demo.dao.RateDao;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.nio.file.Paths;

public class HelloController {

    /* ---------- FXML pola ---------- */
    @FXML private ListView<ClassTeacher> groupList;

    @FXML private TextField filterField;
    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher,String> firstNameCol, lastNameCol, conditionCol;
    @FXML private TableColumn<Teacher,Integer> birthYearCol;
    @FXML private TableColumn<Teacher,Double> salaryCol;

    @FXML private TableView<Object[]> statsTable;
    @FXML private TableColumn<Object[],String>  groupCol;
    @FXML private TableColumn<Object[],Long>    countCol;
    @FXML private TableColumn<Object[],Double>  avgCol;

    @FXML private void onAddGroup()    { System.out.println("Add group (TODO)"); }
    @FXML private void onDeleteGroup() { System.out.println("Delete group (TODO)"); }
    @FXML private void onModifyGroup() { System.out.println("Modify group (TODO)"); }
    @FXML private void onSortGroups()  { System.out.println("Sort groups (TODO)"); }
    @FXML private void onDeleteTeacher(){ System.out.println("Delete teacher (TODO)"); }
    @FXML private void onModifyTeacher(){ System.out.println("Modify teacher (TODO)"); }
    @FXML private void onSortTeachers(){ System.out.println("Sort teachers (TODO)"); }

    /* ---------- serwisy ---------- */
    private final TeacherService teacherSvc = new TeacherService();
    private final RateService    rateSvc    = new RateService();
    private final CsvExporter    csvExp     = new CsvExporter();

    /* ---------- dane ---------- */
    private final ObservableList<Teacher> masterTeachers =
            FXCollections.observableArrayList( teacherSvc.all() );
    private final FilteredList<Teacher> filtered =
            new FilteredList<>( masterTeachers );

    private final ClassContainer container = new ClassContainer();

    /* ---------- init ---------- */
    @FXML
    private void initialize() {

        /* przykładowe dwie grupy */
        container.addClass("IA", 30);
        container.addClass("IB", 25);
        groupList.setItems(FXCollections.observableArrayList(
                container.get("IA"), container.get("IB")));

        /* kolumny nauczycieli */
        firstNameCol.setCellValueFactory(c -> Bindings.createStringBinding(c.getValue()::getFirstName));
        lastNameCol .setCellValueFactory(c -> Bindings.createStringBinding(c.getValue()::getLastName));
        conditionCol.setCellValueFactory(c -> Bindings.createStringBinding(() -> c.getValue().getCondition().name()));
        birthYearCol.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getBirthYear));
        salaryCol   .setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getSalary));
        salaryCol   .setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        teacherTable.setItems(filtered);

        /* kolumny statystyk */
        groupCol.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[0]));
        countCol.setCellValueFactory(c -> new SimpleObjectProperty<>((Long)   c.getValue()[1]));
        avgCol  .setCellValueFactory(c -> new SimpleObjectProperty<>((Double) c.getValue()[2]));
        loadStats();
    }

    /* ---------- akcje UI ---------- */
    @FXML private void onFilter() {        // ENTER w polu tekstowym
        String phrase = filterField.getText().toLowerCase();
        filtered.setPredicate(t -> t.getLastName().toLowerCase().contains(phrase));
    }

    @FXML private void onAddTeacher() {
        Teacher t = new Teacher("New","Teacher",
                TeacherCondition.OBECNY, Year.now().getValue(), 0);
        teacherSvc.save(t);
        masterTeachers.setAll( teacherSvc.all() );
    }

    @FXML private void onAddRate() {
        if (groupList.getSelectionModel().isEmpty()) return;
        ClassTeacher grp = groupList.getSelectionModel().getSelectedItem();

        Rate r = new Rate();
        r.setComment("ok");
        r.setDate(LocalDate.now());
        r.setValue(5);
        r.setGroup(grp);

        rateSvc.addRate(r);
        loadStats();
    }

    @FXML
    private void onExportCsv() {
        try {
            Path p = Paths.get("teachers.csv");   // zamiast Path.of(...)
            csvExp.exportTeachers(p);
            System.out.println("CSV zapisany w: " + p.toAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* ---------- pomoc ---------- */
    private void loadStats() {
        statsTable.setItems(
                FXCollections.observableArrayList( rateSvc.stats() ));
    }
}
