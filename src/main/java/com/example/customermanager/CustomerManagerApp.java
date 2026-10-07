package com.example.customermanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerManagerApp extends Application {

    // Stores all customers
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // =========================
        // 1. NAME FIELD
        // =========================

        Label nameLabel = new Label("Name:");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");


        // =========================
        // 2. PROVINCE LIST
        // =========================

        Label provinceLabel = new Label("Province:");

        ComboBox<String> provinceBox = new ComboBox<>();

        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceBox.setPromptText("Select province");


        // =========================
        // 3. TABLEVIEW
        // =========================

        TableView<Customer> tableView = new TableView<>();

        // Name column
        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        // Province column
        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        // Add columns to table
        tableView.getColumns().addAll(
                nameColumn,
                provinceColumn
        );

        // Connect the table to our customer list
        tableView.setItems(customers);

        // Set column widths
        nameColumn.setPrefWidth(180);
        provinceColumn.setPrefWidth(180);


        // =========================
        // 4. ADD CUSTOMER BUTTON
        // =========================

        Button addButton = new Button("_Add Customer");

        // Allows Alt + A keyboard access
        addButton.setMnemonicParsing(true);

        addButton.setDefaultButton(true);

        addButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            // -------- VALIDATION --------

            if (name.isEmpty()) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please enter the customer's name."
                );

                nameField.requestFocus();
                return;
            }

            if (province == null) {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Invalid Input",
                        "Please select a province."
                );

                provinceBox.requestFocus();
                return;
            }

            // -------- CREATE CUSTOMER --------

            Customer customer =
                    new Customer(name, province);

            // Add customer to ObservableList
            customers.add(customer);

            // Clear input fields
            nameField.clear();
            provinceBox.getSelectionModel().clearSelection();

            // Return keyboard focus to name field
            nameField.requestFocus();
        });


        // =========================
        // 5. DELETE CUSTOMER BUTTON
        // =========================

        Button deleteButton = new Button("_Delete Customer");

        // Allows Alt + D keyboard access
        deleteButton.setMnemonicParsing(true);

        deleteButton.setOnAction(event -> {

            // Get selected customer
            Customer selectedCustomer =
                    tableView.getSelectionModel().getSelectedItem();

            // Check whether a customer was selected
            if (selectedCustomer == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "No Customer Selected",
                        "Please select a customer to delete."
                );

                return;
            }

            // Ask for confirmation
            Alert confirmation =
                    new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete "
                                    + selectedCustomer.getName()
                                    + "?",
                            ButtonType.YES,
                            ButtonType.NO
                    );

            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Customer");

            confirmation.showAndWait().ifPresent(response -> {

                if (response == ButtonType.YES) {

                    customers.remove(selectedCustomer);
                }
            });
        });


        // =========================
        // FORM LAYOUT
        // =========================

        HBox nameBox = new HBox(
                10,
                nameLabel,
                nameField
        );

        HBox provinceBoxLayout = new HBox(
                10,
                provinceLabel,
                provinceBox
        );

        HBox buttonBox = new HBox(
                10,
                addButton,
                deleteButton
        );


        // =========================
        // MAIN LAYOUT
        // =========================

        VBox root = new VBox(
                15,
                nameBox,
                provinceBoxLayout,
                buttonBox,
                tableView
        );

        root.setPadding(new Insets(20));


        // =========================
        // SCENE
        // =========================

        Scene scene = new Scene(
                root,
                500,
                500
        );

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        // Start with keyboard focus on name field
        nameField.requestFocus();
    }


    // =========================
    // ALERT METHOD
    // =========================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }


    // =========================
    // CUSTOMER CLASS
    // =========================

    public static class Customer {

        private String name;
        private String province;

        public Customer(String name, String province) {
            this.name = name;
            this.province = province;
        }

        public String getName() {
            return name;
        }

        public String getProvince() {
            return province;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }


    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {
        launch(args);
    }
}