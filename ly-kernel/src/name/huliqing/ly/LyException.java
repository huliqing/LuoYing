/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

/**
 * @author huliqing
 */
public class LyException extends Exception {
    
    public LyException(String message) {
        super(message);
    }
   
    public LyException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public LyException(Throwable cause) {
        super(cause);
    }
    
}
