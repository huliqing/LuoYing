/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.service;

import name.huliqing.fighter.Inject;
import name.huliqing.fighter.object.effect.Effect;

/**
 *
 * @author huliqing
 */
public interface EffectService extends Inject {
    
    Effect loadEffect(String effectId);
}
