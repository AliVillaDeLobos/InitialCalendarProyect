package dgtic.core.system.service;

import dgtic.core.system.model.entities.Tarea;
import dgtic.core.system.model.enums.Color;
import dgtic.core.system.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TareaServiceImpl implements TareaService {
    @Autowired
    private TareaRepository repoitory;

    @Override
    public List<Tarea> buscarPorColorYEmail(Color color, String email) {
        return repoitory.findTareaByClaseTarea_ColorAndClaseTarea_Usuario_Email(color, email);
    }

    @Override
    public Page<Tarea> buscarPorColorYEmailP(Color color, String email, Pageable pageable) {
        List<Tarea> tareas = repoitory.findTareaByClaseTarea_ColorAndClaseTarea_Usuario_Email(color, email);
        int inicio = Math.min((int) pageable.getOffset(), tareas.size());
        int fin = Math.min( inicio + pageable.getPageSize(), tareas.size());
        Page<Tarea> page = new PageImpl<>(tareas.subList(inicio, fin), pageable, tareas.size());
        return page;
    }

    @Override
    public Page<Tarea> buscarPorColorONombreCT(Color color, String nombre, Pageable pageable) {
        List<Tarea> tareas = repoitory.buscarPorColorONombreClaseTarea(color, nombre);
        int inicio = Math.min((int) pageable.getOffset(), tareas.size());
        int fin = Math.min( inicio + pageable.getPageSize(),  tareas.size());
        Page<Tarea> page = new PageImpl<>(tareas.subList(inicio, fin), pageable, tareas.size());
        return page;
    }

    @Override
    public Tarea save(Tarea tarea) {
        return repoitory.save(tarea);
    }

    @Override
    public Optional<Tarea> findByIdAndUsuario(Integer id, String email) {
        return repoitory.findByIdTareaAndClaseTarea_Usuario_Email(id, email);
    }

    @Override
    public Optional<Tarea> findById(Integer id) {
        return repoitory.findById(id);
    }

    @Override
    public Optional<Tarea> findByNombre(String nombre) {
        return repoitory.findByNombre(nombre);
    }

    @Override
    public void deleteById(Integer id) {
        repoitory.deleteById(id);
    }

    @Override
    public Page<Tarea> findTareasUsuario(String email, Pageable pageable) {
        List<Tarea> tareas = repoitory.findTareasByClaseTarea_Usuario_Email(email);
        int inicio = Math.min((int) pageable.getOffset(), tareas.size());
        int fin = Math.min( inicio + pageable.getPageSize(), tareas.size());
        Page<Tarea> page = new PageImpl<>(tareas.subList(inicio, fin), pageable, tareas.size());
        return page;
    }

    @Override
    public Collection<Tarea> findTareasUsuarioCollection(String email) {
        return repoitory.findTareasByClaseTarea_Usuario_Email(email);
    }
}
