package com.jsuarez.productos_api.dto;

import lombok.Data;
import java.util.List;


@Data
public class JsonApiResponse<T> {
    private T data;
    private String message; // Para mensajes como "actualizado", "eliminado"
    private Meta meta; // Para paginación
    
    public static <T> JsonApiResponse<T> single(T data) {
        JsonApiResponse<T> response = new JsonApiResponse<>();
        response.setData(data);
        return response;
    }
    
    public static <T> JsonApiResponse<T> single(T data, String message) {
        JsonApiResponse<T> response = new JsonApiResponse<>();
        response.setData(data);
        response.setMessage(message);
        return response;
    }
    
    public static <T> JsonApiResponse<List<T>> collection(List<T> data) {
        JsonApiResponse<List<T>> response = new JsonApiResponse<>();
        response.setData(data);
        return response;
    }
    
    public static <T> JsonApiResponse<List<T>> collection(List<T> data, Meta meta) {
        JsonApiResponse<List<T>> response = new JsonApiResponse<>();
        response.setData(data);
        response.setMeta(meta);
        return response;
    }
    
    public static JsonApiResponse<Object> message(String message) {
        JsonApiResponse<Object> response = new JsonApiResponse<>();
        response.setMessage(message);
        return response;
    }
}