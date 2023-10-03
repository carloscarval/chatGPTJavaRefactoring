module chatGPTJavaRefactoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.google.gson;
    requires org.apache.commons.text;
    requires java.net.http;
    requires httpcore;
    requires httpclient;
    requires org.json;

    opens com.br.chatGPTJavaRefactoring to javafx.fxml;
    exports com.br.chatGPTJavaRefactoring;
}