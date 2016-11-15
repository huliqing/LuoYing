/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import name.huliqing.luoying.data.StateData;
import name.huliqing.luoying.ui.FrameLayout;
import name.huliqing.luoying.ui.Icon;

/**
 * 主要用于显示状态，让状态可视化。
 * @author huliqing
 */
public class StateView extends FrameLayout {
    
    private StateData state;
    
    private Icon icon;
    
    public StateView(float width, float height) {
        super(width, height);
    }
    
    public final void setState(StateData state) {
        // 如果是同一个state,则不需要重设
        if (state == this.state) {
            return;
        }
        this.state = state;
        if (icon == null) {
            icon = new Icon();
            addView(icon);
        }
        icon.setImage(state.getIcon());
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        icon.setWidth(width);
        icon.setHeight(height);
    }
    
}
