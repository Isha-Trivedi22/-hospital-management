package com.example.demo.fx;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DoctorPanel {

    private final TableView<String[]> table = new TableView<>();
    private final TextField nameField  = new TextField();
    private final TextField specField  = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField expField   = new TextField();
    private VBox mainLayout;

    public VBox getView() {
        if (mainLayout != null) {
            loadData();
            return mainLayout;
        }

        table.getColumns().clear();
        String[] cols = {"ID","Name","Specialization","Phone","Email","Experience"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue()[idx]));
            col.setPrefWidth(145);
            table.getColumns().add(col);
        }

        nameField.setPromptText("Name");
        specField.setPromptText("Specialization");
        phoneField.setPromptText("Phone");
        emailField.setPromptText("Email");
        expField.setPromptText("Exp (yrs)");

        Button addBtn     = styledBtn("➕ Add",     "#1a73e8");
        Button deleteBtn  = styledBtn("🗑 Delete",  "#e53935");
        Button refreshBtn = styledBtn("🔄 Refresh", "#43a047");

        addBtn.setOnAction(e -> {
            String name  = nameField.getText().trim();
            String spec  = specField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String exp   = expField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Name is required!");
                return;
            }
            String json = "{\"name\":\"" + name + "\","
                        + "\"specialization\":\"" + spec + "\","
                        + "\"phone\":\"" + phone + "\","
                        + "\"email\":\"" + email + "\","
                        + "\"experience\":" + (exp.isEmpty() ? "0" : exp) + "}";
            new Thread(() -> {
                String result = ApiClient.post("/doctors", json);
                System.out.println("Add doctor result: " + result);
                Platform.runLater(() -> {
                    nameField.clear(); specField.clear();
                    phoneField.clear(); emailField.clear(); expField.clear();
                    loadData();
                });
            }).start();
        });

        deleteBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                new Thread(() -> {
                    ApiClient.delete("/doctors/" + sel[0]);
                    Platform.runLater(this::loadData);
                }).start();
            }
        });

        refreshBtn.setOnAction(e -> loadData());

        HBox form = new HBox(8, nameField, specField, phoneField,
                             emailField, expField,
                             addBtn, deleteBtn, refreshBtn);
        form.setPadding(new Insets(10));

        Label title = new Label("👨 Doctor Management");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:#1a73e8;");

        mainLayout = new VBox(12, title, form, table);
        mainLayout.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);

        loadData();
        return mainLayout;
    }

    private void loadData() {
        new Thread(() -> {
            try {
                String response = ApiClient.get("/doctors");
                System.out.println("Doctors GET: " + response);
                JsonArray arr = JsonParser.parseString(response).getAsJsonArray();
                List<String[]> rows = new ArrayList<>();
                for (JsonElement el : arr) {
                    JsonObject o = el.getAsJsonObject();
                    rows.add(new String[]{
                        safe(o, "id"),
                        safe(o, "name"),
                        safe(o, "specialization"),
                        safe(o, "phone"),
                        safe(o, "email"),
                        safe(o, "experience")
                    });
                }
                Platform.runLater(() -> {
                    table.getItems().setAll(rows);
                    System.out.println("Doctors loaded: " + rows.size());
                });
            } catch (Exception e) {
                System.out.println("Doctor load error: " + e.getMessage());
            }
        }).start();
    }

    private String safe(JsonObject o, String key) {
        try {
            if (o.has(key) && !o.get(key).isJsonNull())
                return o.get(key).getAsString();
        } catch (Exception ignored) {}
        return "";
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private Button styledBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color
                 + ";-fx-text-fill:white;-fx-background-radius:6;");
        return b;
    }
}