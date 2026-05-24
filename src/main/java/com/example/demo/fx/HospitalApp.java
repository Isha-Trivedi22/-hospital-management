package com.example.demo.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HospitalApp extends Application {

    private final PatientPanel patientPanel         = new PatientPanel();
    private final DoctorPanel doctorPanel           = new DoctorPanel();
    private final AppointmentPanel appointmentPanel = new AppointmentPanel();
    private final BillingPanel billingPanel         = new BillingPanel();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hospital Management System");

        Button btnPatients     = new Button("🧑 Patients");
        Button btnDoctors      = new Button("👨 Doctors");
        Button btnAppointments = new Button("📅 Appointments");
        Button btnBilling      = new Button("💰 Billing");

        String btnStyle = """
            -fx-background-color: #1a73e8;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-pref-width: 180;
            -fx-pref-height: 45;
        """;

        btnPatients.setStyle(btnStyle);
        btnDoctors.setStyle(btnStyle);
        btnAppointments.setStyle(btnStyle);
        btnBilling.setStyle(btnStyle);

        VBox sidebar = new VBox(12, btnPatients, btnDoctors,
                                btnAppointments, btnBilling);
        sidebar.setPadding(new Insets(30, 20, 20, 20));
        sidebar.setStyle("-fx-background-color: #0d1b2a;");
        sidebar.setPrefWidth(210);

        StackPane content = new StackPane();
        content.setStyle("-fx-background-color: #f0f4f8;");

        Label welcome = new Label("Welcome to Hospital Management System");
        welcome.setStyle("-fx-font-size:20px;-fx-font-weight:bold;-fx-text-fill:#1a73e8;");
        content.getChildren().add(welcome);

        btnPatients.setOnAction(e ->
            content.getChildren().setAll(patientPanel.getView()));
        btnDoctors.setOnAction(e ->
            content.getChildren().setAll(doctorPanel.getView()));
        btnAppointments.setOnAction(e ->
            content.getChildren().setAll(appointmentPanel.getView()));
        btnBilling.setOnAction(e ->
            content.getChildren().setAll(billingPanel.getView()));

        HBox root = new HBox(sidebar, content);
        HBox.setHgrow(content, Priority.ALWAYS);

        Scene scene = new Scene(root, 1100, 680);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}