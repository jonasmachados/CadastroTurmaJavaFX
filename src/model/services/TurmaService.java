 
package model.services;

import java.util.List;
import model.dao.TurmaDao;
import model.dao.DaoFactory;
import model.entities.Turma;

/**
 *
 * @author Jonas create 26/12/2020
 */
public class TurmaService {
    
    private TurmaDao dao = DaoFactory.createTurmaDao();
    
    //Lista todos os objetos Turma
    public List<Turma> findyAll(){
        return dao.findAll();
    }
    
    //Metodo que vai Inserir ou atualizar os dados do Turma
    public void saveOrUpdate(Turma obj){
        if(obj.getCodigo()== null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }
    
    //Metodo para remover um Seller do banco de dados
    public void remove(Turma obj){
        dao.deleteById(obj.getCodigo());
    }
}
    
































