/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.chat;

import com.jme3.app.Application;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.data.ChatData;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.object.AbstractPlayObject;
import name.huliqing.core.xml.DataProcessor;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.anim.Anim;
import name.huliqing.core.object.hitchecker.HitChecker;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.state.UIState;

/**
 * 对话面板，
 * @author huliqing
 * @param <T>
 */
public abstract class Chat<T extends ChatData> extends AbstractPlayObject implements DataProcessor<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
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
    protected HitChecker hitChecker;
    
    // ---- inner
    protected T data;
    protected Actor actor;
    // 父Chat，如果当前为root chat则parent为null.
    protected Chat parent;
    
    /** 显示在对话框上的名称 */
    protected String chatName;
    
    protected UI chatUI;

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
                this.animations[i] = Loader.loadAnimation(tempAnims[i]);
            }
        }
        String tempHitChecker = data.getAttribute("hitChecker");
        if (tempHitChecker != null) {
            hitChecker = Loader.loadHitChecker(tempHitChecker);
        }
    }

    @Override
    public T getData() {
        return data;
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        // 检查是否可见
        if (!isVisibleForPlayer()) {
            playService.removeObject(this);
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
            playService.removeObject(parent);
        }
    }
    
    /**
     * 创建Chat主UI界面
     * @param width
     * @param height
     * @return 
     */
    protected abstract UI  createChatUI(float width, float height);
    
    protected void displayChatUI(UI ui) {
        UIState.getInstance().addUI(ui.getDisplay());
        if (animations != null) {
            for (Anim anim : animations) {
                anim.setTarget(ui);
                anim.start();
            }
        }
    }

    @Override
    public void update(float tpf) {
        // 检查是否可见
        if (!isVisibleForPlayer()) {
            playService.removeObject(this);
            return;
        }
        
        // 更新动画
        if (animations != null) {
            for (Anim anim : animations) {
                anim.update(tpf);
            }
        }
    }
    
    @Override
    public void cleanup() {
        if (reopenParent && parent != null) {
            playService.addObject(parent);
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
    
    /**
     * 检查Chat是否对当前场景中的玩家可见。
     * @return 
     */
    public boolean isVisibleForPlayer() {
        return isVisible(playService.getPlayer());
    }
    
    /**
     * 判断当前Chat对目标target是否可见。
     * @param target
     * @return 
     */
    public boolean isVisible(Actor target) {
        if (target == null || target == actor || actorService.isDead(target))
            return false;
        
        if (actor == null 
                || actor.getModel().getWorldTranslation().distanceSquared(target.getModel().getWorldTranslation()) > maxDistanceSquared 
                || !playService.isInScene(actor)) 
            return false;
        
        if (hitChecker != null && !hitChecker.canHit(actor, target))
            return false;
        
        return true;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
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
        playService.removeObject(this);
    }
    
}
