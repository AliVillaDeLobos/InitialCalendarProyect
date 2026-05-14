package dgtic.core.system.service;

import dgtic.core.system.model.entities.Descripcion;
import dgtic.core.system.repository.DescripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DescripcionServiceImpl implements DescripcionService{
    @Autowired
    private DescripcionRepository repository;

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Optional<Descripcion> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Descripcion save(Descripcion descripcion) {
        return repository.save(descripcion);
    }

}
