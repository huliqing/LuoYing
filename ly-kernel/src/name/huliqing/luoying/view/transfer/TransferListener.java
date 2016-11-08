/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.transfer;

/**
 * 交易侦听器
 * @author huliqing
 */
public interface TransferListener {
    
    /**
     * 当transfer添加了一个物体的时候这个方法会被调用。
     * @param transfer
     * @param data
     * @param count 
     */
    void onAdded(Transfer transfer, TransferData data, int count);
    
    /**
     * 当transfer移除掉一个物体的时候这个方法会被调用。
     * @param transfer
     * @param data
     * @param count 
     */
    void onRemoved(Transfer transfer, TransferData data, int count);
}
