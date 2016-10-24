/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.ly.layer.service.GameService;

/**
 * 队伍面板
 * @author huliqing
 */
public class TeamView extends LinearLayout {
//    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final GameService gameService = Factory.get(GameService.class);
//    private final GameNetwork gameNetwork = Factory.get(GameNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
    // 组ID，所有匹配该ID的角色都将添加到当前面板中
    private int teamId;
    
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
    
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
    
    /**
     * 设置面板的主角色
     * @param actor 
     */
    public void setMainActor(Entity actor) {
        mainFace.setActor(actor);
    }
    
    /**
     * 检查是否要将一个角色添加到队伍或从队伍中移除.
     * @param actor 
     */
    public void checkAddOrRemove(Entity actor) {
        // teamId不匹配则移出队伍
        int team = actorService.getTeam(actor);
        if (team != teamId) {
            removeActor(actor);
            return;
        }
        // 如果已经添加到列表中，则不处理
        if (isAdded(actor)) {
            return;
        }
        // 添加到列表
        partnerPanel.addActor(actor);
        if (partnerPanel.getRowTotal() > 0) {
            partnerPanel.setVisible(true);
            partnerPanel.refreshPageData();
        }
    }
    
    public void removeActor(Entity actor) {
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
    public void clearPartners() {
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
                        actorNetwork.setFollow(player, target.getData().getUniqueId()); 
                        
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
