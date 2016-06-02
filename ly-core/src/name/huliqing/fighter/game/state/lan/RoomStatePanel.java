/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import name.huliqing.fighter.game.state.lan.mess.MessPlayClientData;
import java.util.List;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.game.view.ClientsView;
import name.huliqing.fighter.utils.MathUtils;
import name.huliqing.fighter.ui.Window;

/**
 * 用于显示进入房间的客户端列表及游戏信息，主机和客户端共用该UI
 * @author huliqing
 */
public class RoomStatePanel extends Window {

    // 客户端列表面板
    private ClientsView clientsView;
    
    // 游戏信息面板
    private LanGameInfoView gameOverview;
    
    public RoomStatePanel(float width, float height) {
        super(ResourceManager.get("lan.clients"), width, height);
        this.setBackgroundVisible(false);
        this.setLayout(Layout.horizontal);
        
        // 客户端列表
        clientsView = new ClientsView();
        // 游戏信息
        gameOverview = new LanGameInfoView();
        
        addView(clientsView);
        addView(gameOverview);
        
    }
    
    public void setGameData(GameData gameData) {
        gameOverview.setGameData(gameData);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float w = getContentWidth();
        float h = getContentHeight();
        clientsView.setWidth(w * MathUtils.GOLD_SEPARATE);
        clientsView.setHeight(h);
        
        gameOverview.setWidth(w - clientsView.getWidth());
        gameOverview.setHeight(h);
        
    }
    
    /**
     * 设置新的客户端列表
     * @param clientData 
     */
    public void setClients(List<MessPlayClientData> clients) {
        clientsView.setClients(clients);
    }
    
    /**
     * 获取当前选中的客户端,注意：该方法可能返回null,因为可能没有选中任何
     * 客户端。
     * @return 
     */
    public MessPlayClientData getSelected() {
        return clientsView.getSelected();
    }

}
