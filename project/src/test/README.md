dale# 🧪 Estructura de Tests - Coworking Project

## 📁 **Organización de carpetas**

```
src/test/java/com/coworking/project/
├── business/service/           # Unit Tests - Servicios (Business Logic)
│   ├── ReporteServiceTest.java
│   ├── UsuarioServiceTest.java
│   └── ReservaServiceTest.java
├── presentation/controller/    # Integration Tests - Controladores REST
│   ├── ReporteControllerTest.java
│   ├── UsuarioControllerTest.java
│   └── ReservaControllerTest.java
├── persistence/dao/           # Unit Tests - DAOs (Data Access)
│   ├── ReporteDAOTest.java
│   ├── UsuarioDAOTest.java
│   └── ReservaDAOTest.java
├── persistence/mapper/        # Unit Tests - MapStruct Mappers
│   ├── ReporteMapperTest.java
│   ├── UsuarioMapperTest.java
│   └── ReservaMapperTest.java
└── integration/              # Integration Tests E2E
    ├── ReporteIntegrationTest.java
    └── FullWorkflowTest.java
```

## 🎯 **Tipos de Tests**

### **1. Unit Tests (Servicios)**
- **Ubicación**: `business/service/`
- **Objetivo**: Probar lógica de negocio aislada
- **Características**:
  - ✅ Sin base de datos
  - ✅ Sin Spring Context
  - ✅ Mocks para dependencias
  - ✅ Ejecución rápida (< 1s)
- **Anotaciones**: `@ExtendWith(MockitoExtension.class)`

### **2. Integration Tests (Controllers)**
- **Ubicación**: `presentation/controller/`
- **Objetivo**: Probar endpoints REST completos
- **Características**:
  - ✅ Spring Context mínimo
  - ✅ MockMvc para HTTP requests
  - ✅ Mocks para servicios
  - ✅ Validación de JSON
- **Anotaciones**: `@WebMvcTest(ReporteController.class)`

### **3. Unit Tests (DAOs)**
- **Ubicación**: `persistence/dao/`
- **Objetivo**: Probar acceso a datos
- **Características**:
  - ✅ Mocks para repositories
  - ✅ Validación de queries
  - ✅ Mapeo DTO ↔ Entity
- **Anotaciones**: `@ExtendWith(MockitoExtension.class)`

### **4. Unit Tests (Mappers)**
- **Ubicación**: `persistence/mapper/`
- **Objetivo**: Probar MapStruct mappers
- **Características**:
  - ✅ Conversión DTO ↔ Entity
  - ✅ Validación de campos
  - ✅ Casos edge (nulls, etc.)
- **Anotaciones**: `@ExtendWith(SpringExtension.class)`

## 📋 **Patrón AAA (Arrange-Act-Assert)**

### **Estructura estándar:**

```java
@Test
@DisplayName("Descripción clara del comportamiento esperado")
void nombreMetodo_condicion_resultadoEsperado() {
    // Arrange - Configurar datos y mocks
    TipoDTO inputData = new TipoDTO();
    inputData.setField("value");
    
    when(mockDependency.method(any())).thenReturn(expectedResult);
    
    // Act - Ejecutar método bajo prueba
    TipoDTO result = service.methodUnderTest(inputData);
    
    // Assert - Verificar resultado y comportamiento
    assertThat(result).isNotNull();
    assertThat(result.getField()).isEqualTo("expected");
    verify(mockDependency, times(1)).method(inputData);
}
```

## 🏗️ **Convenciones de Naming**

### **Clases de Test:**
- `[ClaseUnderTest]Test.java`
- Ejemplo: `ReporteServiceTest.java`

### **Métodos de Test:**
- `metodoBajoTests_condicionEntrada_resultadoEsperado()`
- Ejemplos:
  - `crearReporte_validData_returnsCreatedReporte()`
  - `obtenerReportePorId_notFound_throwsException()`

### **Display Names:**
- Español, descriptivo, centrado en el comportamiento
- Formato: `"OPERACION - condición produce resultado"`
- Ejemplos:
  - `"CREATE - reporte válido retorna reporte creado"`
  - `"GET by id - no existente lanza RuntimeException"`

## 🧩 **Nested Classes para Organización**

```java
@Nested
@DisplayName("Crear Reporte")
class CrearReporte {
    // Tests relacionados con creación
}

@Nested
@DisplayName("Obtener Reporte") 
class ObtenerReporte {
    // Tests relacionados con consultas
}
```

## 📊 **Coverage Esperado**

| Capa | Coverage Mínimo |
|------|-----------------|
| Services | 90%+ |
| Controllers | 85%+ |
| DAOs | 80%+ |
| Mappers | 95%+ |

## 🚀 **Comandos de Ejecución**

### **Todos los tests:**
```bash
./gradlew test
```

### **Solo unit tests:**
```bash
./gradlew test --tests "*.service.*"
```

### **Solo integration tests:**
```bash
./gradlew test --tests "*.controller.*"
```

### **Con coverage:**
```bash
./gradlew test jacocoTestReport
```

## 🎯 **Próximos Tests a Implementar:**

1. ✅ **ReporteServiceTest** - Completado
2. 🔄 **UsuarioServiceTest** - En progreso
3. ⏳ **ReporteControllerTest** - Pendiente
4. ⏳ **UsuarioControllerTest** - Pendiente
5. ⏳ **ReporteDAOTest** - Pendiente

---

*Estructura diseñada siguiendo las mejores prácticas de testing en Spring Boot y los principios SOLID.*