/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.attribute.Attribute;
import name.huliqing.luoying.object.attribute.NumberAttribute;
import name.huliqing.luoying.object.attribute.ValueChangeListener;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.ly.constants.AttrConstants;
import name.huliqing.ly.layer.network.GameNetwork;
import name.huliqing.ly.layer.service.GameService;

/**
 * 队伍面板, 注：角色的team分组必须大于0,并且必须与当前的teamId相同才会被添加到队伍面板中.
 * @author huliqing
 */
public class TeamView extends LinearLayout implements SceneListener, ValueChangeListener<Object>{
//    private final PlayService playService = Factory.get(PlayService.class);
    private final GameService gameService = Factory.get(GameService.class);
    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    
    // 用于Team属性和Entity关联，通过这个键可以从角色的TeamAttribute在获取角色的EntityId
    private final String KEY_ENTITY_ID = "_ENTITY_ID_";
    
    private Scene scene;
    
    private final float facePanelWidth;
    private final float facePanelHeight;
    
    // 主角面板
    private FaceView mainFace;
    // 队友面板列表
    private final PartnerPanel partnerPanel;
    // 队友显示数量
    private final int partnerPageSize = 6;
    private final float partnerPanelFactor = 0.75f;
    
    public TeamView(float facePanelWidth, float facePanelHeight) {
        this.facePanelWidth = facePanelWidth;
        this.facePanelHeight = facePanelHeight;
        width = facePanelWidth;
        height = facePanelHeight + facePanelHeight * partnerPanelFactor * partnerPageSize;
        
        mainFace = new FaceView(facePanelWidth, facePanelHeight);
        mainFace.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    if (mainFace.getActor() != null) {
                        gameService.setTarget(mainFace.getActor());
                    }
                }
            }
        });
        
        partnerPanel = new PartnerPanel(partnerPageSize, facePanelWidth * partnerPanelFactor, height - facePanelHeight);
        partnerPanel.setVisible(false);
        
        addView(mainFace);
        addView(partnerPanel);
        
//        setDebug(true);
//        mainFace.setDebug(true);
//        partnerPanel.setDebug(true);
    }

    public void setScene(Scene newScene) {
        if (scene != null) {
            scene.removeSceneListener(this);
        }
        scene = newScene;
        scene.addSceneListener(this);
    }
    
    /**
     * 设置面板的主角色
     * @param actor 
     */
    public void setMainActor(Entity actor) {
        mainFace.setActor(actor);
        clearPartners();
        // 添加队伍分组成员
        List<Actor> actors = scene.getEntities(Actor.class, null);
        for (Actor a : actors) {
            checkAddOrRemove(a);
        }
    }
    
    /**
     * 检查是否要将一个角色添加到队伍或从队伍中移除.
     * @param actor 
     */
    private void checkAddOrRemove(Entity actor) {
        // 是主角，则不需要添加到同伴面板
        if (actor == mainFace.getActor()) {
            return;
        }
        // 约定：mainTeamId <= 0 表示当前无队伍。所以不需要添加任何角色到队伍中。
        int mainTeamId = gameService.getTeam(mainFace.getActor());
        if (mainTeamId <= 0) {
            return;
        }
        // teamId不匹配则移出队伍
        if (gameService.getTeam(actor) != mainTeamId) {
            removeActor(actor);
            return;
        }
        // 添加到列表
        partnerPanel.addActor(actor);
        if (partnerPanel.getRowTotal() > 0) {
            partnerPanel.setVisible(true);
            partnerPanel.refreshPageData();
        }
    }
    
    /**
     * 从队伍面板中移除指定角色，角色必须存在于队伍面板中，否则什么也不会做。
     * @param actor 
     */
    private void removeActor(Entity actor) {
        if (partnerPanel.removeActor(actor)) {
            partnerPanel.refreshPageData();
            if (partnerPanel.getRowTotal() <= 0) {
                partnerPanel.setVisible(false);
            }
        }
    }
    
    /**
     * 清理队伍,不包含主角色
     */
    private void clearPartners() {
        partnerPanel.clear();
    }
    
    public void update(float tpf) {
        mainFace.update(tpf);
        partnerPanel.update(tpf);
    }
    
    // 判断角色是否已经在team中
    private boolean isAdded(Entity actor) {
        if (actor == mainFace.getActor()) 
            return true;
        for (Entity a : partnerPanel.getDatas()) {
            if (actor == a) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onSceneLoaded(Scene scene) {
        // ignore
    }
    
    /**
     * 当游戏场景中添加了实体时该方法被调用。
     * @param scene
     * @param entityAdded 
     */
    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        if (mainFace.getActor() == null || !(entityAdded instanceof Actor)) {
            return;
        }
        Attribute teamAttribute = entityAdded.getAttributeManager().getAttribute(AttrConstants.TEAM);
        if (teamAttribute != null) {
            teamAttribute.getData().setAttribute(KEY_ENTITY_ID, entityAdded.getEntityId());
            teamAttribute.addListener(this);
            checkAddOrRemove(entityAdded);
        }
    }
    
    /**
     * 当游戏场景中移除了实体时该方法被调用。
     * @param scene
     * @param entityRemoved 
     */
    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        if (mainFace.getActor() == null || !(entityRemoved instanceof Actor)) {
            return;
        }
        removeActor(entityRemoved);
    }

    /**
     * 这个方法主要用于监听角色的team属性变化，并将角色添加或移到指定的分组（UI界面）<br>
     * 不要直接调用这个方法
     * @param attribute 
     */
    @Override
    public void onValueChanged(Attribute attribute) {
        if (AttrConstants.TEAM.equals(attribute.getName())) {
            Long entityId = attribute.getData().getAsLong(KEY_ENTITY_ID);
            if (entityId == null) 
                return;
            Entity entity = scene.getEntity(entityId);
            if (entity == null)
                return;
            
            // 如果主角的队伍分组发生变化，则必须刷新队伍面板，因为这表示当前主玩家可能脱离队伍，也可能加入其它队伍。
            // 否则如果队伍分组变化的是其它玩家，则检查是将该玩家添加到队伍面板还是移出。
            if (entity == mainFace.getActor()) {
                setMainActor(mainFace.getActor());
            } else {
                checkAddOrRemove(entity);
            }
        }
    }
    
    // ---- Partner Panel ------------------------------------------------------
    
    private class PartnerPanel extends ListView<Entity> {

        private final List<Entity> partners = new ArrayList<Entity>();
        
        public PartnerPanel(int pageSize, float width, float height) {
            super(width, height);
            this.pageSize = pageSize;
        }

        @Override
        protected Row<Entity> createEmptyRow() {
            return new PartnerRow();
        }

        @Override
        public List<Entity> getDatas() {
            return partners;
        }
        
        public void addActor(Entity actor) {
            partners.add(actor);
        }
        
        public boolean removeActor(Entity actor) {
            return partners.remove(actor);
        }
        
        public void update(float tpf) {
            for (int i = 0; i < rows.size(); i++) {
                ((PartnerRow) rows.get(i)).facePanel.update(tpf);
            }
        }
        
        public void clear() {
            partners.clear();
            setNeedUpdate();
        }
        
    }
    
    private class PartnerRow extends Row<Entity> {
        private final FaceView facePanel = new FaceView(facePanelWidth * partnerPanelFactor, facePanelHeight * partnerPanelFactor);
        
        public PartnerRow() {
            super();
            
            facePanel.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (!isPress) {
                        if (facePanel.getActor() != null) {
                            gameService.setTarget(facePanel.getActor());
                        }
                    }
                }
            });
            facePanel.addDBClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    Entity player = gameService.getPlayer();
                    Entity target = facePanel.getActor();
                    if (player != null && target != null && player != target) {
                        gameNetwork.setFollow(player, target.getData().getUniqueId()); 
                        
                    }
                }
            });
            addView(facePanel);
        }
        
        @Override
        public void displayRow(Entity actor) {
            facePanel.setActor(actor);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
//            facePanel.setWidth(width);
//            facePanel.setHeight(height);
//            facePanel.updateView();
        }
    }
}
