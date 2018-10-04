package com.example.oronz.mobiclientapp;

public class MobiClientApplication extends android.app.Application {


    String user_name,hash_key,api_key;
    String _Clerk_password,_Clerk_username;

    public MobiClientApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setHash_key(String hash_key) {
        this.hash_key = hash_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getHash_key() {
        return hash_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public void set_Clerk_password(String _Clerk_password) {
        this._Clerk_password = _Clerk_password;
    }

    public void set_Clerk_username(String _Clerk_username) {
        this._Clerk_username = _Clerk_username;
    }

    public String get_Clerk_password() {
        return _Clerk_password;
    }

    public String get_Clerk_username() {
        return _Clerk_username;
    }
}
