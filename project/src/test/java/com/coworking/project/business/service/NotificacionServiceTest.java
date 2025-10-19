package com.coworking.project.business.service;

import com.coworking.project.businessLayer.dto.NotificacionCreateDTO;
import com.coworking.project.businessLayer.dto.NotificacionDTO;
import com.coworking.project.businessLayer.dto.NotificacionUpdateDTO;
import com.coworking.project.businessLayer.service.impl.NotificacionServiceImpl;
import com.coworking.project.persistenceLayer.dao.NotificacionDAO;
import com.coworking.project.util.NotificacionEstado;
import com.coworking.project.util.TipoNotificacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionService - Pruebas Unitarias")
class NotificacionServiceTest {

    @Mock
    private NotificacionDAO notificacionDAO;

    @InjectMocks
    private NotificacionServiceImpl notificacionService;

    private static final int VALID_ID = 1;
    private static final int VALID_USUARIO_ID = 101;
    private static final TipoNotificacion TIPO_ALERTA = TipoNotificacion.ALERTA_VENCIMIENTO;
    private static final TipoNotificacion TIPO_RECORDATORIO = TipoNotificacion.RECORDATORIO;
    private static final NotificacionEstado ESTADO_PENDIENTE = NotificacionEstado.PENDIENTE;
    private static final NotificacionEstado ESTADO_LEIDA = NotificacionEstado.LEIDO;
    private static final String MENSAJE = "Tu reserva fue confirmada";

    private NotificacionDTO validDTO;
    private NotificacionCreateDTO createDTO;
    private NotificacionUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        validDTO = new NotificacionDTO();
        validDTO.setIdNotificacion(VALID_ID);
        validDTO.setUsuarioCedula(VALID_USUARIO_ID);
        validDTO.setTipoNotificacion(TIPO_ALERTA);
        validDTO.setEstado(ESTADO_PENDIENTE);
        validDTO.setMensaje(MENSAJE);
        validDTO.setFechaEnvio(LocalDateTime.now());

        createDTO = new NotificacionCreateDTO();
        createDTO.setUsuarioCedula(VALID_USUARIO_ID);
        createDTO.setTipoNotificacion(TIPO_ALERTA);
        createDTO.setMensaje(MENSAJE);

        updateDTO = new NotificacionUpdateDTO();
        updateDTO.setEstado(ESTADO_LEIDA);
        updateDTO.setMensaje("Notificación actualizada");
    }

    @Nested
    @DisplayName("Crear Notificación")
    class CrearNotificacion {

        @Test
        @DisplayName("Debería crear notificación exitosamente con datos válidos")
        void deberiaCrearNotificacionExitosamente() {
            given(notificacionDAO.createNotificacion(any(NotificacionCreateDTO.class))).willReturn(validDTO);

            NotificacionDTO resultado = notificacionService.crearNotificacion(createDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdNotificacion()).isEqualTo(VALID_ID);
            assertThat(resultado.getTipoNotificacion()).isEqualTo(TIPO_ALERTA);
            assertThat(resultado.getMensaje()).isEqualTo(MENSAJE);

            then(notificacionDAO).should().createNotificacion(createDTO);
        }

        @Test
        @DisplayName("Debería pasar los datos correctos al DAO al crear notificación")
        void deberiaPasarDatosCorrectosAlDAO() {
            ArgumentCaptor<NotificacionCreateDTO> captor = ArgumentCaptor.forClass(NotificacionCreateDTO.class);
            given(notificacionDAO.createNotificacion(any(NotificacionCreateDTO.class))).willReturn(validDTO);

            notificacionService.crearNotificacion(createDTO);

            then(notificacionDAO).should().createNotificacion(captor.capture());
            NotificacionCreateDTO captured = captor.getValue();
            assertThat(captured.getMensaje()).isEqualTo(MENSAJE);
            assertThat(captured.getTipoNotificacion()).isEqualTo(TIPO_ALERTA);
        }

        @Test
        @DisplayName("Debería manejar excepción si el DAO falla al crear notificación")
        void deberiaManejarExcepcionSiDAOFallaAlCrear() {
            given(notificacionDAO.createNotificacion(any(NotificacionCreateDTO.class)))
                    .willThrow(new RuntimeException("Error al guardar notificación"));

            assertThatThrownBy(() -> notificacionService.crearNotificacion(createDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Error al guardar notificación");
        }
    }

    @Nested
    @DisplayName("Obtener Notificación por ID")
    class ObtenerNotificacion {

        @Test
        @DisplayName("Debería obtener notificación correctamente por ID válido")
        void deberiaObtenerNotificacionPorId() {
            given(notificacionDAO.findById(VALID_ID)).willReturn(Optional.of(validDTO));

            NotificacionDTO resultado = notificacionService.obtenerNotificacionPorId(VALID_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdNotificacion()).isEqualTo(VALID_ID);
            then(notificacionDAO).should().findById(VALID_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción si la notificación no existe")
        void deberiaLanzarExcepcionSiNoExiste() {
            given(notificacionDAO.findById(VALID_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> notificacionService.obtenerNotificacionPorId(VALID_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("no encontrada");
        }
    }

    @Nested
    @DisplayName("Listar Notificaciones")
    class ListarNotificaciones {

        @Test
        @DisplayName("Debería listar todas las notificaciones correctamente")
        void deberiaListarNotificaciones() {
            NotificacionDTO n2 = new NotificacionDTO();
            n2.setIdNotificacion(2);
            n2.setMensaje("Otra notificación");

            given(notificacionDAO.findAll()).willReturn(Arrays.asList(validDTO, n2));

            List<NotificacionDTO> resultado = notificacionService.listarNotificaciones();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(1).getMensaje()).contains("Otra");
        }

        @Test
        @DisplayName("Debería retornar lista vacía si no hay notificaciones")
        void deberiaRetornarListaVacia() {
            given(notificacionDAO.findAll()).willReturn(Collections.emptyList());

            List<NotificacionDTO> resultado = notificacionService.listarNotificaciones();

            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("Actualizar Notificación")
    class ActualizarNotificacion {

        @Test
        @DisplayName("Debería actualizar notificación correctamente")
        void deberiaActualizarNotificacion() {
            NotificacionDTO actualizada = new NotificacionDTO();
            actualizada.setIdNotificacion(VALID_ID);
            actualizada.setEstado(ESTADO_LEIDA);

            given(notificacionDAO.update(eq(VALID_ID), any(NotificacionUpdateDTO.class)))
                    .willReturn(Optional.of(actualizada));

            NotificacionDTO resultado = notificacionService.actualizarNotificacion(VALID_ID, updateDTO);

            assertThat(resultado.getEstado()).isEqualTo(ESTADO_LEIDA);
            then(notificacionDAO).should().update(eq(VALID_ID), eq(updateDTO));
        }

        @Test
        @DisplayName("Debería lanzar excepción si no existe la notificación a actualizar")
        void deberiaLanzarExcepcionSiNoExisteAlActualizar() {
            given(notificacionDAO.update(eq(VALID_ID), any(NotificacionUpdateDTO.class)))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> notificacionService.actualizarNotificacion(VALID_ID, updateDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("no encontrada");
        }
    }

    @Nested
    @DisplayName("Eliminar Notificación")
    class EliminarNotificacion {

        @Test
        @DisplayName("Debería eliminar notificación exitosamente")
        void deberiaEliminarExitosamente() {
            given(notificacionDAO.deleteById(VALID_ID)).willReturn(true);

            assertThatCode(() -> notificacionService.eliminarNotificacion(VALID_ID))
                    .doesNotThrowAnyException();

            then(notificacionDAO).should().deleteById(VALID_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción si la notificación no existe al eliminar")
        void deberiaLanzarExcepcionSiNoExisteAlEliminar() {
            given(notificacionDAO.deleteById(VALID_ID)).willReturn(false);

            assertThatThrownBy(() -> notificacionService.eliminarNotificacion(VALID_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("no encontrada");
        }
    }

    @Nested
    @DisplayName("Búsquedas por Criterios")
    class BusquedasPorCriterios {

        @Test
        @DisplayName("Debería buscar notificaciones por tipo")
        void deberiaBuscarPorTipo() {
            given(notificacionDAO.findByTipo(TIPO_RECORDATORIO))
                    .willReturn(List.of(validDTO));

            List<NotificacionDTO> resultado = notificacionService.listarNotificacionesPorTipo(TIPO_RECORDATORIO);

            assertThat(resultado).hasSize(1);
            then(notificacionDAO).should().findByTipo(TIPO_RECORDATORIO);
        }

        @Test
        @DisplayName("Debería buscar notificaciones por estado")
        void deberiaBuscarPorEstado() {
            given(notificacionDAO.findByEstado(ESTADO_PENDIENTE))
                    .willReturn(List.of(validDTO));

            List<NotificacionDTO> resultado = notificacionService.listarNotificacionesPorEstado(ESTADO_PENDIENTE);

            assertThat(resultado).hasSize(1);
            then(notificacionDAO).should().findByEstado(ESTADO_PENDIENTE);
        }

        @Test
        @DisplayName("Debería buscar notificaciones por usuario")
        void deberiaBuscarPorUsuario() {
            given(notificacionDAO.findByUsuario(VALID_USUARIO_ID))
                    .willReturn(List.of(validDTO));

            List<NotificacionDTO> resultado = notificacionService.listarNotificacionesPorUsuario(VALID_USUARIO_ID);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getUsuarioCedula()).isEqualTo(VALID_USUARIO_ID);
        }
    }
}
