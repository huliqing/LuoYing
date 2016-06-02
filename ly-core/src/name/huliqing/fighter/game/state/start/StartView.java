/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.start;

import java.util.List;
import name.huliqing.fighter.save.SaveHelper;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Listener;

/**
 *
 * @author huliqing
 */
public class StartView extends LinearLayout {
//    private final ConfigService configService = Factory.get(ConfigService.class);
    private StartState startState;
    
    private SButton newGame;    // 新游戏
    private SButton continued;  // 继续游戏
    private SButton save;       // 存档
    private SButton network;    // 局域网
    private SButton settings;   // 设置
    private SButton exit;       // 退出
    
    public StartView(float width, float height, StartState startState) {
        super(width, height);
        this.startState = startState;
        init();
    }
    
    private void init() {
        this.setBackgroundVisible(false);
        
        // ---- button
        newGame = new SButton("start.new");
        newGame.setFontSize(40);
        
        continued = new SButton("start.continue");
        continued.setFontSize(40);
        
        save = new SButton("start.save");
        save.setFontSize(40);
        
        network = new SButton("start.network");
        network.setFontSize(40);
        
        settings = new SButton("start.settings");
        settings.setFontSize(40);
        
        exit = new SButton("start.exit");
        exit.setFontSize(40);
        
        this.addView(newGame);
        this.addView(continued);
        this.addView(save);
        this.addView(network);
        this.addView(settings);
        this.addView(exit);
        
        // ---- event
        setEvent();
        
        // 如果存在自动存档才显示“继续”按钮
        continued.setVisible(SaveHelper.existsLastSaveStory());
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren(); 
        float w = getContentWidth();
        float h = getContentHeight();
        
        int btnSize = getViews().size();
        List<UI> cui = getViews();
        float marginTB = 25;
        float btnWidth = w;
        float btnHeight = h / btnSize - marginTB;
        
        for (int i = 0; i < cui.size(); i++) {
            UI child = cui.get(i);
            child.setWidth(btnWidth);
            child.setHeight(btnHeight);
            if (i == 0) continue;
            child.setMargin(0, marginTB, 0, 0);
        }
    }
    
    private void setEvent() {
        newGame.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                startState.showStoryPanel(null);
            }
        });
        
        continued.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                startState.showStoryPanel(SaveHelper.loadStoryLast());
            }
        });
        
        save.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                startState.showSavePanel();
            }
        });
        
        network.addClickListener(new Listener() {

            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                startState.startLanState();
            }
        
        });
        
        settings.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                startState.showLocalePanel();
            }
        });
        
        exit.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) {
                    disableAllButton();
                    return;
                }
                // 不要使用app.stop();
                // app.stop在android下无法完全退出应用
                System.exit(0);
            }
        });
    }

    public SButton getNewGame() {
        return newGame;
    }

    public SButton getContinued() {
        return continued;
    }

    public SButton getSave() {
        return save;
    }

    public SButton getSettings() {
        return settings;
    }
    
    private void disableAllButton() {
        newGame.setActived(false);
        continued.setActived(false);
        save.setActived(false);
        network.setActived(false);
        settings.setActived(false);
        exit.setActived(false);
    }
}
