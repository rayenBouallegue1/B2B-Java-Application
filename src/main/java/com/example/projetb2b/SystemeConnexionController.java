package com.example.projetb2b;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class SystemeConnexionController implements Initializable {
    @FXML
    private TextField confmdp;

    @FXML
    private TextField NomEntrpFieldInscrip;
    @FXML
    private TextField clearPassword;

    @FXML
    private TextField loginField;

    @FXML
    private TextField mdp;

    @FXML
    private PasswordField mdpField;

    @FXML
    private  TextField login ;

    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private Hyperlink seConnecterLink;
    @FXML
    private AnchorPane registerFrom;

    @FXML
    private AnchorPane loginForm ;

    @FXML
    private Hyperlink CreateAccount ;

    @FXML
    private TextField EmailFieldInscription;

    @FXML
    private TextField NumFiscFieldInscrip;

    private Stage loginStage;

    // Method to set the stage
    public void setLoginStage(Stage stage) {
        this.loginStage = stage;
    }

    SystemeConnexion systeme = new SystemeConnexion() ;
    public void seConnecter(){

        DBManager db = new DBManager() ;
        ArrayList<user> users = new ArrayList <user> () ;
        users = db.GetAllUsers() ;

        if(loginField.getText().equals("admin") && mdpField.getText().equals("admin"))
        {
            loginStage.close();
            loadHomePageAdmin() ;

        }
        else {
            Client client = systeme.authentifier(loginField.getText() , mdpField.getText(),users) ;
            if (client!=null)
            {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Connection successful!");
                alert.showAndWait();
                loadHomePage(client);
                loginStage.close();


            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Merci de verifier les information ! ");
                alert.showAndWait();
                loginField.clear();
                mdpField.clear();
            }
        }

    }
    public void inscrireUser()
    {
        if(mdp.getText().equals(confmdp.getText()))
        {
            user user1 = new user(login.getText(),mdp.getText(),NumFiscFieldInscrip.getText()) ;
            Client client = new Client() ;
            client.setNom_entrep(NomEntrpFieldInscrip.getText());
            client.setNum_identification_fiscale(NumFiscFieldInscrip.getText());
            client.setEmail(EmailFieldInscription.getText());
            client.EnregistrerClientEnBase() ;
            if(systeme.enregistrerUser(user1) )
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Creation de compte avec succées!");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la creation de compte ");
                alert.showAndWait();
            }


        }
    }


    public void switchForm(ActionEvent event)
    {
        if(event.getSource()==seConnecterLink)
        {
            registerFrom.setVisible(false);
            loginForm.setVisible(true);
        }
        else if(event.getSource()==CreateAccount) {

            loginForm.setVisible(false);
            registerFrom.setVisible(true);
        }
    }
    private void loadHomePage(Client clt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AllProductsList.fxml"));
            Parent root = loader.load();
            AllProductListController controller = loader.getController() ;
            controller.setClt(clt);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            controller.setLoginStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadHomePageAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();
            AdminController controller = loader.getController() ;

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            controller.setLoginStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPasswordCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                clearPassword.setText(mdpField.getText());
                clearPassword.setVisible(true);
                mdpField.setVisible(false);
            } else {
                mdpField.setText(clearPassword.getText());
                clearPassword.setVisible(false);
                mdpField.setVisible(true);
            }
        });
    }
}
