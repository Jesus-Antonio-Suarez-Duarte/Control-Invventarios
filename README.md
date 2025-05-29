# Control de Inventarios - Microservicios Challenge

## Descripción
Sistema de microservicios para gestión de productos e inventario siguiendo el estándar JSON API.

## Tecnologías
- Java 21
- Spring Boot 3.5
- Maven
- Docker
- JSON API Standard

## Arquitectura del Sistema

### productos-api
Microservicio encargado de la gestión CRUD de productos.
├── config/          # Configuraciones (CORS, timeout, retry)
├── controller/      # REST Controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities
├── exception/      # Custom Exceptions y handlers
├── mapper/         # Mappers Entity <-> DTO
├── repository/     # JPA Repositories
├── service/        # Business Logic interfaces
│   └── impl/       # Implementaciones de services
└── util/           # Utilidades generales
### inventario-api
Microservicio encargado de la gestión de inventario con comunicación a productos-api.
├── config/          # Configuraciones
├── controller/      # REST Controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities
├── exception/      # Custom Exceptions
├── mapper/         # Mappers
├── repository/     # JPA Repositories
├── service/        # Business Logic interfaces
│   └── impl/       # Implementaciones
├── client/         # Comunicación con productos-api
│   └── impl/       # Implementación con timeout/retry
└── util/           # Utilidades
## Estado del Proyecto
🚧 En desarrollo - Estructura básica creada
