package com.coworking.project.business.service;

import com.coworking.project.businessLayer.dto.RecursoCreateDTO;
import com.coworking.project.businessLayer.dto.RecursoDTO;
import com.coworking.project.businessLayer.dto.RecursoUpdateDTO;
import com.coworking.project.businessLayer.service.impl.RecursoServiceImpl;
import com.coworking.project.persistenceLayer.dao.RecursoDAO;
import com.coworking.project.util.RecursoEstado;
import com.coworking.project.util.TipoRecurso;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para RecursoServiceImpl.
 */
public class RecursoServiceTest {

    @Mock
    private RecursoDAO recursoDAO;

    @InjectMocks
    private RecursoServiceImpl recursoService;

    private RecursoDTO recursoDTO;
    private RecursoCreateDTO createDTO;
    private RecursoUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        recursoDTO = new RecursoDTO();
        recursoDTO.setIdRecurso(1L);
        recursoDTO.setTipoRecurso(TipoRecurso.SALA_REUNIONES);
        recursoDTO.setEstado(RecursoEstado.activo);

        createDTO = new RecursoCreateDTO();
        createDTO.setTipoRecurso(TipoRecurso.SALA_REUNIONES);
        createDTO.setRecursoEstado(RecursoEstado.activo);

        updateDTO = new RecursoUpdateDTO();
        updateDTO.setEstado(RecursoEstado.inactivo);
    }

    @Test
    void crearRecurso_DeberiaCrearRecursoExitosamente() {
        when(recursoDAO.createRecurso(any(RecursoCreateDTO.class))).thenReturn(recursoDTO);

        RecursoDTO resultado = recursoService.crearRecurso(createDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdRecurso());
        verify(recursoDAO, times(1)).createRecurso(any(RecursoCreateDTO.class));
    }

    @Test
    void obtenerRecursoPorId_DeberiaRetornarRecursoExistente() {
        when(recursoDAO.findById(1L)).thenReturn(Optional.of(recursoDTO));

        RecursoDTO resultado = recursoService.obtenerRecursoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdRecurso());
        verify(recursoDAO, times(1)).findById(1L);
    }

    @Test
    void obtenerRecursoPorId_DeberiaLanzarExcepcionSiNoExiste() {
        when(recursoDAO.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> recursoService.obtenerRecursoPorId(1L));

        assertTrue(ex.getMessage().contains("no encontrado"));
        verify(recursoDAO, times(1)).findById(1L);
    }

    @Test
    void listarRecursos_DeberiaRetornarListaDeRecursos() {
        when(recursoDAO.findAll()).thenReturn(List.of(recursoDTO));

        List<RecursoDTO> resultado = recursoService.listarRecursos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(recursoDAO, times(1)).findAll();
    }

    @Test
    void actualizarRecurso_DeberiaActualizarRecursoExistente() {
        when(recursoDAO.update(eq(1L), any(RecursoUpdateDTO.class))).thenReturn(Optional.of(recursoDTO));

        RecursoDTO resultado = recursoService.actualizarRecurso(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdRecurso());
        verify(recursoDAO, times(1)).update(eq(1L), any(RecursoUpdateDTO.class));
    }

    @Test
    void actualizarRecurso_DeberiaLanzarExcepcionSiNoExiste() {
        when(recursoDAO.update(eq(1L), any(RecursoUpdateDTO.class))).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> recursoService.actualizarRecurso(1L, updateDTO));

        assertTrue(ex.getMessage().contains("no encontrado"));
        verify(recursoDAO, times(1)).update(eq(1L), any(RecursoUpdateDTO.class));
    }

    @Test
    void eliminarRecurso_DeberiaEliminarRecursoExistente() {
        when(recursoDAO.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> recursoService.eliminarRecurso(1L));
        verify(recursoDAO, times(1)).deleteById(1L);
    }

    @Test
    void eliminarRecurso_DeberiaLanzarExcepcionSiNoExiste() {
        when(recursoDAO.deleteById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> recursoService.eliminarRecurso(1L));

        assertTrue(ex.getMessage().contains("no encontrado"));
        verify(recursoDAO, times(1)).deleteById(1L);
    }

    @Test
    void obtenerRecursosPorTipo_DeberiaRetornarListaDeRecursos() {
        when(recursoDAO.findByTipoRecurso(TipoRecurso.SALA_REUNIONES)).thenReturn(List.of(recursoDTO));

        List<RecursoDTO> resultado = recursoService.obtenerRecursosPorTipo(TipoRecurso.SALA_REUNIONES);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(recursoDAO, times(1)).findByTipoRecurso(TipoRecurso.SALA_REUNIONES);
    }

    @Test
    void listarRecursosActivos_DeberiaRetornarListaDeRecursosActivos() {
        when(recursoDAO.findByEstado(RecursoEstado.activo)).thenReturn(List.of(recursoDTO));

        List<RecursoDTO> resultado = recursoService.listarRecursosActivos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(RecursoEstado.activo, resultado.get(0).getEstado());
        verify(recursoDAO, times(1)).findByEstado(RecursoEstado.activo);
    }

    @Test
    void buscarPorEstado_DeberiaRetornarListaDeRecursosPorEstado() {
        when(recursoDAO.findByEstado(RecursoEstado.inactivo)).thenReturn(List.of(recursoDTO));

        List<RecursoDTO> resultado = recursoService.buscarPorEstado(RecursoEstado.inactivo);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(recursoDAO, times(1)).findByEstado(RecursoEstado.inactivo);
    }
}
