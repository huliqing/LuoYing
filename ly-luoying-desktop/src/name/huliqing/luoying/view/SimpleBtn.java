/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view;

import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.UIFactory;

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
