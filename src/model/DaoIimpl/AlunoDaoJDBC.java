package model.DaoIimpl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import model.dao.AlunoDao;
import model.entities.Aluno;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;
	
	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Aluno findById(Integer matricula) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`aluno` WHERE matricula = ?");
			st.setInt(1, matricula);
			rs = st.executeQuery();
			if (rs.next()) {
				Aluno obj = new Aluno();
				obj.setMatricula(rs.getInt("Matricula"));
				obj.setNome(rs.getString("Nome"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`aluno` ORDER BY nome");
			rs = st.executeQuery();

			List<Aluno> list = new ArrayList<>();

			while (rs.next()) {
				Aluno obj = new Aluno();
				obj.setMatricula(rs.getInt("matricula"));
				obj.setNome(rs.getString("nome"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

        //METODO PARA INSERIR DADOS
	@Override
	public void insert(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO `escola`.`aluno` " +
				"(Nome) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setMatricula(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

        //METODO PARA ATUALIZAR DADOS
	@Override
	public void update(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE `escola`.`aluno` " +
				"SET nome = ? " +
				"WHERE matricula = ?");

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getMatricula());
                      
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer matricula) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM `escola`.`aluno` WHERE matricula = ?");

			st.setInt(1, matricula);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
}
