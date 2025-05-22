module com.example.demo {
    /* JavaFX */
    requires javafx.controls;
    requires javafx.fxml;

    /* JPA & Hibernate */
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    /* eksporty */
    exports com.example.demo.ui;
    exports com.example.demo.entity;
    exports com.example.demo.container;
    exports com.example.demo.seed;

    /* otwarte pakiety do refleksji */
    opens com.example.demo.ui       to javafx.fxml;
    opens com.example.demo.entity   to org.hibernate.orm.core, jakarta.persistence, javafx.fxml;



//    requires opencsv;

//    opens com.example.demo.entity to org.hibernate.orm.core, jakarta.persistence;
//    opens com.example.demo.ui     to javafx.fxml;
}
