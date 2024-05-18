package com.example.projetb2b;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import  java.security.MessageDigest ;
import java.util.ArrayList;

/**
 *
 * @author rayen
 */

public class SystemeConnexion  extends Application  {

    Client MainClient ;
    public Client authentifier(String Login , String mdp, ArrayList<user> Utilisateurs) {
        String hashedPassword = DoHashing(mdp) ;
        DBManager db = new DBManager() ;
        for (user utilisateur : Utilisateurs) {
            if (utilisateur.getLogin().equals(Login) && utilisateur.getPassword().equals(hashedPassword)) {
                MainClient=db.getClientByNumIdentification(utilisateur.getNumFiscleEntrp())  ;
                return MainClient ;
            }
        }

        return null ;
    }

    public String DoHashing(String password) {
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("MD5") ;
            msgDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte [] biteArrayResult = msgDigest.digest() ;
            StringBuilder sb = new StringBuilder() ;
            for(byte b:biteArrayResult)
            {
                sb.append(String.format("%02x",b));
            }
            return sb.toString() ;
        }catch(NoSuchAlgorithmException  e)
        {
            e.printStackTrace();
            return "" ;
        }
    }

    public boolean enregistrerUser(user utilisateur)
    {
        Connection connexion = null;
        try {
            DBManager dbManager = new DBManager() ;
            connexion =dbManager.GetConnection() ;
            String sql = "INSERT INTO \"Users\" (\"Login\", mot_de_passe , \"NumFiscEntrp\") VALUES (?, ?,?)";
            PreparedStatement preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1, utilisateur.getLogin());
            preparedStatement.setString(2,DoHashing(utilisateur.getPassword()) );
            preparedStatement.setString(3,utilisateur.getNumFiscleEntrp() );
            int rowsAffected = preparedStatement.executeUpdate();
            dbManager.closeConnection();
            return rowsAffected > 0 ;

        }
        catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
            return false ;
        }
    }
    public boolean Sedeconnecter(user utilisateur)
    {
        if (utilisateur !=null)
        {
            utilisateur = null ;
            return true ;
        }
        else
        {
            return false ;
        }

    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(com.example.projetb2b.Produit.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 460, 460);
        SystemeConnexionController syc = fxmlLoader.getController() ;
        syc.setLoginStage(stage);
        stage.setTitle("Welcome to Our B2B!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}