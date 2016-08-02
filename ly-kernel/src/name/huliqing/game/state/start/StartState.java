/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.game.state.start;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.IdConstants;
import name.huliqing.core.constants.InterfaceConstants;
import name.huliqing.core.mvc.service.GameService;
import name.huliqing.core.loader.Loader;
import name.huliqing.core.save.SaveHelper;
import name.huliqing.core.save.SaveStory;
import name.huliqing.core.shape.QuadXY;
import name.huliqing.core.utils.MatUtils;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Corner;
import name.huliqing.core.ui.UI.Listener;
import name.huliqing.core.ui.state.UIState;
import name.huliqing.game.Fighter;
import name.huliqing.game.LyLabPlayState;
import name.huliqing.game.state.lan.LanState;

/**
 * 开始界面
 * @author huliqing
 */
public class StartState extends AbstractAppState {
    private final GameService gameService = Factory.get(GameService.class);
    
    // 用于支持默认显示哪一个面板
    public enum Menu {
        menu_new,
        menu_story,
        menu_save,
        menu_settings,
    }
    
    private final Fighter app;
    private Node localRoot;
    
    private StartView startPanel;
    private StoryView storyPanel;      // 故事列表 
    private SaveView savePanel;        // 存档列表
    private LocaleView settingsPanel;    // 语言选择面板
    private Menu defMenu;
    
    // UI 
    private Icon viewBtn;
    
    private ParticleEmitter emitter;
    
    public StartState(SimpleApplication app) {
        this.app = (Fighter) app;
    }

    @Override
    public void initialize(final AppStateManager stateManager, final Application _app) {
        super.initialize(stateManager, _app);
        app.getInputManager().setCursorVisible(true);
        app.getFlyByCamera().setEnabled(false);
        
        localRoot = new Node("StartState_localRoot");
        app.getRootNode().attachChild(localRoot);
        
        float sw = LY.getSettings().getWidth();
        float sh = LY.getSettings().getHeight();
        float lw = LY.getSettings().getWidth() * 0.382f;
        float rw = LY.getSettings().getWidth() * 0.618f;
        float topSpace = sh * 0.1f;
        
        // 落樱剑: ）
        Icon ly = new Icon("Interface/ly.png");
        ly.setWidth(rw);
        ly.setHeight(rw);
        ly.setToCorner(Corner.RC);
        ly.setMargin(0, 0, (rw - ly.getWidth()) * 0.5f, 0);
        ly.setBackgroundColor(new ColorRGBA(1,1,1,0.4f), true);
        UIState.getInstance().addUI(ly);
        
        startPanel = new StartView(lw * 0.7f, sh * 0.8f, this);
        startPanel.setMargin(lw * 0.15f, topSpace, 0, 0);
        startPanel.setToCorner(Corner.LT);
        
        storyPanel = new StoryView(rw * 0.8f, sh * 0.7f, this); // stageOk为已经完成数，+1则激活下一关卡，如果存在。
        storyPanel.setVisible(false);
        storyPanel.setMargin(0, topSpace, rw * 0.1f, 0);
        storyPanel.setToCorner(Corner.RT);
        storyPanel.updateStoryList();
        
        savePanel = new SaveView(rw * 0.8f, sh * 0.7f, this);
        savePanel.setVisible(false);
        savePanel.setMargin(0, topSpace, rw * 0.1f, 0);
        savePanel.setToCorner(Corner.RT);
        
        settingsPanel = new LocaleView(rw * 0.8f, sh * 0.7f, this);
        settingsPanel.setVisible(false);
        settingsPanel.setMargin(0, topSpace, rw * 0.1f, 0);
        settingsPanel.setToCorner(Corner.RT);
        
        // ==== for view ====
        viewBtn = new Icon("Interface/icon/lab.png");
        viewBtn.setWidth(LY.getSettings().getHeight() * 0.12f);
        viewBtn.setHeight(viewBtn.getWidth());
        viewBtn.setMargin(5, 0, 0, 5);
        viewBtn.setToCorner(Corner.LB);
        viewBtn.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    startState(new LyLabPlayState(LY.getApp(), gameService.loadGameData(IdConstants.GAME_LAB)));
                }
            }
        });
        
        UIState.getInstance().addUI(startPanel);
        UIState.getInstance().addUI(storyPanel);
        UIState.getInstance().addUI(savePanel);
        UIState.getInstance().addUI(settingsPanel);
        UIState.getInstance().addUI(viewBtn);
        startPanel.setVisible(true);
        
        //----
        
        // ---- 分割线
        Material mat = MatUtils.createTransparent(InterfaceConstants.UI_LINE_V);
        mat.setColor("Color", new ColorRGBA(1,1,1,0.75f));
        QuadXY qv = new QuadXY(2, sh * 0.8f);
        Spatial separate = new Geometry("separate", qv);
        separate.setMaterial(mat);
        separate.setLocalTranslation(lw, (sh - qv.getHeight()) * 0.5f, 0);
        UIState.getInstance().addUI(separate);
        
        // ---- 樱花发射器,注：发射器放置在rootNode中（非GUI ROOT)，所以需要重置
        // 镜头，否则每次切换state后镜头发生变化，可能导致镜头离发射器太远。
        // 樱花看起来很小。
//        emitter = Loader.loadEmitter(IdConstants.EMITTER_SAKURA);
//        emitter.setLocalTranslation(0, 4.5f, 0);
//        localRoot.attachChild(emitter);
//        app.getCamera().setLocation(new Vector3f(0,0,10));
//        app.getCamera().lookAt(new Vector3f(0,0,-1), Vector3f.UNIT_Y);
        
        emitter = Loader.loadEmitter(IdConstants.EMITTER_SAKURA).getParticleEmitter();
        emitter.setLocalTranslation(sw * 0.5f, sh, 1);
        emitter.setLocalScale(50);
        // 必须正确设置inWorldSpace,FaceNormal,QueueBucket否则在GUI上无法显示emitter
        emitter.setInWorldSpace(false);
        emitter.setFaceNormal(Vector3f.UNIT_Z);
        emitter.setQueueBucket(RenderQueue.Bucket.Gui);
        UIState.getInstance().addUI(emitter);
        
        // ---- 默认显示故事列表面板
        SaveStory lastSave = SaveHelper.loadStoryLast();
        if (defMenu == null) {
            if (lastSave != null) {
                defMenu = Menu.menu_story;
            }
        }
        if (defMenu != null) {
            switch (defMenu) {
                case menu_new:
                    showStoryPanel(lastSave);
                    startPanel.getNewGame().fireClick(false);
                    startPanel.getNewGame().setActived(true);
                    break;
                case menu_story:
                    showStoryPanel(lastSave);
                    startPanel.getContinued().fireClick(false);
                    startPanel.getContinued().setActived(true);
                    break;
                case menu_save:
                    showSavePanel();
                    startPanel.getSave().fireClick(false);
                    startPanel.getSave().setActived(true);
                    break;
                case menu_settings:
                    showLocalePanel();
                    startPanel.getSettings().fireClick(false);
                    startPanel.getSettings().setActived(true);
                    break;
            }
        }
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
    }
    
    public Fighter getApp() {
        return this.app;
    }
    
    public void showStoryPanel(SaveStory saveStory) {
        hideAllPanel();
        storyPanel.setSaveStory(saveStory);
        storyPanel.setVisible(true);
        storyPanel.refreshPageData();
        storyPanel.updateStoryList();
    }
    
    public void showSavePanel() {
        hideAllPanel();
        savePanel.setVisible(true);
    }
    
    public void showLocalePanel() {
        hideAllPanel();
        settingsPanel.setVisible(true);
        settingsPanel.refreshPageData();
    }
    
    private void hideAllPanel() {
        storyPanel.setVisible(false);
        savePanel.setVisible(false);
        settingsPanel.setVisible(false);
    }
    
    /**
     * 开始执行appState
     * @param appState 
     */
    public void startState(AppState appState) {
        startPanel.setVisible(false);
        savePanel.setVisible(false);
        storyPanel.setVisible(false);
        settingsPanel.setVisible(false);
        viewBtn.setVisible(false);
        app.changeState(appState);
    }
    
    /**
     * 进入局域网模式
     */
    public void startLanState() {
        startState(new LanState());
    }
    
    public void refreshState(Menu menu) {
        StartState startState = new StartState(app);
        startState.defMenu = menu;
        startState(startState);
    }
    
    @Override
    public void cleanup() {
        localRoot.removeFromParent();
        UIState.getInstance().clearUI();
        super.cleanup();
    }
    
    
}
