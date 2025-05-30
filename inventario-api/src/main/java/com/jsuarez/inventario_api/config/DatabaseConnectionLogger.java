package com.jsuarez.inventario_api.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseConnectionLogger {

    @EventListener(ApplicationReadyEvent.class)
    public void logDatabaseConnection() {
        log.info("✅ Aplicación iniciada correctamente");
        log.info("🔗 Conexión a base de datos Oracle establecida");
         }
}