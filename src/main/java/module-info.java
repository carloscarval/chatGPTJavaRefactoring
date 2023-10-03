module com.example.javasimplifier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;

    opens com.example.javasimplifier to javafx.fxml;
    exports com.example.javasimplifier;
}