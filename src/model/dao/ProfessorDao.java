package model.dao;

import java.util.List;
import model.entities.Professor;

public interface ProfessorDao {

	void insert(Professor obj);
	void update(Professor obj);
	void deleteById(Integer id);
	Professor findById(Integer id);
	List<Professor> findAll();
}
