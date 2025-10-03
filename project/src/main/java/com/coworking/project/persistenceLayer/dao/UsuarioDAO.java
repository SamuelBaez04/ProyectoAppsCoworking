package com.coworking.project.persistenceLayer.dao;



import com.coworking.project.businessLayer.dto.UsuarioCreateDTO;
import com.coworking.project.businessLayer.dto.UsuarioDTO;
import com.coworking.project.businessLayer.dto.UsuarioUpdateDTO;
import com.coworking.project.persistenceLayer.entity.UsuarioEntity;
import com.coworking.project.persistenceLayer.mapper.UsuarioMapper;
import com.coworking.project.persistenceLayer.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioDAO {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioDTO crearUsuario(UsuarioCreateDTO createDTO) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(createDTO);
        UsuarioEntity savedEntity = usuarioRepository.save(usuarioEntity);
        return usuarioMapper.toDTO(savedEntity);
    }

    public Optional<UsuarioDTO> findyById(int id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDTO);
    }

    public List<UsuarioDTO> findAll() {
        List<UsuarioEntity> entities = usuarioRepository.findAll();
        return usuarioMapper.toDTOList(entities);
    }

    public Optional<UsuarioDTO> update(int id, UsuarioUpdateDTO usuarioUpdateDTO) {
        return usuarioRepository.findById(id).map(existingEntity ->{
           usuarioMapper.updateEntityFromDTO(usuarioUpdateDTO, existingEntity);
           UsuarioEntity updatedEntity = usuarioRepository.save(existingEntity);
           return usuarioMapper.toDTO(updatedEntity);
        });
    }

    public boolean deleteById(int id){
        if(usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Optional<UsuarioDTO> findByEmail(String email){
        return usuarioRepository.findByEmail(email).map(usuarioMapper::toDTO);
    }

    public List<UsuarioDTO> findByNombreCompletoContaining(String nombreCompleto){
        List<UsuarioEntity> entities = usuarioRepository.findByNombreCompletoContainingIgnoreCase(nombreCompleto);
        return usuarioMapper.toDTOList(entities);
    }

    public List<UsuarioDTO> findUsuariosConReservasActivas(){
        List<UsuarioEntity> entities = usuarioRepository.findUsuariosConReservasActivas();
        return usuarioMapper.toDTOList(entities);
    }

    public int countReservasByUsuarioId(int id){
        return usuarioRepository.countReservasByUsuarioId(id);
    }



}
