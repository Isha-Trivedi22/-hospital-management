package com.example.demo.fx;

import com.google.gson.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DoctorPanel {

    private TableView<String[]> table = new TableView<>();
    private TextField nameField  = new TextField();
    private TextField specField  = new TextField();
    private TextField phoneField = new TextField();
    private TextField emailField = new TextField();
    private TextField expField   = new TextField();

    public VBox getView() {
        String[] cols = {"ID", "Name", "Specialization", "Phone", "Email", "Experience"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
            col.setPrefWidth(140);
            table.getColumns().add(col);
        }
        loadData();

        nameField.setPromptText("Name");
        specField.setPromptText("Specialization");
        phoneField.setPromptText("Phone");
        emailField.setPromptText("Email");
        expField.setPromptText("Experience (yrs)");

        Button addBtn     = styledBtn("➕ Add", "#1a73e8");
        Button deleteBtn  = styledBtn("🗑 Delete", "#e53935");
        Button refreshBtn = styledBtn("🔄 Refresh", "#43a047");

        addBtn.setOnAction(e -> {
            String json = String.format(
                "{\"name\":\"%s\",\"specialization\":\"%s\",\"phone\":\"%s\",\"email\":\"%s\",\"experience\":%s}",
                nameField.getText(), specField.getText(),
                phoneField.getText(), emailField.getText(), expField.getText());
            ApiClient.post("/doctors", json);
            nameField.clear(); specField.clear(); phoneField.clear();
            emailField.clear(); expField.clear();
            loadData();
        });

        deleteBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) { ApiClient.delete("/doctors/" + sel[0]); loadData(); }
        });

        refreshBtn.setOnAction(e -> loadData());

        HBox form = new HBox(8, nameField, specField, phoneField, emailField, expField,
                             addBtn, deleteBtn, refreshBtn);
        form.setPadding(new Insets(10));

        Label title = new Label("👨 Doctor Management");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:#1a73e8;");

        VBox layout = new VBox(12, title, form, table);
        layout.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);
        return layout;
    }

    private void loadData() {
        table.getItems().clear();
        try {
            JsonArray arr = JsonParser.parseString(ApiClient.get("/doctors")).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject o = el.getAsJsonObject();
                table.getItems().add(new String[]{
                    o.get("id").getAsString(), o.get("name").getAsString(),
                    o.get("specialization").getAsString(), o.get("phone").getAsString(),
                    o.get("email").getAsString(), o.get("experience").getAsString()
                });
            }
        } catch (Exception ignored) {}
    }

    private Button styledBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;-fx-background-radius:6;");
        return b;
    }
}