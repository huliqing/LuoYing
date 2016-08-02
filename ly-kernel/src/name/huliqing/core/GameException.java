/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core;

/**
 *
 * @author huliqing
 */
public class GameException extends RuntimeException {

    public GameException(String message) {
        super(message);
    }
    
    public GameException(String message, Throwable e) {
        super(message, e);
    }
}
