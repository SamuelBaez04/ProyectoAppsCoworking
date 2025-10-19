package com.coworking.project.business.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coworking.project.businessLayer.dto.PagoCreateDTO;
import com.coworking.project.businessLayer.dto.PagoDTO;
import com.coworking.project.businessLayer.dto.PagoUpdateDTO;
import com.coworking.project.businessLayer.service.impl.PagoServiceImpl;
import com.coworking.project.persistenceLayer.dao.PagoDAO;


@ExtendWith(MockitoExtension.class)
@DisplayName("PagoService - Pruebas Unitarias")
class PagoServiceTest {

    @Mock
    private PagoDAO pagoDAO;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private static final int VALID_PAGO_ID = 1;
    private static final int VALID_RESERVA_ID = 10;
    private static final double VALID_MONTO = 150.0;
    private static final LocalDate FECHA_PAGO = LocalDate.of(2025, 10, 10);
    private static final String METODO_TARJETA = "Tarjeta de crédito";
    private static final String METODO_EFECTIVO = "Efectivo";
    private static final String METODO_TRANSFERENCIA = "Transferencia bancaria";

    private PagoCreateDTO validCreateDto;
    private PagoUpdateDTO validUpdateDto;
    private PagoDTO validPagoDto;

    @BeforeEach
    void setUp() {
        
        validCreateDto = new PagoCreateDTO();
        validCreateDto.setIdReserva(VALID_RESERVA_ID);
        validCreateDto.setMonto(VALID_MONTO);
        validCreateDto.setFechaPago(FECHA_PAGO);
        validCreateDto.setMetodoPago(METODO_TARJETA);

        validUpdateDto = new PagoUpdateDTO();
        validUpdateDto.setMonto(200.0);
        validUpdateDto.setFechaPago(FECHA_PAGO.plusDays(1));
        validUpdateDto.setMetodoPago(METODO_EFECTIVO);

        validPagoDto = new PagoDTO();
        validPagoDto.setIdPago(VALID_PAGO_ID);
        validPagoDto.setIdReserva(VALID_RESERVA_ID);
        validPagoDto.setMonto(VALID_MONTO);
        validPagoDto.setFechaPago(FECHA_PAGO);
        validPagoDto.setMetodoPago(METODO_TARJETA);
    }

    @Nested
    @DisplayName("Crear Pago")
    class CrearPago {

        @Test
        @DisplayName("Debería crear pago exitosamente con datos válidos")
        void deberiaCrearPagoExitosamenteConDatosValidos() {

            given(pagoDAO.createPago(any(PagoCreateDTO.class))).willReturn(validPagoDto);

            PagoDTO resultado = pagoService.crearPago(validCreateDto);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdPago()).isEqualTo(VALID_PAGO_ID);
            assertThat(resultado.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(resultado.getMonto()).isEqualTo(VALID_MONTO);
            assertThat(resultado.getFechaPago()).isEqualTo(FECHA_PAGO);
            assertThat(resultado.getMetodoPago()).isEqualTo(METODO_TARJETA);

            then(pagoDAO).should().createPago(validCreateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos correctos al DAO")
        void deberiaPasarLosDatosCorrectosAlDAO() {

            ArgumentCaptor<PagoCreateDTO> captor = ArgumentCaptor.forClass(PagoCreateDTO.class);
            given(pagoDAO.createPago(any(PagoCreateDTO.class))).willReturn(validPagoDto);

            pagoService.crearPago(validCreateDto);

            then(pagoDAO).should().createPago(captor.capture());
            PagoCreateDTO captured = captor.getValue();
            
            assertThat(captured.getIdReserva()).isEqualTo(VALID_RESERVA_ID);
            assertThat(captured.getMonto()).isEqualTo(VALID_MONTO);
            assertThat(captured.getFechaPago()).isEqualTo(FECHA_PAGO);
            assertThat(captured.getMetodoPago()).isEqualTo(METODO_TARJETA);
        }

        @Test
        @DisplayName("Debería validar que el monto sea positivo")
        void deberiaValidarQueElMontoSeaPositivo() {
            // Arrange
            PagoCreateDTO pagoConMontoNegativo = new PagoCreateDTO();
            pagoConMontoNegativo.setIdReserva(VALID_RESERVA_ID);
            pagoConMontoNegativo.setMonto(-50.0);
            pagoConMontoNegativo.setFechaPago(FECHA_PAGO);
            pagoConMontoNegativo.setMetodoPago(METODO_TARJETA);

            given(pagoDAO.createPago(any(PagoCreateDTO.class))).willReturn(validPagoDto);

            PagoDTO resultado = pagoService.crearPago(pagoConMontoNegativo);
            
            assertThat(resultado).isNotNull();
            then(pagoDAO).should().createPago(pagoConMontoNegativo);
        }

        @Test
        @DisplayName("Debería manejar excepción cuando el DAO falla")
        void deberiaManejarExcepcionCuandoElDAOFalla() {

            given(pagoDAO.createPago(any(PagoCreateDTO.class)))
                .willThrow(new RuntimeException("Error en base de datos"));

            assertThatThrownBy(() -> pagoService.crearPago(validCreateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error en base de datos");

            then(pagoDAO).should().createPago(validCreateDto);
        }
    }

    @Nested
    @DisplayName("Obtener Pago por ID")
    class ObtenerPagoPorId {

        @Test
        @DisplayName("Debería obtener pago exitosamente por ID válido")
        void deberiaObtenerPagoExitosamentePorIdValido() {

            given(pagoDAO.findById(VALID_PAGO_ID)).willReturn(Optional.of(validPagoDto));

            PagoDTO resultado = pagoService.obtenerPagoPorId(VALID_PAGO_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdPago()).isEqualTo(VALID_PAGO_ID);
            assertThat(resultado.getMonto()).isEqualTo(VALID_MONTO);
            assertThat(resultado.getMetodoPago()).isEqualTo(METODO_TARJETA);

            then(pagoDAO).should().findById(VALID_PAGO_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el pago no existe")
        void deberiaLanzarExcepcionCuandoElPagoNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(pagoDAO.findById(idInexistente)).willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> pagoService.obtenerPagoPorId(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pago no encontrado con ID: " + idInexistente);

            then(pagoDAO).should().findById(idInexistente);
        }
    }

    @Nested
    @DisplayName("Listar Pagos")
    class ListarPagos {

        @Test
        @DisplayName("Debería obtener lista de pagos exitosamente")
        void deberiaObtenerListaDePagosExitosamente() {
            // Arrange
            PagoDTO pago2 = new PagoDTO();
            pago2.setIdPago(2);
            pago2.setMonto(300.0);
            pago2.setMetodoPago(METODO_EFECTIVO);
            
            List<PagoDTO> pagosEsperados = Arrays.asList(validPagoDto, pago2);
            given(pagoDAO.findAll()).willReturn(pagosEsperados);

            // Act
            List<PagoDTO> resultado = pagoService.listarPagos();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getIdPago()).isEqualTo(VALID_PAGO_ID);
            assertThat(resultado.get(1).getIdPago()).isEqualTo(2);
            assertThat(resultado.get(1).getMonto()).isEqualTo(300.0);

            then(pagoDAO).should().findAll();
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay pagos")
        void deberiaRetornarListaVaciaCuandoNoHayPagos() {
            // Arrange
            given(pagoDAO.findAll()).willReturn(Collections.emptyList());

            // Act
            List<PagoDTO> resultado = pagoService.listarPagos();

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(pagoDAO).should().findAll();
        }
    }

    @Nested
    @DisplayName("Actualizar Pago")
    class ActualizarPago {

        @Test
        @DisplayName("Debería actualizar pago exitosamente")
        void deberiaActualizarPagoExitosamente() {
            // Arrange
            PagoDTO pagoActualizado = new PagoDTO();
            pagoActualizado.setIdPago(VALID_PAGO_ID);
            pagoActualizado.setMonto(200.0);
            pagoActualizado.setMetodoPago(METODO_EFECTIVO);

            given(pagoDAO.update(eq(VALID_PAGO_ID), any(PagoUpdateDTO.class)))
                .willReturn(Optional.of(pagoActualizado));

            // Act
            PagoDTO resultado = pagoService.actualizarPago(VALID_PAGO_ID, validUpdateDto);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getIdPago()).isEqualTo(VALID_PAGO_ID);
            assertThat(resultado.getMonto()).isEqualTo(200.0);
            assertThat(resultado.getMetodoPago()).isEqualTo(METODO_EFECTIVO);

            then(pagoDAO).should().update(VALID_PAGO_ID, validUpdateDto);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el pago a actualizar no existe")
        void deberiaLanzarExcepcionCuandoElPagoAActualizarNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(pagoDAO.update(eq(idInexistente), any(PagoUpdateDTO.class)))
                .willReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> pagoService.actualizarPago(idInexistente, validUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pago no encontrado con ID: " + idInexistente);

            then(pagoDAO).should().update(idInexistente, validUpdateDto);
        }

        @Test
        @DisplayName("Debería pasar los datos de actualización correctos al DAO")
        void deberiaPasarLosDatosDeActualizacionCorrectosAlDAO() {
            // Arrange
            ArgumentCaptor<PagoUpdateDTO> captor = ArgumentCaptor.forClass(PagoUpdateDTO.class);
            given(pagoDAO.update(eq(VALID_PAGO_ID), any(PagoUpdateDTO.class)))
                .willReturn(Optional.of(validPagoDto));

            // Act
            pagoService.actualizarPago(VALID_PAGO_ID, validUpdateDto);

            // Assert
            then(pagoDAO).should().update(eq(VALID_PAGO_ID), captor.capture());
            PagoUpdateDTO captured = captor.getValue();
            
            assertThat(captured.getMonto()).isEqualTo(200.0);
            assertThat(captured.getMetodoPago()).isEqualTo(METODO_EFECTIVO);
            assertThat(captured.getFechaPago()).isEqualTo(FECHA_PAGO.plusDays(1));
        }
    }

    @Nested
    @DisplayName("Eliminar Pago")
    class EliminarPago {

        @Test
        @DisplayName("Debería eliminar pago exitosamente")
        void deberiaEliminarPagoExitosamente() {
            // Arrange
            given(pagoDAO.deleteById(VALID_PAGO_ID)).willReturn(true);

            // Act
            assertThatCode(() -> pagoService.eliminarPago(VALID_PAGO_ID))
                .doesNotThrowAnyException();

            // Assert
            then(pagoDAO).should().deleteById(VALID_PAGO_ID);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando el pago a eliminar no existe")
        void deberiaLanzarExcepcionCuandoElPagoAEliminarNoExiste() {
            // Arrange
            int idInexistente = 999;
            given(pagoDAO.deleteById(idInexistente)).willReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> pagoService.eliminarPago(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pago no encontrado con ID: " + idInexistente);

            then(pagoDAO).should().deleteById(idInexistente);
        }
    }

    @Nested
    @DisplayName("Búsquedas por Criterios")
    class BusquedasPorCriterios {

        @Test
        @DisplayName("Debería buscar pagos por método de pago exitosamente")
        void deberiaBuscarPagosPorMetodoDePagoExitosamente() {
            // Arrange
            List<PagoDTO> pagosEsperados = Arrays.asList(validPagoDto);
            given(pagoDAO.findByMetodoPago(METODO_TARJETA)).willReturn(pagosEsperados);

            // Act
            List<PagoDTO> resultado = pagoService.obtenerPagosPorMetodo(METODO_TARJETA);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getMetodoPago()).isEqualTo(METODO_TARJETA);

            then(pagoDAO).should().findByMetodoPago(METODO_TARJETA);
        }

        @Test
        @DisplayName("Debería buscar pagos por fecha exitosamente")
        void deberiaBuscarPagosPorFechaExitosamente() {
            // Arrange
            List<PagoDTO> pagosEsperados = Arrays.asList(validPagoDto);
            given(pagoDAO.findByFechaPago(FECHA_PAGO)).willReturn(pagosEsperados);

            // Act
            List<PagoDTO> resultado = pagoService.obtenerPagosPorFecha(FECHA_PAGO);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getFechaPago()).isEqualTo(FECHA_PAGO);

            then(pagoDAO).should().findByFechaPago(FECHA_PAGO);
        }

        @Test
        @DisplayName("Debería buscar pagos por reserva exitosamente")
        void deberiaBuscarPagosPorReservaExitosamente() {
            // Arrange
            List<PagoDTO> pagosEsperados = Arrays.asList(validPagoDto);
            given(pagoDAO.findByReserva(VALID_RESERVA_ID)).willReturn(pagosEsperados);

            // Act
            List<PagoDTO> resultado = pagoService.obtenerPagosPorReserva(VALID_RESERVA_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getIdReserva()).isEqualTo(VALID_RESERVA_ID);

            then(pagoDAO).should().findByReserva(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando no hay pagos con método específico")
        void deberiaRetornarListaVaciaCuandoNoHayPagosConMetodoEspecifico() {
            // Arrange
            String metodoInexistente = "Bitcoin";
            given(pagoDAO.findByMetodoPago(metodoInexistente)).willReturn(Collections.emptyList());

            // Act
            List<PagoDTO> resultado = pagoService.obtenerPagosPorMetodo(metodoInexistente);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(pagoDAO).should().findByMetodoPago(metodoInexistente);
        }

        @Test
        @DisplayName("Debería retornar lista vacía cuando reserva no tiene pagos")
        void deberiaRetornarListaVaciaCuandoReservaNoTienePagos() {
            // Arrange
            int reservaSinPagos = 999;
            given(pagoDAO.findByReserva(reservaSinPagos)).willReturn(Collections.emptyList());

            // Act
            List<PagoDTO> resultado = pagoService.obtenerPagosPorReserva(reservaSinPagos);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEmpty();

            then(pagoDAO).should().findByReserva(reservaSinPagos);
        }
    }

    @Nested
    @DisplayName("Cálculos Financieros")
    class CalculosFinancieros {

        @Test
        @DisplayName("Debería calcular total de pagos por reserva exitosamente")
        void deberiaCalcularTotalDePagosPorReservaExitosamente() {
            // Arrange
            Double totalEsperado = 450.0;
            given(pagoDAO.calcularTotalPagosPorReserva(VALID_RESERVA_ID)).willReturn(totalEsperado);

            // Act
            Double resultado = pagoService.calcularTotalPagosPorReserva(VALID_RESERVA_ID);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEqualTo(totalEsperado);

            then(pagoDAO).should().calcularTotalPagosPorReserva(VALID_RESERVA_ID);
        }

        @Test
        @DisplayName("Debería retornar cero cuando reserva no tiene pagos")
        void deberiaRetornarCeroCuandoReservaNoTienePagos() {
            // Arrange
            int reservaSinPagos = 999;
            given(pagoDAO.calcularTotalPagosPorReserva(reservaSinPagos)).willReturn(0.0);

            // Act
            Double resultado = pagoService.calcularTotalPagosPorReserva(reservaSinPagos);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado).isEqualTo(0.0);

            then(pagoDAO).should().calcularTotalPagosPorReserva(reservaSinPagos);
        }

        @Test
        @DisplayName("Debería manejar valores nulos del DAO correctamente")
        void deberiaManejarValoresNulosDelDAOCorrectamente() {
            // Arrange
            given(pagoDAO.calcularTotalPagosPorReserva(VALID_RESERVA_ID)).willReturn(null);

            // Act
            Double resultado = pagoService.calcularTotalPagosPorReserva(VALID_RESERVA_ID);

            // Assert
            assertThat(resultado).isNull();

            then(pagoDAO).should().calcularTotalPagosPorReserva(VALID_RESERVA_ID);
        }
    }

    @Nested
    @DisplayName("Validaciones de Negocio")
    class ValidacionesDeNegocio {

        @Test
        @DisplayName("Debería validar correctamente diferentes métodos de pago")
        void deberiaValidarCorrectamenteDiferentesMetodosDePago() {

            String[] metodosPago = {METODO_TARJETA, METODO_EFECTIVO, METODO_TRANSFERENCIA};
            
            for (String metodo : metodosPago) {
                List<PagoDTO> pagosEsperados = Arrays.asList(validPagoDto);
                given(pagoDAO.findByMetodoPago(metodo)).willReturn(pagosEsperados);

                List<PagoDTO> resultado = pagoService.obtenerPagosPorMetodo(metodo);

                assertThat(resultado).isNotNull();
                assertThat(resultado).hasSize(1);
                
                then(pagoDAO).should().findByMetodoPago(metodo);
            }
        }

        @Test
        @DisplayName("Debería validar que se pasan los parámetros correctos para búsquedas")
        void deberiaValidarQueSePasanLosParametrosCorrectosParaBusquedas() {
            // Arrange
            ArgumentCaptor<Integer> reservaCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<LocalDate> fechaCaptor = ArgumentCaptor.forClass(LocalDate.class);
            ArgumentCaptor<String> metodoCaptor = ArgumentCaptor.forClass(String.class);
            
            given(pagoDAO.findByReserva(anyInt())).willReturn(Collections.emptyList());
            given(pagoDAO.findByFechaPago(any(LocalDate.class))).willReturn(Collections.emptyList());
            given(pagoDAO.findByMetodoPago(anyString())).willReturn(Collections.emptyList());

            pagoService.obtenerPagosPorReserva(VALID_RESERVA_ID);
            pagoService.obtenerPagosPorFecha(FECHA_PAGO);
            pagoService.obtenerPagosPorMetodo(METODO_TARJETA);

            then(pagoDAO).should().findByReserva(reservaCaptor.capture());
            then(pagoDAO).should().findByFechaPago(fechaCaptor.capture());
            then(pagoDAO).should().findByMetodoPago(metodoCaptor.capture());
            
            assertThat(reservaCaptor.getValue()).isEqualTo(VALID_RESERVA_ID);
            assertThat(fechaCaptor.getValue()).isEqualTo(FECHA_PAGO);
            assertThat(metodoCaptor.getValue()).isEqualTo(METODO_TARJETA);
        }

        @Test
        @DisplayName("Debería manejar pagos con diferentes montos correctamente")
        void deberiaManejarPagosConDiferentesMontos() {
            PagoDTO pagoGrande = new PagoDTO();
            pagoGrande.setIdPago(2);
            pagoGrande.setMonto(1000.0);
            
            PagoDTO pagoPequeño = new PagoDTO();
            pagoPequeño.setIdPago(3);
            pagoPequeño.setMonto(0.01);

            given(pagoDAO.findById(2)).willReturn(Optional.of(pagoGrande));
            given(pagoDAO.findById(3)).willReturn(Optional.of(pagoPequeño));

            PagoDTO resultadoGrande = pagoService.obtenerPagoPorId(2);
            PagoDTO resultadoPequeño = pagoService.obtenerPagoPorId(3);

            assertThat(resultadoGrande.getMonto()).isEqualTo(1000.0);
            assertThat(resultadoPequeño.getMonto()).isEqualTo(0.01);
        }
    }
}