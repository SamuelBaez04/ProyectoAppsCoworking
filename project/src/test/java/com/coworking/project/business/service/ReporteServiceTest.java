package com.coworking.project.business.service;

import java.time.LocalDate;
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
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coworking.project.businessLayer.dto.ReporteCreateDTO;
import com.coworking.project.businessLayer.dto.ReporteDTO;
import com.coworking.project.businessLayer.dto.ReporteUpdateDTO;
import com.coworking.project.businessLayer.service.impl.ReporteServiceImpl;
import com.coworking.project.persistenceLayer.dao.ReporteDAO;
import com.coworking.project.util.TipoReporte;

/**
 * Unit Tests para ReporteServiceImpl
 *
 * OBJETIVO: Probar la lógica de negocio del servicio de forma aislada
 * - No requiere base de datos
 * - No requiere Spring Context
 * - Usa mocks para dependencias
 * - Ejecución rápida
 * 
 * Estructura AAA:
 * - Arrange: Configuración de datos y mocks
 * - Act: Ejecutar método bajo prueba
 * - Assert: Verificar resultado y comportamiento
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReporteService - Unit Tests")
public class ReporteServiceTest {

    @Mock
    private ReporteDAO reporteDAO;

    @InjectMocks
    private ReporteServiceImpl reporteService;

    private ReporteCreateDTO validCreateDTO;
    private ReporteDTO validReporteDTO;
    private ReporteUpdateDTO validUpdateDTO;
    private Integer validReporteId;
    
    @BeforeEach
    void setUp() {
        validReporteId = 1;
        
        // Setup valid ReporteCreateDTO
        validCreateDTO = new ReporteCreateDTO();
        validCreateDTO.setTitulo("Reporte de Ingresos Mensual");
        validCreateDTO.setDescripcion("Informe detallado de ingresos");
        validCreateDTO.setFechaGeneracion(LocalDate.of(2025, 10, 17));
        validCreateDTO.setTipoReporte(TipoReporte.INGRESOS);
        validCreateDTO.setTotalRegistros(45);
        validCreateDTO.setMontoTotal(12500.50);
        
        // Setup valid ReporteDTO
        validReporteDTO = new ReporteDTO();
        validReporteDTO.setIdReporte(validReporteId);
        validReporteDTO.setTitulo(validCreateDTO.getTitulo());
        validReporteDTO.setDescripcion(validCreateDTO.getDescripcion());
        validReporteDTO.setFechaGeneracion(validCreateDTO.getFechaGeneracion());
        validReporteDTO.setTipoReporte(validCreateDTO.getTipoReporte());
        validReporteDTO.setTotalRegistros(validCreateDTO.getTotalRegistros());
        validReporteDTO.setMontoTotal(validCreateDTO.getMontoTotal());
        
        // Setup valid ReporteUpdateDTO
        validUpdateDTO = new ReporteUpdateDTO();
        validUpdateDTO.setTitulo("Reporte Actualizado");
        validUpdateDTO.setDescripcion("Descripción actualizada");
    }

    @Nested
    @DisplayName("Crear Reporte")
    class CrearReporte {

        @Test
        @DisplayName("CREATE - reporte válido retorna reporte creado")
        void crearReporte_validData_returnsCreatedReporte() {
            // Arrange
            ReporteCreateDTO toCreate = new ReporteCreateDTO();
            toCreate.setTitulo("Nuevo Reporte");
            toCreate.setTipoReporte(TipoReporte.OCUPACION);

            ReporteDTO persisted = new ReporteDTO();
            persisted.setIdReporte(validReporteId);
            persisted.setTitulo(toCreate.getTitulo());
            persisted.setTipoReporte(toCreate.getTipoReporte());
            persisted.setFechaGeneracion(LocalDate.now());

            when(reporteDAO.createReporte(any(ReporteCreateDTO.class))).thenReturn(persisted);

            // Act
            ReporteDTO result = reporteService.crearReporte(toCreate);

            // Assert - estado
            assertThat(result).isNotNull();
            assertThat(result.getIdReporte()).isEqualTo(validReporteId);
            assertThat(result.getTitulo()).isEqualTo(toCreate.getTitulo());
            assertThat(result.getTipoReporte()).isEqualTo(toCreate.getTipoReporte());
            assertThat(result.getFechaGeneracion()).isNotNull();

            // Assert - comportamiento
            ArgumentCaptor<ReporteCreateDTO> captor = ArgumentCaptor.forClass(ReporteCreateDTO.class);
            verify(reporteDAO, times(1)).createReporte(captor.capture());
            ReporteCreateDTO passed = captor.getValue();
            assertThat(passed.getTitulo()).isEqualTo(toCreate.getTitulo());
            assertThat(passed.getFechaGeneracion()).isNotNull(); // Service asigna fecha si es null
        }

        @Test
        @DisplayName("CREATE - fecha generación null se asigna automáticamente")
        void crearReporte_nullFechaGeneracion_assignsCurrentDate() {
            // Arrange
            ReporteCreateDTO toCreate = new ReporteCreateDTO();
            toCreate.setTitulo("Reporte Sin Fecha");
            toCreate.setTipoReporte(TipoReporte.GENERAL);
            toCreate.setFechaGeneracion(null);

            ReporteDTO persisted = new ReporteDTO();
            persisted.setIdReporte(validReporteId);
            persisted.setFechaGeneracion(LocalDate.now());

            when(reporteDAO.createReporte(any(ReporteCreateDTO.class))).thenReturn(persisted);

            // Act
            ReporteDTO result = reporteService.crearReporte(toCreate);

            // Assert
            assertThat(result.getFechaGeneracion()).isEqualTo(LocalDate.now());
            
            ArgumentCaptor<ReporteCreateDTO> captor = ArgumentCaptor.forClass(ReporteCreateDTO.class);
            verify(reporteDAO, times(1)).createReporte(captor.capture());
            ReporteCreateDTO passed = captor.getValue();
            assertThat(passed.getFechaGeneracion()).isEqualTo(LocalDate.now());
        }
    }

    @Nested
    @DisplayName("Obtener Reporte")
    class ObtenerReporte {

        @Test
        @DisplayName("GET by id - reporte existente retorna DTO")
        void obtenerReportePorId_existing_returnsReporte() {
            // Arrange
            when(reporteDAO.findById(validReporteId)).thenReturn(Optional.of(validReporteDTO));

            // Act
            ReporteDTO result = reporteService.obtenerReportePorId(validReporteId);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getIdReporte()).isEqualTo(validReporteId);
            assertThat(result.getTitulo()).isEqualTo(validReporteDTO.getTitulo());
            verify(reporteDAO, times(1)).findById(validReporteId);
        }

        @Test
        @DisplayName("GET by id - no existente lanza RuntimeException")
        void obtenerReportePorId_notFound_throws() {
            // Arrange
            Integer nonExistentId = 999;
            when(reporteDAO.findById(nonExistentId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> reporteService.obtenerReportePorId(nonExistentId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Reporte no encontrado con ID: " + nonExistentId);

            verify(reporteDAO, times(1)).findById(nonExistentId);
        }

        @Test
        @DisplayName("GET all - retorna lista no vacía")
        void listarReportes_nonEmpty() {
            // Arrange
            List<ReporteDTO> reportes = List.of(validReporteDTO);
            when(reporteDAO.findAll()).thenReturn(reportes);

            // Act
            List<ReporteDTO> results = reporteService.listarReportes();

            // Assert
            assertThat(results).isNotEmpty();
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getTitulo()).isEqualTo(validReporteDTO.getTitulo());
            verify(reporteDAO, times(1)).findAll();
        }

        @Test
        @DisplayName("GET all - retorna lista vacía correctamente")
        void listarReportes_empty() {
            // Arrange
            when(reporteDAO.findAll()).thenReturn(List.of());

            // Act
            List<ReporteDTO> results = reporteService.listarReportes();

            // Assert
            assertThat(results).isEmpty();
            verify(reporteDAO, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Actualizar Reporte")
    class ActualizarReporte {

        @Test
        @DisplayName("UPDATE - actualiza reporte existente correctamente")
        void actualizarReporte_valid_updatesSuccessfully() {
            // Arrange
            ReporteUpdateDTO updateData = new ReporteUpdateDTO();
            updateData.setTitulo("Título Actualizado");
            updateData.setDescripcion("Nueva descripción");

            ReporteDTO updated = new ReporteDTO();
            updated.setIdReporte(validReporteId);
            updated.setTitulo(updateData.getTitulo());
            updated.setDescripcion(updateData.getDescripcion());

            when(reporteDAO.update(eq(validReporteId), any(ReporteUpdateDTO.class)))
                    .thenReturn(Optional.of(updated));

            // Act
            ReporteDTO result = reporteService.actualizarReporte(validReporteId, updateData);

            // Assert - estado
            assertThat(result).isNotNull();
            assertThat(result.getIdReporte()).isEqualTo(validReporteId);
            assertThat(result.getTitulo()).isEqualTo(updateData.getTitulo());
            assertThat(result.getDescripcion()).isEqualTo(updateData.getDescripcion());

            // Assert - comportamiento
            verify(reporteDAO, times(1)).update(eq(validReporteId), eq(updateData));
        }

        @Test
        @DisplayName("UPDATE - reporte no existente lanza RuntimeException")
        void actualizarReporte_notFound_throws() {
            // Arrange
            Integer nonExistentId = 999;
            when(reporteDAO.update(eq(nonExistentId), any(ReporteUpdateDTO.class)))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> reporteService.actualizarReporte(nonExistentId, validUpdateDTO))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Reporte no encontrado con ID: " + nonExistentId);

            verify(reporteDAO, times(1)).update(eq(nonExistentId), eq(validUpdateDTO));
        }
    }

    @Nested
    @DisplayName("Eliminar Reporte")
    class EliminarReporte {

        @Test
        @DisplayName("DELETE - reporte existente se elimina correctamente")
        void eliminarReporte_existing_deletesSuccessfully() {
            // Arrange
            when(reporteDAO.deleteById(validReporteId)).thenReturn(true);

            // Act & Assert (no debe lanzar excepción)
            assertThatCode(() -> reporteService.eliminarReporte(validReporteId))
                    .doesNotThrowAnyException();

            verify(reporteDAO, times(1)).deleteById(validReporteId);
        }

        @Test
        @DisplayName("DELETE - reporte no existente lanza RuntimeException")
        void eliminarReporte_notFound_throws() {
            // Arrange
            Integer nonExistentId = 999;
            when(reporteDAO.deleteById(nonExistentId)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> reporteService.eliminarReporte(nonExistentId))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Reporte no encontrado con ID: " + nonExistentId);

            verify(reporteDAO, times(1)).deleteById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("Consultas Específicas")
    class ConsultasEspecificas {

        @Test
        @DisplayName("GET by tipo - retorna reportes del tipo especificado")
        void obtenerReportesPorTipo_validType_returnsReportes() {
            // Arrange
            TipoReporte tipo = TipoReporte.INGRESOS;
            List<ReporteDTO> reportes = List.of(validReporteDTO);
            when(reporteDAO.findByTipoReporte(tipo)).thenReturn(reportes);

            // Act
            List<ReporteDTO> results = reporteService.obtenerReportesPorTipo(tipo);

            // Assert
            assertThat(results).isNotEmpty();
            assertThat(results).hasSize(1);
            assertThat(results.get(0).getTipoReporte()).isEqualTo(tipo);
            verify(reporteDAO, times(1)).findByTipoReporte(tipo);
        }

        @Test
        @DisplayName("GET by fecha - retorna reportes de la fecha especificada")
        void listarReportesPorFecha_validDate_returnsReportes() {
            // Arrange
            LocalDate fecha = LocalDate.of(2025, 10, 17);
            List<ReporteDTO> reportes = List.of(validReporteDTO);
            when(reporteDAO.findByFechaGeneracion(fecha)).thenReturn(reportes);

            // Act
            List<ReporteDTO> results = reporteService.listarReportesPorFecha(fecha);

            // Assert
            assertThat(results).isNotEmpty();
            assertThat(results.get(0).getFechaGeneracion()).isEqualTo(fecha);
            verify(reporteDAO, times(1)).findByFechaGeneracion(fecha);
        }

        @Test
        @DisplayName("GET by rango fechas - retorna reportes en el rango")
        void listarReportesPorRangoDeFechas_validRange_returnsReportes() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2025, 10, 1);
            LocalDate fechaFin = LocalDate.of(2025, 10, 31);
            List<ReporteDTO> reportes = List.of(validReporteDTO);
            when(reporteDAO.findReportesPorRangoDeFechas(fechaInicio, fechaFin)).thenReturn(reportes);

            // Act
            List<ReporteDTO> results = reporteService.listarReportesPorRangoDeFechas(fechaInicio, fechaFin);

            // Assert
            assertThat(results).isNotEmpty();
            verify(reporteDAO, times(1)).findReportesPorRangoDeFechas(fechaInicio, fechaFin);
        }
    }
}