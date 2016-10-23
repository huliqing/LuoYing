/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.lan;

/**
 * @author huliqing
 */
public interface RoomState {
    
    /**
     * 开始当前
     */
    void startGame();
    
    /**
     * 踢出当前选中的客户端连接
     */
    void kickClient();
    
    /**
     * 返回,退出房间
     */
    void back();
    
}
