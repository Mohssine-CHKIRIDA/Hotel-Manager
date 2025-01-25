package projet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Interface extends Application {

    public void start(Stage primaryStage) throws Exception {
        data data = new data();
        Label headerLabel = new Label("Hotel Management System");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2E86C1;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Client Name");

        // Buttons
        Button researchButton = new Button("Search Reservation");
        Button viewRoomsButton = new Button("View Available Rooms");
        Button reserveButton = new Button("Make a Reservation");
        Button PaymentButton = new Button("Add Payment");

        // TextArea for Output
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-control-inner-background: #F8F9F9; -fx-font-family: 'Arial'; -fx-font-size: 12px;");

        // Styling Buttons
        String buttonStyle = "-fx-background-color: #2980B9; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 5px 10px; -fx-background-radius: 5;";
        researchButton.setStyle(buttonStyle);
        viewRoomsButton.setStyle(buttonStyle);
        reserveButton.setStyle(buttonStyle);
        PaymentButton.setStyle(buttonStyle);
        ListView<String> roomListView = new ListView<>();
        roomListView.setPrefSize(400, 300); 
        // Button Actions
        viewRoomsButton.setOnAction(e -> {
            try {
                ArrayList<Chambre> chs = data.viewRooms("disponible");
                Stage roomStage = new Stage();
                roomStage.setTitle("Chambres Disponibles");
                for (Chambre ch : chs) {
                    String roomDetails = "ID: " + ch.getId() +
                                         " | Type: " + ch.getType() +
                                         " | Prix: " + ch.getPrix();
                    roomListView.getItems().add(roomDetails);
                }
                if (chs.isEmpty()) {
                    roomListView.getItems().add("Aucune chambre disponible actuellement.");
                }
                VBox layout = new VBox(10);
                layout.getChildren().add(roomListView);
                layout.setPadding(new Insets(10));
                Scene roomScene = new Scene(layout, 400, 300);
                roomStage.setScene(roomScene);
                roomStage.show();
            } catch (SQLException e1) {
                outputArea.setText("Erreur lors de la récupération des chambres : " + e1.getMessage());
            }
        });

        researchButton.setOnAction(e -> {
            try {
                String result = data.rechercherReservation(nameField.getText());
                outputArea.setText(result != null ? result : "Reservation not found.");
            } catch (SQLException | DateSwitchException e1) {
                outputArea.setText("Error: " + e1.getMessage());
            }
        });

        reserveButton.setOnAction(e -> {
            Stage reservationStage = createReservationStage(data, outputArea);
            reservationStage.show();
        });
        PaymentButton.setOnAction(e -> {
            Stage paymentStage = createPaymentStage(data,outputArea);
            paymentStage.show();
        });

        VBox root = new VBox(10, headerLabel, nameField, researchButton, viewRoomsButton, reserveButton, PaymentButton, outputArea);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #ECF0F1;");
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Hotel Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Stage createReservationStage(data data, TextArea outputArea) {
        // Input Fields for Reservation
        TextField clientNameField = new TextField();
        clientNameField.setPromptText("Client Name");
        TextField clientEmailField = new TextField();
        clientEmailField.setPromptText("Client Email");
        TextField clientPhoneField = new TextField();
        clientPhoneField.setPromptText("Client Phone");
        TextField roomTypeField = new TextField();
        roomTypeField.setPromptText("Room Type");
        Label startDateField = new Label("Start  Date :");
        DatePicker startdatePicker = new DatePicker();
        Label endDateField = new Label("End  Date :");
        DatePicker endtdatePicker = new DatePicker();
        
        Button confirmButton = new Button("Confirm Reservation");
        confirmButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-size: 14px;");

        VBox reservationBox = new VBox(10, clientNameField, clientEmailField, clientPhoneField, roomTypeField, startDateField,startdatePicker, endDateField,endtdatePicker, confirmButton);
        reservationBox.setPadding(new Insets(15));
        reservationBox.setStyle("-fx-background-color: #FDFEFE;");

        Stage reservationStage = new Stage();
        reservationStage.setTitle("Reservation Form");
        reservationStage.setScene(new Scene(reservationBox, 400, 400));

        confirmButton.setOnAction(event -> {
            try {
                Client client = new Client(0,clientNameField.getText(), clientEmailField.getText(), clientPhoneField.getText());
                ArrayList<Chambre> availableRooms = Hotel.filtrer(data.chambres(), ch -> ch.getStatut().equals("disponible"));

                if (!availableRooms.isEmpty()) {
                    Chambre selectedRoom = availableRooms.get(0);
                    Calendar startDate = new GregorianCalendar();
                    Calendar endDate = new GregorianCalendar();
                    startDate.setTime(Date.valueOf(startdatePicker.getValue()));
                    endDate.setTime(Date.valueOf(endtdatePicker.getValue()));     
                    String status = "confirmée";
                    String result = data.reserver(client, selectedRoom, startDate, endDate, status);
                    data.updateChambre(selectedRoom,"occupée");
                    outputArea.setText(result);
                    reservationStage.close();
                } else {
                    outputArea.setText("No available rooms.");
                }

            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        return reservationStage;
    }
    public Stage createPaymentStage(data data, TextArea outputArea) {
        Stage paymentStage = new Stage();
        paymentStage.setTitle("Ajouter Paiement");

        // UI Components
        Label reservationIdLabel = new Label("Reservation ID:");
        TextField reservationIdField = new TextField();
        reservationIdField.setPromptText("Entrez l'ID de la réservation");
        reservationIdField.setTooltip(new Tooltip("ID unique de la réservation"));

        Label montantLabel = new Label("Montant:");
        TextField montantField = new TextField();
        montantField.setPromptText("Entrez le montant (en €)");
        montantField.setTooltip(new Tooltip("Montant du paiement"));

        Label methodeLabel = new Label("Méthode:");
        ComboBox<String> methodeBox = new ComboBox<>();
        methodeBox.getItems().addAll("Carte", "Espèces", "Virement");
        methodeBox.setPromptText("Sélectionnez une méthode");
        methodeBox.setTooltip(new Tooltip("Mode de paiement utilisé"));

        Label statutLabel = new Label("Statut:");
        ComboBox<String> statutBox = new ComboBox<>();
        statutBox.getItems().addAll("en attente", "effectué", "annulé");
        statutBox.setPromptText("Sélectionnez un statut");
        statutBox.setTooltip(new Tooltip("Statut du paiement"));

        Label dateLabel = new Label("Date de Paiement:");
        DatePicker datePicker = new DatePicker();
        datePicker.setTooltip(new Tooltip("Sélectionnez la date du paiement"));

        Button addPaymentButton = new Button("Ajouter Paiement");
        addPaymentButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addPaymentButton.setTooltip(new Tooltip("Cliquez pour enregistrer le paiement"));

        Button cancelButton = new Button("Annuler");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setTooltip(new Tooltip("Cliquez pour annuler"));

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.add(reservationIdLabel, 0, 0);
        grid.add(reservationIdField, 1, 0);
        grid.add(montantLabel, 0, 1);
        grid.add(montantField, 1, 1);
        grid.add(methodeLabel, 0, 2);
        grid.add(methodeBox, 1, 2);
        grid.add(statutLabel, 0, 3);
        grid.add(statutBox, 1, 3);
        grid.add(dateLabel, 0, 4);
        grid.add(datePicker, 1, 4);

        HBox buttonBox = new HBox(10, addPaymentButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 5, 2, 1);

        grid.setAlignment(Pos.CENTER);

        Scene paymentScene = new Scene(grid, 450, 400);
        paymentStage.setScene(paymentScene);

        // Button Actions
        addPaymentButton.setOnAction(e -> {
            try {
                // Validate Inputs
                if (reservationIdField.getText().isEmpty() || montantField.getText().isEmpty() ||
                    methodeBox.getValue() == null || statutBox.getValue() == null || datePicker.getValue() == null) {
                    showAlert(Alert.AlertType.WARNING, "Champs Obligatoires", 
                              "Tous les champs doivent être remplis.");
                    return;
                }

                int reservationId = Integer.parseInt(reservationIdField.getText());
                double montant = Double.parseDouble(montantField.getText());
                String methode = methodeBox.getValue();
                String statut = statutBox.getValue();
                Calendar datePaiement = Calendar.getInstance();
                datePaiement.setTime(java.sql.Date.valueOf(datePicker.getValue()));
                String result = data.ajouterPaiement(reservationId, montant, methode, statut, datePaiement);
                showAlert(Alert.AlertType.INFORMATION, "Succès", result);
                paymentStage.close();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "ID de réservation ou montant invalide.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du paiement : " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> paymentStage.close());

        return paymentStage;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        Interface.launch(args);
    }
}
