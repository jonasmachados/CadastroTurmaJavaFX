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
import model.dao.ProfessorDao;
import model.entities.Professor;

public class ProfessorDaoJDBC implements ProfessorDao {

	private Connection conn;
	
	public ProfessorDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Professor findById(Integer idnome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`professor` WHERE idnome = ?");
			st.setInt(1, idnome);
			rs = st.executeQuery();
			if (rs.next()) {
				Professor obj = new Professor();
				obj.setIdnome(rs.getInt("idnome"));
				obj.setNome(rs.getString("nome"));
                                obj.setTitulacao(rs.getString("titulacao"));
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
	public List<Professor> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`professor` ORDER BY nome");
			rs = st.executeQuery();

			List<Professor> list = new ArrayList<>();

			while (rs.next()) {
				Professor obj = new Professor();
				obj.setIdnome(rs.getInt("idnome"));
				obj.setNome(rs.getString("nome"));
                                obj.setTitulacao(rs.getString("titulacao"));
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
	public void insert(Professor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO `escola`.`professor` " +
				"(nome, titulacao) " +
				"VALUES " +
				"(?, ?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
                        st.setString(2, obj.getTitulacao());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdnome(id);
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
	public void update(Professor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE `escola`.`professor` " +
				"SET nome = ?, titulacao = ? " +
				"WHERE idnome = ?");

			st.setString(1, obj.getNome());
                        st.setString(2, obj.getTitulacao());
			st.setInt(3, obj.getIdnome());
                      
			st.executeUpdate();
		}
		catch (SQLException e) {
                    e.printStackTrace();
			throw new DbException(e.getMessage());
		} 
		finally {
			DB.closeStatement(st);
		}
	}
        
	@Override
	public void deleteById(Integer idnome) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM `escola`.`professor` WHERE idnome = ?");

			st.setInt(1, idnome);

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
