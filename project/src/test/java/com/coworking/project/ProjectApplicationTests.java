package com.coworking.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración para verificar que el contexto de Spring Boot se carga correctamente
 * Usa perfil 'test' con base de datos H2 en memoria
 */
@SpringBootTest
@ActiveProfiles("test")
class ProjectApplicationTests {

	@Test
	void contextLoads() {
		// Este test verifica que el contexto de Spring Boot se carga sin errores
		// La configuración de test usa H2 en memoria en lugar de MySQL
	}

}
