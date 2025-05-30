package com.jsuarez.productos_api.dto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class MetaTest {
	   @Test
	    void testMetaCreation() {
	        // Given & When
	        Meta meta = Meta.of(0, 10, 25L, 3, true, false);
	        
	        // Then
	        assertEquals(0, meta.getPage());
	        assertEquals(10, meta.getSize());
	        assertEquals(25L, meta.getTotalElements());
	        assertEquals(3, meta.getTotalPages());
	        assertTrue(meta.isFirst());
	        assertFalse(meta.isLast());
	    }

	    @Test
	    void testMetaGettersAndSetters() {
	        // Given
	        Meta meta = new Meta();
	        
	        // When
	        meta.setPage(1);
	        meta.setSize(20);
	        meta.setTotalElements(50L);
	        meta.setTotalPages(3);
	        meta.setFirst(false);
	        meta.setLast(false);
	        
	        // Then
	        assertEquals(1, meta.getPage());
	        assertEquals(20, meta.getSize());
	        assertEquals(50L, meta.getTotalElements());
	        assertEquals(3, meta.getTotalPages());
	        assertFalse(meta.isFirst());
	        assertFalse(meta.isLast());
	    }
	}
