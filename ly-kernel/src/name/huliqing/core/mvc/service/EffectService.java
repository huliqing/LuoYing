/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.Inject;
import name.huliqing.core.object.effect.Effect;

/**
 *
 * @author huliqing
 */
public interface EffectService extends Inject {
    
    Effect loadEffect(String effectId);
}
