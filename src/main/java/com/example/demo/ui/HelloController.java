package com.example.demo.ui;

import com.example.demo.container.ClassContainer;
import com.example.demo.entity.*;
import com.example.demo.service.*;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

/** Kontroler głównego widoku JavaFX. */
public class HelloController {

    /* ---------- FXML ---------- */
    @FXML private ListView<ClassTeacher> groupList;

    @FXML private TextField filterField;
    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher,String>  firstNameCol, lastNameCol;
    @FXML private TableColumn<Teacher,String>  conditionCol;
    @FXML private TableColumn<Teacher,Integer> birthYearCol;
    @FXML private TableColumn<Teacher,Double>  salaryCol;

    @FXML private TableView<Object[]> statsTable;
    @FXML private TableColumn<Object[],String>  groupCol;
    @FXML private TableColumn<Object[],Long>    countCol;
    @FXML private TableColumn<Object[],Double>  avgCol;

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

    /* ---------- filtr: wybrana grupa + fraza nazwiska ---------- */
    private final SimpleObjectProperty<ClassTeacher> selGroup   = new SimpleObjectProperty<>(null);
    private final SimpleStringProperty               nameFilter = new SimpleStringProperty("");

    private final Runnable updatePredicate = () -> filtered.setPredicate(t -> {
        boolean byGroup = selGroup.get() == null ||
                t.getGroup() == null   ||
                t.getGroup().equals(selGroup.get());
        boolean byName  = t.getLastName().toLowerCase()
                .contains(nameFilter.get().toLowerCase());
        return byGroup && byName;
    });

    /* ---------- init ---------- */
    @FXML
    private void initialize() {

        /* demo grupy */
        container.addClass("IA", 30);
        container.addClass("IB", 25);
        refreshGroupView();

        /* --------- filtracja --------- */
        groupList.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            selGroup.set(n); updatePredicate.run();
        });
        filterField.textProperty().addListener((obs,o,n)->{
            nameFilter.set(n); updatePredicate.run();
        });

        /* --------- kolumny + edycja --------- */
        teacherTable.setEditable(true);

        firstNameCol.setEditable(true);
        lastNameCol .setEditable(true);
        conditionCol.setEditable(true);
        birthYearCol.setEditable(true);
        salaryCol   .setEditable(true);

        firstNameCol.setCellValueFactory(c -> Bindings.createStringBinding(c.getValue()::getFirstName));
        lastNameCol .setCellValueFactory(c -> Bindings.createStringBinding(c.getValue()::getLastName));
        conditionCol.setCellValueFactory(c -> Bindings.createStringBinding(() -> c.getValue().getCondition().name()));
        birthYearCol.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getBirthYear));
        salaryCol   .setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getSalary));

        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol .setCellFactory(TextFieldTableCell.forTableColumn());
        birthYearCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        salaryCol   .setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        conditionCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(
                new StringConverter<String>() {
                    @Override public String toString(String s){ return s; }
                    @Override public String fromString(String s){ return s; }
                },
                Arrays.stream(TeacherCondition.values()).map(Enum::name).toArray(String[]::new)
        ));

        firstNameCol.setOnEditCommit(e -> { Teacher t=e.getRowValue(); t.setFirstName(e.getNewValue()); teacherSvc.save(t);} );
        lastNameCol .setOnEditCommit(e -> { Teacher t=e.getRowValue(); t.setLastName(e.getNewValue());  teacherSvc.save(t);} );
        birthYearCol.setOnEditCommit(e -> { Teacher t=e.getRowValue(); t.setBirthYear(e.getNewValue()); teacherSvc.save(t);} );
        salaryCol   .setOnEditCommit(e -> { Teacher t=e.getRowValue(); t.setSalary(e.getNewValue());    teacherSvc.save(t);} );
        conditionCol.setOnEditCommit(e -> {
            Teacher t=e.getRowValue();
            t.setCondition(TeacherCondition.valueOf(e.getNewValue()));
            teacherSvc.save(t);
            teacherTable.refresh();
        });

        teacherTable.setItems(filtered);

        /* statystyki ocen */
        groupCol.setCellValueFactory(c -> new SimpleStringProperty((String) c.getValue()[0]));
        countCol.setCellValueFactory(c -> new SimpleObjectProperty<>((Long)   c.getValue()[1]));
        avgCol  .setCellValueFactory(c -> new SimpleObjectProperty<>((Double) c.getValue()[2]));
        loadStats();
    }

    /* ---------- ENTER w polu nazwiska ---------- */
    @FXML private void onFilter() {
        nameFilter.set(filterField.getText());
        updatePredicate.run();
    }

    /* ---------- TEACHER CRUD ---------- */
    @FXML private void onAddTeacher() {
        Teacher t = new Teacher("New","Teacher", TeacherCondition.OBECNY,
                Year.now().getValue(), 0);
        teacherSvc.save(t);
        masterTeachers.setAll( teacherSvc.all() );
    }

    @FXML private void onDeleteTeacher() {
        Teacher sel = teacherTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Select a teacher first"); return; }
        teacherSvc.delete(sel.getId());
        masterTeachers.setAll( teacherSvc.all() );
    }

    @FXML private void onModifyTeacher() {
        Teacher sel = teacherTable.getSelectionModel().getSelectedItem();
        if (sel == null) { alert("Select a teacher first"); return; }
        TextInputDialog d = new TextInputDialog(String.valueOf(sel.getSalary()));
        d.setHeaderText("New salary:");
        d.showAndWait().ifPresent(s -> {
            try { sel.setSalary(Double.parseDouble(s)); teacherSvc.save(sel); teacherTable.refresh(); }
            catch (NumberFormatException ex){ alert("Invalid number"); }
        });
    }

    @FXML private void onSortTeachers() {
        FXCollections.sort(masterTeachers, Comparator.comparing(Teacher::getLastName));
    }

    /* ---------- GROUP CRUD ---------- */
    @FXML private void onAddGroup() {
        TextInputDialog dName = new TextInputDialog();
        dName.setHeaderText("Enter group name:");
        Optional<String> nameOpt = dName.showAndWait();
        if (!nameOpt.isPresent()) return;

        TextInputDialog dMax = new TextInputDialog("30");
        dMax.setHeaderText("Max teachers:");
        Optional<String> maxOpt = dMax.showAndWait();
        if (!maxOpt.isPresent()) return;

        try {
            container.addClass(nameOpt.get(), Integer.parseInt(maxOpt.get()));
            refreshGroupView();
        } catch (Exception ex) { alert(ex.getMessage()); }
    }

    @FXML private void onDeleteGroup() {
        ClassTeacher g = groupList.getSelectionModel().getSelectedItem();
        if (g == null) { alert("Select group first"); return; }
        container.removeClass(g.getName());
        refreshGroupView();
    }

    @FXML private void onModifyGroup() {
        ClassTeacher g = groupList.getSelectionModel().getSelectedItem();
        if (g == null) { alert("Select group first"); return; }
        TextInputDialog d = new TextInputDialog(String.valueOf(g.getMaxTeachers()));
        d.setHeaderText("New max teachers for "+g.getName());
        d.showAndWait().ifPresent(s -> {
            try { g.setMaxTeachers(Integer.parseInt(s)); refreshGroupView(); }
            catch (NumberFormatException ex){ alert("Invalid number"); }
        });
    }

    @FXML private void onSortGroups() {
        FXCollections.sort(groupList.getItems(),
                Comparator.comparingDouble(ClassTeacher::getFillPercentage).reversed());
    }

    /* ---------- RATE ---------- */
    @FXML private void onAddRate() {
        ClassTeacher grp = groupList.getSelectionModel().getSelectedItem();
        if (grp == null) { alert("Select group first"); return; }

        Rate r = new Rate();
        r.setGroup(grp);
        r.setDate(LocalDate.now());
        r.setComment("ok");
        r.setValue(5);
        rateSvc.addRate(r);
        loadStats();
    }

    /* ---------- CSV ---------- */
    @FXML private void onExportCsv() {
        try {
            csvExp.exportTeachers(Paths.get("teachers.csv"));
            alert("CSV saved in project root");
        } catch (IOException ex) {
            ex.printStackTrace();
            alert("IO error");
        }
    }

    /* ---------- pomoc ---------- */
    private void loadStats() {
        statsTable.setItems(FXCollections.observableArrayList(rateSvc.stats()));
    }

    private void refreshGroupView() {
        List<ClassTeacher> list = new ArrayList<>();
        for (String n : container.showFillPercentage().keySet()) {
            list.add(container.get(n));
        }
        groupList.setItems(FXCollections.observableArrayList(list));
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
