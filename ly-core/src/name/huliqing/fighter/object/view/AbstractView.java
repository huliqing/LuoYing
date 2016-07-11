/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.view;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.ViewData;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.loader.Loader;
import name.huliqing.fighter.object.AbstractPlayObject;
import name.huliqing.fighter.object.SyncData;
import name.huliqing.fighter.object.NetworkObject;
import name.huliqing.fighter.object.anim.Anim;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UI.Corner;
import name.huliqing.fighter.ui.state.UIState;
import name.huliqing.fighter.utils.ConvertUtils;

/**
 * View的基类
 * @author huliqing
 */
public abstract class AbstractView extends AbstractPlayObject implements View, NetworkObject {

    private final PlayService playService = Factory.get(PlayService.class);
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    
    protected ViewData data;
    
    // View的展示时间,如果为小于或等于0的值则永不停止。
    protected float useTime;
    // 是否开启同步及同步数据
    protected boolean syncEnabled;
    // 是否可拖动
    protected boolean dragEnabled;
    // 是否自适应宽度高度
    protected boolean resize;
    // 固定位置,并不是始终固定
    protected Vector3f fixedPosition;
    // 角落位置,与fixPosition只能取一个
    protected Corner cornerPosition;
    // View的动画id
    private AnimWrap[] animations;
    
    // ---- inner
    protected LinearLayout viewRoot;
    protected float timeUsed;
    protected final SyncData syncData = new SyncData();
    protected boolean enabled = true;
    
    public AbstractView(ViewData data) {
        this.data = data;
        float sw = playService.getScreenWidth();
        float sh = playService.getScreenHeight();
        
        enabled = data.getAsBoolean("enabled", enabled);
        useTime = data.getAsFloat("useTime", 0);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
        syncEnabled = data.getAsBoolean("syncEnabled", false);
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
        String tempCornerPosition = data.getAttribute("cornerPosition");
        if (tempCornerPosition != null) {
            this.cornerPosition = Corner.identify(tempCornerPosition);
        }
        
        // Format: "animation|timePoint,animation|timePoint,..."
        String[] tempAnims = data.getAsArray("animations");
        if (tempAnims != null) {
            this.animations = new AnimWrap[tempAnims.length];
            for (int i = 0; i < tempAnims.length; i++) {
                String[] taArr = tempAnims[i].split("\\|");
                AnimWrap aw = new AnimWrap();
                aw.animationId = taArr[0];
                if (taArr.length > 1) {
                    aw.timeStart = ConvertUtils.toFloat(taArr[1], 0);
                }
                this.animations[i] = aw;
            }
        }
    }
        
    @Override
    public ViewData getData() {
        return data;
    }

    @Override
    public ViewData getUpdateData() {
        data.setAttribute("enabled", enabled);
        data.setAttribute("timeUsed", timeUsed);
        data.setAttribute("useTime", useTime);
        return data;
    }

    @Override
    public void setUseTime(float useTime) {
        this.useTime = useTime;
        putSyncData("useTime", useTime);
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        // 开关不发生变化则不处理。主要避免频繁触发同步
        if (this.enabled == enabled) 
            return;

        this.enabled = enabled;
        putSyncData("enabled", enabled);
        // 在关闭逻辑的情况下，这个必须立即发送同步，否则syncData不会在下次发送
        // 因为逻辑已经停止运行。
        if (!enabled) {
            playNetwork.syncObject(this, syncData, true);
            syncData.clear();
        }
    }
    
    @Override
    public final long getSyncId() {
        return data.getUniqueId();
    }

    @Override
    public final boolean isSyncEnabled() {
        return syncEnabled;
    }

    @Override
    public final void setSyncEnabled(boolean enabled) {
        syncEnabled = enabled;
    }
    
    // 从服务端回来的同步数据
    @Override
    public void applySyncData(SyncData data) {
//        enabled = data.getAsBoolean("enabled", enabled);// remove20160523
        useTime = data.getAsFloat("useTime", useTime);
        timeUsed = data.getAsFloat("timeUsed", timeUsed);
    }

    /**
     * 添加同步数据,等待同步
     * @param key
     * @param value
     */
    protected final void putSyncData(String key, Object value) {
        if (!isSyncEnabled()) 
            return;
        syncData.setAttribute(key, value);
    }

    @Override
    public final void initialize(Application app) {
        super.initialize(app); 
        
        // 1.初始化View
        doViewInit();
        
        // 2.在初化View之后再开始Anim，顺序不要反了,因为部分anim可能需要在
        // View初始化完位置和大小之后才能初始化。
        doViewAnimation(0); 
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
    
    @Override
    public final void update(float tpf) {
        if (!enabled) 
            return;
        
        timeUsed += tpf;
        
        doViewAnimation(tpf);
        
        doViewLogic(tpf);
        
        // 同步数据
        if (syncEnabled && !syncData.isEmpty()) {
            playNetwork.syncObject(this, syncData, true);
            syncData.clear();
        }
        
        if (useTime > 0) {
            if (timeUsed > useTime) {
                doExit();
            }
        }
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
    
    /**
     * 结束View的运行,不要把removevObject(this)放在cleanup中。cleanup会由外部
     * 去调用。
     */
    protected final void doExit() {
        // 把View移除出场景
        playService.removeObject(this);
    }
    
    @Override
    public void cleanup() {
        timeUsed = 0;
        // 清理animation
        if (animations != null) {
            for (AnimWrap aw : animations) {
                aw.cleanup();
            }
        }
        viewRoot.removeFromParent();
    }

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
                    animation = Loader.loadAnimation(animationId);
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
