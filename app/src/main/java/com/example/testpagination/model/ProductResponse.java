package com.example.testpagination.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductResponse {

    @SerializedName("items")
    public List<ModelProduct> items;

}
