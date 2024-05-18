package com.example.projetb2b;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AjouterProduitController {

    @FXML
    private TextField libelleField ;

    @FXML
    private TextArea descriptionArea ;
    @FXML
    private TextField PriceField ;
    @FXML
    private TextField CategorieField ;
    @FXML
    private Button ajouterProduitbtn ;
    @FXML
    private Button Resetbtn ;

    public void resetFields()
    {
        libelleField.clear();
        descriptionArea.clear();
        PriceField.clear();
        CategorieField.clear();
    }
    public void addProductToDatabase()
    {   Produit pdt = new Produit(libelleField.getText(),descriptionArea.getText(),Double.parseDouble(PriceField.getText()) ) ;
        try{
            pdt.SaveProduct() ;
        }catch (Exception e)
        {
        e.printStackTrace();
        }
    }


}
