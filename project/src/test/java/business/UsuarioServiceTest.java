package business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.service.impl.UsuarioServiceImpl;
import com.coworking.project.persistenceLayer.dao.UsuarioDAO;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Unit Test")
public class UsuarioServiceTest {

    @Mock
    private UsuarioDAO usuarioDao;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioDTO validUserDto;
    private int validUserid;

    @BeforeEach
    public void setUp() {
        validUserDto = new UsuarioDTO();
        validUserDto.setIdRol(1);
        validUserDto.setNombreCompleto("John");
        validUserDto.setEmail("email@gmail.com");
        validUserDto.setTelefono("1234567890");
        validUserDto.setDireccion("123 Main St");
}

@Test
    @DisplayName("CERATE - usuario valido retorna usuario creado")
    void createUsser_validData_returnsCreatedUser() {
        // Arrange
        UsuarioDTO toCreate = new UsuarioDTO();
        toCreate.setNombreCompleto ("John");
        toCreate.setEmail("email@gmail.com");

        UsuarioDTO persisted = new UsuarioDTO();
        persisted.setNombreCompleto(toCreate.getNombreCompleto());
        
    }
}