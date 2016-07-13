/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.effect;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.data.EffectData;
import name.huliqing.fighter.loader.Loader;

/**
 * @deprecated 20160202,使用GroupEffect代替就可以了
 * 简单的效果组，该组内的效果一个一个按顺序执行，当一个执行完之后才会执行
 * 另一个效果。直到所有效果结束后，当前效果才算结束。
 * 使用该效果组需要注意以下几点：
 * 1.当前效果不会执行任何实质内容，只会逐一执行各个子效果。
 * 2.不要把带循环的效果加入到组内，否则可能该循环效果会阻止后续子效果的执行。
 * 3.当前的效果各阶段的时间设置将不会影响到子效果的执行时间。
 * @author huliqing
 * @since 1.3 - 20150504
 */
public class SimpleGroupEffect extends AbstractEffect {
    
    // 子效果列表
    private List<Effect> childrenEffect = new ArrayList<Effect>();
    
    // 当前正在执行的效果
    private Effect currentEffect;
    // 当前正在执行的效果在子效果列表中的索引
    private int index = -1;
    
    // 是否已经结束,只有所有子效果运行完才算结束
    private boolean end;

    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        String[] effects = data.getProto().getAsArray("effects");
        for (String id : effects) {
            Effect effect = Loader.loadEffect(id);
            childrenEffect.add(effect);
        }
    }
    
    /**
     * 检查执行下一个效果，如果存在下一个效果则执行，否则标记为结束
     */
    private void checkDoNext() {
        index++;
        if (index >= childrenEffect.size()) {
            end = true;
            return;
        }
        currentEffect = childrenEffect.get(index);
        currentEffect.start();
        // 把子效果attach进来即可，效果会自己运行，效果运行结束后会自动脱离。
        localRoot.attachChild((AbstractEffect) currentEffect);
    }
    
    @Override
    protected void doInit() {
        super.doInit();
        end = false;
        index = -1;
        checkDoNext();
    }

    @Override
    protected void updatePhaseAll(float tpf) {
        if (currentEffect.isEnd()) {
            checkDoNext();
        }
    }

    // 覆盖父类的方法。只有子效果运行完之后才算完全结束,即使当前效果在时间上已经
    // 结束
    @Override
    protected boolean confirmEnd() {
        return end;
    }

    @Override
    public void cleanup() {
        for (Effect effect : childrenEffect) {
            // 防止意外
            if (!effect.isEnd()) {
                effect.cleanup();
            }
        }
        super.cleanup();
    }
    
    
}
