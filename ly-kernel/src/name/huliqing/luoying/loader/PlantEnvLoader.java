/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.loader;

import com.jme3.math.Vector3f;
import name.huliqing.luoying.data.ModelEntityData;
import name.huliqing.luoying.xml.Proto;
import name.huliqing.luoying.utils.MathUtils;

/**
 * 植被数据载入器，这个类主要用于树森或花草等env的载入，为这些植被模型随机
 * 生成高低、粗壮不同的缩放大小，可使场景中的各种植被有大小不一的感觉。
 * @author huliqing
 * @param <T>
 */
public class PlantEnvLoader<T extends ModelEntityData> extends EntityDataLoader<T> {

    @Override
    public void load(Proto proto, T store) {
        super.load(proto, store);
        
        boolean randomScale = proto.getAsBoolean("randomScale", false);
        if (randomScale) {
            float minScale = proto.getAsFloat("minScale", 1);
            float maxScale = proto.getAsFloat("maxScale", 1);
            float finalScale = MathUtils.getRandomFloat(minScale, maxScale);
            store.setScale(new Vector3f(finalScale,finalScale,finalScale));
        }
    }
    
    
}
