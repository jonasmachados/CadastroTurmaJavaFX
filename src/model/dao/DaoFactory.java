package model.dao;

import db.DB;
import model.DaoIimpl.AlunoDaoJDBC;
import model.DaoIimpl.ProfessorDaoJDBC;


public class DaoFactory {

	public static AlunoDao createAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
        
        public static ProfessorDao createProfessorDao() {
		return new ProfessorDaoJDBC(DB.getConnection());
	}

}
