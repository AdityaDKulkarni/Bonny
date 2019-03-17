package com.bonny.bonnyparent.models;

/**
 * @author Aditya Kulkarni
 */

public class UserModel {
    private String last_name,detail;

    public String getLast_name(){
        return last_name;
    }

    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    private Integer pk;

    public Integer getPk(){
        return pk;
    }

    public void setPk(Integer pk){
        this.pk = pk;
    }

    private String first_name;

    public String getFirst_name(){
        return first_name;
    }

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    private String email;

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    private String username;

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getDetail() {
        return detail;
    }

}
