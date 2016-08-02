/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import name.huliqing.core.view.ShortcutView;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import name.huliqing.core.LY;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.utils.MatUtils;

/**
 * 针对技能类型的shortcut,主要提供冷却提示动画
 * @author huliqing
 */
public class ShortcutSkillView extends ShortcutView {
    
    private SkillData data;
    // 技能CD遮罩颜色
    private final ColorRGBA maskColor = new ColorRGBA(1.0f, 0, 0.5f, 0.5f);
    // 材质
    private final Material maskMat = MatUtils.createSkillCooldown(maskColor);
    // 遮罩实体
    private final Geometry maskObj = new Geometry("mask");
    // 遮罩的缩放,比skillUI边框小一点就行。
    private final float maskScale = .85f;
    // 更新频率，冷却时间越长更新频率越慢，以节约资源
    private float interval;
    private float intervalUsed;
    // 判断是否需要更新mask,主要用于提高性能
    private boolean needCheckAndUpdateMask = true;

    public ShortcutSkillView(float width, float height, Actor actor, ProtoData data) {
        super(width, height, actor, data);
        if (!(data instanceof SkillData)) {
            throw new IllegalArgumentException("Not a skill data!");
        }
        this.data = (SkillData) data;
        createMask(maskObj, maskMat);
        attachChild(maskObj);
        // 更新频率，以一秒钟更新30次为标准频率。冷却时间越长更新频率越低。
        // 在低于1秒的冷却时间内尽可能快的更新。
        interval = 1f / 120 * this.data.getCooldown();
        
        // 技能类的快捷方式不需要侦听ItemListener
        actor.removeItemListener(this);
    }

    @Override
    public void updateShortcut(float tpf) {
        // 这里主要为提高性能，不过当界面存在多个相当的技能图标时，技能的冷却提示
        // 只有当前被点击的才会产生动画效果。如果这里注释掉则可同时看到相同技能的
        // 冷却提示动画。
        if (!needCheckAndUpdateMask) {
            return;
        }
        
        // 使用interval来降低更新频率,冷却时间越长则更新频率越低。
        intervalUsed += tpf;
        if (intervalUsed > interval) {
            intervalUsed = 0;
            // params: Color, Percent
            float p = getPTimeOfLapse();
            maskMat.setFloat("Percent", p);
            if (p >= 1.0f) {
                needCheckAndUpdateMask = false;
            }
        }
    }

    @Override
    protected void onShortcutClick(boolean isPress) {
        super.onShortcutClick(isPress);
        if (!isPress) {
            if (data.getCooldown() > 0) {
                needCheckAndUpdateMask = true;
            }
        }
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        maskObj.setLocalScale(width * maskScale, height * maskScale, 1);
        maskObj.setLocalTranslation(width * (1 - maskScale) * 0.5f, height * (1 - maskScale) * 0.5f, 0);
    }
    
    private float getPTimeOfLapse() {
        long lapse = LY.getGameTime() - data.getLastPlayTime();
        float percent = lapse / (data.getCooldown() * 1000);
        return percent;
    }
    
    private void createMask(Geometry mask, Material maskMat) {
        Quad quad = new Quad(1,1);
        mask.setMesh(quad);
        mask.setMaterial(maskMat);
        // 非常重要，shortcut是放置在GUI上的，所以这里必须指定Bucket为GUI，否则会看不到模型。
        mask.setQueueBucket(RenderQueue.Bucket.Gui);
    }
}
