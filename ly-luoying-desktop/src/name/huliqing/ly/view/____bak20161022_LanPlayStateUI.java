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
//package name.huliqing.ly.view;
//
//import com.jme3.app.Application;
//import com.jme3.scene.Spatial;
//import name.huliqing.luoying.ui.UIUtils;
//import name.huliqing.luoying.Factory;
//import name.huliqing.luoying.LuoYing;
//import name.huliqing.luoying.layer.service.PlayService;
//import name.huliqing.luoying.object.actor.Actor;
//import name.huliqing.luoying.object.Loader;
//import name.huliqing.luoying.object.anim.Anim;
//import name.huliqing.luoying.ui.Icon;
//import name.huliqing.luoying.ui.UI;
//import name.huliqing.luoying.ui.UI.Corner;
//import name.huliqing.luoying.ui.state.UIState;
//import name.huliqing.ly.constants.IdConstants;
//import name.huliqing.ly.state.GameState;
//
///**
// * 场景控制,包含设置,人物控制,UI等
// * @author huliqing
// */
//public class LanPlayStateUI extends PlayStateUI {
//    private final PlayService playService = Factory.get(PlayService.class);
//    
//    // 队伍视图及目标视图
//    protected TeamView teamView;
//    protected FaceView targetFace;
//    
//    // 角色面板
//    private ActorMainPanel userPanel;
//    private Anim userPanelAnim;
//    
//    // UI:技能按钮
//    private UI attack;
//    
//    private final GameState gameState;
//    
//    public LanPlayStateUI(GameState gameState) {
//        super();
//        this.gameState = gameState;
//    }
//
//    @Override
//    public void initialize(Application app) {
//        // UI在LanPlayState中的initialize中进行了手动ui.initialize()，所以这里
//        // 要避免重复载入
//        if (isInitialized()) {
//            return;
//        }
//        super.initialize(app);
//        
//        // UI size
//        float fullWidth = LuoYing.getSettings().getWidth();
//        float fullHeight = LuoYing.getSettings().getHeight();
//        
//        // ---- 玩家头像和目标头像
//        
//        float faceWidth = fullWidth * 0.28f;
//        float faceHeight = fullHeight * 0.12f;
//        
//        teamView = new TeamView(faceWidth, faceHeight);
//        teamView.setToCorner(name.huliqing.luoying.ui.AbstractUI.Corner.LT);
//        
//        targetFace = new FaceView(faceWidth, faceHeight);
//        targetFace.setToCorner(name.huliqing.luoying.ui.AbstractUI.Corner.CT);
//        targetFace.setVisible(false);
//        
//        // ---- 角色面板及动画控制
//        userPanel = new ActorMainPanel(fullWidth * 0.8f, fullHeight * 0.8f);
//        userPanel.setToCorner(Corner.CC);
//        userPanel.setCloseable(true);
//        userPanel.setDragEnabled(true);
//        userPanelAnim = Loader.load(IdConstants.ANIM_VIEW_MOVE);
//        userPanelAnim.setTarget(userPanel);
//        
//        // ---- 人物属性开关铵钮
//        // 按钮：人物面板,包含装甲、武器、技能、属性、任务等等
//        Icon userBtn = new Icon("Interface/icon/bag.png");
//        userBtn.setUseAlpha(true);
//        userBtn.addClickListener(new name.huliqing.luoying.ui.UI.Listener() {
//            @Override
//            public void onClick(UI ui, boolean isPress) {
//                if (isPress) return;
//                displayUserPanel(null);
//            }
//        });
//        
//        // ---- 攻击按钮
//        float iconWidth = fullHeight * 0.25f;
//        attack = UIUtils.createMultView(iconWidth, iconWidth, "Interface/icon/weapon.png", "Interface/ui/circleButton/circleButton.png");
//        attack.setMargin(0, 0, 10, 10);
//        attack.setToCorner(UI.Corner.RB);
//        attack.setDragEnabled(false); // 拖久了或拖得太猛可能出现莫明消失的BUG（出屏幕边界，暂不允许拖动）
//        attack.addClickListener(new UI.Listener() {
//            @Override
//            public void onClick(UI ui, boolean isPressed) {
//                if (isPressed) return;
//                gameState.attack();
//            }
//        });
//        
//        // ---- 添加到界面
//        playState.addObject(teamView, true);
//        playState.addObject(targetFace, true);
//        playState.addObject(attack.getDisplay(), true);
//        toolsView.addView(userBtn, 0);
//    }
//
//    @Override
//    public void doLogic(float tpf) {
//        teamView.update(tpf);
//        targetFace.update(tpf);
//    }
//
//    public TeamView getTeamView() {
//        return teamView;
//    }
//    
//    public void setTargetFace(Actor face) {
//        this.targetFace.setActor(face);
//        this.targetFace.setVisible(true);
//    }
//
//    public FaceView getTargetFace() {
//        return targetFace;
//    }
//    
//    public ActorMainPanel getUserPanel() {
//        return userPanel;
//    }
//    
//    private void displayUserPanel(Actor actor) {
//        if (userPanel.getParent() == null) {
//            userPanel.setActor(actor != null ? actor : playState.getPlayer());
//            UIState.getInstance().addUI(userPanel);
//            playService.addAnimation(userPanelAnim);
//        } else {
//            playState.removeObject(userPanel);
//        }
//    }
//    
//    @Override
//    public void cleanup() {
//        remove(teamView);
//        remove(targetFace);
//        remove(attack.getDisplay());
//        super.cleanup();
//    }
//    
//    private void remove(Spatial spatial) {
//        if (spatial != null) {
//            playState.removeObject(spatial);
//        }
//    }
//
//}
