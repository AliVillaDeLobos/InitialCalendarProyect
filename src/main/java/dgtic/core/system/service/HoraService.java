package dgtic.core.system.service;

import dgtic.core.system.model.entities.Hora;
import org.hibernate.sql.ast.tree.expression.Collation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HoraService {

    Optional<Hora> obtenerHoraPorId(Integer idHora);
    Hora guardar(Hora hora);
    Collection<Hora> guardarTodas(Collection<Hora> horas);
    void eliminarHora(Hora hora);
    List<Integer> obtenerHoraslibres(Integer idDia);
    List<Hora> obtenerTodasHorasPorUsuario(String email);
    void eliminarTodas(Collection<Hora> horas);
    public void insertarHora(Integer idDiaSubtarea, Integer valorHora);


}
