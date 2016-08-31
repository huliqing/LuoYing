/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import java.util.ArrayList;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Listener;

/**
 * 队伍面板
 * @author huliqing
 */
public class TeamView extends LinearLayout {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    
    // 组ID，所有匹配该ID的角色都将添加到当前面板中
    private int teamId;
    
    private float facePanelWidth;
    private float facePanelHeight;
    
    // 主角面板
    private FaceView mainFace;
    // 队友面板列表
    private PartnerPanel partnerPanel;
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
                        playService.setTarget(mainFace.getActor());
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
    public void setMainActor(Actor actor) {
        mainFace.setActor(actor);
    }
    
    /**
     * 检查是否要将一个角色添加到队伍或从队伍中移除.
     * @param actor 
     */
    public void checkAddOrRemove(Actor actor) {
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
    
    public void removeActor(Actor actor) {
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
    private boolean isAdded(Actor actor) {
        if (actor == mainFace.getActor()) 
            return true;
        for (Actor a : partnerPanel.getDatas()) {
            if (actor == a) {
                return true;
            }
        }
        return false;
    } 
    
    // ---- Partner Panel ------------------------------------------------------
    
    private class PartnerPanel extends ListView<Actor> {

        private final List<Actor> partners = new ArrayList<Actor>();
        
        public PartnerPanel(int pageSize, float width, float height) {
            super(width, height);
            this.pageSize = pageSize;
        }

        @Override
        protected Row<Actor> createEmptyRow() {
            return new PartnerRow();
        }

        @Override
        public List<Actor> getDatas() {
            return partners;
        }
        
        public void addActor(Actor actor) {
            partners.add(actor);
        }
        
        public boolean removeActor(Actor actor) {
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
    
    private class PartnerRow extends Row<Actor> {
        private final FaceView facePanel = new FaceView(facePanelWidth * partnerPanelFactor, facePanelHeight * partnerPanelFactor);
        
        public PartnerRow() {
            super();
            
            facePanel.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (!isPress) {
                        if (facePanel.getActor() != null) {
                            playService.setTarget(facePanel.getActor());
                        }
                    }
                }
            });
            facePanel.addDBClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    Actor player = playService.getPlayer();
                    Actor target = facePanel.getActor();
                    if (player != null && target != null && player != target) {
                        userCommandNetwork.follow(player, target.getData().getUniqueId());
                        
                    }
                }
            });
            addView(facePanel);
        }
        
        @Override
        public void displayRow(Actor actor) {
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
