/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view.talk;

/**
 *
 * @author huliqing
 */
public interface TalkLogic {
    
    void start();
    
    void update(float tpf);
    
    boolean isEnd();
    
    void cleanup();
    
    /**
     * 是否为network模式
     * @param network 
     */
    void setNetwork(boolean network);
}
