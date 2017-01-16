/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.converter.tiles;

/**
 *
 * @author huliqing
 */
public class ShadowModeChoiceFieldConverter extends ChoiceFieldConverter {

    public ShadowModeChoiceFieldConverter() {
        super("Off", "Cast", "Receive", "CastAndReceive", "Inherit");
    }
    
}
