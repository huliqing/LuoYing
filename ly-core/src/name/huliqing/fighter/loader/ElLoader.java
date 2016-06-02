/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import java.util.HashMap;
import name.huliqing.fighter.object.DataLoaderFactory;
import name.huliqing.fighter.data.ElData;
import name.huliqing.fighter.object.el.HitEl;
import name.huliqing.fighter.object.el.El;
import name.huliqing.fighter.object.el.LevelEl;
import name.huliqing.fighter.object.el.XpDropEl;

/**
 *
 * @author huliqing
 */
class ElLoader {
    
    // 缓存Level处理器
    private final static HashMap<String, El> elCacheMap = new HashMap<String, El>();
    
    public static El load(String elId) {
        
        El el = elCacheMap.get(elId);
        if (el == null) {
            el = createEl(DataLoaderFactory.createElData(elId));
            
            if (el == null) {
                throw new NullPointerException("Could not find el, elId=" + elId);
            }
            
            elCacheMap.put(elId, el);
        }
        return el;
    }
    
    private static El createEl(ElData data) {
        String tagName = data.getProto().getTagName();
        if (tagName.equals("elLevel")) {
            return new LevelEl(data);
        } else if (tagName.equals("elHit")) {
            return new HitEl(data);
        } else if (tagName.equals("elXpDrop")) {
            return new XpDropEl(data);
        }
        throw new UnsupportedOperationException("Unsupported tagName=" + tagName + ", elData=" + data);
    }
}
