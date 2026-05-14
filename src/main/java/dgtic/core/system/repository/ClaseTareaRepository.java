package dgtic.core.system.repository;

import dgtic.core.system.model.entities.ClaseTarea;
import dgtic.core.system.model.enums.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface ClaseTareaRepository extends JpaRepository<ClaseTarea, Integer> {
        void deleteById(Integer id);
        Optional<ClaseTarea> findByIdClaseTarea(Integer idClaseTarea);
//        List<ClaseTarea> findAllByColorAndUsuario_Email(String color, String usuarioEmail);
        List<ClaseTarea> findAllByUsuario_Email(String usuarioEmail);
        List<ClaseTarea> findAllByUsuario_EmailAndNombreContainsIgnoreCase(String email, String nombre);
        List<ClaseTarea> findAllByColorAndUsuario_Email(Color color, String usuarioEmail);

}
