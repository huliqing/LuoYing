/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.start;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapFont.Align;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Config;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.IdConstants;
import name.huliqing.fighter.data.GameData;
import name.huliqing.fighter.game.service.GameService;
import name.huliqing.fighter.game.state.game.StoryPlayState;
import name.huliqing.fighter.game.state.start.StoryView.StoryData;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.save.SaveStory;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.UI;

/**
 * @author huliqing
 */
public class StoryView extends ListView<StoryData> {
    private final GameService gameService = Factory.get(GameService.class);
    private final List<StoryData> datas = new ArrayList<StoryData>(4);

    private StartState startState;
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
                    GameData gameData = gameService.loadGameData(storyData.gameId);
                    StoryPlayState sps = new StoryPlayState(gameData);
                    sps.setSaveStory(saveStory);
                    startState.startState(sps);
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
