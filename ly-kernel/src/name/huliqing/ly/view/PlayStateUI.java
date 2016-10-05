/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import com.jme3.app.Application;
import name.huliqing.ly.state.MenuTool;
import name.huliqing.ly.state.PlayState;
import name.huliqing.ly.LY;
import name.huliqing.ly.object.IntervalLogic;
import name.huliqing.ly.ui.Icon;
import name.huliqing.ly.ui.LinearLayout.Layout;
import name.huliqing.ly.ui.UI;
import name.huliqing.ly.ui.UI.Corner;
import name.huliqing.ly.ui.UI.Listener;

/**
 *
 * @author huliqing
 */
public abstract class PlayStateUI extends IntervalLogic {
    
    protected PlayState playState;
    
    // 工具栏
    protected ToolsView toolsView;
    
    // 设置面板
    private SettingView settingPanel;
    
    public PlayStateUI () {
        super(0);
        playState = LY.getPlayState();
    }
    
    @Override
    public void initialize(Application app) {
        super.initialize(app);
        
        // 设置面板是公共必须有的
        float sw = LY.getSettings().getWidth();
        float sh = LY.getSettings().getHeight();
        
        // ==== 工具栏
        toolsView = new ToolsView();
        toolsView.setLayout(Layout.horizontal);
        toolsView.setToolSize(sw * 0.07f, sw * 0.07f);
        toolsView.setToolSpace(sw * 0.02f);
        
        // ==== “设置”按钮
        Icon setting = new Icon("Interface/icon/setting.png");
        setting.setUseAlpha(true);
        setting.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPressed) {
                if (isPressed) return;
                if (settingPanel.getParent() == null) {
                    playState.addObject(settingPanel, true);
                } else {
                    playState.removeObject(settingPanel);
                }
            }
        });
        toolsView.addView(setting);
        
        // ==== “设置”面板
        settingPanel = new SettingView(playState, sw * 0.5f, sh * 0.5f);
        settingPanel.setToCorner(Corner.CC);
        settingPanel.setDragEnabled(true);
        settingPanel.setCloseable(true);
        
        // add to localRoot
        playState.addObject(toolsView, true);
    }
    
    public MenuTool getMenuTool() {
        return toolsView;
    }
    
    @Override
    public void cleanup() {
        if (toolsView != null) {
            playState.removeObject(toolsView);
            toolsView = null;
        }
        if (settingPanel != null) {
            playState.removeObject(settingPanel);
            settingPanel = null;
        }
        playState = null;
        super.cleanup();
    }
}
