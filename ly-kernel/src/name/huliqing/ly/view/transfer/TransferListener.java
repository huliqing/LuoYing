/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.transfer;

/**
 * 
 * @author huliqing
 * @param <T>
 */
public interface TransferListener<T> {
    
    void onAdded(Transfer<T> transfer, T data, int count);
    
    void onRemoved(Transfer<T> transfer, T data, int count);
}
