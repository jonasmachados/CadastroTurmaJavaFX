 
package model.services;

import java.util.List;
import model.dao.AlunoDao;
import model.dao.DaoFactory;
import model.entities.Aluno;

/**
 *
 * @author Jonas create 20/12/2020
 */
public class AlunoService {
    
    private AlunoDao dao = DaoFactory.createAlunoDao();
    
    //Lista todos os objetos Seller
    public List<Aluno> findyAll(){
        return dao.findAll();
    }
    
    //Metodo que vai Inserir ou atualizar os dados do Aluno
    public void saveOrUpdate(Aluno obj){
        if(obj.getMatricula()== null){
            dao.insert(obj);
        }
        else{
            dao.update(obj);
        }
    }
    
    //Metodo para remover um Seller do banco de dados
    public void remove(Aluno obj){
        dao.deleteById(obj.getMatricula());
    }
}
    
































