package com.coworking.project.business.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.dto.UsuarioUpdateDTO;
import com.coworking.project.businessLayer.service.impl.UsuarioServiceImpl;
import com.coworking.project.persistenceLayer.dao.UsuarioDAO;

/**
 * Unit Tests para UsuarioServiceImpl
 * 
 * Utiliza AAA Pattern (Arrange-Act-Assert) y BDD style assertions
 * Cobertura completa de casos de éxito y casos de error
 * Mockea el DAO layer para aislar la lógica de negocio
 * 
 * @author Test Automation
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService Tests - Business Logic with DAO Layer")
class UsuarioServiceTestFinal {

    // === MOCKS ===
    @Mock
    private UsuarioDAO usuarioDAO;
    
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    // === TEST DATA ===
    private static final Integer VALID_CEDULA = 123456789;
    private static final Integer VALID_ID_ROL = 1;
    private static final String VALID_EMAIL = "juan.perez@example.com";
    
    private UsuarioCreateDTO validCreateDto;
    private UsuarioDTO validUsuarioDto;
    private UsuarioUpdateDTO validUpdateDto;

    @BeforeEach
    void setUp() {
        // Arrange - Setup valid test data objects

        // Setup valid UsuarioCreateDTO
        validCreateDto = new UsuarioCreateDTO();
        validCreateDto.setCedula(VALID_CEDULA);
        validCreateDto.setNombreCompleto("Juan Carlos Perez");
        validCreateDto.setIdRol(VALID_ID_ROL);
        validCreateDto.setDireccion("Calle Falsa 123, Ciudad");
        validCreateDto.setPassword("password123");
        validCreateDto.setTelefono("3001234567");
        validCreateDto.setEmail(VALID_EMAIL);

        // Setup valid UsuarioDTO (response)
        validUsuarioDto = new UsuarioDTO();
        validUsuarioDto.setCedula(VALID_CEDULA);
        validUsuarioDto.setNombreCompleto("Juan Carlos Perez");
        validUsuarioDto.setIdRol(VALID_ID_ROL);
        validUsuarioDto.setDireccion("Calle Falsa 123, Ciudad");
        validUsuarioDto.setTelefono("3001234567");
        validUsuarioDto.setEmail(VALID_EMAIL);

        // Setup valid UsuarioUpdateDTO
        validUpdateDto = new UsuarioUpdateDTO();
        validUpdateDto.setNombreCompleto("Juan Carlos Perez Actualizado");
        validUpdateDto.setTelefono("3001234568");
        validUpdateDto.setDireccion("Nueva Direccion 456");
    }

    @Nested
    @DisplayName("Crear Usuario Tests")
    class CrearUsuario {

        @Test
        @DisplayName("Debería crear usuario exitosamente con datos válidos")
        void deberiaCrearUsuarioExitosamenteConDatosValidos() {
            // Arrange
            given(usuarioDAO.existByEmail(VALID_EMAIL)).willReturn(false);
            given(usuarioDAO.crearUsuario(validCreateDto)).willReturn(validUsuarioDto);

            // Act
            UsuarioDTO result = usuarioService.crearUsuario(validCreateDto);

            // Assert
            then(usuarioDAO).should().existByEmail(VALID_EMAIL);
            then(usuarioDAO).should().crearUsuario(validCreateDto);
            
            assertThat(result)
                .isNotNull()
                .extracting("cedula", "nombreCompleto", "email", "idRol")
                .containsExactly(VALID_CEDULA, "Juan Carlos Perez", VALID_EMAIL, VALID_ID_ROL);
        }

        @Test
        @DisplayName("Debería fallar cuando el email ya existe")
        void deberiaFallarCuandoElEmailYaExiste() {
            // Arrange
            given(usuarioDAO.existByEmail(VALID_EMAIL)).willReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.crearUsuario(validCreateDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Ya exite un usuario con el email" + VALID_EMAIL);
                    
            then(usuarioDAO).should().existByEmail(VALID_EMAIL);
            then(usuarioDAO).should(never()).crearUsuario(any(UsuarioCreateDTO.class));
        }

        @Test
        @DisplayName("Debería manejar error del DAO durante creación")
        void deberiaManejarErrorDelDAODuranteCreacion() {
            // Arrange
            given(usuarioDAO.existByEmail(VALID_EMAIL)).willReturn(false);
            given(usuarioDAO.crearUsuario(validCreateDto)).willThrow(new RuntimeException("Database error"));

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.crearUsuario(validCreateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Database error");
                    
            then(usuarioDAO).should().existByEmail(VALID_EMAIL);
            then(usuarioDAO).should().crearUsuario(validCreateDto);
        }
    }

    @Nested
    @DisplayName("Obtener Usuario Tests")
    class ObtenerUsuario {

        @Test
        @DisplayName("Debería obtener usuario por cédula exitosamente")
        void deberiaObtenerUsuarioPorCedulaExitosamente() {
            // Arrange
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));

            // Act
            UsuarioDTO result = usuarioService.obtenerUsuarioPorCedula(VALID_CEDULA);

            // Assert
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            
            assertThat(result)
                .isNotNull()
                .extracting("cedula", "nombreCompleto", "email")
                .containsExactly(VALID_CEDULA, "Juan Carlos Perez", VALID_EMAIL);
        }

        @Test
        @DisplayName("Debería fallar cuando usuario no existe por cédula")
        void deberiaFallarCuandoUsuarioNoExistePorCedula() {
            // Arrange
            Integer nonExistentCedula = 999999999;
            given(usuarioDAO.findyById(nonExistentCedula)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.obtenerUsuarioPorCedula(nonExistentCedula))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Vendedor no encontrado con cedula: " + nonExistentCedula);
                    
            then(usuarioDAO).should().findyById(nonExistentCedula);
        }

        @Test
        @DisplayName("Debería obtener usuario por email exitosamente")
        void deberiaObtenerUsuarioPorEmailExitosamente() {
            // Arrange
            given(usuarioDAO.findByEmail(VALID_EMAIL)).willReturn(Optional.of(validUsuarioDto));

            // Act
            UsuarioDTO result = usuarioService.obtenerUsuarioPorEmail(VALID_EMAIL);

            // Assert
            then(usuarioDAO).should().findByEmail(VALID_EMAIL);
            
            assertThat(result)
                .isNotNull()
                .extracting("cedula", "nombreCompleto", "email")
                .containsExactly(VALID_CEDULA, "Juan Carlos Perez", VALID_EMAIL);
        }

        @Test
        @DisplayName("Debería fallar cuando usuario no existe por email")
        void deberiaFallarCuandoUsuarioNoExistePorEmail() {
            // Arrange
            String nonExistentEmail = "noexiste@example.com";
            given(usuarioDAO.findByEmail(nonExistentEmail)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.obtenerUsuarioPorEmail(nonExistentEmail))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado con email: " + nonExistentEmail);
                    
            then(usuarioDAO).should().findByEmail(nonExistentEmail);
        }

        @Test
        @DisplayName("Debería listar todos los usuarios exitosamente")
        void deberiaListarTodosLosUsuariosExitosamente() {
            // Arrange
            List<UsuarioDTO> usuarios = List.of(validUsuarioDto);
            given(usuarioDAO.findAll()).willReturn(usuarios);

            // Act
            List<UsuarioDTO> results = usuarioService.listarUsuarios();

            // Assert
            then(usuarioDAO).should().findAll();
            
            assertThat(results)
                .isNotNull()
                .hasSize(1)
                .extracting("cedula", "nombreCompleto", "email")
                .containsExactly(tuple(VALID_CEDULA, "Juan Carlos Perez", VALID_EMAIL));
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay usuarios")
        void deberiaRetornarListaVaciaCuandoNoHayUsuarios() {
            // Arrange
            given(usuarioDAO.findAll()).willReturn(List.of());

            // Act
            List<UsuarioDTO> results = usuarioService.listarUsuarios();

            // Assert
            then(usuarioDAO).should().findAll();
            assertThat(results).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Actualizar Usuario Tests")
    class ActualizarUsuario {

        @Test
        @DisplayName("Debería actualizar usuario exitosamente con datos válidos")
        void deberiaActualizarUsuarioExitosamenteConDatosValidos() {
            // Arrange
            UsuarioDTO updatedUsuarioDto = new UsuarioDTO();
            updatedUsuarioDto.setCedula(VALID_CEDULA);
            updatedUsuarioDto.setNombreCompleto("Juan Carlos Perez Actualizado");
            updatedUsuarioDto.setTelefono("3001234568");
            updatedUsuarioDto.setDireccion("Nueva Direccion 456");
            updatedUsuarioDto.setEmail(VALID_EMAIL);
            updatedUsuarioDto.setIdRol(VALID_ID_ROL);
            
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));
            given(usuarioDAO.update(VALID_CEDULA, validUpdateDto)).willReturn(Optional.of(updatedUsuarioDto));

            // Act
            UsuarioDTO result = usuarioService.actualizarUsuario(VALID_CEDULA, validUpdateDto);

            // Assert
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            then(usuarioDAO).should().update(VALID_CEDULA, validUpdateDto);
            
            assertThat(result)
                .isNotNull()
                .extracting("nombreCompleto", "telefono", "direccion")
                .containsExactly("Juan Carlos Perez Actualizado", "3001234568", "Nueva Direccion 456");
        }

        @Test
        @DisplayName("Debería fallar cuando usuario no existe para actualización")
        void deberiaFallarCuandoUsuarioNoExisteParaActualizacion() {
            // Arrange
            Integer nonExistentCedula = 999999999;
            given(usuarioDAO.findyById(nonExistentCedula)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.actualizarUsuario(nonExistentCedula, validUpdateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado con cedula: " + nonExistentCedula);
                    
            then(usuarioDAO).should().findyById(nonExistentCedula);
            then(usuarioDAO).should(never()).update(any(Integer.class), any(UsuarioUpdateDTO.class));
        }

        @Test
        @DisplayName("Debería fallar cuando DAO no puede actualizar")
        void deberiaFallarCuandoDAONoPuedeActualizar() {
            // Arrange
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));
            given(usuarioDAO.update(VALID_CEDULA, validUpdateDto)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.actualizarUsuario(VALID_CEDULA, validUpdateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Error al actualizar usuario");
                    
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            then(usuarioDAO).should().update(VALID_CEDULA, validUpdateDto);
        }
    }

    @Nested
    @DisplayName("Eliminar Usuario Tests")
    class EliminarUsuario {

        @Test
        @DisplayName("Debería eliminar usuario exitosamente sin reservas")
        void deberiaEliminarUsuarioExitosamenteSinReservas() {
            // Arrange
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));
            given(usuarioDAO.countReservasByUsuarioId(VALID_CEDULA)).willReturn(0);
            given(usuarioDAO.deleteById(VALID_CEDULA)).willReturn(true);

            // Act
            assertThatCode(() -> usuarioService.eliminarUsuario(VALID_CEDULA))
                    .doesNotThrowAnyException();

            // Assert
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            then(usuarioDAO).should().countReservasByUsuarioId(VALID_CEDULA);
            then(usuarioDAO).should().deleteById(VALID_CEDULA);
        }

        @Test
        @DisplayName("Debería fallar cuando usuario no existe para eliminar")
        void deberiaFallarCuandoUsuarioNoExisteParaEliminar() {
            // Arrange
            Integer nonExistentCedula = 999999999;
            given(usuarioDAO.findyById(nonExistentCedula)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.eliminarUsuario(nonExistentCedula))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Vendedor no encontrado con cedula: " + nonExistentCedula);
                    
            then(usuarioDAO).should().findyById(nonExistentCedula);
            then(usuarioDAO).should(never()).deleteById(any(Integer.class));
        }

        @Test
        @DisplayName("Debería fallar cuando usuario tiene reservas activas")
        void deberiaFallarCuandoUsuarioTieneReservasActivas() {
            // Arrange
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));
            given(usuarioDAO.countReservasByUsuarioId(VALID_CEDULA)).willReturn(3);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.eliminarUsuario(VALID_CEDULA))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede elimianr el usuario por que tiene 3 reserva(s) activa(s)");
                    
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            then(usuarioDAO).should().countReservasByUsuarioId(VALID_CEDULA);
            then(usuarioDAO).should(never()).deleteById(any(Integer.class));
        }

        @Test
        @DisplayName("Debería fallar cuando DAO no puede eliminar")
        void deberiaFallarCuandoDAONoPuedeEliminar() {
            // Arrange
            given(usuarioDAO.findyById(VALID_CEDULA)).willReturn(Optional.of(validUsuarioDto));
            given(usuarioDAO.countReservasByUsuarioId(VALID_CEDULA)).willReturn(0);
            given(usuarioDAO.deleteById(VALID_CEDULA)).willReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> usuarioService.eliminarUsuario(VALID_CEDULA))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Error al eliminar vendedor con cedula: " + VALID_CEDULA);
                    
            then(usuarioDAO).should().findyById(VALID_CEDULA);
            then(usuarioDAO).should().countReservasByUsuarioId(VALID_CEDULA);
            then(usuarioDAO).should().deleteById(VALID_CEDULA);
        }
    }
}