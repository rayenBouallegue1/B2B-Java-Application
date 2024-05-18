module com.example.projetb2b {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires edu.stanford.nlp.corenlp;
    requires org.apache.commons.text;
    requires org.apache.commons.lang3;

    opens com.example.projetb2b to javafx.fxml;
    exports com.example.projetb2b;
}