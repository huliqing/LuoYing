/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.GameService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.utils.MathUtils;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.SimpleRow;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.Window;

/**
 * 用于选择游戏的列表界面
 * @author huliqing
 */
public class CreateRoomStateGameListPanel extends Window {
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final GameService gameService = Factory.get(GameService.class);
    
    // 左边的游戏列表
    private final GameList gameList;
    
    // 右边的游戏说明
    private final LanGameInfoView overview;
    
    public CreateRoomStateGameListPanel(float width, float height) {
        super(ResourceManager.get("lan.gameList"), width, height);
        setLayout(Layout.horizontal);
        
        // gameList.setSelected(0)需要引用overview,所以需要先初始化
        overview = new LanGameInfoView();
        
        gameList = new GameList(0, 0);
        gameList.refreshPageData();
        gameList.setSelected(0);
        
        addView(gameList);
        addView(overview);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float contextWidth = getContentWidth();
        float contextHeight = getContentHeight();
        gameList.setWidth(contextWidth * MathUtils.GOLD_SEPARATE);
        gameList.setHeight(contextHeight);
        
        overview.setWidth(contextWidth - gameList.getWidth());
        overview.setHeight(contextHeight);
    }
    
    /**
     * 获取被选中的游戏id,如果没有选择则返回null.
     * @return 
     */
    public String getSelected() {
        if (gameList.selected != null) {
            return gameList.selected.gameId;
        }
        return null;
    }
    
    private class GameList extends ListView<String> {
        
        private List<String> games;
        // 当前选中的行
        private GameRow selected;
        
        public GameList(float width, float height) {
            super(width, height);
            games = new ArrayList<String>();
            String[] lanGames = configService.getLanGames();
            for (String gameName : lanGames) {
                if (gameName != null && !gameName.trim().isEmpty()) {
                    games.add(gameName);
                }
            }
        }
        
        @Override
        protected Row<String> createEmptyRow() {
            return new GameRow();
        }
        
        @Override
        public List<String> getDatas() {
            return games;
        }
        
        public void setSelected(int index) {
            if (index > rows.size() - 1)
                return;
            setSelected((GameRow) rows.get(index));
        }
        
        public void setSelected(GameRow row) {
            for (int i = 0; i < rows.size(); i++) {
                ((GameRow) rows.get(i)).setActive(false);
            }
            this.selected = row;
            this.selected.setActive(true);
            GameData gameData = gameService.loadGameData(row.gameId);
            overview.setGameData(gameData);
        }
        
    }
    
    private class GameRow extends SimpleRow<String> {
        private String gameId;
        public GameRow() {
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    gameList.setSelected(GameRow.this);
                }
            });
        }
        
        @Override
        public void displayRow(String dd) {
            gameId = dd;
            if (gameId == null) 
                return;
            text.setText(ResourceManager.getObjectName(gameId));
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            text.setWidth(width);
            text.setHeight(height);
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
        }
        
        public void setActive(boolean actived) {
            setBackgroundVisible(actived);
        }

        @Override
        protected void clickEffect(boolean isPressed) {
        }
        
    }
}
