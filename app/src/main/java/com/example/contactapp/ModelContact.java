package com.example.contactapp;

public class ModelContact
{
    private String id, fName, lName, cName, image, pNum, email, addedTime, updatedTime;

    public ModelContact(String id, String fName, String lName, String cName, String image, String pNum, String email, String addedTime, String updatedTime) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.cName = cName;
        this.image = image;
        this.pNum = pNum;
        this.email = email;
        this.addedTime = addedTime;
        this.updatedTime = updatedTime;
    }

    public String getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getcName() {
        return cName;
    }

    public String getImage() {
        return image;
    }

    public String getpNum() {
        return pNum;
    }

    public String getEmail() {
        return email;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }
}
