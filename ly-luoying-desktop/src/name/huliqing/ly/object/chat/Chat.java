/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.object.chat;

import com.jme3.scene.Spatial;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.ly.data.ChatData;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.ControlAdapter;
import name.huliqing.luoying.object.anim.Anim;
import name.huliqing.luoying.object.el.STBooleanEl;
import name.huliqing.luoying.object.entity.AbstractEntity;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.layer.service.GameService;

/**
 * 对话功能。
 * @author huliqing
 * @param <T>
 */
public abstract class Chat<T extends ChatData> extends AbstractEntity<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final ElService elService = Factory.get(ElService.class);
    
    // Chat的宽度和高度限制
    protected float width;
    protected float height;
    // 最远距离限制，当玩家与目标actor的距离达到这个限制时将不显示这个Chat
    protected float maxDistance = 10;
    private float maxDistanceSquared;
    // 是否在当前Chat打开时关闭父Chat.以避免杂乱重叠的窗口影响视角效果
    protected boolean closeParent = true;
    // 是否在当前Chat关闭时重新打开父Chat(即“返回上一步”的功能相同)
    protected boolean reopenParent;
    // Chat动画
    protected Anim[] animations;
    // 用于判断当前Chat是否对目标角色可见
    protected STBooleanEl hitCheckEl;
    
    // ---- inner
    protected Entity actor;
    // 父Chat，如果当前为root chat则parent为null.
    protected Chat parent;
    
    /** 显示在对话框上的名称 */
    protected String chatName;
    
    protected UI chatUI;
    
    protected final ControlAdapter control = new ControlAdapter() {
        @Override
        public void update(float tpf) {
            chatUpdate(tpf);
        }
    };

    @Override
    public void setData(T data) {
        this.data = data;
        this.width = data.getAsFloat("widthWeight", 0.3f) * playService.getScreenWidth();
        this.height = data.getAsFloat("heightWeight", 0.5f) * playService.getScreenHeight();
        this.maxDistance = data.getAsFloat("maxDistance", maxDistance);
        this.maxDistanceSquared = maxDistance * maxDistance;
        this.closeParent = data.getAsBoolean("closeParent", closeParent);
        this.reopenParent = data.getAsBoolean("reopenParent", reopenParent);
        String[] tempAnims = data.getAsArray("animations");
        if (tempAnims != null) {
            this.animations = new Anim[tempAnims.length];
            for (int i = 0; i < tempAnims.length; i++) {
                this.animations[i] = Loader.load(tempAnims[i]);
            }
        }
        hitCheckEl = elService.createSTBooleanEl(data.getAsString("hitCheckEl", "#{true}"));
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void updateDatas() {
        super.updateDatas();
    }
    
    @Override
    public void initEntity() {
        // 检查是否可见
        if (!isVisibleForPlayer()) {
            scene.removeEntity(this);
            return;
        }
        
        // 1.创建并Chat界面并设置动画效果
        if (chatUI == null) {
            chatUI = createChatUI(width, height);
        }
        
        // 2.将界面加入主场景,并启动动画
        if (chatUI != null) {
            displayChatUI(chatUI);
        }
        
        // 3.检查是否关闭父窗口
        if (closeParent && parent != null) {
            scene.removeEntity(parent);
        }
        
        scene.getRoot().addControl(control);
    }
    
    @Override
    public void cleanup() {
        scene.getRoot().removeControl(control);
        
        if (reopenParent && parent != null) {
            scene.addEntity(parent);
        }
        
        if (animations != null) {
            for (Anim anim : animations) {
                anim.cleanup();
            }
        }
        
        if (chatUI != null) {
            chatUI.getDisplay().removeFromParent();
        }
        super.cleanup();
    }    
    
    @Override
    public Spatial getSpatial() {
        if (chatUI != null) {
            return chatUI.getDisplay();
        }
        return null;
    }
    
    protected void displayChatUI(UI ui) {
        UIState.getInstance().addUI(ui.getDisplay());
        if (animations != null) {
            for (Anim anim : animations) {
                anim.setTarget(ui);
                anim.start();
            }
        }
    }

    /**
     * 更新Chat逻辑
     * @param tpf 
     */
    protected void chatUpdate(float tpf) {
        // 检查是否可见
        if (!isVisibleForPlayer()) {
            scene.removeEntity(this);
            return;
        }
        
        // 更新动画
        if (animations != null) {
            for (Anim anim : animations) {
                anim.update(tpf);
            }
        }
    }
    
    /**
     * 检查Chat是否对当前场景中的玩家可见。
     * @return 
     */
    public boolean isVisibleForPlayer() {
        return isVisible(gameService.getPlayer());
    }
    
    /**
     * 判断当前Chat对目标target是否可见。
     * @param target
     * @return 
     */
    public boolean isVisible(Entity target) {
        if (target == null || target == actor)
            return false;
        
        if (actor == null 
                || actor.getScene() == null
                || actor.getSpatial().getWorldTranslation().distanceSquared(target.getSpatial().getWorldTranslation()) > maxDistanceSquared) 
            return false;
        
        if (!hitCheckEl.setTarget(target.getAttributeManager()).getValue())
            return false;
        
        return true;
    }

    public Entity getActor() {
        return actor;
    }

    public void setActor(Entity actor) {
        this.actor = actor;
        hitCheckEl.setSource(actor.getAttributeManager());
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }

    public float getMaxDistanceSquared() {
        return maxDistanceSquared;
    }
    
    /**
     * 获取Chat的名称或title
     * @return 
     */
    public String getChatName() {
        if (chatName == null) {
            chatName = ResourceManager.getObjectName(data);
            if (chatName.startsWith("<")) {
                chatName = ResourceManager.get(ResConstants.COMMON_CHAT);
            }
        }
        return chatName;
    }
    
    /**
     * 结束对话，该方法将把当前Chat从场景中移除
     */
    protected void endChat() {
        scene.removeEntity(this);
    }
    
    /**
     * 创建Chat主UI界面
     * @param width
     * @param height
     * @return 
     */
    protected abstract UI  createChatUI(float width, float height);
}
