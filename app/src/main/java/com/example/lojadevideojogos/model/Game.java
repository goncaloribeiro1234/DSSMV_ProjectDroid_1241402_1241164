package com.example.lojadevideojogos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Jogo à venda na loja (coleção "games" no RestDB).
 *
 * @SerializedName liga o nome do campo no JSON (snake_case) ao atributo Java (camelCase).
 * Serializable permite passar o objeto entre Activities através de Intent.
 */
public class Game implements Serializable {

    // Gerado pelo RestDB. No POST fica null e o Gson não o envia.
    @SerializedName("_id")
    private String id;

    @SerializedName("rawg_id")
    private int rawgId;

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("price")
    private double price;

    @SerializedName("stock")
    private int stock;

    @SerializedName("on_sale")
    private boolean onSale;

    // Double (objeto) porque pode ser null quando não há promoção.
    @SerializedName("promo_price")
    private Double promoPrice;

    // Construtor vazio: necessário para o Gson.
    public Game() { }

    public Game(String id, int rawgId, String title, String imageUrl,
                double price, int stock, boolean onSale, Double promoPrice) {
        this.id = id;
        this.rawgId = rawgId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.stock = stock;
        this.onSale = onSale;
        this.promoPrice = promoPrice;
    }

    /** Preço que o cliente realmente paga: o promocional se houver promoção, senão o normal. */
    public double getFinalPrice() {
        if (onSale && promoPrice != null) {
            return promoPrice;
        }
        return price;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getRawgId() { return rawgId; }
    public void setRawgId(int rawgId) { this.rawgId = rawgId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isOnSale() { return onSale; }
    public void setOnSale(boolean onSale) { this.onSale = onSale; }

    public Double getPromoPrice() { return promoPrice; }
    public void setPromoPrice(Double promoPrice) { this.promoPrice = promoPrice; }
}