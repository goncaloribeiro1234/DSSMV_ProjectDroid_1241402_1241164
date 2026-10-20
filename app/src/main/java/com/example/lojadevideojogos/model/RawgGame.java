package com.example.lojadevideojogos.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RawgGame {
    private int id;
    private String name;
    private String released;
    private double rating;

    @SerializedName("background_image")
    private String backgroundImage;

    // As plataformas vêm dentro de um array de objetos, que por sua vez têm um objeto 'platform'
    private List<PlatformWrapper> platforms;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getReleased() { return released; }
    public double getRating() { return rating; }
    public String getBackgroundImage() { return backgroundImage; }
    public List<PlatformWrapper> getPlatforms() { return platforms; }

    // Classes aninhadas para mapear o JSON complexo das plataformas
    public static class PlatformWrapper {
        private Platform platform;
        public Platform getPlatform() { return platform; }
    }

    public static class Platform {
        private int id;
        private String name;
        public String getName() { return name; }
    }
}