package com.jsuarez.inventario_api.client;

import com.jsuarez.inventario_api.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductoClient {

    private final RestTemplate restTemplate;

    @Value("${microservicio.productos.url:http://localhost:8080}")
    private String productosBaseUrl;

    @Value("${api.key}")
    private String apiKey;

    /**
     * Obtiene un producto por su ID desde el microservicio de productos
     * 
     * @param productoId ID del producto
     * @return Optional con el producto si existe
     */
    public Optional<ProductoSimple> obtenerProductoPorId(Long productoId) {
        log.debug("Consultando producto ID: {} en microservicio de productos", productoId);
        
        try {
            String url = productosBaseUrl + "/api/v1/productos/" + productoId;
            
            // Configurar headers con API Key
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-Key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            log.debug("Realizando petición GET a: {}", url);
            
            // Usar ProductoIndividualResponse para productos individuales
            ResponseEntity<ProductoIndividualResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                ProductoIndividualResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ProductoIndividualResponse individualResponse = response.getBody();
                
                if (individualResponse.getData() != null) {
                    ProductoSimple producto = new ProductoSimple(individualResponse.getData());
                    log.debug("Producto encontrado: ID={}, Nombre={}, Precio={}", 
                             producto.getId(), producto.getNombre(), producto.getPrecio());
                    return Optional.of(producto);
                }
            }
            
            log.warn("Producto no encontrado en la respuesta del microservicio - ID: {}", productoId);
            return Optional.empty();
            
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Producto no encontrado - ID: {}", productoId);
            return Optional.empty();
            
        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al consultar producto ID {}: {} - {}", 
                     productoId, e.getStatusCode(), e.getMessage());
            throw new RuntimeException("Error al comunicarse con el microservicio de productos: " + e.getMessage());
            
        } catch (ResourceAccessException e) {
            log.error("❌ Error de conectividad al consultar producto ID {}: {}", productoId, e.getMessage());
            System.out.println("❌ No se puede conectar con el microservicio de productos - Verifique que esté ejecutándose");
            throw new RuntimeException("No se pudo conectar con el microservicio de productos. Verifique que esté ejecutándose.");
            
        } catch (Exception e) {
            log.error("❌ Error inesperado al consultar producto ID {}: {}", productoId, e.getMessage(), e);
            System.out.println("❌ Error interno al comunicarse con microservicio de productos");
            throw new RuntimeException("Error interno al consultar producto: " + e.getMessage());
        }
    }

    /**
     * Verifica si un producto existe en el microservicio de productos
     * 
     * @param productoId ID del producto
     * @return true si el producto existe, false en caso contrario
     */
    public boolean existeProducto(Long productoId) {
        log.debug("Verificando existencia del producto ID: {}", productoId);
        return obtenerProductoPorId(productoId).isPresent();
    }
}