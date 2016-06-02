/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.view;

import java.util.List;
import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import name.huliqing.fighter.game.state.lan.play.LanPlayState;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.Button;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.Window;

/**
 * 封装客户端列表的window
 * @author huliqing
 */
public class ClientsWin extends Window {
    
    private LanPlayState lanPlayState;
    
    // ----客户端列表
    private ClientsView clientsView;
    
    // ----帮助面板
    private LinearLayout helpPanel;
    // 帮助说明
    private HelpView help;
    
    // ---- 按钮面板
    private LinearLayout btnPanel;
    private Button btnKick;

    public ClientsWin(LanPlayState playState, float width, float height) {
        super(width, height);
        setTitle(ResourceManager.get("lan.clients"));
        this.lanPlayState = playState;
        
        clientsView = new ClientsView();
        
        help = new HelpView(width - 20
                , UIFactory.getUIConfig().getButtonHeight()
                , ResourceManager.get("lan.help.running"));
//        help.setDebug(true);
        
        help.setMargin(10, 0, 0, 0);
        helpPanel = new LinearLayout();
        helpPanel.addView(help);
        
        btnKick = new Button(ResourceManager.get("lan.kick"));
        btnKick.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                MessPlayClientData selected = clientsView.getSelected();
                if (selected == null) {
                    return;
                }
                if (selected.getConnId() != -1) { // －1为主机
                    lanPlayState.kickClient(selected.getConnId());
                }
            }
        });
        
        btnPanel = new LinearLayout();
        btnPanel.setLayout(Layout.horizontal);
        btnPanel.setBackgroundColor(UIFactory.getUIConfig().getDesColor(), true);
        btnPanel.addView(btnKick);
        
        addView(clientsView);
        addView(helpPanel);
        addView(btnPanel);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float fullWidth = getContentWidth();
        float fullHeight = getContentHeight();
        
        float helpPanelHeight = UIFactory.getUIConfig().getButtonHeight();
        float btnPanelHeight = btnPanel.isVisible() ? UIFactory.getUIConfig().getButtonHeight() : 0;
        float clientViewHeight = fullHeight - helpPanelHeight - btnPanelHeight;
        
        clientsView.setWidth(fullWidth - 20);
        clientsView.setMargin(10, 0, 0, 0);
        clientsView.setHeight(clientViewHeight);
        
        helpPanel.setWidth(fullWidth);
        helpPanel.setHeight(helpPanelHeight);
        
        btnPanel.setWidth(fullWidth);
        btnPanel.setHeight(btnPanelHeight);
        
        btnKick.setWidth(UIFactory.getUIConfig().getButtonWidth() + 40); // 宽度不够，在手机下出现换行
        btnKick.setHeight(btnPanelHeight);
        
    }
    
    /**
     * 设置客户端列表,如果给定的列表为null则清空列表
     * @param clients 
     */
    public void setClients(List<MessPlayClientData> clients) {
        clientsView.setClients(clients);
        setNeedUpdate();
    }
    
    public void setBtnPanelVisiable(boolean visiable) {
        btnPanel.setVisible(visiable);
        setNeedUpdate();
    }

}
