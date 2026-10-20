package com.example.lojadevideojogos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Pedido de um cliente para adicionar um jogo à loja (coleção "requests" no RestDB).
 * Fluxo: nasce como PENDING; o gestor passa-o a APPROVED ou REJECTED.
 */
public class GameRequest implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("rawg_id")
    private int rawgId;

    @SerializedName("title")
    private String title;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("status")
    private RequestStatus status;

    // Guardada como texto (ex.: "2026-10-14") para simplificar.
    @SerializedName("requested_date")
    private String requestedDate;

    public GameRequest() { }

    public GameRequest(String id, int rawgId, String title, String imageUrl,
                       RequestStatus status, String requestedDate) {
        this.id = id;
        this.rawgId = rawgId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.status = status;
        this.requestedDate = requestedDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getRawgId() { return rawgId; }
    public void setRawgId(int rawgId) { this.rawgId = rawgId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getRequestedDate() { return requestedDate; }
    public void setRequestedDate(String requestedDate) { this.requestedDate = requestedDate; }
}