package com.example.projetb2b;

public class Panier {
    private int id_commande ;
    private  String libelle_produit ;
    private int Id_user ;
    private  int quantite ;
    private  double montant ;

    public Panier(int id_commande, String libelle_produit, int id_user, int quantite, double montant) {
        this.id_commande = id_commande;
        this.libelle_produit = libelle_produit;
        Id_user = id_user;
        this.quantite = quantite;
        this.montant = montant;
    }

    public Panier(String libelle_produit, int id_user, int quantite, double montant) {
        this.libelle_produit = libelle_produit;
        Id_user = id_user;
        this.quantite = quantite;
        this.montant = montant;
    }

    public Panier() {
    }

    public int getId_commande() {
        return id_commande;
    }

    public void setId_commande(int id_commande) {
        this.id_commande = id_commande;
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
