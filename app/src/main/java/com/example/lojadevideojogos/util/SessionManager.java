package com.example.lojadevideojogos.util;

public class SessionManager {
    private static SessionManager instance;

    public enum Profile {
        CLIENTE, GESTOR
    }

    private Profile currentProfile;

    private SessionManager() {
        this.currentProfile = Profile.CLIENTE;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setProfile(Profile profile) {
        this.currentProfile = profile;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public boolean isManager() {
        return currentProfile == Profile.GESTOR;
    }
}