/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import name.huliqing.fighter.ui.Button;
import name.huliqing.fighter.ui.UIFactory;

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
