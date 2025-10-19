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


@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - Pruebas Unitarias")
class ReservaServiceTest {

    @Mock
    private ReservaDAO reservaDAO;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private static final int VALID_RESERVA_ID = 1;
    private static final int VALID_USUARIO_CEDULA = 123456789;
    private static final Long VALID_RECURSO_ID = 3L;
    private static final LocalDate FECHA_INICIO = LocalDate.of(2025, 10, 15);
    private static final LocalDate FECHA_FIN = LocalDate.of(2025, 10, 15);
    private static final LocalTime HORA_INICIO = LocalTime.of(9, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(11, 0);

    private ReservaCreateDTO validCreateDto;
    private ReservaUpdateDTO validUpdateDto;
    private ReservaDTO validReservaDto;

    @BeforeEach
    void setUp() {
        validCreateDto = new ReservaCreateDTO();
        validCreateDto.setFechaInicio(FECHA_INICIO);
        validCreateDto.setHoraInicio(HORA_INICIO);
        validCreateDto.setFechaFin(FECHA_FIN);
        validCreateDto.setHoraFin(HORA_FIN);
        validCreateDto.setEstado(ReservaEstado.pendiente);
        validCreateDto.setIdRecurso(VALID_RECURSO_ID);
        validCreateDto.setUsuarioReserva(VALID_USUARIO_CEDULA);

        validUpdateDto = new ReservaUpdateDTO();
        validUpdateDto.setFechaInicio(FECHA_INICIO.plusDays(1));
        validUpdateDto.setHoraInicio(HORA_INICIO.plusHours(1));
        validUpdateDto.setFechaFin(FECHA_FIN.plusDays(1));
        validUpdateDto.setHoraFin(HORA_FIN.plusHours(1));
        validUpdateDto.setEstado(ReservaEstado.confirmada);

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
            given(reservaDAO.createReserva(any(ReservaCreateDTO.class))).willReturn(validReservaDto);

            ReservaDTO resultado = reservaService.crearReserva(validCreateDto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getFechaInicio()).isEqualTo(FECHA_INICIO);
            assertThat(resultado.getHoraInicio()).isEqualTo(HORA_INICIO);
            assertThat(resultado.getIdRecurso()).isEqualTo(VALID_RECURSO_ID);
            assertThat(resultado.getUsuarioReserva()).isEqualTo(VALID_USUARIO_CEDULA);
            assertThat(resultado.getEstado()).isEqualTo(ReservaEstado.pendiente);

            then(reservaDAO).should().createReserva(validCreateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos correctos al DAO")
        void deberiaPasarLosDatosCorrectosAlDAO() {

            ArgumentCaptor<ReservaCreateDTO> captor = ArgumentCaptor.forClass(ReservaCreateDTO.class);
            given(reservaDAO.createReserva(any(ReservaCreateDTO.class))).willReturn(validReservaDto);

            reservaService.crearReserva(validCreateDto);

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

            given(reservaDAO.createReserva(any(ReservaCreateDTO.class)))
                .willThrow(new RuntimeException("Error en base de datos"));

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

            given(reservaDAO.findById(VALID_RESERVA_ID)).willReturn(Optional.of(validReservaDto));


            ReservaDTO resultado = reservaService.obtenerReservaPorId(VALID_RESERVA_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getFechaInicio()).isEqualTo(FECHA_INICIO);
            assertThat(resultado.getIdRecurso()).isEqualTo(VALID_RECURSO_ID);

            then(reservaDAO).should().findById(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva no existe")
        void deberiaLanzarExcepcionCuandoLaReservaNoExiste() {
            int idInexistente = 999;
            given(reservaDAO.findById(idInexistente)).willReturn(Optional.empty());

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
            ReservaDTO reserva2 = new ReservaDTO();
            reserva2.setIdReserva(2);
            reserva2.setEstado(ReservaEstado.confirmada);
            
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto, reserva2);
            given(reservaDAO.findAll()).willReturn(reservasEsperadas);

            List<ReservaDTO> resultado = reservaService.listarReservas();

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.get(1).getIdReserva()).isEqualTo(2);

            then(reservaDAO).should().findAll();
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay reservas")
        void deberiaRetornarListaVaciaCuandoNoHayReservas() {
            given(reservaDAO.findAll()).willReturn(Collections.emptyList());

            List<ReservaDTO> resultado = reservaService.listarReservas();

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
            
            ReservaDTO reservaActualizada = new ReservaDTO();
            reservaActualizada.setIdReserva(VALID_RESERVA_ID);
            reservaActualizada.setEstado(ReservaEstado.confirmada);
            reservaActualizada.setFechaInicio(FECHA_INICIO.plusDays(1));

            given(reservaDAO.update(eq(VALID_RESERVA_ID), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.of(reservaActualizada));

            ReservaDTO resultado = reservaService.actualizarReserva(VALID_RESERVA_ID, validUpdateDto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getEstado()).isEqualTo(ReservaEstado.confirmada);

            then(reservaDAO).should().update(VALID_RESERVA_ID, validUpdateDto);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva a actualizar no existe")
        void deberiaLanzarExcepcionCuandoLaReservaAActualizarNoExiste() {
            int idInexistente = 999;
            given(reservaDAO.update(eq(idInexistente), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.empty());

            assertThatThrownBy(() -> reservaService.actualizarReserva(idInexistente, validUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Reserva no encontrada con ID: " + idInexistente);

            then(reservaDAO).should().update(idInexistente, validUpdateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos de actualización correctos al DAO")
        void deberiaPasarLosDatosDeActualizacionCorrectosAlDAO() {

            ArgumentCaptor<ReservaUpdateDTO> captor = ArgumentCaptor.forClass(ReservaUpdateDTO.class);
            given(reservaDAO.update(eq(VALID_RESERVA_ID), any(ReservaUpdateDTO.class)))
                .willReturn(Optional.of(validReservaDto));

            reservaService.actualizarReserva(VALID_RESERVA_ID, validUpdateDto);

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
            given(reservaDAO.deleteById(VALID_RESERVA_ID)).willReturn(true);

            boolean resultado = reservaService.eliminarReserva(VALID_RESERVA_ID);

            assertThat(resultado).isTrue();

            then(reservaDAO).should().deleteById(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando la reserva a eliminar no existe")
        void deberiaLanzarExcepcionCuandoLaReservaAEliminarNoExiste() {
            int idInexistente = 999;
            given(reservaDAO.deleteById(idInexistente)).willReturn(false);

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
            ReservaEstado estado = ReservaEstado.confirmada;
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByEstado(estado)).willReturn(reservasEsperadas);

            List<ReservaDTO> resultado = reservaService.buscarPorEstado(estado);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdReserva()).isEqualTo(VALID_RESERVA_ID);

            then(reservaDAO).should().findByEstado(estado);
        }

        @Test
        @DisplayName("Debería buscar reservas por usuario exitosamente")
        void deberiaBuscarReservasPorUsuarioExitosamente() {
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByUsuario(VALID_USUARIO_CEDULA)).willReturn(reservasEsperadas);

            List<ReservaDTO> resultado = reservaService.buscarPorUsuario(VALID_USUARIO_CEDULA);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getUsuarioReserva()).isEqualTo(VALID_USUARIO_CEDULA);

            then(reservaDAO).should().findByUsuario(VALID_USUARIO_CEDULA);
        }

        @Test
        @DisplayName("Debería buscar reservas por recurso exitosamente")
        void deberiaBuscarReservasPorRecursoExitosamente() {

            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByRecurso(VALID_RECURSO_ID)).willReturn(reservasEsperadas);

            List<ReservaDTO> resultado = reservaService.buscarPorRecurso(VALID_RECURSO_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdRecurso()).isEqualTo(VALID_RECURSO_ID);

            then(reservaDAO).should().findByRecurso(VALID_RECURSO_ID);
        }

        @Test
        @DisplayName("Debería buscar reservas por rango de fechas exitosamente")
        void deberiaBuscarReservasPorRangoDeFechasExitosamente() {

            LocalDate fechaInicio = LocalDate.of(2025, 10, 1);
            LocalDate fechaFin = LocalDate.of(2025, 10, 31);
            List<ReservaDTO> reservasEsperadas = Arrays.asList(validReservaDto);
            given(reservaDAO.findByRangoFechas(fechaInicio, fechaFin)).willReturn(reservasEsperadas);

            List<ReservaDTO> resultado = reservaService.buscarPorRangoDeFechas(fechaInicio, fechaFin);

            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getFechaInicio()).isEqualTo(FECHA_INICIO);

            then(reservaDAO).should().findByRangoFechas(fechaInicio, fechaFin);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay reservas que coincidan con estado")
        void deberiaRetornarListaVaciaCuandoNoHayReservasQueCoincidanConEstado() {
            ReservaEstado estado = ReservaEstado.cancelada;
            given(reservaDAO.findByEstado(estado)).willReturn(Collections.emptyList());

            List<ReservaDTO> resultado = reservaService.buscarPorEstado(estado);

            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(reservaDAO).should().findByEstado(estado);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando usuario no tiene reservas")
        void deberiaRetornarListaVaciaCuandoUsuarioNoTieneReservas() {
            int usuarioSinReservas = 999999999;
            given(reservaDAO.findByUsuario(usuarioSinReservas)).willReturn(Collections.emptyList());

            List<ReservaDTO> resultado = reservaService.buscarPorUsuario(usuarioSinReservas);

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

            LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
            LocalDate fechaFin = LocalDate.of(2025, 12, 31);
            ArgumentCaptor<LocalDate> inicioCaptor = ArgumentCaptor.forClass(LocalDate.class);
            ArgumentCaptor<LocalDate> finCaptor = ArgumentCaptor.forClass(LocalDate.class);
            
            given(reservaDAO.findByRangoFechas(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Collections.emptyList());

            reservaService.buscarPorRangoDeFechas(fechaInicio, fechaFin);

            then(reservaDAO).should().findByRangoFechas(inicioCaptor.capture(), finCaptor.capture());
            
            assertThat(inicioCaptor.getValue()).isEqualTo(fechaInicio);
            assertThat(finCaptor.getValue()).isEqualTo(fechaFin);
        }
    }
}