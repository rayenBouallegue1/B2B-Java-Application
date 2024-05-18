package com.example.projetb2b;

public class Reclamation {
    private String Objet ;
    private String Type ;
    private  String Message ;

    public Reclamation(String objet, String type, String message) {
        Objet = objet;
        Type = type;
        Message = message;
    }

    public Reclamation() {
    }

    public String getObjet() {
        return Objet;
    }

    public void setObjet(String objet) {
        Objet = objet;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
