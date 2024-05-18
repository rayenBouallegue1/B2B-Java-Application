package com.example.projetb2b;

public class Client extends user{
    private  String num_identification_fiscale ;
    private  String Nom_entrep ;
    private String email ;
    private  String pays ;
    private  String Adresse  ;

    public Client(String num_identification_fiscale, String nom_entrep, String email, String pays, String adresse , String login , String mdp ) {
        super(login,mdp,num_identification_fiscale) ;
        this.num_identification_fiscale = num_identification_fiscale;
        Nom_entrep = nom_entrep;
        this.email = email;
        this.pays = pays;
        Adresse = adresse;

    }

    public Client() {
    }

    public void EnregistrerClientEnBase()
    {
        DBManager db = new DBManager() ;
        db.enregistrerClient(this);

    }
    public void miseAJourClient() {
        DBManager dbManager = new DBManager();
        dbManager.miseAJourClient(this);
    }

    public String getNum_identification_fiscale() {
        return num_identification_fiscale;
    }

    public void setNum_identification_fiscale(String num_identification_fiscale) {
        this.num_identification_fiscale = num_identification_fiscale;
    }

    public String getNom_entrep() {
        return Nom_entrep;
    }

    public void setNom_entrep(String nom_entrep) {
        Nom_entrep = nom_entrep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }
}
