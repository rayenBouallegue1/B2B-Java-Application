package com.example.projetb2b;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap ;
import edu.stanford.nlp.util.Interner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.commons.text.similarity.JaroWinklerDistance ;

import java.sql.Connection;
import java.sql.ResultSet;


public class ChatBot extends Application {
    List<String> productNames = Arrays.asList("lenovo", "dell", "hp pavillon");
    private StanfordCoreNLP pipeline;



    public ChatBot()
    {
        // Initialize StanfordNLP pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner , parse ,sentiment , depparse");
        pipeline = new StanfordCoreNLP(props);

    }

    public String processQuery(String query) {
        // Create an Annotation with the user's query
        Annotation document = new Annotation(query);
        CoreDocument document2 = new CoreDocument(query);
        pipeline.annotate(document2);

        // Run all the selected annotators on this text
        pipeline.annotate(document);
        Annotation annotation = new Annotation(query);
        pipeline.annotate(annotation);

        // Extract relevant information from the annotated document
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        List<CoreMap> Sentences2 = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        for( CoreMap sentence : Sentences2 )
        {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class) ;
            System.out.println("Sentence : "+sentence+"Sentiment"+sentiment);
            System.out.println(dependencies);
        }
        for (CoreMap sentence : sentences) {
            // Tokenize
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            System.out.println(tokens);
            for (CoreLabel token : tokens) {
                String namedEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String productName = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class) ;
                String lemma = token.lemma() ;
                System.out.println(lemma);
            }
            //System.out.println(tokens);
        }
        return "I'm sorry, I couldn't understand your query.";
    }

    public Map<String,Long>getAllKnownWords() {
        DBManager db = new DBManager() ;
        Connection connexion = db.GetConnection() ;
        String sqlQuery = "SELECT word,count FROM public.\"charBotWords\"";
        ResultSet resultSet =  db.executeQuery(sqlQuery) ;
        String Word = "" ;
        ArrayList <Produit> AllProducts  ;
        AllProducts = db.GetAllProduct() ;

        Map<String,Long> wordMap = new HashMap<String,Long>() ;
        try {
            while(resultSet.next()){
                wordMap.put(resultSet.getString("word"),resultSet.getLong("count")) ;
            }
        }

        catch (SQLException e){
            e.printStackTrace();
        }
        for(Produit pdt : AllProducts){
            wordMap.put(pdt.getLibelleProduit(),100000000L) ;
        }
        return  wordMap ;

    }
    public String ClosestWord(String Wrong , Map<String, Long> ListeDesMots)
    {
        int MaxDistance  = 2 ;
        String MotCorrige = "" ;
        int distance = 0 ;
        Long frequence = 0L ;
        //Map<String , Long> CloseWords = new HashMap<String , Long>() ;
        String ClosestWord = "" ;
        if (ListeDesMots.containsKey(Wrong))
            return Wrong ;
        for(String key : ListeDesMots.keySet() )
        {
            distance = LevenshteinDistance.getDefaultInstance().apply(Wrong.toLowerCase(),key);

            if (distance < MaxDistance && ListeDesMots.get(key) > frequence)
            {   System.out.println(key);

                System.out.println(ClosestWord);
                MotCorrige  = key ;
                frequence = ListeDesMots.get(key) ;
            }

        }
        if ( ! MotCorrige.equals(""))
        {
            return MotCorrige ;
        }
        else return Wrong ;
    }
    public String CorrigeVocab(String UserInput)
    {

        Map<String , Long > WordMap= new HashMap<String , Long>() ;
        WordMap = getAllKnownWords()  ;
        String [] UserWords = UserInput.split(" ") ;
        StringBuilder CorrectedInput = new StringBuilder() ;
        for (int i = 0 ; i<UserWords.length ; i++ )
        {   System.out.println(UserWords[i]);
            CorrectedInput.append(ClosestWord(UserWords[i], WordMap)+" ")  ;
        }
        return  CorrectedInput.toString() ;

    }

    public  boolean IsGreeting(String text){
        List<String> greetings = Arrays.asList("hello", "hi", "hey", "cc","salut" ,"good morning", "good afternoon", "good evening");
        for(String greeting : greetings)
        {
            if (text.toLowerCase().contains(greeting))
                return true;
        }
        return false ;
    }

    public boolean isAskingAboutBot(String query) {
        // Convert the query to lowercase for case-insensitive matching


        // Define keywords indicating the user is asking about the bot
        List<String> botKeywords = Arrays.asList("how are you", "how's it going", "how are things", "how do you do", "how's everything", "what's up");
        // Check if the query contains any of the bot keywords
        for (String keyword : botKeywords) {
            if (query.toLowerCase().equals(keyword)) {
                return true;
            }
        }
        return false;
    }
    private boolean containsInsult(String userInput) {
        List<String> ListeDesInsultes = Arrays.asList("Stupid","bot" , "Idiot","Fool","Imbecile","Moron","Dumb" , "Loser","Bastard") ;
        int ScoreInsulte = 0  ;
        if (userInput.toLowerCase().contains("you are"))
            ScoreInsulte+=3 ;
        for(String insul : ListeDesInsultes)
        {
            if (userInput.toLowerCase().contains(insul))
                ScoreInsulte+=1 ;
        }
        if (ScoreInsulte ==0)
            return false ;
        if (ScoreInsulte>1)
            return true ;
        return false ;
    }


    public String extractLibelleProduit(String userInput)
    {
        String [] UserWords = userInput.split(" ") ;
        DBManager db = new DBManager() ;
        ArrayList <Produit> AllProducts  ;
        AllProducts = db.GetAllProduct() ;
        for (Produit produit : AllProducts)
        {
            if ( userInput.contains(produit.getLibelleProduit()))
            {
                return produit.getLibelleProduit() ;
            }
        }
        return  null ;
    }
    public Produit SearchForProduct(String Libelle)
    {
        DBManager db = new DBManager() ;
        ArrayList <Produit> AllProducts  ;
        AllProducts = db.GetAllProduct() ;
        for (Produit prod : AllProducts)
        {
            if(prod.getLibelleProduit().equals(Libelle))
            {
                return  prod ;
            }
        }
        return null ;

    }

    public Boolean isAskingAbtProduct(String userInput)
    {      int score = 0 ;
        List<String> ListeDesMots = Arrays.asList("how", "much","price","prix","product","cost","price tag" ,"what is") ;
        String [] UserWords = userInput.split(" ") ;
        for(String mot : UserWords)
        {
            if (ListeDesMots.contains(mot))
                score+=1 ;
        }
        return score >= 2 ;
    }

    public String GenerateResponse(String Query)
    {
        String response  ="" ;
        String CorrectUserInout = "" ;
        CorrectUserInout = CorrigeVocab(Query) ;
        if (IsGreeting(CorrectUserInout)){
            response+="hello , i hope you are doing well !"  ;
        }
        else {
            if(isAskingAboutBot(CorrectUserInout))
                response="I'm just a chatbot that can help u in some question about our company or our products ! i dont have feelings and moods " ;
        }

        if (isAskingAbtProduct(CorrectUserInout))
        {
            String libelleProduit = extractLibelleProduit(CorrectUserInout) ;
            Produit pdt = SearchForProduct(libelleProduit) ;
            if (pdt != null )
            {
                response+= "the product : " +pdt.getLibelleProduit() + " Is now available for only  "+pdt.getPrix()+"TND" ;
            }
            else {
                response+= "This product is not available . Make sure that you are writting correctly the product name ! Can i help you in any thing else " ;
            }
        }
        if (containsInsult(CorrectUserInout))
        {
            response+= "Im sorry to make you feel like that . im just a simple chatbot made using Machine learning and nlp techniques to help you have a bettre experience with our platform , \n you can ask me things aabout our products about the company or things like that ! ";
        }

        return response+"\n" ;
    }


    public void EnregistrerFrequenceBase(){
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        Connection connection = null;
        ResultSet resultSet = null;
        DBManager db = new DBManager() ;
        try {
            connection = db.GetConnection() ;

            String sql = "UPDATE \"ChatbotWords\" SET \"frequence\" = ? WHERE word = ?";

            // Prepare the statement
            pstmt2 = connection.prepareStatement(sql);
            // SQL query to select words and frequencies from interm table
            String selectSql = "SELECT \"Id\" , \"word\", \"frequence\" FROM \"intermid\"";

            // Execute the query to get words and frequencies from interm table
            pstmt = connection.prepareStatement(selectSql);
            resultSet = pstmt.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                String word = resultSet.getString("word");
                String frequencyStr = resultSet.getString("frequence");
                frequencyStr = frequencyStr.replaceAll("\\s", "");

                int frequency = Integer.parseInt(frequencyStr);

                //System.out.println(word);
                //System.out.println(frequency);
                // Update the frequency in chatBotWords table
                pstmt2.setInt(1, frequency);
                pstmt2.setString(2, word);
                pstmt2.executeUpdate();

            }
            System.out.println("Modification des frequance avec succées");
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


  /*  public static void main(String[] args) {
        ChatBot ch = new ChatBot() ;
        Scanner sc = new Scanner(System.in) ;
        String userInput ;
        System.out.println("Welcome To the chatbot HOUHOU");
        userInput = sc.nextLine() ;
        String CorrectedInput =  ch.CorrigeVocab(userInput) ;
        System.out.println(CorrectedInput);
        System.out.println(ch.GenerateResponse(CorrectedInput));
    }
*/


        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(com.example.projetb2b.ChatBot.class.getResource("ChatBot.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 610, 410);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }

