package one.digitalinovation.padroesprojetosspring.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositoy extends CrudRepository<Cliente, Long>{

}
