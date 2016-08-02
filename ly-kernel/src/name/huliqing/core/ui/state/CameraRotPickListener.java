/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui.state;

/**
 * 镜头转动操作,主要将鼠标按下超过一定时间的操作视为镜头转动。不与其它的选择
   操作混淆在一起。镜头的转动由系统另外处理,这里主要避免在镜头转动的时候仍然去触发UI事件的问题.
 * @author huliqing
 */
public class CameraRotPickListener implements PickListener {

    // 镜头转动,当鼠标按下的时间达到CRPMT(cameraRotatePressMaxTime)时，视为
    // 镜头转动操作。优先级高于游戏逻辑中的其它“选择”操作。
    private final static long CRPMT = 170L;
    private long pressTime;
    
    @Override
    public boolean pick(boolean isPressed, float tpf) {
        if (isPressed) {
            pressTime = System.currentTimeMillis();
            return true;
        }
        
        return System.currentTimeMillis() - pressTime >= CRPMT;
    }
    
}
