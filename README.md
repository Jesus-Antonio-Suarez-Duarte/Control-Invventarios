# Sistema de Gestión de Productos e Inventario - Microservicios

Este proyecto implementa un sistema de microservicios para la gestión de productos e inventario, siguiendo los estándares JSON:API para la comunicación entre servicios y buenas prácticas de desarrollo.

## 🎯 Descripción del Proyecto

El sistema está compuesto por dos microservicios independientes que trabajan de forma coordinada:

1. **🛍️ Servicio de Productos (puerto 8080)**: Gestiona el catálogo de productos con operaciones CRUD completas.
2. **📦 Servicio de Inventario (puerto 8082)**: Gestiona el inventario de productos con comunicación en tiempo real al servicio de productos.

### ✨ Funcionalidades Principales

#### Servicio de Productos:
- ✅ Gestión completa de productos (CRUD)
- 📄 Paginación avanzada
- 🔍 Filtros y búsquedas
- 📊 Respuestas en formato JSON:API

#### Servicio de Inventario:
- ✅ Consulta de inventario con información detallada de productos
- 🛒 Procesamiento de compras con validación de stock
- 📈 Actualización de cantidades en tiempo real
- 🎯 Validación de existencia de productos antes de crear inventario
- 📊 Estados de stock automáticos (STOCK_BAJO, STOCK_MEDIO, STOCK_ALTO)
- 💰 Cálculo automático de valor total del inventario
- 🔄 Eventos de inventario en consola con emojis
- 🌐 Comunicación HTTP con el servicio de productos
- ⚡ Manejo robusto de errores de conectividad

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 21, Spring Boot 3.5.0
- **Base de Datos**: Oracle Database 21c, H2 (para pruebas)
- **Persistencia**: Spring Data JPA, Hibernate
- **Documentación**: Swagger/OpenAPI 3 (SpringDoc)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Cobertura**: JaCoCo
- **Validación**: Bean Validation (Hibernate Validator)
- **Build**: Maven
- **Contenedores**: Docker y Docker Compose

## 📋 Requisitos Previos

- **JDK 21** o superior
- **Maven 3.8+**
- **Oracle Database 21c** (para desarrollo/producción)
- **Docker y Docker Compose** (opcional, para despliegue con contenedores)

## 🏗️ Estructura del Proyecto

```
sistema-inventario/
├── productos-api/                    # Microservicio de Productos
│   ├── src/main/java/
│   │   ├── config/                   # Configuraciones (CORS, timeout, retry)
│   │   ├── controller/               # REST Controllers
│   │   ├── dto/                      # Data Transfer Objects
│   │   ├── entity/                   # JPA Entities
│   │   ├── exception/                # Custom Exceptions y handlers
│   │   ├── mapper/                   # Mappers Entity <-> DTO
│   │   ├── repository/               # JPA Repositories
│   │   ├── service/                  # Business Logic interfaces
│   │   │   └── impl/                 # Implementaciones de services
│   │   └── util/                     # Utilidades generales
│   ├── src/test/java/                # Tests unitarios e integración
│   ├── Dockerfile
│   └── pom.xml
│
├── inventario-api/                   # Microservicio de Inventario
│   ├── src/main/java/
│   │   ├── client/                   # Comunicación HTTP con productos-api
│   │   ├── config/                   # Configuraciones (RestTemplate, timeouts)
│   │   ├── controller/               # REST Controllers con JSON:API
│   │   ├── dto/                      # DTOs (JsonApiResponse, CompraResponse, etc.)
│   │   ├── entity/                   # JPA Entities
│   │   ├── repository/               # JPA Repositories
│   │   ├── service/                  # Business Logic interfaces
│   │   │   └── impl/                 # Implementaciones con comunicación
│   │   └── exception/                # Custom Exceptions
│   ├── src/test/java/                # Tests completos (Entity, DTO, Service, Controller)
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml               # Orquestación de servicios
└── README.md
```

## ⚙️ Configuración y Ejecución

### Configuración de Base de Datos

#### Oracle Database (Desarrollo/Producción):
```properties
# application.properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=JASD
spring.datasource.password=1234
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
```

#### H2 Database (Testing):
```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

### 🚀 Ejecución Local con Maven

1. **Clonar el repositorio:**
   ```bash
   git clone <tu-repositorio>
   cd sistema-inventario
   ```

2. **Compilar los proyectos:**
   ```bash
   # Servicio de Productos
   cd productos-api
   mvn clean package
   
   # Servicio de Inventario
   cd ../inventario-api
   mvn clean package
   ```

3. **Ejecutar los servicios** (en terminales separadas):
   ```bash
   # Terminal 1 - Servicio de Productos
   cd productos-api
   mvn spring-boot:run
   # Disponible en: http://localhost:8080
   
   # Terminal 2 - Servicio de Inventario
   cd inventario-api
   mvn spring-boot:run
   # Disponible en: http://localhost:8082
   ```

### 🐳 Ejecución con Docker

1. **Construir y ejecutar:**
   ```bash
   docker-compose build
   docker-compose up -d
   ```

2. **Verificar estado:**
   ```bash
   docker-compose ps
   ```

3. **Ver logs:**
   ```bash
   docker-compose logs -f inventario-api
   ```

4. **Detener servicios:**
   ```bash
   docker-compose down
   ```

## 📚 Documentación de la API

### Swagger UI (Interfaces Interactivas):
- **Productos**: http://localhost:8080/swagger-ui.html
- **Inventario**: http://localhost:8082/swagger-ui.html

### JSON API Docs:
- **Productos**: http://localhost:8080/api-docs
- **Inventario**: http://localhost:8082/api-docs

## 🧪 Testing y Calidad

### Ejecutar Tests:
```bash
# Tests unitarios
mvn test

# Tests con cobertura
mvn verify

# Tests específicos
mvn test -Dtest="InventarioControllerTest"
mvn test -Dtest="*ServiceTest"
```

### Cobertura de Código:
```bash
# Generar reporte JaCoCo
mvn jacoco:report

# Ver reporte en: target/site/jacoco/index.html
```

### 📊 Cobertura Actual:
- **Entity Tests**: ✅ 100%
- **DTO Tests**: ✅ 100%
- **Service Tests**: ✅ 95%+
- **Controller Tests**: ✅ 90%+

## 🌐 Endpoints Principales

### 🛍️ Productos API (Puerto 8080):
```
GET    /api/v1/productos              # Listar productos (paginado)
GET    /api/v1/productos/{id}         # Obtener producto por ID
POST   /api/v1/productos              # Crear producto
PUT    /api/v1/productos/{id}         # Actualizar producto
DELETE /api/v1/productos/{id}         # Eliminar producto
```

### 📦 Inventario API (Puerto 8082):
```
# Consultas básicas
GET    /api/v1/inventario                           # Todos los inventarios
GET    /api/v1/inventario/producto/{id}             # Inventario por producto

# Consultas con comunicación entre microservicios
GET    /api/v1/inventario/completo                  # Inventarios + info productos (paginado)
GET    /api/v1/inventario/producto/{id}/completo    # Inventario completo por producto

# Operaciones
POST   /api/v1/inventario                           # Crear inventario básico
POST   /api/v1/inventario/con-validacion            # Crear con validación de producto
PUT    /api/v1/inventario/producto/{id}/cantidad    # Actualizar cantidad
PUT    /api/v1/inventario/producto/{id}/compra      # Procesar compra
DELETE /api/v1/inventario/producto/{id}             # Eliminar inventario
```

## 📊 Ejemplos de Respuestas JSON:API

### Inventario Completo:
```json
{
  "data": [
    {
      "inventarioId": 1,
      "productoId": 15,
      "cantidadEnStock": 100,
      "nombreProducto": "Cloro granulado 100lt",
      "precioProducto": 1000.00,
      "valorTotalInventario": 100000.00,
      "estadoStock": "STOCK_ALTO"
    }
  ],
  "message": "✅ Inventarios completos obtenidos exitosamente",
  "meta": {
    "page": 1,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

### Respuesta de Compra:
```json
{
  "data": [
    {
      "inventarioId": 1,
      "productoId": 15,
      "nombreProducto": "Cloro granulado 100lt",
      "cantidadComprada": 25,
      "cantidadRestante": 75,
      "estadoCompra": "EXITOSA",
      "mensaje": "🛒 Compra procesada correctamente"
    }
  ],
  "message": "🛒 Compra procesada correctamente",
  "meta": null
}
```

## 🏛️ Diagrama de Arquitectura

```
                    HTTP/JSON:API + API Keys
┌────────────────┐ <----------------------------> ┌────────────────┐
│                │                                 │                │
│  productos-api │ <----------------------------> │ inventario-api │
│   (Port 8080)  │     RestTemplate + Timeouts    │   (Port 8082)  │
│                │                                 │                │
│  ├── Products  │                                 │  ├── Inventory │
│  ├── JSON:API  │                                 │  ├── Purchase  │
│  ├── Pagination│                                 │  ├── Stock Mgt │
│  └── CRUD      │                                 │  └── Events    │
│                │                                 │                │
└────────────────┘                                 └────────────────┘
       │                                                    │
       │                                                    │
       ▼                                                    ▼
┌────────────────┐                                 ┌────────────────┐
│                │                                 │                │
│ Oracle Database│                                 │ Oracle Database│
│   productos    │                                 │   inventario   │
│                │                                 │                │
└────────────────┘                                 └────────────────┘

                               Features:
                    ┌─────────────────────────────────┐
                    │ ✅ JSON:API Standard            │
                    │ 🔄 Microservices Communication │
                    │ 📊 Pagination & Filtering      │
                    │ 🛒 Stock Management            │
                    │ ⚡ Error Handling & Resilience │
                    │ 🧪 Comprehensive Testing       │
                    │ 📚 Swagger Documentation       │
                    │ 🐳 Docker Support              │
                    └─────────────────────────────────┘
```

## 🎯 Decisiones Técnicas

### 1. **Arquitectura de Microservicios**
- **Razón**: Desarrollo, despliegue y escalamiento independiente
- **Beneficios**: Tecnologías específicas por servicio, equipos independientes

### 2. **JSON:API Standard**
- **Razón**: Respuestas consistentes y estandarizadas
- **Implementación**: Formato uniforme con `data`, `message`, y `meta`

### 3. **Comunicación HTTP**
- **RestTemplate** con timeouts configurables (5s conectar, 10s leer)
- **API Keys** para autenticación entre servicios
- **Retry logic** y manejo de errores robusto

### 4. **Base de Datos Oracle**
- **Desarrollo/Producción**: Oracle 21c por robustez y transacciones ACID
- **Testing**: H2 en memoria para velocidad y simplicidad

### 5. **Manejo de Errores**
- **Global Exception Handlers** en cada servicio
- **Códigos HTTP apropiados** (200, 201, 404, 400, 503)
- **Mensajes descriptivos** con emojis para mejor UX

### 6. **Testing Completo**
- **Unit Tests**: Entity, DTO, Service, Controller
- **Integration Tests**: Repository, HTTP communication
- **Mocking**: Mockito para dependencias externas
- **Cobertura**: JaCoCo para métricas de calidad

## 🚀 Mejoras Implementadas

### ✨ Características Avanzadas:
- 🎯 **Validación de productos** antes de crear inventario
- 📊 **Estados de stock automáticos** (BAJO/MEDIO/ALTO)
- 💰 **Cálculo automático de valores** de inventario
- 🔄 **Eventos en consola** con emojis para mejor debugging
- 📄 **Paginación completa** en inventarios
- ⚡ **Manejo resiliente** de errores de conectividad
- 🌐 **Comunicación HTTP robusta** entre microservicios

### 🧪 Testing Robusto:
- ✅ **27 tests** en InventarioServiceImpl
- ✅ **20 tests** en InventarioController
- ✅ **50+ tests** en DTOs y Entities
- ✅ **Cobertura > 85%** en componentes críticos

## 🤝 Contribución

1. Fork el proyecto
2. Crea tu rama: `git checkout -b feature/nueva-caracteristica`
3. Commitea cambios: `git commit -am 'Añadir nueva característica'`
4. Push a la rama: `git push origin feature/nueva-caracteristica`
5. Abre un Pull Request

### 📝 Estándares de Código:
- ✅ Tests para nueva funcionalidad
- ✅ Documentación Swagger actualizada
- ✅ Manejo de errores apropiado
- ✅ Logs descriptivos con emojis
- ✅ Cobertura de tests > 80%

## 📞 Contacto

**Desarrollador**: Jesus Antonio Suarez Duarte  
**Email**: Jebus702@hotmail.com  
**Proyecto**: Sistema de Microservicios - Gestión de Inventario

---

## 📈 Estado del Proyecto

🎉 **COMPLETADO** - Sistema funcional con:
- ✅ Ambos microservicios operativos
- ✅ Comunicación HTTP establecida  
- ✅ Tests completos implementados
- ✅ Documentación Swagger disponible
- ✅ Docker support configurado
- ✅ JSON:API standard implementado
- ✅ Manejo robusto de errores
- ✅ Cobertura de tests > 85%

**Ready for Production** 🚀