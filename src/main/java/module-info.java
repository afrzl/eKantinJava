module pointOfSales {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jbcrypt;
    requires de.jensd.fx.glyphs.fontawesome;
    requires jasperreports;
    requires java.desktop;

    opens Views to javafx.fxml;
    opens Views.Canteen to javafx.fxml;
    opens Views.Admin to javafx.fxml;
    opens Controllers.Canteen to javafx.fxml;
    opens Controllers.Admin to javafx.fxml;
    opens Controllers to javafx.fxml;
    opens Models to javafx.fxml;

    exports Views;
    exports Models;
    exports Controllers;
}