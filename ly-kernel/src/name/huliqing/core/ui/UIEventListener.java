/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.ui;

/**
 * 全局UI事件监听
 * @author huliqing
 */
public interface UIEventListener {
    
    /**
     * UI被点击
     * @param ui 被点击的UI
     * @param isPressed 鼠标是否按下
     * @param dbclick 是否为双击事件
     */
    void UIClick(UI ui, boolean isPressed, boolean dbclick);
    
    /**
     * UI开始被拖动
     * @param ui 
     */
    void UIDragStart(UI ui);
    
    /**
     * UI释放拖动
     * @param ui 
     */
    void UIDragEnd(UI ui);
    
    /**
     * 当UI被释放时,释放与click或DBClick的isPressed=false不同。有时候点击按钮（非可拖的按钮）
     * 后把鼠标拖动到按钮区域之外再释放，这时并不会触发按钮的isPressed的click事件，
     * 在这种情况下通过 {@link #UIClick(name.huliqing.fighter.ui.UI, boolean, boolean) }
     * 无法监听到该事件，只能通过UIRelease方法来监听。
     * @param ui 
     */
    void UIRelease(UI ui);
}
