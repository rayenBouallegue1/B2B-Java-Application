package com.example.projetb2b;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductCardController implements Initializable {

    @FXML
    private Label NomLabel;

    @FXML
    private Label PrixLabel;

    @FXML
    private Spinner<Integer> QuantitySlider;

    @FXML
    private  SpinnerValueFactory spin ;
    @FXML
    private Button addBtn;
    @FXML
    private ImageView ImageViewProduct;

    private  Produit produit ;
    private AllProductListController productListController;



    public void setProductListController(AllProductListController controller) {
        this.productListController = controller;
    }
    public void SetData(Produit p )
    {
        NomLabel.setText(p.getLibelleProduit());
        PrixLabel.setText(Double.toString(p.getPrix()));
        produit = p ;
        String imagePath = "C:/Users/rayen/Desktop/TEKUP/produit.jpg" ;
        Image image = new Image("file:" + imagePath);
        ImageViewProduct.setImage(image);
    }






    public void setQueantity()
    {
        spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,0) ;
        QuantitySlider.setValueFactory(spin);
    }

    public void ajouterPanier(){
        DBManager db = new DBManager() ;
        db.SaveCommande(produit, QuantitySlider.getValue() , 9 );
        Panier pan = new Panier(produit.getLibelleProduit(),9,QuantitySlider.getValue(), produit.getPrix()*QuantitySlider.getValue()) ;
        productListController.setMontant(produit.getPrix()*QuantitySlider.getValue());
        productListController.GetTable().getItems().add(pan) ;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    setQueantity();
    }

}
