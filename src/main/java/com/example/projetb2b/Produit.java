package com.example.projetb2b;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Produit extends Application {
    private String LibelleProduit ;
    private int CodeProduit  ;
    private  String descriptionProduit ;
    private  double prix ;

    private  String PathImage ;

    public Produit(String libelleProduit,int codeProduit ,String descriptionProduit, double prix) {
        LibelleProduit = libelleProduit;
        CodeProduit = codeProduit ;
        this.descriptionProduit = descriptionProduit;
        this.prix = prix;

    }
    public Produit(String libelleProduit,int codeProduit ,String descriptionProduit, double prix,String Path) {
        LibelleProduit = libelleProduit;
        CodeProduit = codeProduit ;
        this.descriptionProduit = descriptionProduit;
        this.prix = prix;
        this.PathImage=Path ;

    }

    public String getPathImage() {
        return PathImage;
    }

    public void setPathImage(String pathImage) {
        PathImage = pathImage;
    }

    public Produit(String libelleProduit , String descriptionProduit, double prix) {
        LibelleProduit = libelleProduit;
        this.descriptionProduit = descriptionProduit;
        this.prix = prix;

    }
    public Produit() { }


    public void  SaveProduct(){
        DBManager db = new DBManager() ;
        db.InsertProduct(this) ;
    }

    public String getLibelleProduit() {
        return LibelleProduit;
    }

    public int getCodeProduit() {
        return CodeProduit;
    }

    public double getPrix() {
        return prix;
    }

    public String getDescriptionProduit()
        {
        return  descriptionProduit ;
        }

    public void setLibelleProduit(String libelleProduit) {
        LibelleProduit = libelleProduit;
    }

    public void setCodeProduit(int codeProduit) {
        CodeProduit = codeProduit;
    }

    public void setStock(double stock) {
        prix = stock;
    }
    public boolean EstDisponible()
    {
        return getPrix()>0  ;
    }




    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(com.example.projetb2b.Produit.class.getResource("AllProductsList.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1291, 730);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
