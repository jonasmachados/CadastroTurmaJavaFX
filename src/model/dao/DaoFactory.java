package model.dao;

import db.DB;
import model.DaoIimpl.AlunoDaoJDBC;


public class DaoFactory {

	public static AlunoDao createAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}

}
