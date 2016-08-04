/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.transfer;

import name.huliqing.core.data.ObjectData;

/**
 * 
 * @author huliqing
 */
public interface TransferListener {
    
    void onAdded(Transfer transfer, ObjectData data, int count);
    
    void onRemoved(Transfer transfer, ObjectData data, int count);
}
