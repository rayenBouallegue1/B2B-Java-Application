package com.example.projetb2b;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.w3c.dom.events.MouseEvent;

public class ChatBotController {
    @FXML
    private TextField textField ;

    @FXML
    private TextArea textArea ;

    @FXML
    private Button button ;


    public void printHello() {
        ChatBot ch = new ChatBot() ;
        String Userinput = textField.getText() ;
        textArea.appendText("User : " + Userinput +"\n");
        textArea.appendText("Hello Gatouss : "+ch.GenerateResponse(Userinput)+"\n");
        textField.clear();

    }

}
