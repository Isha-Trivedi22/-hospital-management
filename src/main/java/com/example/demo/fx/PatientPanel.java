package com.example.demo.fx;

import com.google.gson.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class PatientPanel {

    private TableView<String[]> table = new TableView<>();
    private TextField nameField    = new TextField();
    private TextField ageField     = new TextField();
    private TextField genderField  = new TextField();
    private TextField phoneField   = new TextField();
    private TextField diseaseField = new TextField();

    public VBox getView() {
        table.getColumns().clear();
        table.getItems().clear();

        String[] cols = {"ID", "Name", "Age", "Gender", "Phone", "Disease"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
            col.setPrefWidth(140);
            table.getColumns().add(col);
        }

        loadData();

        nameField.setPromptText("Name");
        ageField.setPromptText("Age");
        genderField.setPromptText("Gender");
        phoneField.setPromptText("Phone");
        diseaseField.setPromptText("Disease");

        Button addBtn     = styledBtn("➕ Add",     "#1a73e8");
        Button deleteBtn  = styledBtn("🗑 Delete",  "#e53935");
        Button refreshBtn = styledBtn("🔄 Refresh", "#43a047");

        addBtn.setOnAction(e -> {
            String name    = nameField.getText().trim();
            String age     = ageField.getText().trim();
            String gender  = genderField.getText().trim();
            String phone   = phoneField.getText().trim();
            String disease = diseaseField.getText().trim();

            if (name.isEmpty() || age.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("Please fill Name and Age at minimum!");
                alert.showAndWait();
                return;
            }

            String json = "{\"name\":\"" + name + "\","
                        + "\"age\":" + age + ","
                        + "\"gender\":\"" + gender + "\","
                        + "\"phone\":\"" + phone + "\","
                        + "\"disease\":\"" + disease + "\"}";

            new Thread(() -> {
                String result = ApiClient.post("/patients", json);
                System.out.println("POST result: " + result);
                Platform.runLater(() -> {
                    nameField.clear();
                    ageField.clear();
                    genderField.clear();
                    phoneField.clear();
                    diseaseField.clear();
                    loadData();
                });
            }).start();
        });

        deleteBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                new Thread(() -> {
                    ApiClient.delete("/patients/" + sel[0]);
                    Platform.runLater(this::loadData);
                }).start();
            }
        });

        refreshBtn.setOnAction(e -> loadData());

        HBox form = new HBox(8, nameField, ageField, genderField,
                             phoneField, diseaseField,
                             addBtn, deleteBtn, refreshBtn);
        form.setPadding(new Insets(10));

        Label title = new Label("🧑 Patient Management");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:#1a73e8;");

        VBox layout = new VBox(12, title, form, table);
        layout.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);
        return layout;
    }

    private void loadData() {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/patients");
                System.out.println("GET response: " + response);
                JsonArray arr = JsonParser.parseString(response).getAsJsonArray();

                List<String[]> rows = new ArrayList<>();
                for (JsonElement el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    rows.add(new String[]{
                        safe(o, "id"),
                        safe(o, "name"),
                        safe(o, "age"),
                        safe(o, "gender"),
                        safe(o, "phone"),
                        safe(o, "disease")
                    });
                }

                Platform.runLater(() -> {
                    table.getItems().clear();
                    table.getItems().addAll(rows);
                    System.out.println("Loaded " + rows.size() + " patients");
                });

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }).start();
    }

    private String safe(JsonObject o, String key) {
        try {
            if (o.has(key) && !o.get(key).isJsonNull()) {
                return o.get(key).getAsString();
            }
        } catch (Exception ignored) {}
        return "";
    }

    private Button styledBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color
                 + ";-fx-text-fill:white;-fx-background-radius:6;");
        return b;
    }
}