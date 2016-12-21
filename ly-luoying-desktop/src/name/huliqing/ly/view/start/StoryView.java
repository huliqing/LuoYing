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
package name.huliqing.ly.view.start;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Config;
import name.huliqing.luoying.data.GameData;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.game.GameAppState;
import name.huliqing.luoying.save.SaveStory;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.ly.constants.IdConstants;
import name.huliqing.ly.object.game.StoryServerNetworkRpgGame;
import name.huliqing.ly.view.start.StoryView.StoryData;

/**
 * @author huliqing
 */
public class StoryView extends ListView<StoryData> {
    private final List<StoryData> datas = new ArrayList<StoryData>(4);

    private final StartState startState;
    private SaveStory saveStory;
    
    /**
     * @param width
     * @param height
     * @param state
     */
    public StoryView(float width, float height, StartState state) {
        super(width, height, "StoryPanel");
        this.startState = state;
        
        datas.add(new StoryData(get("story.treasure"), IdConstants.GAME_STORY_TREASURE));
        datas.add(new StoryData(get("story.gb"), IdConstants.GAME_STORY_GB));
        datas.add(new StoryData(get("story.guard"), IdConstants.GAME_STORY_GUARD));
        datas.add(new StoryData(get("story.toBeContinued"), null));
        pageSize = datas.size();
        
        // 更新要显示的关卡数
        updateStoryList();
    }

    @Override
    public int getRowTotal() {
        return datas.size();
    }
    
    @Override
    protected Row createEmptyRow() {
        return new StoryRow(this);
    }

    @Override
    public List getDatas() {
        return datas;
    }

    public void setSaveStory(SaveStory saveStory) {
        this.saveStory = saveStory;
    }
    
    /**
     * 更新关卡列表，比如当存档装载的时候，关卡列表需要重新更新。
     */
    public final void updateStoryList() {
        int completeStage = 0;
        if (saveStory != null) {
            completeStage = saveStory.getStoryCount();
        }
        if (Config.debug) {
            completeStage = 999;
        }
        for (int i = 0; i < datas.size() - 1; i++) {
            datas.get(i).enabled = (i <= completeStage);
        }
        datas.get(datas.size() - 1).enabled = false;
        refreshPageData();
    }
    
    private String get(String resId) {
        return ResourceManager.get(resId);
    }
    
    class StoryData {
        String gameName;
        String gameId;
        boolean enabled;
        public StoryData(String gameName, String gameId) {
            this.gameName = gameName;
            this.gameId = gameId;
        }
    }
    
    private class StoryRow extends Row<StoryData> {
        private StoryData storyData;
        private Text text;

        public StoryRow(ListView parent) {
            super(parent);
            // Row text
            this.text = new Text("");
            this.text.setBackground(UIFactory.getUIConfig().getBackground(), true);
            this.text.setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
            addView(this.text);
            
            // Row event
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress || storyData.gameId == null) 
                        return;
                    
//                    GameData gameData = Loader.loadData(storyData.gameId);
//                    StoryServerPlayState sps = new LyStoryServerPlayState((Start) LuoYing.getApp(), gameData, saveStory);
//                    startState.startState(sps);
                    
                    GameData gameData = Loader.loadData(storyData.gameId);
                    StoryServerNetworkRpgGame game = Loader.load(gameData);
                    if (saveStory != null) {
                        game.setSaveStory(saveStory);
                    }
                    GameAppState gameApp = new GameAppState(game);
                    startState.startState(gameApp);
                    
                }
            });
        }

        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            float space = 20;
            text.setWidth(width);
            text.setHeight(height - space * 2);
            text.setMargin(0, space, 0, 0);
            text.setAlignment(Align.Center);
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
            text.setFontColor(storyData.enabled ? ColorRGBA.White : ColorRGBA.DarkGray);
        }

        @Override
        public boolean fireClick(boolean isPress) {
            if (!storyData.enabled) {
                return false;
            }
            return super.fireClick(isPress);
        }
        
        @Override
        protected void clickEffect(boolean isPress) {
            if (isPress) {
                text.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            } else {
                text.setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
            }
        }
        
        @Override
        public void displayRow(StoryData data) {
            storyData = data;
            text.setText(data.gameName);
        }
        
    }
    
}
