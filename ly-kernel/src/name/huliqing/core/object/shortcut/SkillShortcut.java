/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.shortcut;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;
import name.huliqing.core.Factory;
import name.huliqing.core.LY;
import name.huliqing.core.data.SkillData;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.SkillNetwork;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.object.skill.Skill;
import name.huliqing.core.utils.MatUtils;

/**
 * 用于技能(skill)的快捷方式
 * @author huliqing
 */
public class SkillShortcut extends BaseUIShortcut<SkillData> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final SkillNetwork skillNetwork = Factory.get(SkillNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
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
    
    private Control updateControl;

    @Override
    public void initialize() {
        super.initialize();
        
        createMask(maskObj, maskMat);
        view.attachChild(maskObj);
        
        // 更新频率，以一秒钟更新30次为标准频率。冷却时间越长更新频率越低。
        // 在低于1秒的冷却时间内尽可能快的更新。
        interval = 1f / 120 * objectData.getCooldown();
        
        // 用于更新技能 shortcut图标
        if (updateControl == null) {
            updateControl = new AbstractControl() {
                @Override
                protected void controlUpdate(float tpf) {updateShortcut(tpf);}
                @Override
                protected void controlRender(RenderManager rm, ViewPort vp) {}
            };
            actor.getSpatial().addControl(updateControl);
        }
    }
    
    /**
     * 更新技能图标冷却效果
     * @param tpf 
     */
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
            float p = getTimePercent();
            maskMat.setFloat("Percent", p);
            if (p >= 1.0f) {
                needCheckAndUpdateMask = false;
            }
        }
    }

    @Override
    public void cleanup() {
        if (updateControl != null) {
            actor.getSpatial().removeControl(updateControl);
        }
        super.cleanup();
    }

    @Override
    public void removeObject() {
        // 技能不能删除
    }
    
    @Override
    public void onShortcutClick(boolean pressed) {
        if (!pressed) {
            if (objectData.getCooldown() > 0) {
                needCheckAndUpdateMask = true;
            }
            // 一些技能在执行前必须设置目标对象。
            actorNetwork.setTarget(actor, playService.getTarget());
            
            // 执行技能
            Skill skill = skillService.getSkill(actor, objectData.getId());
            skillNetwork.playSkill(actor, skill, false);
        }
    }

    @Override
    protected void updateShortcutViewChildren(float width, float height) {
        super.updateShortcutViewChildren(width, height); 
        maskObj.setLocalScale(width * maskScale, height * maskScale, 1);
        maskObj.setLocalTranslation(width * (1 - maskScale) * 0.5f, height * (1 - maskScale) * 0.5f, 0);
    }
    
    private float getTimePercent() {
        long lapse = LY.getGameTime() - objectData.getLastPlayTime();
        float percent = lapse / (objectData.getCooldown() * 1000);
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
