package dgtic.core.system.service;

import dgtic.core.system.dto.SubtareasEliminadasDto;
import dgtic.core.system.model.entities.Subtarea;
import dgtic.core.system.model.entities.SubtareaEliminada;
import dgtic.core.system.model.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface SubtareaEliminadaService {

    Page<SubtareasEliminadasDto> subtareasEliminadas(Pageable pageable, List<Subtarea> subtareas, String email);
    Collection<SubtareaEliminada> todasPorUsuario(String email);
    void restaurarSubtarea(Integer idSubtareaEliminada);
}
