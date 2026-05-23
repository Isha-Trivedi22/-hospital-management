package com.example.demo.fx;

import com.google.gson.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BillingPanel {

    private TableView<String[]> table  = new TableView<>();
    private TextField patientField = new TextField();
    private TextField doctorField  = new TextField();
    private TextField dateField    = new TextField();
    private TextField amountField  = new TextField();
    private TextField statusField  = new TextField();

    public VBox getView() {
        String[] cols = {"ID", "Patient", "Doctor", "Date", "Amount", "Status"};
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[idx]));
            col.setPrefWidth(140);
            table.getColumns().add(col);
        }
        loadData();

        patientField.setPromptText("Patient Name");
        doctorField.setPromptText("Doctor Name");
        dateField.setPromptText("Date");
        amountField.setPromptText("Amount");
        statusField.setPromptText("PAID / UNPAID");

        Button addBtn     = styledBtn("➕ Add Bill", "#1a73e8");
        Button deleteBtn  = styledBtn("🗑 Delete", "#e53935");
        Button refreshBtn = styledBtn("🔄 Refresh", "#43a047");

        addBtn.setOnAction(e -> {
            String json = String.format(
                "{\"patientName\":\"%s\",\"doctorName\":\"%s\",\"date\":\"%s\",\"amount\":%s,\"status\":\"%s\"}",
                patientField.getText(), doctorField.getText(),
                dateField.getText(), amountField.getText(), statusField.getText());
            ApiClient.post("/bills", json);
            patientField.clear(); doctorField.clear(); dateField.clear();
            amountField.clear(); statusField.clear();
            loadData();
        });

        deleteBtn.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) { ApiClient.delete("/bills/" + sel[0]); loadData(); }
        });

        refreshBtn.setOnAction(e -> loadData());

        HBox form = new HBox(8, patientField, doctorField, dateField,
                             amountField, statusField, addBtn, deleteBtn, refreshBtn);
        form.setPadding(new Insets(10));

        Label title = new Label("💰 Billing Management");
        title.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:#1a73e8;");

        VBox layout = new VBox(12, title, form, table);
        layout.setPadding(new Insets(20));
        VBox.setVgrow(table, Priority.ALWAYS);
        return layout;
    }

    private void loadData() {
        table.getItems().clear();
        try {
            JsonArray arr = JsonParser.parseString(ApiClient.get("/bills")).getAsJsonArray();
            for (JsonElement el : arr) {
                JsonObject o = el.getAsJsonObject();
                table.getItems().add(new String[]{
                    o.get("id").getAsString(), o.get("patientName").getAsString(),
                    o.get("doctorName").getAsString(), o.get("date").getAsString(),
                    o.get("amount").getAsString(), o.get("status").getAsString()
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