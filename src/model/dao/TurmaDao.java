package model.dao;

import java.util.List;
import model.entities.Turma;

/**
 * FXML Controller class
 *
 * @author Jonas create 26/12/2020
 */

public interface TurmaDao {

	void insert(Turma obj);
	void update(Turma obj);
	void deleteById(Integer id);
	Turma findById(Integer id);
	List<Turma> findAll();
}
