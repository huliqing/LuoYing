/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.transfer;

import name.huliqing.core.data.ProtoData;

/**
 *
 * @author huliqing
 */
public interface TransferListener {
    
    void onAdded(Transfer transfer, ProtoData data, int count);
    
    void onRemoved(Transfer transfer, ProtoData data, int count);
}
