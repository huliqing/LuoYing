/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view;

import name.huliqing.ly.game.view.system.SystemMainPanel;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.ly.game.state.PlayState;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.ui.Button;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.Window;
import name.huliqing.core.ui.state.UIState;

/**
 * 设置面板
 * @author huliqing
 */
public class SettingView extends Window {
//    private final ConfigService configService = Factory.get(ConfigService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private PlayState playState;
    
    private Button system;
    private Button about;
    private Button back;
    
    // “系统”设置：包含声音、快捷方式、游戏难度、效果等等设置
    private SystemMainPanel systemPanel;
    // “关于”设置：关于游戏的一些说明
    private AboutView aboutPanel;
    
    public SettingView(PlayState playState, float width, float height) {
        super(width, height);
        setTitle(ResourceManager.get("setting.title"));
        this.playState = playState;
        init();
    }
    
    private void init() {
        float sw = playService.getScreenWidth();
        float sh = playService.getScreenHeight();
        float psw = sw * 1f;
        float psh = sh * 1f;
        
        // ==== button
        system = new Button(ResourceManager.get("setting.system"));
        about = new Button(ResourceManager.get("setting.about"));
        back = new Button(ResourceManager.get("setting.return"));
        
        this.addView(system);
        this.addView(about);
        this.addView(back);
        
        // ==== Panel
        aboutPanel = new AboutView(psw, psh);
        systemPanel = new SystemMainPanel(psw, psh);
        systemPanel.setToCorner(Corner.CC);
        systemPanel.setCloseable(true);
        
        // ==== Binding listener
        // back panel
        back.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    playState.exit();// 执行退出
                }
            }
        });
        
        // 系统设置
        system.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                SettingView.this.removeFromParent();
                UIState.getInstance().addUI(systemPanel);
            }
        });
        
        // about
        about.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                SettingView.this.removeFromParent();
                UIState.getInstance().addUI(aboutPanel);
            }
        });
        
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        List<UI> cuis = getViews();
        float btnWidth = getContentWidth();
        float btnHeight = getContentHeight() / cuis.size();
        for (UI ui : cuis) {
            ui.setWidth(btnWidth);
            ui.setHeight(btnHeight);
        }
    }
}
