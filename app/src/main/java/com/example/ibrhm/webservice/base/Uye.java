package com.example.ibrhm.webservice.base;

/**
 * Created by ibrhm on 7.02.2017.
 */


public class Uye {
    private String id;
    private String name;
    private String email;
    private String no;

    public Uye(String id,String name, String email, String no)
    {
        this.id=id;
        this.name = name;
        this.email = email;
        this.no = no;
    }
    public String getid(){return this.id;}
    public String getAdSoyad(){return this.name;}
    public String getEmail(){return this.email;}
    public String getUyeID(){return this.no;}
}
