package com.example.projetb2b;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML
    private TextField libelleField ;

    @FXML
    private TextArea descriptionArea ;
    @FXML
    private TextField PriceField ;

    @FXML
    private Button ajouterProduitbtn;

    @FXML
    private Button MenuAjoutProdui;

    @FXML
    private Button MenuListeREclamation;
    @FXML
    private Button Resetbtn ;
    @FXML
    private AnchorPane AjoutProduitPanel;

    @FXML
    private AnchorPane ListeReclamationPanel;
    @FXML
    private AnchorPane ChatBotPanel;


    @FXML
    private TableView<Produit> TableProduits;
    @FXML
    private Label NbrProduits;
    @FXML
    private Label nbrChiffreAffaire;

    @FXML
    private Label nbrCommande;

    @FXML
    private Label nbreClient;

    @FXML
    private Label nbreReclamation;
    @FXML
    private TableView<Reclamation> RaclamationTable ;
    @FXML
    private Button DashboardBtn;
    @FXML
    private AnchorPane PanelDashboard;


    public void resetFields()
    {
        libelleField.clear();
        descriptionArea.clear();
        PriceField.clear();
    }
    public void addProductToDatabase()
    {   Produit pdt = new Produit(libelleField.getText(),descriptionArea.getText(),Double.parseDouble(PriceField.getText()) ) ;
        try{
            pdt.SaveProduct() ;
            DisplayAllProducts();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void modifProduit()
    {
        TableProduits.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // When a new row is selected, print the libelleProduit of the selected product
                System.out.println("Selected Product: " + newSelection.getLibelleProduit());
                libelleField.setText(newSelection.getLibelleProduit());
                descriptionArea.setText(newSelection.getDescriptionProduit());
                PriceField.setText(Double.toString(newSelection.getPrix()));
            }
        });
    }

    public void UpdateProduct()
    {
        DBManager db = new DBManager() ;
        db.updateProduct(libelleField.getText(),Double.parseDouble(PriceField.getText()),descriptionArea.getText());

    }
    public void DisplayAllProducts()
    {
        TableColumn<Produit, String> libelleColumn = new TableColumn<>("Libellé Produit");
        TableColumn<Produit, Double> prixColumn = new TableColumn<>("Prix");
        TableColumn<Produit, String> DescriptionColumn = new TableColumn<>("description");
        DBManager db = new DBManager() ;
        TableProduits.getColumns().clear();
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("LibelleProduit"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionProduit"));
        TableProduits.getColumns().addAll(libelleColumn, prixColumn, DescriptionColumn);
        ArrayList<Produit> productList = db.GetAllProduct() ;

        ObservableList<Produit> observableProductList = FXCollections.observableArrayList(productList);

        TableProduits.setItems(observableProductList);

    }
    public void DisplayAllReclamation()
    {
        TableColumn<Reclamation, String> ObjetColumn = new TableColumn<>("Objet reclamation");
        TableColumn<Reclamation, String> TypeColumn = new TableColumn<>("Type reclamtion");
        TableColumn<Reclamation, String> MessageColumn = new TableColumn<>("Message Reclamation");
        DBManager db = new DBManager() ;
        RaclamationTable.getColumns().clear();
        ObjetColumn.setCellValueFactory(new PropertyValueFactory<>("Objet"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        MessageColumn.setCellValueFactory(new PropertyValueFactory<>("Message"));
        RaclamationTable.getColumns().addAll(ObjetColumn, TypeColumn, MessageColumn);
        ArrayList<Reclamation> ReclamationList = db.GetAllReclamation() ;

        ObservableList<Reclamation> observableProductList = FXCollections.observableArrayList(ReclamationList);

        RaclamationTable.setItems(observableProductList);
    }
    private Stage HomeStage;

    public void setLoginStage(Stage stage) {
        this.HomeStage = stage;
    }

    public void ActivateReclamationPAnel()
    {
        AjoutProduitPanel.setVisible(false);
        ListeReclamationPanel.setVisible(true);
        DisplayAllReclamation();
        MenuListeREclamation.setDisable(true);
        MenuAjoutProdui.setDisable(false);
        DashboardBtn.setDisable(false);
        PanelDashboard.setVisible(false);
        ChatBotPanel.setVisible(false);


    }
    public void ActivateChatbot()
    {
        AjoutProduitPanel.setVisible(false);
        ListeReclamationPanel.setVisible(false);
        MenuListeREclamation.setDisable(false);
        MenuAjoutProdui.setDisable(false);
        DashboardBtn.setDisable(false);
        PanelDashboard.setVisible(false);
        ChatBotPanel.setVisible(true);


    }
    public void ActivateAjoutProduitPanel()
    {
        AjoutProduitPanel.setVisible(true);
        ListeReclamationPanel.setVisible(false);
        DisplayAllProducts();
        MenuListeREclamation.setDisable(false);
        MenuAjoutProdui.setDisable(true);
        DashboardBtn.setDisable(false);
        PanelDashboard.setVisible(false);
        ChatBotPanel.setVisible(false);


    }

    public void ActivateDashboardPanel(){
        AjoutProduitPanel.setVisible(false);
        ListeReclamationPanel.setVisible(false);
        ChatBotPanel.setVisible(false);
        MenuListeREclamation.setDisable(false);
        MenuAjoutProdui.setDisable(false);

        DashboardBtn.setDisable(true);
        PanelDashboard.setVisible(true);


    }
    public void handleLogoutButton() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            HomeStage.close();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBManager db = new DBManager() ;
        DisplayAllProducts();
        nbreClient.setText(Integer.toString(db.NombreClients()));
        nbreReclamation.setText(Integer.toString(db.NombreReclamation()));
        NbrProduits.setText(Integer.toString(db.NombreProduit()));
        nbrCommande.setText("0");
        nbrChiffreAffaire.setText("0.0$");
        ActivateDashboardPanel() ;
        modifProduit();

    }

}
