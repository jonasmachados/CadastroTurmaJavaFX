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
import model.dao.TurmaDao;
import model.entities.Turma;

public class TurmaDaoJDBC implements TurmaDao {

	private Connection conn;
	
	public TurmaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Turma findById(Integer codigo) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`turma` WHERE codigo = ?");
			st.setInt(1, codigo);
			rs = st.executeQuery();
			if (rs.next()) {
				Turma obj = new Turma();
				obj.setCodigo(rs.getInt("codigo"));
				obj.setSala(rs.getString("sala"));
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
	public List<Turma> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM `escola`.`turma` ORDER BY sala");
			rs = st.executeQuery();

			List<Turma> list = new ArrayList<>();

			while (rs.next()) {
				Turma obj = new Turma();
				obj.setCodigo(rs.getInt("codigo"));
				obj.setSala(rs.getString("sala"));
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
	public void insert(Turma obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"INSERT INTO `escola`.`turma` " +
				"(sala) " +
				"VALUES " +
				"(?)", 
				Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getSala());

			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setCodigo(id);
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
	public void update(Turma obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"UPDATE `escola`.`turma` " +
				"SET sala = ? " +
				"WHERE codigo = ?");

			st.setString(1, obj.getSala());
			st.setInt(2, obj.getCodigo());
                      
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
	public void deleteById(Integer codigo) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM `escola`.`turma` WHERE codigo = ?");

			st.setInt(1, codigo);

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
