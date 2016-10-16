/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying;

/**
 * @author huliqing
 */
public class LuoYingException extends RuntimeException {
    
    public LuoYingException(String message) {
        super(message);
    }
   
    public LuoYingException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public LuoYingException(Throwable cause) {
        super(cause);
    }
    
}
