/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.asset.AssetNotFoundException;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.util.TempVars;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.constants.InterfaceConstants;
import name.huliqing.luoying.data.StateData;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.anim.Loop;
import name.huliqing.luoying.object.anim.ColorAnim;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.entity.EntityDataListener;
import name.huliqing.ly.object.chat.Chat;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.xml.ObjectData;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.layer.service.GameService;

/**
 * 显示角色头像
 * @author huliqing
 */
public final class FaceView extends LinearLayout implements EntityDataListener{
    private final PlayService playService = Factory.get(PlayService.class);
    private final ChatService chatService = Factory.get(ChatService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    private Entity actor;
    private NumberAttribute lifeAttribute;
    private NumberAttribute lifeAttributeMax;
    private NumberAttribute manaAttribute;
    private NumberAttribute manaAttributeMax;
    private NumberAttribute xpAttribute;
    private NumberAttribute xpAttributeNext;
    
    // 左区：角色头像
    private LinearLayout leftZone;
    private PicPanel picPanel;
    
    // 右区：名称，血槽，状态
    private LinearLayout rightZone;
    private NamePanel namePanel;
    private ProgressPanel progressPanel;
    private StatePanel statePanel;
    
    // 对话区
    private Icon chatIcon;
    
    // 最近一次坐标
    private final Vector3f lastPos = new Vector3f();
    // 最近一次的生命值
    private int lastLife = -1;
    // 坐标数字格式化
    private final DecimalFormat decimal = new DecimalFormat("#");
    // 更新间隔时间，单位秒
    private final float interval = 0.25f;
    private float intervalUsed;
    
    public FaceView(float width, float height) {
        super(width, height);
        
        float lw = height;
        float lh = lw;
        float rw = width - lw;
        float rh = height;
        
        leftZone = new LinearLayout(lw, lh);
        picPanel = new PicPanel(lw, lh);
        leftZone.addView(picPanel);
        
        rightZone = new LinearLayout(rw, rh);
        namePanel = new NamePanel(rw, rh * 0.3f);
        progressPanel = new ProgressPanel(rw, rh * 0.4f);
        statePanel = new StatePanel(rw, rh * 0.3f);
        rightZone.addView(namePanel);
        rightZone.addView(progressPanel);
        rightZone.addView(statePanel);
        
        chatIcon = new Icon(InterfaceConstants.UI_CHAT);
        chatIcon.setWidth(rh * 0.5f);
        chatIcon.setHeight(rh * 0.5f);
        chatIcon.setMargin(0, namePanel.getHeight() * 0.8f, 0, 0);
        chatIcon.setVisible(false);
        chatIcon.addClickListener(new Listener() {
            @Override
            public void onClick(UI view, boolean isPressed) {
                if (isPressed) 
                    return;
                // 添加chat,chat是否可见由chat内部判断处理。
                Chat chat = chatService.getChat(actor);
                if (chat == null)
                    return;
                playService.addEntity(chat);
            }
        });
        
        setLayout(Layout.horizontal);
        addView(leftZone);
        addView(rightZone);
        addView(chatIcon);
    }
    
    /**
     * 获取当前的目标,如果当前没有目标则返回null.
     * @return 
     */
    public Entity getActor() {
        return actor;
    }
    
    public void setActor(Entity newActor) {
        if (this.actor != null) {
            this.actor.removeEntityDataListener(this);
        }
        
        this.actor = newActor;
        this.actor.addEntityDataListener(this);
        
        lifeAttribute = newActor.getAttributeManager().getAttribute(AttrConstants.HEALTH, NumberAttribute.class);
        lifeAttributeMax = newActor.getAttributeManager().getAttribute(AttrConstants.HEALTH_MAX, NumberAttribute.class);
        
        manaAttribute = newActor.getAttributeManager().getAttribute(AttrConstants.MANA, NumberAttribute.class);
        manaAttributeMax = newActor.getAttributeManager().getAttribute(AttrConstants.MANA_MAX, NumberAttribute.class);
        
        xpAttribute = newActor.getAttributeManager().getAttribute(AttrConstants.XP, NumberAttribute.class);
        xpAttributeNext = newActor.getAttributeManager().getAttribute(AttrConstants.XP_NEXT, NumberAttribute.class);
        
        picPanel.setActor(newActor);
        namePanel.setActor(newActor);
        lastLife = lifeAttribute != null ? lifeAttribute.intValue() : 0;
        
        // 立即更新一次，必要的
        update(interval);
    }

    public void update(float tpf) {
        if (actor == null || !isVisible()) {
            return;
        }
        
        // ---- 实时更新
        picPanel.colorAnim.update(tpf);
        
        // ---- 非实时更新
        intervalUsed += tpf;
        if (intervalUsed < interval) {
            return;
        }
        intervalUsed = 0;
        
        namePanel.update(tpf);
        progressPanel.update(tpf);
        statePanel.update(tpf);
        
        // 注：因为actor有可能已经死亡，并且已经被清理，所以需要判断isInitialized,否则可能会NullPointer
        if (actor != null && actor.isInitialized()) {
            // 显示受伤动画,头像动画
            if (lifeAttribute.floatValue() < lastLife) {
                picPanel.colorAnim.start();
            }
            lastLife = lifeAttribute.intValue();
            
            // 确定Chat是否显示
            Chat chat = chatService.getChat(actor);
            chatIcon.setVisible(chat != null && chat.isVisibleForPlayer());
        }
    }
    
    @Override
    public void onDataAdded(ObjectData data, int amount) {
        if (data instanceof StateData) {
            statePanel.needRefreshStates = true;
        }
    }
    
    @Override
    public void onDataRemoved(ObjectData data, int amount) {
        if (data instanceof StateData) {
            statePanel.needRefreshStates = true;
        }
    }
    
    @Override
    public void onDataUsed(ObjectData data) {
        // ignore
    }
    
    /**
     * 角色头像
     */
    private class PicPanel extends LinearLayout {
        private final Material mat;
        private final FaceColorAnim colorAnim;
        
        public PicPanel(float width, float height) {
            super(width, height);
            
            final Texture texture = LuoYing.getAssetManager().loadTexture(InterfaceConstants.UI_MISS);
            mat = new Material(LuoYing.getAssetManager(), AssetConstants.MATERIAL_FACE_PANEL);
            mat.setTexture("Texture", texture);
            mat.setColor("Color", new ColorRGBA(1,1,1,1));
            mat.setColor("BorderColor", new ColorRGBA(0.7f, 0.7f, 0.7f, 0.95f));
            mat.setFloat("Radius", width * 0.5f);
            mat.setFloat("BorderWidth", width * 0.5f * 0.18f);
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            
            // Face
            Quad quad = new Quad(width, height);
            Geometry faceGeo = new Geometry("", quad);
            faceGeo.setMaterial(mat);
            faceGeo.setQueueBucket(Bucket.Gui);
            attachChild(faceGeo);
            
            // Panel 
            colorAnim = new FaceColorAnim();
            colorAnim.setStartColor(ColorRGBA.Red);
            colorAnim.setEndColor(ColorRGBA.White);
            colorAnim.setLoop(Loop.cycle);
            colorAnim.setSpeed(2);
            colorAnim.setTarget(faceGeo);
        }
        
        public void setActor(Entity actor) {
            String icon = actor.getData().getIcon();
            if (icon == null) {
                icon = InterfaceConstants.UI_MISS;
            }
            Texture tex;
            try {
                tex = LuoYing.getAssetManager().loadTexture(icon);
            } catch (AssetNotFoundException e) {
                tex = LuoYing.getAssetManager().loadTexture(InterfaceConstants.UI_MISS);
            }
            mat.setTexture("Texture", tex);
        }
    }
    
    /**
     * 状态面板
     */
    private class StatePanel extends LinearLayout {
        // 状态列表
        private final List<StateView> stateIconList = new ArrayList<StateView>(5);
        private final List<StateData> stateDatas = new ArrayList<StateData>();
        private boolean needRefreshStates = true;
        
        public StatePanel(float width, float height) {
            super(width, height);
            setLayout(Layout.horizontal);
        }
        
        void update(float tpf) {
            if (needRefreshStates) {
                stateDatas.clear();
                actor.getData().getObjectDatas(StateData.class, stateDatas);
                needRefreshStates = false;
            }
            // 确保生成足够的状态槽位
            if (stateDatas.size() > stateIconList.size()) {
                for (int i = stateIconList.size(); i < stateDatas.size(); i++) {
                    StateView stateIcon = new StateView(height, height);
                    stateIconList.add(stateIcon);
                    addView(stateIcon);
                }
            }
            for (int i = 0; i < stateIconList.size(); i++) {
                StateView si = stateIconList.get(i);
                if (i < stateDatas.size()) {
                    si.setState(stateDatas.get(i));
                    if (si.getParent() == null) {
                        addView(si);
                    }
                } else {
                    if (si.getParent() != null) {
                        removeView(si);
                    }
                }
            }
        }
    }
    
    /**
     * 生命值,魔法值,经验值进度条
     */
    private class ProgressPanel extends LinearLayout {
        // 生命值\魔法值面板\经验槽面板
        private final ProgressView health;
        private final ProgressView magic;
        private final ProgressView xp;
        public ProgressPanel(float width, float height) {
            super(width, height);
            
            // 生命值
            health = new ProgressView();
            health.setColor(ColorRGBA.Red.mult(2f));
            health.setWidth(width);
            health.setHeight(height * 0.4f);
            
            // 魔法值
            magic = new ProgressView();
            magic.setColor(ColorRGBA.Blue.mult(2f));
            magic.setWidth(width - 4);
            magic.setHeight(height * 0.35f);
            
            xp = new ProgressView();
            xp.setColor(ColorRGBA.Green.mult(2f));
            xp.setWidth(width - 8);
            xp.setHeight(height * 0.25f);
            
            health.setMargin(0, 0, 0, 0);
            magic.setMargin(-2, 0, 0, 0);
            xp.setMargin(-4, 0, 0, 0);
            
            addView(health);
            addView(magic);
            addView(xp);
            
//            health.setDebug(true);
//            magic.setDebug(true);
//            xp.setDebug(true);
        }
        
        public void update(float tpf) {
            // life
            if (lifeAttribute != null && lifeAttributeMax != null) {
                health.setMaxValue(lifeAttributeMax.intValue());
                health.setValue(lifeAttribute.intValue());
            }

            // mana
            if (manaAttribute != null && manaAttributeMax != null) {
                magic.setMaxValue(manaAttributeMax.intValue());
                magic.setValue(manaAttribute.intValue());
            }

            // xp
            if (xpAttribute != null && xpAttributeNext != null) {
                xp.setMaxValue(xpAttributeNext.intValue());
                xp.setValue(xpAttribute.intValue());
            }
        }
    }
    
    /**
     * 名称,骷髅头,位置信息
     */
    private class NamePanel extends LinearLayout {
        // 角色名称
        private final Text actorName;
        // 骷髅图标,敌人大于等于10级时显示
        private final Icon skull;
        // 角色坐标
        private final Text position;
        public NamePanel(float width, float height) {
            super(width, height);
            
            // 名称、坐标、骷髅
            actorName = new Text("");
            actorName.setFontSize(height);
            
            position = new Text("");
            position.setMargin(2, 0, 0, 0);
            position.setFontSize(height);
            
            skull = new Icon();
            skull.setImage(InterfaceConstants.UI_ITEM_SKULL);
            skull.setUseAlpha(true);
            skull.setColor(ColorRGBA.Red);
            skull.setWidth(height);
            skull.setHeight(height);
            skull.setMargin(2, 0, 0, 0);
            
            setLayout(Layout.horizontal);
            addView(actorName);
            addView(position);
            addView(skull);
        }

        public void setActor(Entity actor) {
            actorName.setText(actor.getData().getName());
            actorName.resize();
        }
        
        public void update(float tpf) {
            if (actor == null) return;
            
            // 1.update position
            Vector3f pos = actor.getSpatial().getWorldTranslation();
            if (pos.distanceSquared(lastPos) >= 1) {
                lastPos.set(pos);
                position.setText(" (" + decimal.format(lastPos.x) + ","  + decimal.format(lastPos.z) + ")");
                position.resize();
            }

            // update skull
            Entity player = gameService.getPlayer();
            if (player != null) {
                // 显示骷髅头
                int levelDis = gameService.getLevel(actor) - gameService.getLevel(player);
                skull.setVisible(levelDis >= 10);

                // 根据级别差距显示目标颜色
                if (levelDis <= 0) {
                    actorName.setFontColor(ColorRGBA.White);
                } else if (levelDis >= 10) {
                    actorName.setFontColor(ColorRGBA.Red);
                } else {
                    TempVars tv = TempVars.get();
                    tv.color.interpolateLocal(ColorRGBA.White, ColorRGBA.Red, levelDis/10f);
                    actorName.setFontColor(tv.color);
                    tv.release();
                }
            }
        }
    }
    
    /**
     * 头像颜色动画,当角色受伤时在头像上显示颜色渐变动画来提示角色受攻击.
     */
    private class FaceColorAnim extends ColorAnim { 
        
        private final int loopLimit = 2;
        private int loopUsed;
        
        @Override
        public void display(float interpolation) {
            super.display(interpolation);
            if (interpolation >= 1.0f) {
                loopUsed++;
            }
            if (loopUsed >= loopLimit) {
                cleanup();
            }
        }

        @Override
        public void cleanup() {
            loopUsed = 0;
            super.cleanup(); 
        }
        
    }
}
