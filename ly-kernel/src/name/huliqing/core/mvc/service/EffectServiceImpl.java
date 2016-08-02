/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.effect.EffectCache;

/**
 *
 * @author huliqing
 */
public class EffectServiceImpl implements EffectService {

    @Override
    public void inject() {
        // ignore
    }
    
    @Override
    public Effect loadEffect(String effectId) {
        return EffectCache.getInstance().getEffect(effectId);
    }
}
