/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.ad;

/**
 *
 * @author huliqing
 */
public interface AdInsertListener {
    
    void notifyAdInsertLoadOK();
    
    void notifyAdInsertLoadFailure();
    
    void notifyAdInsertClosed();
}
