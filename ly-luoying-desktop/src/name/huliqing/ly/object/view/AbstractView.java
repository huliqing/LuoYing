/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.ly.object.view;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.ly.data.ViewData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.ControlAdapter;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.entity.AbstractEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.luoying.utils.ConvertUtils;

/**
 * View的基类
 * @author huliqing
 */
public abstract class AbstractView extends AbstractEntity implements View {
    private final PlayService playService = Factory.get(PlayService.class);
    
    // View的展示时间,如果为小于或等于0的值则永不停止。
    protected float useTime;
    // 是否可拖动
    protected boolean dragEnabled;
    // 是否自适应宽度高度
    protected boolean resize;
    // 固定位置,并不是始终固定
    protected Vector3f fixedPosition;
    // 角落位置,与fixPosition只能取一个
    protected Corner cornerPosition;
    // View的动画id
    private List<AnimWrap> animations;
    
    // ---- inner
    protected LinearLayout viewRoot;
    protected float timeUsed;
    
    protected boolean enabled = true;
    
    protected final ControlAdapter control = new ControlAdapter() {
        @Override
        public void update(float tpf) {
            viewUpdate(tpf);
        }
    };
    
    @Override
    public void setData(EntityData data) {
        this.data = data;
        float sw = playService.getScreenWidth();
        float sh = playService.getScreenHeight();
        
        enabled = data.getAsBoolean("enabled", enabled);
        useTime = data.getAsFloat("useTime", 0);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        
        dragEnabled = data.getAsBoolean("dragEnabled", false);
        resize = data.getAsBoolean("resize", false);
        
        viewRoot = new LinearLayout(data.getAsFloat("widthWeight", 0) * sw, data.getAsFloat("heightWeight", 0) * sh);
        
        // [left,top,right,bottom]
        float[] marginWeight = data.getAsFloatArray("marginWeight");
        if (marginWeight != null) {
            viewRoot.setMargin(marginWeight[0] * sw, marginWeight[1] * sh, marginWeight[2] * sw, marginWeight[3] * sh);
        }
        
        // Fixed position weight
        Vector3f tempFixedPosition = data.getAsVector3f("fixedPosition");
        if (tempFixedPosition != null) {
            fixedPosition = new Vector3f(
                     tempFixedPosition.x * playService.getScreenWidth()
                    ,tempFixedPosition.y * playService.getScreenHeight()
                    ,0
            );
        }
        
        // Corner position
        String tempCornerPosition = data.getAsString("cornerPosition");
        if (tempCornerPosition != null) {
            this.cornerPosition = Corner.identify(tempCornerPosition);
        }
        
        // Format: "animation|timePoint,animation|timePoint,..."
        String[] tempAnims = data.getAsArray("animations");
        if (tempAnims != null) {
            this.animations = new ArrayList<AnimWrap>(tempAnims.length);
            for (int i = 0; i < tempAnims.length; i++) {
                String[] taArr = tempAnims[i].split("\\|");
                AnimWrap aw = new AnimWrap();
                aw.animationId = taArr[0];
                if (taArr.length > 1) {
                    aw.timeStart = ConvertUtils.toFloat(taArr[1], 0);
                }
                this.animations.add(aw);
            }
        }
    }
    
    @Override
    public EntityData getData() {
        return data;
    }
    
    @Override
    public void updateDatas() {
        super.updateDatas();
        data.setAttribute("timeUsed", timeUsed);
        data.setAttribute("useTime", useTime);
    }

    @Override
    protected Spatial initSpatial() {
        return viewRoot;
    }
    
    @Override
    public final void initEntity() {
        // 1.初始化View
        doViewInit();
        
        // 2.在初化View之后再开始Anim，顺序不要反了,因为部分anim可能需要在
        // View初始化完位置和大小之后才能初始化。
        doViewAnimation(0);
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene); 
        // 把control加在sceneRoot上，注：因为UI是不执行UpdateLogicState的。
        scene.getRoot().addControl(control);
    }
    
    @Override
    public void cleanup() {
        // 清理animation
        if (animations != null) {
            for (AnimWrap aw : animations) {
                aw.cleanup();
            }
        }
        timeUsed = 0;
        scene.getRoot().removeControl(control);
        viewRoot.removeFromParent();
    }
    
    protected void doViewInit() {
        // 把viewRoot添加到场景
        UIState.getInstance().addUI(viewRoot);
        
        if (resize) {
            viewRoot.resize();
        }
        if (dragEnabled) {
            viewRoot.setDragEnabled(dragEnabled);
        }
        if (fixedPosition != null) {
            viewRoot.setPosition(fixedPosition.x, fixedPosition.y);
        } else if (cornerPosition != null) {
            viewRoot.setToCorner(cornerPosition);
        }
    }
    
    protected final void viewUpdate(float tpf) {
        if (!enabled) 
            return;
        
        timeUsed += tpf;
        
        doViewAnimation(tpf);
        
        doViewLogic(tpf);
        
        if (useTime > 0 && timeUsed > useTime) {
            doExit();
        }
    } 
    
    /**
     * 结束View的运行,不要把removevObject(this)放在cleanup中。cleanup会由外部
     * 去调用。
     */
    protected final void doExit() {
        // 把View移除出场景
        scene.removeEntity(this);
    }

    @Override
    public void setUseTime(float useTime) {
        this.useTime = useTime;
    }
   
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 更新动画
     * @param tpf 
     */
    private void doViewAnimation(float tpf) {
        // 更新动画
        if (animations != null) {
            for (AnimWrap aw : animations) {
                aw.update(tpf, timeUsed);
            }
        }
    }
    
    /**
     * 更新View逻辑
     * @param tpf 
     */
    protected void doViewLogic(float tpf) {}

    private class AnimWrap {
        // 动画ID
        String animationId;
        // 执行该动画的时间点.单位秒,该时间从View开始运行后计算。
        // 如 timeStart=1.5表示在View开始运行1.5秒后启动该动画
        float timeStart;
        
        boolean started;
        Anim animation;
        
        void update(float tpf, float timeUsed) {
            if (started) {
                animation.update(tpf);
                return;
            }
            
            if (timeUsed >= timeStart) {
                if (animation == null) {
                    animation = Loader.load(animationId);
                    animation.setTarget(viewRoot);
                }
                animation.start();
                started = true;
            }
        }
        
        void cleanup() {
            if (animation != null && !animation.isEnd()) {
                animation.cleanup();
            }
            started = false;
        }
    }
}
