/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.state.start;

import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.ui.Button;
import name.huliqing.ly.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class SButton extends Button {
    
    public SButton(String textKey) {
        super(ResourceManager.get(textKey));
        setBackground("Interface/ui/squareButton/squareButton.png", true);
    }
    
    @Override
    protected void clickEffect(boolean isPress) {
        if (isPress) {
            setActived(isPress);
        }
    }
    
    public void setActived(boolean actived) {
        setBackgroundColor(actived ? UIFactory.getUIConfig().getActiveColor() : UIFactory.getUIConfig().getButtonBgColor(), true);
    }

}
