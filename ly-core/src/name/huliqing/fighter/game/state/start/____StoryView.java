///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.state.start;
//
//import com.jme3.font.BitmapFont;
//import com.jme3.font.BitmapFont.Align;
//import com.jme3.math.ColorRGBA;
//import java.util.List;
//import name.huliqing.fighter.Config;
//import name.huliqing.fighter.constants.IdConstants;
//import name.huliqing.fighter.data.GameData;
//import name.huliqing.fighter.game.state.lan.play.StoryState;
//import name.huliqing.fighter.data.DataLoaderFactory;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.save.SaveStory;
//import name.huliqing.fighter.ui.ListView;
//import name.huliqing.fighter.ui.Row;
//import name.huliqing.fighter.ui.Text;
//import name.huliqing.fighter.ui.UIFactory;
//import name.huliqing.fighter.ui.UI;
//
///**
// * @author huliqing
// */
//public class StoryView extends ListView<String> {
//    private StartState startState;
//    
//    private StoryRow treasureRow;
//    private StoryRow gbRow;
//    private StoryRow guardRow;
//    private StoryRow toBeContinued;
//
//    private SaveStory saveStory;
//    
//    /**
//     * @param width
//     * @param height
//     * @param state
//     */
//    public StoryView(float width, float height, StartState state) {
//        super(width, height, "StoryPanel");
//        this.startState = state;
//        
//        treasureRow = new StoryRow(get("story.treasure"), IdConstants.GAME_STORY_TREASURE); 
//        gbRow = new StoryRow(get("story.gb"), IdConstants.GAME_STORY_GB);
//        guardRow = new StoryRow(get("story.guard"), IdConstants.GAME_STORY_GUARD);
//        
//        toBeContinued = new StoryRow(get("story.toBeContinued"), null);
//        toBeContinued.setEnabled(false);
//        
//        rows.add(treasureRow);
//        rows.add(gbRow);
//        rows.add(guardRow);
//        rows.add(toBeContinued);
//        for (int i = 0; i < rows.size(); i++) {
//            StoryRow row = (StoryRow) rows.get(i);
//            attachChild(row);
//        }
//        this.pageSize = rows.size();
//        
//        // 更新要显示的关卡数
//        updateStoryList();
//    }
//
//    @Override
//    public int getRowTotal() {
//        return rows.size();
//    }
//    
//    @Override
//    protected Row createEmptyRow() {
//        return null;
//    }
//
//    @Override
//    public List getDatas() {
//        return null;
//    }
//
//    public void setSaveStory(SaveStory saveStory) {
//        this.saveStory = saveStory;
//    }
//    
//    /**
//     * 更新关卡列表，比如当存档装载的时候，关卡列表需要重新更新。
//     */
//    public final void updateStoryList() {
//        int completeStage = 0;
//        if (saveStory != null) {
//            completeStage = saveStory.getStoryCount();
//        }
//        if (Config.debug) {
//            completeStage = 999;
//        }
//        for (int i = 0; i < rows.size() - 1; i++) {
//            StoryRow row = (StoryRow) rows.get(i);
//            row.setEnabled(i <= completeStage);
//        }
//    }
//    
//    private String get(String resId) {
//        return ResourceManager.get(resId);
//    }
//    
//    private class StoryRow extends Row {
//        
//        private String gameId;
//        private boolean enabled;
//        private Text text;
//
//        public StoryRow(String text, String _gameId) {
//            super();
//            this.gameId = _gameId;
//            // Row text
//            this.text = new Text(text);
//            this.text.setBackground(UIFactory.getUIConfig().getBackground(), true);
//            this.text.setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
//            addView(this.text);
//            
//            // Row event
//            if (this.gameId != null) {
//                addClickListener(new Listener() {
//                    @Override
//                    public void onClick(UI ui, boolean isPress) {
//                        if (isPress) return;
//                        GameData gameData = DataLoaderFactory.createGameData(gameId);
//                        StoryState story = new StoryState(gameData);
//                        story.setSaveStory(saveStory);
//                        startState.startState(story);
//                    }
//                });
//            }
////            this.setDebug(true);
//        }
//
//        @Override
//        public void updateViewChildren() {
//            super.updateViewChildren();
//            float space = 20;
//            text.setWidth(width);
//            text.setHeight(height - space * 2);
//            text.setMargin(0, space, 0, 0);
//            text.setAlignment(Align.Center);
//            text.setVerticalAlignment(BitmapFont.VAlign.Center);
//        }
//        
//        public void setEnabled(boolean enabled) {
//            this.enabled = enabled;
//            text.setFontColor(enabled ? ColorRGBA.White : ColorRGBA.DarkGray);
//        }
//
//        @Override
//        public boolean fireClick(boolean isPress) {
//            if (!enabled) {
//                return false;
//            }
//            return super.fireClick(isPress);
//        }
//        
//        @Override
//        protected void clickEffect(boolean isPress) {
//            if (isPress) {
//                text.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
//            } else {
//                text.setBackgroundColor(UIFactory.getUIConfig().getButtonBgColor(), true);
//            }
//        }
//        
//        @Override
//        public void displayRow(Object data) {
//            // ignore
//        }
//        
//    }
//    
//}
