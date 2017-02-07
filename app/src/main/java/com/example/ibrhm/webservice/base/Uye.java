package com.example.ibrhm.webservice.base;

/**
 * Created by ibrhm on 7.02.2017.
 */


public class Uye {
    private String name;
    private String email;
    private String no;

    public Uye(String adSoyad, String email, String uyeID)
    {
        this.name = adSoyad;
        this.email = email;
        this.no = uyeID;
    }

    public String getAdSoyad(){return this.name;}
    public String getEmail(){return this.email;}
    public String getUyeID(){return this.no;}
}
