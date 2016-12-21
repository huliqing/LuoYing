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
package name.huliqing.ly.view;

import java.util.List;
import name.huliqing.luoying.network.LanGame;
import name.huliqing.luoying.data.ConnData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.ly.constants.ResConstants;

/**
 * 封装客户端列表的window
 * @author huliqing
 */
public class ClientsWin extends Window {
    
    // ----客户端列表
    private ClientsView clientsView;
    
    // ----帮助面板
    private LinearLayout helpPanel;
    // 帮助说明
    private HelpView help;
    
    // ---- 按钮面板
    private final LinearLayout btnPanel;
    // 踢出玩家
    private Button btnKick;
    
    private LanGame lanGame;

    public ClientsWin(final LanGame lanGame, float width, float height) {
        super(width, height);
        this.lanGame = lanGame;
        setTitle(ResourceManager.get(ResConstants.LAN_CLIENTS));
        
        clientsView = new ClientsView();
        
        help = new HelpView(width - 20
                , UIFactory.getUIConfig().getButtonHeight()
                , ResourceManager.get(ResConstants.LAN_HELP_RUNNING));
//        help.setDebug(true);
        
        help.setMargin(10, 0, 0, 0);
        helpPanel = new LinearLayout();
        helpPanel.addView(help);
        
        btnKick = new Button(ResourceManager.get(ResConstants.LAN_KICK));
        btnKick.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                ConnData selected = clientsView.getSelected();
                if (selected == null) {
                    return;
                }
                if (selected.getConnId() != -1) { // －1为主机
                    lanGame.kickClient(selected.getConnId());
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
    public void setClients(List<ConnData> clients) {
        clientsView.setClients(clients);
        // 如果不是服务端则要隐藏“踢出玩家”按钮
        btnKick.setVisible(lanGame.isServer());
        setNeedUpdate();
    }
    
    public void setBtnPanelVisiable(boolean visiable) {
        btnPanel.setVisible(visiable);
        setNeedUpdate();
    }

}
