package com.example.projetb2b;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class AllProductListController implements Initializable {
    @FXML
    private GridPane ProductListGrid;
    @FXML
    private TableView<Panier> tablePanier;
    @FXML
    private Button ListProduitButton;

    @FXML
    private Button ModifProfilActivationButton;

    @FXML
    private AnchorPane PaneListeProduit1;

    @FXML
    private AnchorPane PaneListeProduit2;

    @FXML
    private AnchorPane PaneListeProduit3;
    @FXML
    private AnchorPane PaneReclammation;
    @FXML
    private AnchorPane PanemodifProfil;

    @FXML
    private TextField AdresseField;

    @FXML
    private Button ConfModifBtn;

    @FXML
    private Button ConfModifPassBtn;

    @FXML
    private TextField ConfNewPassField;

    @FXML
    private TextField EmailField;
    @FXML
    private TextField NewPassField;

    @FXML
    private TextField NomField;

    @FXML
    private TextField NumFiscEntrpField;

    @FXML
    private TextField PaysField;
    @FXML
    private TextField oldPassField;

    @FXML
    private Label WelcomeLabel;

    @FXML
    private Label WarnningLabel;

    @FXML
    private Label SuccesLabel;

    @FXML
    private TextField ObjetReclamation;

    @FXML
    private TextField TypeReclamationField;

    @FXML
    private TextArea messageReclamtion;
    @FXML
    private Button Reclammationbtn;

    @FXML
    private TextArea BotResponse;

    @FXML
    private TextField UserInputChatbot;

    @FXML
    private Button SendBotMessage;
    @FXML
    private AnchorPane ChatBotPanel;
    private Stage HomeStage;

    private Client clt ;

    private double montant=0.0d ;


    public void setMontant(double montant) {
        this.montant += montant;
    }

    public void setLoginStage(Stage stage) {
        this.HomeStage = stage;
    }


    public void setClt(Client clt) {
        this.clt = clt;
        WelcomeLabel.setText("Welcome Back "+clt.getNom_entrep());
    }

    public void displayAllProducts()
    {
        DBManager db = new DBManager() ;
        ArrayList<Produit> allproducts = db.GetAllProduct() ;
        ObservableList<Produit> listerDesPorduits = FXCollections.observableArrayList() ;

        for (Produit pdt : allproducts)
        {
            listerDesPorduits.add(pdt) ;
        }

        int rows = 0 ;
        int columns = 0 ;
        ProductListGrid.getColumnConstraints().clear();
        ProductListGrid.getRowConstraints().clear();

        for(int i= 0 ; i < listerDesPorduits.size();i++)
        {

            try {
                FXMLLoader load = new FXMLLoader() ;

                load.setLocation(getClass().getResource("ProductCard.fxml"));
                AnchorPane pane = load.load() ;
                ProductCardController CardC = load.getController() ;
                CardC.SetData(listerDesPorduits.get(i));
                CardC.setProductListController(this);

                if (columns==3)
                {
                    columns = 0 ;
                    rows+=1  ;
                }

                ProductListGrid.add(pane,columns++,rows);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }



    }

    public void DisplayPanier()
    {
        ObservableList<Panier> panier = FXCollections.observableArrayList();
        DBManager db = new DBManager() ;
        panier =  db.GetAllCommands(9) ;

        tablePanier.getColumns().clear();

        // Create columns for Libelle, Prix, and Quantite
        TableColumn<Panier, String> libelleColumn = new TableColumn<>("Libellé Produit");
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle_produit"));

        TableColumn<Panier, Double> prixColumn = new TableColumn<>("Prix");
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

        TableColumn<Panier, Integer> quantiteColumn = new TableColumn<>("Quantité");
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Add columns to the TableView
        tablePanier.getColumns().addAll(libelleColumn, prixColumn, quantiteColumn);

            tablePanier.setItems(panier);

    }

    public TableView<Panier> GetTable()
    {
        return  this.tablePanier ;
    }
public void ActivateModifProfilPane()
{
    PaneListeProduit2.setVisible(false);
    PaneListeProduit3.setVisible(false);
    PaneReclammation.setVisible(false);
    PanemodifProfil.setVisible(true);
    ModifProfilActivationButton.setDisable(true);
    ListProduitButton.setDisable(false);
    Reclammationbtn.setDisable(false);
    ChatBotPanel.setVisible(false);
    SetModifFields(clt);

}
    public void ActivateListeProduitPane()
    {   PanemodifProfil.setVisible(false);
        PaneReclammation.setVisible(false);
        PaneListeProduit2.setVisible(true);
        PaneListeProduit3.setVisible(true);
        ModifProfilActivationButton.setDisable(false);
        ListProduitButton.setDisable(true);
        Reclammationbtn.setDisable(false);
        ChatBotPanel.setVisible(false);



    }
    public void ActivateReclamationPane()
    {    PanemodifProfil.setVisible(false);
        PaneListeProduit2.setVisible(false);
        PaneListeProduit3.setVisible(false);
         PaneReclammation.setVisible(true);
        ModifProfilActivationButton.setDisable(false);
        ListProduitButton.setDisable(false);
        Reclammationbtn.setDisable(true);
        ChatBotPanel.setVisible(false);

    }
    public void ActivateChatbot()
    {
        PanemodifProfil.setVisible(false);
        PaneListeProduit2.setVisible(false);
        PaneListeProduit3.setVisible(false);
        PaneReclammation.setVisible(false);
        ModifProfilActivationButton.setDisable(false);
        ListProduitButton.setDisable(false);
        Reclammationbtn.setDisable(false);
        ChatBotPanel.setVisible(true);

    }

    public void SetModifFields(Client client)
    {
        NomField.setText(client.getNom_entrep());
        EmailField.setText(client.getEmail());
        AdresseField.setText(client.getAdresse());
        NumFiscEntrpField.setText(client.getNum_identification_fiscale());
        PaysField.setText(client.getPays());


    }

    public void MettreAjourDonneesClient()
    {
        clt.setNom_entrep(NomField.getText());
        clt.setEmail(EmailField.getText());
        clt.setPays(PaysField.getText());
        clt.setAdresse(AdresseField.getText());
        clt.miseAJourClient();

    }

    public void ModifPassword()
    {
        if(NewPassField.getText().equals(ConfNewPassField.getText()))
        {
            DBManager db = new DBManager() ;
            db.UpdatePassword(clt.getNum_identification_fiscale(),NewPassField.getText());
            WarnningLabel.setVisible(false);
            SuccesLabel.setText("Modification realiser avec succes");
            SuccesLabel.setVisible(true);

        }
        else {
            WarnningLabel.setText("merci de tapez le meme mot de passe !");
            WarnningLabel.setVisible(true);
        }

    }

    public void SupprimerCompte()
    {
        DBManager db = new DBManager() ;
        db.SupprimeCompte(clt.getNum_identification_fiscale());
        handleLogoutButton();
    }

    public void ValiderLaCommande()
    {

    }


    public void sendReclamation(){
        DBManager db = new DBManager() ;
        db.EnregistrerReclamation(TypeReclamationField.getText(),ObjetReclamation.getText(),messageReclamtion.getText());
    }
    public void resetReclamation()
    {   TypeReclamationField.clear();
        ObjetReclamation.clear();
        messageReclamtion.clear();
    }
    public void handleLogoutButton() {

        try {
            clt=null ;
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

    public void ResponseBot() {
        ChatBot ch = new ChatBot() ;
        String Userinput = UserInputChatbot.getText() ;
        BotResponse.appendText("User : " + Userinput +"\n");
        BotResponse.appendText("Hello : "+ch.GenerateResponse(Userinput)+"\n");
        UserInputChatbot.clear();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayAllProducts();
        DisplayPanier();
        ActivateListeProduitPane() ;


    }
}
