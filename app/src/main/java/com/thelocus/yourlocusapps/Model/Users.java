package com.thelocus.yourlocusapps.Model;

public class Users
{
    private String ID, Name, Password;

    public Users()
    {

    }

    public Users(String ID, String name, String password) {
        this.ID = ID;
        Name = name;
        Password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
