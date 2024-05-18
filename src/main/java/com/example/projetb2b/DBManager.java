package com.example.projetb2b;

import java.sql.*;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DBManager {
    private  Connection connexion;

    public DBManager () {
        try {
            String url = "jdbc:postgresql://localhost:5432/ProjetJava";
            String username = "postgres";
            String password = "admin";
            connexion = DriverManager.getConnection(url, username, password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public  Connection GetConnection(){
        return  this.connexion ;
    }


    public  void closeConnection() {
        if (connexion != null) {
            try {
                connexion.close();
                connexion = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public ArrayList GetAllProduct()
    {   ArrayList <Produit> lesProduits = new ArrayList <> () ;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Produit pdt ;
        try {
            String selectSql = "SELECT * FROM \"product\" ";
            pstmt = this.connexion.prepareStatement(selectSql) ;
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String libelleProduit = resultSet.getString("libelle_produit");
                double prixProduit = resultSet.getDouble("prix_produit");
                String descripitionProduit = resultSet.getString("description_produit");
                int codeProduit = resultSet.getInt("id") ;
                pdt = new Produit(libelleProduit,codeProduit ,descripitionProduit,prixProduit) ;
                lesProduits.add(pdt) ;

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return lesProduits ;
    }
    public ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connexion.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public boolean InsertProduct(Produit produit)
    {

        try {
            Statement statement = connexion.createStatement();
            PreparedStatement pstmt = null;
            String requete  = "insert into \"product\" (libelle_produit, prix_produit , description_produit )VALUES (?,?,?) ";
            pstmt = connexion.prepareStatement(requete) ;
            pstmt.setString(1,produit.getLibelleProduit());
            pstmt.setDouble(2, produit.getPrix());
            pstmt.setString(3,produit.getDescriptionProduit());
            pstmt.executeUpdate() ;
            return  true ;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false ;
        }

    }

    public ArrayList GetAllUsers()
    {   ArrayList <user> users = new ArrayList <user> () ;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        user User ;
        try {
            String selectSql = "SELECT * FROM \"Users\" ";
            pstmt = this.connexion.prepareStatement(selectSql) ;
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String mdp = resultSet.getString("mot_de_passe");
                String NumFiscEntrp = resultSet.getString("NumFiscEntrp");
                 User=new user(login,mdp, NumFiscEntrp) ;
                users.add(User) ;

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users ;
    }
    public boolean executeUpdate(String query) {
        int rowsAffected = 0;
        try {
            Statement statement = connexion.createStatement();
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return rowsAffected>0 ;
    }

    public void SaveCommande(Produit pdt, int quantite, int id_client) {
        String sqlCheck = "SELECT * FROM \"panier\" WHERE libelle_produit = ? AND \"Id_client\" = ?";
        String sqlInsert = "INSERT INTO \"panier\"(libelle_produit, \"Id_client\", quantite, montant) VALUES (?, ?, ?, ?)";
        String sqlUpdate = "UPDATE \"panier\" SET quantite = ?, montant = ? WHERE libelle_produit = ? AND \"Id_client\" = ?";

        try (
                PreparedStatement checkStatement = connexion.prepareStatement(sqlCheck);
                PreparedStatement insertStatement = connexion.prepareStatement(sqlInsert);
                PreparedStatement updateStatement = connexion.prepareStatement(sqlUpdate);
        ) {
            // Vérifier si le produit existe déjà dans le panier
            checkStatement.setString(1, pdt.getLibelleProduit());
            checkStatement.setInt(2, id_client);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Le produit existe déjà, mise à jour de la quantité et du montant
                int existingQuantity = resultSet.getInt("quantite");
                double existingMontant = resultSet.getDouble("montant");
                int newQuantity = existingQuantity + quantite;
                double newMontant = existingMontant + (quantite * pdt.getPrix());

                updateStatement.setInt(1, newQuantity);
                updateStatement.setDouble(2, newMontant);
                updateStatement.setString(3, pdt.getLibelleProduit());
                updateStatement.setInt(4, id_client);
                updateStatement.executeUpdate();
            } else {
                // Le produit n'existe pas encore, ajout d'une nouvelle ligne
                insertStatement.setString(1, pdt.getLibelleProduit());
                insertStatement.setInt(2, id_client);
                insertStatement.setInt(3, quantite);
                insertStatement.setDouble(4, (quantite * pdt.getPrix()));
                insertStatement.executeUpdate();
            }

            System.out.println("La commande a été insérée ou mise à jour avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion ou de la mise à jour de la commande : " + e.getMessage());
        }
    }


    public void ValidateCmmande(Panier panier)
    {
        String sql = "INSERT INTO Commande(libelle_produit, Id_user, quantite, montant) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setString(1, panier.getLibelle_produit());
            statement.setInt(2, panier.getId_user());
            statement.setInt(3, panier.getQuantite());
            statement.setDouble(4, panier.getMontant());
            statement.executeUpdate();
            System.out.println("Le panier a été enregistré avec succès dans la table Commande.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du panier dans la table Commande : " + e.getMessage());
        }
    }


    public ObservableList<Panier> GetAllCommands(int idUtilisateur)
    {

        ObservableList<Panier> panier = FXCollections.observableArrayList();
        // Déclaration de la requête SQL
        String sql = "SELECT \"Id_Commande\", libelle_produit, \"Id_client\", quantite, montant FROM public.\"panier\" WHERE \"Id_client\" = ?";

        try (

                PreparedStatement statement = connexion.prepareStatement(sql);
        ) {
            // Attribution des valeurs aux paramètres de la requête
            statement.setInt(1, idUtilisateur);

            // Exécution de la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcours des résultats et ajout des commandes à la liste observable
                while (resultSet.next()) {
                    int idCommande = resultSet.getInt("Id_Commande");
                    String libelleProduit = resultSet.getString("libelle_produit");
                    int idClient = resultSet.getInt("Id_client");
                    int quantite = resultSet.getInt("quantite");
                    double montant = resultSet.getDouble("montant");

                    Panier commande = new Panier( idCommande ,libelleProduit ,idClient ,  quantite, montant);
                    panier.add(commande);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panier ;
    }

    public void enregistrerClient(Client client) {

        try {

            String query = "INSERT INTO \"Client\" (\"Num_Identification_Fiscale\", \"Nom_entreprise\", \"email\", \"pays\", \"adresse\") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connexion.prepareStatement(query);
            statement.setString(1, client.getNum_identification_fiscale());
            statement.setString(2, client.getNom_entrep());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPays());
            statement.setString(5, client.getAdresse());
            statement.executeUpdate();
            System.out.println("Client enregistré avec succès dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement du client dans la base de données : " + e.getMessage());
        }
    }
    public void miseAJourClient(Client client) {

        try {
            String query = "UPDATE \"Client\" SET \"Nom_entreprise\" = ?, \"email\" = ?, \"pays\" = ?, \"adresse\" = ? WHERE \"Num_Identification_Fiscale\" = ?";
            PreparedStatement statement = connexion.prepareStatement(query);
            statement.setString(1, client.getNom_entrep());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPays());
            statement.setString(4, client.getAdresse());
            statement.setString(5, client.getNum_identification_fiscale());
            statement.executeUpdate();
            System.out.println("Informations du client mises à jour avec succès dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des informations du client dans la base de données : " + e.getMessage());
        }
    }

    public Client getClientByNumIdentification(String numIdentification) {
        Client client = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            String query = "SELECT * FROM \"Client\" WHERE \"Num_Identification_Fiscale\" = ?";
            statement = connexion.prepareStatement(query);
            statement.setString(1, numIdentification);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                client = new Client();
                client.setNum_identification_fiscale(resultSet.getString("num_identification_fiscale"));
                client.setNom_entrep(resultSet.getString("Nom_entreprise"));
                client.setEmail(resultSet.getString("email"));
                client.setPays(resultSet.getString("pays"));
                client.setAdresse(resultSet.getString("Adresse"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client depuis la base de données : " + e.getMessage());
        }

        return client;
    }


    public void UpdatePassword(String NumIdentifFiscal , String NewPassword) {

            String sql = "UPDATE \"Users\" SET \"mot_de_passe\" = ? WHERE \"NumFiscEntrp\" = ?";

            try {
                PreparedStatement statement = connexion.prepareStatement(sql) ;
                SystemeConnexion sc = new SystemeConnexion() ;
                // Définir les valeurs des paramètres dans la requête SQL
                statement.setString(1, sc.DoHashing( NewPassword));
                statement.setString(2,NumIdentifFiscal);

                // Exécuter la requête de mise à jour
                int rowsAffected = statement.executeUpdate();
            } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde de l'utilisateur : " + e.getMessage());
        }
    }
    public void SupprimeCompte(String NumIdentifFiscal)
    {
        String sql = "DELETE FROM \"Users\" WHERE  \"NumFiscEntrp\" = ?";
        String sql2 = "DELETE FROM \"Client\" WHERE  \"Num_Identification_Fiscale\" = ?";

        try {
            PreparedStatement statement = connexion.prepareStatement(sql) ;
            PreparedStatement statement2 = connexion.prepareStatement(sql2) ;

            statement.setString(1, NumIdentifFiscal);
            statement2.setString(1, NumIdentifFiscal);
             statement.executeUpdate();
             statement2.executeUpdate();

        }
        catch (SQLException e) {
        System.err.println("Error deleting user account: " + e.getMessage());
    }

}

public  void EnregistrerReclamation(String type, String objet , String message){
    String sql = "INSERT INTO \"Reclamation\" (\"type\", \"objet\", \"Message\") VALUES (?, ?, ?)";
    try {
        PreparedStatement statement = connexion.prepareStatement(sql)  ;
        statement.setString(1, type);
        statement.setString(2, objet);
        statement.setString(3, message);
        statement.executeUpdate();
    }catch (Exception e)
    {
        e.printStackTrace();
    }


}

    public ArrayList GetAllReclamation()
    {   ArrayList <Reclamation> lesReclamation = new ArrayList <> () ;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Reclamation rec ;
        try {
            String selectSql = "SELECT * FROM \"Reclamation\" ";
            pstmt = this.connexion.prepareStatement(selectSql) ;
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String TypeReclamation = resultSet.getString("type");
                String ObjetReclamation = resultSet.getString("objet");
                String messageReclamation = resultSet.getString("Message");
                int codeProduit = resultSet.getInt("id") ;
                rec = new Reclamation(ObjetReclamation,TypeReclamation,messageReclamation) ;
                lesReclamation.add(rec) ;

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return lesReclamation ;
    }

    public int NombreClients()
    {
        int numberOfRows = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM \"Client\"";
        try{

        statement = connexion.prepareStatement(query);
        resultSet = statement.executeQuery();

        // Récupérer le résultat
        if (resultSet.next()) {
            numberOfRows = resultSet.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
        return  numberOfRows ;
    }
    public int NombreReclamation()
    {
        int numberOfRows = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM \"Reclamation\"";
        try{

            statement = connexion.prepareStatement(query);
            resultSet = statement.executeQuery();

            // Récupérer le résultat
            if (resultSet.next()) {
                numberOfRows = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  numberOfRows ;
    }

    public int NombreProduit()
    {
        int numberOfRows = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT COUNT(*) FROM \"product\"";
        try{

            statement = connexion.prepareStatement(query);
            resultSet = statement.executeQuery();

            // Récupérer le résultat
            if (resultSet.next()) {
                numberOfRows = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  numberOfRows ;
    }

    public void updateProduct(String libelle, double nouveauPrix, String nouvelleDescription) {
        PreparedStatement statement = null;

        try {

            String sql = "UPDATE product SET prix_produit = ?, description_produit = ? WHERE libelle_produit = ?";

            statement = connexion.prepareStatement(sql);

            statement.setDouble(1, nouveauPrix);
            statement.setString(2, nouvelleDescription);
            statement.setString(3, libelle);

            statement.executeUpdate();
            System.out.println("Produit mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }


}

