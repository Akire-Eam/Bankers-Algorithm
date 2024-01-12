module com.example.cmsc125_lab4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmsc125_lab4 to javafx.fxml;
    exports com.example.cmsc125_lab4;
}