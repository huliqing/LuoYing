/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.gamelogic.AbstractGameLogic;
import name.huliqing.luoying.ui.Icon;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Corner;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.state.UIState;
import name.huliqing.ly.constants.InterfaceConstants;
import name.huliqing.ly.state.MenuTool;

/**
 *
 * @author huliqing
 */
public abstract class PlayStateUI extends AbstractGameLogic {
    
    // 工具栏
    protected ToolsView toolsView;
    
    // 设置面板
    protected SettingView settingPanel;

    @Override
    protected void logicInit(Game game) {
        // 设置面板是公共必须有的
        float sw = LuoYing.getSettings().getWidth();
        float sh = LuoYing.getSettings().getHeight();
        
        // ==== 工具栏
        toolsView = new ToolsView();
        toolsView.setLayout(Layout.horizontal);
        toolsView.setToolSize(sw * 0.07f, sw * 0.07f);
        toolsView.setToolSpace(sw * 0.02f);
        
        // ==== “设置”按钮
        Icon setting = new Icon(InterfaceConstants.UI_SETTING);
        setting.setUseAlpha(true);
        setting.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPressed) {
                if (isPressed) return;
                if (settingPanel.getParent() == null) {
                    UIState.getInstance().addUI(settingPanel);
                } else {
                    settingPanel.removeFromParent();
                }
            }
        });
        toolsView.addView(setting);
        
        // ==== “设置”面板
        settingPanel = new SettingView(sw * 0.5f, sh * 0.5f);
        settingPanel.setToCorner(Corner.CC);
        settingPanel.setDragEnabled(true);
        settingPanel.setCloseable(true);
        
        // add to localRoot
        UIState.getInstance().addUI(toolsView);
    }
    
    public MenuTool getMenuTool() {
        return toolsView;
    }
    
    @Override
    public void cleanup() {
        if (toolsView != null) {
            toolsView.removeFromParent();
            toolsView = null;
        }
        if (settingPanel != null) {
            settingPanel.removeFromParent();
            settingPanel = null;
        }
        super.cleanup();
    }
}
