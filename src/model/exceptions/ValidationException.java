
package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jonas create 07/12/2020
 */

public class ValidationException extends RuntimeException{
    
    private static final long serialVersionUID = 1l;
    
    //MAP É UMA COLECAO
    private Map<String, String> errors = new HashMap<>();
    
    public ValidationException(String msg){
        super(msg);
    }
    
    //METODO QUE ADICIONA OBJ NA COLECAO MAP
    public void addError(String fieldName, String errorMessage){
        errors.put(fieldName, errorMessage);
    }
    
    //METODO GET DO ERRO
    public Map<String, String> getErrors(){
        return errors;
    }
}
