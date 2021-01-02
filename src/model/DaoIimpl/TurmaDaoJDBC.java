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
import java.util.HashMap;
import java.util.Map;
import model.dao.TurmaDao;
import model.entities.Professor;
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
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
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
                    "SELECT turma.*,professor.nome as ProfNome "
                    + "FROM turma INNER JOIN professor "
                    + "ON turma.professorId = professor.idnome "
                    + "ORDER BY codigo");

           rs = st.executeQuery();

            List<Turma> list = new ArrayList<>();
            Map<Integer, Professor> map = new HashMap<>();

            while (rs.next()) {

                Professor prof = map.get(rs.getInt("professorId"));

                if (prof != null) {
                    prof = instantiateProfessor(rs);
                    map.put(rs.getInt("professorId"), prof);
                }

                Turma obj = instantiateTurma(rs, prof);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
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
                    "INSERT INTO `escola`.`turma` "
                    + "(sala, professorid, dataAbertura) "
                    + "VALUES "
                    + "(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getSala());
            st.setDate(2, new java.sql.Date(obj.getDataAbertura().getTime()));

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setCodigo(id);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    //METODO PARA ATUALIZAR DADOS
    @Override
    public void update(Turma obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE `escola`.`turma` "
                    + "SET sala = ? , professorId = ?, dataAbertura = ?, "
                    + "WHERE codigo = ?");

            st.setString(1, obj.getSala());
            st.setInt(2, obj.getProfessor().getIdnome());
            st.setDate(3, new java.sql.Date(obj.getDataAbertura().getTime()));
            st.setInt(4, obj.getCodigo());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
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
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    private Turma instantiateTurma(ResultSet rs, Professor prof) throws SQLException {
        Turma obj = new Turma();
        obj.setCodigo(rs.getInt("codigo"));
        obj.setSala(rs.getString("sala"));
        obj.setProfessor(prof);
        obj.setDataAbertura(new java.sql.Date(rs.getTimestamp("dataAbertura").getTime()));
        obj.setDataFechamento(new java.sql.Date(rs.getTimestamp("dataFechamento").getTime()));
        return obj;
    }

    private Professor instantiateProfessor(ResultSet rs) throws SQLException {
        Professor prof = new Professor();
        prof.setIdnome(rs.getInt("idnome"));
        prof.setNome(rs.getString("nome"));
        prof.setTitulacao(rs.getString("titulacao"));
        return prof;
    }
}
