package dgtic.core.system.service;

import dgtic.core.system.model.entities.Descripcion;

import java.util.Optional;

public interface DescripcionService {

    Optional<Descripcion> findById(Integer id);
    Descripcion save(Descripcion descripcion);
    void deleteById(Integer id);

}
