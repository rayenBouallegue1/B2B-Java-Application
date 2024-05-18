package com.example.projetb2b;

import java.util.ArrayList;

public class Commande {
    private int id_commande ;
    private  String libelle_produit ;
    private int Id_user ;
    private  int quantite ;
    private  double montant ;

    public Commande() {
    }

    public Commande(String libelle_produit, int id_user, int Code_commande ,int quantite, double montant) {
        this.libelle_produit = libelle_produit;
        Id_user = id_user;
        this.quantite = quantite;
        this.montant = montant;
        this.id_commande=Code_commande;
    }
    public Commande(String libelle_produit, int id_user ,int quantite, double montant) {
        this.libelle_produit = libelle_produit;
        Id_user = id_user;
        this.quantite = quantite;
        this.montant = montant;
    }

    public String getLibelle_produit() {
        return libelle_produit;
    }

    public void setLibelle_produit(String libelle_produit) {
        this.libelle_produit = libelle_produit;
    }

    public int getId_user() {
        return Id_user;
    }

    public void setId_user(int id_user) {
        Id_user = id_user;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
}
