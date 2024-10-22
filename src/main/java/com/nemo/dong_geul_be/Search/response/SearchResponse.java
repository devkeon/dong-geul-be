package com.nemo.dong_geul_be.Search.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse<T> {
    private List<T> data;
    private String message;

    public SearchResponse(String message) {
        this.message = message;
    }
}
