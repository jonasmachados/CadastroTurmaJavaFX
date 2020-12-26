 
package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.ProfessorDao;
import model.entities.Professor;

/**
 *
 * @author Jonas create 20/12/2020
 */
public class ProfessorService {
    
    private ProfessorDao dao = DaoFactory.createProfessorDao();
    
    //Lista todos os objetos Seller
    public List<Professor> findyAll(){
        return dao.findAll();
    }
    
    //Metodo que vai Inserir ou atualizar os dados do Professor
    public void saveOrUpdate(Professor obj){
        if(obj.getIdnome()== null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }
    
    //Metodo para remover um Seller do banco de dados
    public void remove(Professor obj){
        dao.deleteById(obj.getIdnome());
    }
}
    
































