package com.example.lojadevideojogos.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RawgSearchResponse {
    @SerializedName("count")
    private int count;

    @SerializedName("results")
    private List<RawgGame> results;

    public List<RawgGame> getResults() { return results; }
}