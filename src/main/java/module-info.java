module pr.phys.physicsengine2d {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens pr.phys.physicsengine2d to javafx.fxml;
    exports pr.phys.physicsengine2d;
}