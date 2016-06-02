/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view.tiles;

import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.UIFactory;

/**
 * 只是统一封装了一下效果样式
 * @author huliqing
 */
public abstract class SimpleRow<T> extends Row<T> {
    
    public SimpleRow(ListView parentView) {
        super(parentView);
      
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        setBackgroundVisible(false);
    }
    
    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            this.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
        }
        setBackgroundVisible(isPress);
    }

    @Override
    public void onRelease() {
        setBackgroundVisible(false);
    }
}
