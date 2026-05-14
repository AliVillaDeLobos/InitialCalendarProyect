package dgtic.core.system.service;

import dgtic.core.system.model.entities.Dia;
import dgtic.core.system.repository.DiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaServiceImpl implements DiaService {
    private final DiaRepository diaRepository;

    @Autowired
    public DiaServiceImpl(DiaRepository diaRepository) {
        this.diaRepository = diaRepository;
    }

    @Override
    public Dia guardar(Dia dia) {
        return diaRepository.save(dia);
    }

    @Override
    public Optional<Dia> obtenerPorId(Integer idDia) {
        return diaRepository.findById(idDia);
    }

    @Override
    public void eliminar(Integer idDia) {
        diaRepository.deleteById(idDia);
    }

    @Override
    public List<Dia> obtenerDiasPorSemana(Integer idSemana) {
        return diaRepository.findBySemana_Id(idSemana);
    }
}
