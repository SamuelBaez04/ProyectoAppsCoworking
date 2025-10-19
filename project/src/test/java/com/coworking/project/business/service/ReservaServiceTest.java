package com.coworking.project.business.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coworking.project.businessLayer.dto.ReservaCreateDTO;
import com.coworking.project.businessLayer.dto.ReservaDTO;
import com.coworking.project.businessLayer.dto.ReservaUpdateDTO;
import com.coworking.project.businessLayer.service.impl.ReservaServiceImpl;
import com.coworking.project.persistenceLayer.dao.ReservaDAO;
import com.coworking.project.util.ReservaEstado;

/**
 * Test unitario para ReservaServiceImpl
 * 
 * Valida la lógica de negocio del servicio de reservas incluyendo:
 * - Gestión completa CRUD de reservas
 * - Validaciones de fechas y horarios
 * - Búsquedas por diferentes criterios
 * - Manejo de estados de reservas
 * - Gestión de excepciones
 * 
 * @author Senior Java Developer
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - Pruebas Unitarias")
class ReservaServiceTest {

    @Mock
    private ReservaDAO reservaDAO;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    // Constantes para pruebas
    private static final int VALID_RESERVA_ID = 1;
    private static final int VALID_USUARIO_CEDULA = 123456789;
    private static final Long VALID_RECURSO_ID = 3L;
    private static final LocalDate FECHA_INICIO = LocalDate.of(2025, 10, 15);
    private static final LocalDate FECHA_FIN = LocalDate.of(2025, 10, 15);
    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(11, 0);

    // DTOs de prueba
    private ReservaCreateDTO validCreateDto;
    private ReservaUpdateDTO validUpdateDto;
    private ReservaDTO validReservaDto;

    @BeforeEach
    void setUp() {
        // Setup ReservaCreateDTO
        validCreateDto = new ReservaCreateDTO();
        validCreateDto.setFechaInicio(FECHA_INICIO);
        validCreateDto.setHoraInicio(HORA_INICIO);
        validCreateDto.setFechaFin(FECHA_FIN);
        validCreateDto.setHoraFin(HORA_FIN);
        validCreateDto.setEstado(ReservaEstado.pendiente);
        validCreateDto.setIdRecurso(VALID_RECURSO_ID);
        validCreateDto.setUsuarioReserva(VALID_USUARIO_CEDULA);

        // Setup ReservaUpdateDTO
        validUpdateDto = new ReservaUpdateDTO();
        validUpdateDto.setFechaInicio(FECHA_INICIO.plusDays(1));
        validUpdateDto.setHoraInicio(HORA_INICIO.plusHours(1));
        validUpdateDto.setFechaFin(FECHA_FIN.plusDays(1));
        validUpdateDto.setHoraFin(HORA_FIN.plusHours(1));
        validUpdateDto.setEstado(ReservaEstado.confirmada);

        // Setup ReservaDTO
        validReservaDto = new ReservaDTO();
        validReservaDto.setIdReserva(VALID_RESERVA_ID);
        validReservaDto.setFechaInicio(FECHA_INICIO);
        validReservaDto.setHoraInicio(HORA_INICIO);
        validReservaDto.setFechaFin(FECHA_FIN);
        validReservaDto.setHoraFin(HORA_FIN);
        validReservaDto.setEstado(ReservaEstado.pendiente);
        validReservaDto.setIdRecurso(VALID_RECURSO_ID);
        validReservaDto.setUsuarioReserva(VALID_USUARIO_CEDULA);
    }

    @Nested
    @DisplayName("Crear Reserva")
    class CrearReserva {

        @Test
        @DisplayName("Debería crear reserva exitosamente con datos válidos")
        void deberiaCrearReservaExitosamenteConDatosValidos() {
            // Arrange
            given(reservaDAO.createReserva(any(ReservaCreateDTO.class))).willReturn(validReservaDto);

            // Act
            ReservaDTO resultado = reservaService.crearReserva(validCreateDto);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getFechaInicio()).isEqualTo(FECHA_INICIO);
            assertThat(resultado.getHoraInicio()).isEqualTo(HORA_INICIO);
            assertThat(resultado.getIdRecurso()).isEqualTo(VALID_RECURSO_ID);
            assertThat(resultado.getUsuarioReserva()).isEqualTo(VALID_USUARIO_CEDULA);
            assertThat(resultado.getEstado()).isEqualTo(ReservaEstado.pendiente);

            // Verify
            then(reservaDAO).should().createReserva(validCreateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos correctos al DAO")
        void deberiaPasarLosDatosCorrectosAlDAO() {
            // Arrange
            ArgumentCaptor<ReservaCreateDTO> captor = ArgumentCaptor.forClass(ReservaCreateDTO.class);
            given(reservaDAO.createReserva(any(ReservaCreateDTO.class))).willReturn(validReservaDto);

            // Act
            reservaService.crearReserva(validCreateDto);

            // Assert
            then(reservaDAO).should().createReserva(captor.capture());
            ReservaCreateDTO captured = captor.getValue();
            
            assertThat(captured.getFechaInicio()).isEqualTo(FECHA_INICIO);
            assertThat(captured.getHoraInicio()).isEqualTo(HORA_INICIO);
            assertThat(captured.getFechaFin()).isEqualTo(FECHA_FIN);
            assertThat(captured.getHoraFin()).isEqualTo(HORA_FIN);
            assertThat(captured.getIdRecurso()).isEqualTo(VALID_RECURSO_ID);
            assertThat(captured.getUsuarioReserva()).isEqualTo(VALID_USUARIO_CEDULA);
        }

        @Test
        @DisplayName("Debería manejar excepción cuando el DAO falla")
        void deberiaManejarExcepcionCuandoElDAOFalla() {
            // Arrange
            given(reservaDAO.createReserva(any(ReservaCreateDTO.class)))
                .willThrow(new RuntimeException("Error en base de datos"));

            // Act & Assert
            assertThatThrownBy(() -> reservaService.crearReserva(validCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error en base de datos");

            then(reservaDAO).should().createReserva(validCreateDto);
        }
    }

    @Nested
    @DisplayName("Obtener Reserva por ID")
    class ObtenerReservaPorId {

        @Test
        @DisplayName("Debería obtener reserva exitosamente por ID válido")
        void deberiaObtenerReservaExitosamentePorIdValido() {
            // Arrange
            given(reservaDAO.findById(VALID_RESERVA_ID)).willReturn(Optional.of(validReservaDto));

            // Act
            ReservaDTO resultado = reservaService.obtenerReservaPorId(VALID_RESERVA_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getFechaInicio()).isEqualTo(FECHA_INICIO);
            assertThat(resultado.getIdRecurso()).isEqualTo(VALID_RECURSO_ID);

            then(reservaDAO).should().findById(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva no existe")
        void deberiaLanzarExcepcionCuandoLaReservaNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(reservaDAO.findById(idInexistente)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> reservaService.obtenerReservaPorId(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reserva no encontrada con ID: " + idInexistente);

            then(reservaDAO).should().findById(idInexistente);
        }
    }

    @Nested
    @DisplayName("Listar Reservas")
    class ListarReservas {

        @Test
        @DisplayName("Debería obtener lista de reservas exitosamente")
        void deberiaObtenerListaDeReservasExitosamente() {
            // Arrange
            ReservaDTO reserva2 = new ReservaDTO();
            reserva2.setIdReserva(2);
            reserva2.setEstado(ReservaEstado.confirmada);
            
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto, reserva2);
            given(reservaDAO.findAll()).willReturn(reservasEsperadas);

            // Act
            List<ReservaDTO> resultado = reservaService.listarReservas();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.get(1).getIdReserva()).isEqualTo(2);

            then(reservaDAO).should().findAll();
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay reservas")
        void deberiaRetornarListaVaciaCuandoNoHayReservas() {
            // Arrange
            given(reservaDAO.findAll()).willReturn(Collections.emptyList());

            // Act
            List<ReservaDTO> resultado = reservaService.listarReservas();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(reservaDAO).should().findAll();
        }
    }

    @Nested
    @DisplayName("Actualizar Reserva")
    class ActualizarReserva {

        @Test
        @DisplayName("Debería actualizar reserva exitosamente")
        void deberiaActualizarReservaExitosamente() {
            // Arrange
            ReservaDTO reservaActualizada = new ReservaDTO();
            reservaActualizada.setIdReserva(VALID_RESERVA_ID);
            reservaActualizada.setEstado(ReservaEstado.confirmada);
            reservaActualizada.setFechaInicio(FECHA_INICIO.plusDays(1));

            given(reservaDAO.update(eq(VALID_RESERVA_ID), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.of(reservaActualizada));

            // Act
            ReservaDTO resultado = reservaService.actualizarReserva(VALID_RESERVA_ID, validUpdateDto);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getEstado()).isEqualTo(ReservaEstado.confirmada);

            then(reservaDAO).should().update(VALID_RESERVA_ID, validUpdateDto);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva a actualizar no existe")
        void deberiaLanzarExcepcionCuandoLaReservaAActualizarNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(reservaDAO.update(eq(idInexistente), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> reservaService.actualizarReserva(idInexistente, validUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reserva no encontrada con ID: " + idInexistente);

            then(reservaDAO).should().update(idInexistente, validUpdateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos de actualización correctos al DAO")
        void deberiaPasarLosDatosDeActualizacionCorrectosAlDAO() {
            // Arrange
            ArgumentCaptor<ReservaUpdateDTO> captor = ArgumentCaptor.forClass(ReservaUpdateDTO.class);
            given(reservaDAO.update(eq(VALID_RESERVA_ID), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.of(validReservaDto));

            // Act
            reservaService.actualizarReserva(VALID_RESERVA_ID, validUpdateDto);

            // Assert
            then(reservaDAO).should().update(eq(VALID_RESERVA_ID), captor.capture());
            ReservaUpdateDTO captured = captor.getValue();
            
            assertThat(captured.getEstado()).isEqualTo(ReservaEstado.confirmada);
            assertThat(captured.getFechaInicio()).isEqualTo(FECHA_INICIO.plusDays(1));
            assertThat(captured.getHoraInicio()).isEqualTo(HORA_INICIO.plusHours(1));
        }
    }

    @Nested
    @DisplayName("Eliminar Reserva")
    class EliminarReserva {

        @Test
        @DisplayName("Debería eliminar reserva exitosamente")
        void deberiaEliminarReservaExitosamente() {
            // Arrange
            given(reservaDAO.deleteById(VALID_RESERVA_ID)).willReturn(true);

            // Act
            boolean resultado = reservaService.eliminarReserva(VALID_RESERVA_ID);

            // Assert
            assertThat(resultado).isTrue();

            then(reservaDAO).should().deleteById(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva a eliminar no existe")
        void deberiaLanzarExcepcionCuandoLaReservaAEliminarNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(reservaDAO.deleteById(idInexistente)).willReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> reservaService.eliminarReserva(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reserva no encontrada con ID: " + idInexistente);

            then(reservaDAO).should().deleteById(idInexistente);
        }
    }

    @Nested
    @DisplayName("Búsquedas por Criterios")
    class BusquedasPorCriterios {

        @Test
        @DisplayName("Debería buscar reservas por estado exitosamente")
        void deberiaBuscarReservasPorEstadoExitosamente() {
            // Arrange
            ReservaEstado estado = ReservaEstado.confirmada;
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByEstado(estado)).willReturn(reservasEsperadas);

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorEstado(estado);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdReserva()).isEqualTo(VALID_RESERVA_ID);

            then(reservaDAO).should().findByEstado(estado);
        }

        @Test
        @DisplayName("Debería buscar reservas por usuario exitosamente")
        void deberiaBuscarReservasPorUsuarioExitosamente() {
            // Arrange
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByUsuario(VALID_USUARIO_CEDULA)).willReturn(reservasEsperadas);

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorUsuario(VALID_USUARIO_CEDULA);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getUsuarioReserva()).isEqualTo(VALID_USUARIO_CEDULA);

            then(reservaDAO).should().findByUsuario(VALID_USUARIO_CEDULA);
        }

        @Test
        @DisplayName("Debería buscar reservas por recurso exitosamente")
        void deberiaBuscarReservasPorRecursoExitosamente() {
            // Arrange
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByRecurso(VALID_RECURSO_ID)).willReturn(reservasEsperadas);

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorRecurso(VALID_RECURSO_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdRecurso()).isEqualTo(VALID_RECURSO_ID);

            then(reservaDAO).should().findByRecurso(VALID_RECURSO_ID);
        }

        @Test
        @DisplayName("Debería buscar reservas por rango de fechas exitosamente")
        void deberiaBuscarReservasPorRangoDeFechasExitosamente() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2025, 10, 1);
            LocalDate fechaFin = LocalDate.of(2025, 10, 31);
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByRangoFechas(fechaInicio, fechaFin)).willReturn(reservasEsperadas);

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorRangoDeFechas(fechaInicio, fechaFin);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getFechaInicio()).isEqualTo(FECHA_INICIO);

            then(reservaDAO).should().findByRangoFechas(fechaInicio, fechaFin);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay reservas que coincidan con estado")
        void deberiaRetornarListaVaciaCuandoNoHayReservasQueCoincidanConEstado() {
            // Arrange
            ReservaEstado estado = ReservaEstado.cancelada;
            given(reservaDAO.findByEstado(estado)).willReturn(Collections.emptyList());

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorEstado(estado);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(reservaDAO).should().findByEstado(estado);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando usuario no tiene reservas")
        void deberiaRetornarListaVaciaCuandoUsuarioNoTieneReservas() {
            // Arrange
            int usuarioSinReservas = 999999999;
            given(reservaDAO.findByUsuario(usuarioSinReservas)).willReturn(Collections.emptyList());

            // Act
            List<ReservaDTO> resultado = reservaService.buscarPorUsuario(usuarioSinReservas);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(reservaDAO).should().findByUsuario(usuarioSinReservas);
        }
    }

    @Nested
    @DisplayName("Validaciones de Negocio")
    class ValidacionesDeNegocio {

        @Test
        @DisplayName("Debería manejar correctamente los diferentes estados de reserva")
        void deberiaManejarCorrectamenteLosDistintosEstadosDeReserva() {
            // Arrange & Act & Assert para cada estado
            for (ReservaEstado estado : ReservaEstado.values()) {
                List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
                given(reservaDAO.findByEstado(estado)).willReturn(reservasEsperadas);

                List<ReservaDTO> resultado = reservaService.buscarPorEstado(estado);

                assertThat(resultado).isNotNull();
                assertThat(resultado).hasSize(1);
                
                then(reservaDAO).should().findByEstado(estado);
            }
        }

        @Test
        @DisplayName("Debería validar que se pasan los parámetros correctos para búsqueda por rango")
        void deberiaValidarQueSeparanLosParametrosCorrectosParaBusquedaPorRango() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
            LocalDate fechaFin = LocalDate.of(2025, 12, 31);
            ArgumentCaptor<LocalDate> inicioCaptor = ArgumentCaptor.forClass(LocalDate.class);
            ArgumentCaptor<LocalDate> finCaptor = ArgumentCaptor.forClass(LocalDate.class);
            
            given(reservaDAO.findByRangoFechas(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Collections.emptyList());

            // Act
            reservaService.buscarPorRangoDeFechas(fechaInicio, fechaFin);

            // Assert
            then(reservaDAO).should().findByRangoFechas(inicioCaptor.capture(), finCaptor.capture());
            
            assertThat(inicioCaptor.getValue()).isEqualTo(fechaInicio);
            assertThat(finCaptor.getValue()).isEqualTo(fechaFin);
        }
    }
}