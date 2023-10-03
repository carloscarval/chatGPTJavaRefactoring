module com.example.javasimplifier {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.apache.commons.text;
    requires lombok;
    requires java.net.http;
    requires httpcore;
    requires httpclient;
    requires org.json;

    opens com.example.javasimplifier to javafx.fxml;
    exports com.example.javasimplifier;
}