package dgtic.core.system.service;

import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.enums.Color;
import dgtic.core.system.repository.ClaseTareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClaseTareaServiceImpl implements ClaseTareaService{
    @Autowired
    private ClaseTareaRepository claseTareaRepoitory;

    @Override
    public Page<ClaseTarea> findClaseTareasColorYUsuario(String nombreColor, String email, Pageable pageable) {
        List<ClaseTarea> claseTareas = findAllByColor_Nombre(nombreColor, email);
        int inicio =Math.min((int) pageable.getOffset(), claseTareas.size());
        int fin =Math.max((int) pageable.getOffset(), claseTareas.size());
        Page<ClaseTarea> page = new PageImpl<>(claseTareas.subList(inicio, fin), pageable, claseTareas.size());
        return page;
    }

    @Override
    public Page<ClaseTarea> findClaseTareasNombreYUsuario(String nombre, String email, Pageable pageable) {
        List<ClaseTarea> claseTareas =  claseTareaRepoitory.findAllByUsuario_EmailAndNombreContainsIgnoreCase(email, nombre);
        int inicio = Math.min((int) pageable.getOffset(), claseTareas.size());
        int fin = Math.min( inicio + pageable.getPageSize(), claseTareas.size());
        Page<ClaseTarea> page = new PageImpl<>(claseTareas.subList(inicio, fin), pageable, claseTareas.size());
        return page;
    }

    @Override
    public void delete(Integer idClasetarea) {
        claseTareaRepoitory.deleteById(idClasetarea);
    }

    @Override
    public List<ClaseTarea> findAllByUsuarioEmail(String email) {
        return claseTareaRepoitory.findAllByUsuario_Email(email);
    }


        @Override
        public List<ClaseTarea> findAllByColor_Nombre(String nombreColor, String email) {
            Color color = Color.valueOf(nombreColor.toUpperCase());
            return claseTareaRepoitory.findAllByColorAndUsuario_Email(color, email);
        }



    @Override
    public Optional<ClaseTarea> findById(Integer id) {
        return claseTareaRepoitory.findById(id);
    }

    @Override
    public ClaseTarea save(ClaseTarea tarea) {
        return claseTareaRepoitory.save(tarea);
    }

    @Override
    public Collection<Color> obtenerColoresUsados(String email) {
         List<ClaseTarea> ct =  claseTareaRepoitory.findAllByUsuario_Email(email);
         return ct.stream().map(ClaseTarea::getColor).collect(Collectors.toList());
    }

    @Override
    public Collection<Color> obtenerColoresDisponibles(String email) {
        Collection<Color> usados = obtenerColoresUsados(email);
        Set<Color> disponibles = EnumSet.allOf(Color.class); //Obtiene todos los valores del enum
        disponibles.removeAll(usados); //Elimina las coicidencias de los que ya estan utilizados
        return disponibles;
    }

    @Override
    public Page<ClaseTarea> findClaseTareasUsuario(String email, Pageable pageable) {
        List<ClaseTarea> claseTareas = claseTareaRepoitory.findAllByUsuario_Email(email);
        int inicio =Math.min((int) pageable.getOffset(), claseTareas.size());
        int fin =Math.max((int) pageable.getOffset(), claseTareas.size());
        Page<ClaseTarea> page = new PageImpl<>(claseTareas.subList(inicio, fin), pageable, claseTareas.size());
        return page;
    }
}
