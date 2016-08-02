/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.state.lan;

import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.UIFactory;

/**
 *
 * @author huliqing
 */
public class SimpleBtn extends Button {
    
    public SimpleBtn(String text) {
        super(text);
        this.width = UIFactory.getUIConfig().getScreenWidth() * 0.18f;
    }

}
