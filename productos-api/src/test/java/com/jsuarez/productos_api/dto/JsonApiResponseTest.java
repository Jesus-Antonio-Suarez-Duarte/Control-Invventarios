package com.jsuarez.productos_api.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JsonApiResponseTest {
    @Test
    void testSingleResponse() {
        // Given
        String data = "test data";
        
        // When
        JsonApiResponse<String> response = JsonApiResponse.single(data);
        
        // Then
        assertEquals(data, response.getData());
        assertNull(response.getMessage());
        assertNull(response.getMeta());
    }

    @Test
    void testSingleResponseWithMessage() {
        // Given
        String data = "test data";
        String message = "Test message";
        
        // When
        JsonApiResponse<String> response = JsonApiResponse.single(data, message);
        
        // Then
        assertEquals(data, response.getData());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testCollectionResponse() {
        // Given
        List<String> data = Arrays.asList("item1", "item2");
        
        // When
        JsonApiResponse<List<String>> response = JsonApiResponse.collection(data);
        
        // Then
        assertEquals(data, response.getData());
        assertNull(response.getMessage());
    }

    @Test
    void testCollectionResponseWithMeta() {
        // Given
        List<String> data = Arrays.asList("item1", "item2");
        Meta meta = Meta.of(0, 10, 2L, 1, true, true);
        
        // When
        JsonApiResponse<List<String>> response = JsonApiResponse.collection(data, meta);
        
        // Then
        assertEquals(data, response.getData());
        assertEquals(meta, response.getMeta());
    }

    @Test
    void testMessageResponse() {
        // Given
        String message = "Operation completed";
        
        // When
        JsonApiResponse<Object> response = JsonApiResponse.message(message);
        
        // Then
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
    }
}