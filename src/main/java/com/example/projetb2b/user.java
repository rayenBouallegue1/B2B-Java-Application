package com.example.projetb2b;

/**
 *
 * @author rayen
 */

import java.util.Objects;

public class user {
    private String Login ;
    private String password;
    private  String NumFiscleEntrp ;
    public user(String Login, String password , String NumFiscleEntrp ) {
        this.Login = Login;
        this.password = password;
        this.NumFiscleEntrp = NumFiscleEntrp ;
    }
    public user() {

    }


    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public String getNumFiscleEntrp() {
        return NumFiscleEntrp;
    }

    public void setNumFiscleEntrp(String numFiscleEntrp) {
        NumFiscleEntrp = numFiscleEntrp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final user other = (user) obj;
        if (!Objects.equals(this.Login, other.Login)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }



}

