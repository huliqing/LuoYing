/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import name.huliqing.ly.Inject;
import name.huliqing.ly.object.effect.Effect;

/**
 *
 * @author huliqing
 */
public interface EffectService extends Inject {
    
    Effect loadEffect(String effectId);
}
