/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.state.start;

import com.jme3.font.BitmapFont;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.save.SaveHelper;
import name.huliqing.luoying.save.SaveStory;
import name.huliqing.luoying.save.SaveStoryList;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.LinearLayout.Layout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UI.Listener;
import name.huliqing.luoying.ui.Window;

/**
 * 存档面板
 * @author huliqing
 */
public class SaveView extends Window {
    private StartState startState;
    private SaveList saveList;
    private ButtonPanel btnPanel;

    public SaveView(float width, float height, StartState startState) {
        super(ResourceManager.get("save.title"), width, height);
        this.startState = startState;
        saveList = new SaveList(0, 0);
        btnPanel = new ButtonPanel();
        addView(saveList);
        addView(btnPanel);
    }
    
    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float fw = getContentWidth();
        float fh = getContentHeight();
        float btnHeight = UIFactory.getUIConfig().getButtonHeight();
        saveList.refreshPageData();
        saveList.setHeight(fh - btnHeight);
        btnPanel.setWidth(fw);
        btnPanel.setHeight(btnHeight);
        
        saveList.setWidth(fw);
    }
    
    private String get(String resId) {
        return ResourceManager.get(resId);
    }
    
    // class ---- 存档列表
    private class SaveList extends ListView<String> {
        
        private List<String> saves = new ArrayList<String>();
        // 当前选中的行
        private SaveRow selected;
        
        public SaveList(float width, float height) {
            super(width, height, "SaveList");
            this.setPageSize(5);
        }

        @Override
        protected Row<String> createEmptyRow() {
            return new SaveRow(this);
        }

        @Override
        public List<String> getDatas() {
            saves.clear();
            SaveStoryList list = SaveHelper.loadStoryList();
            if (list != null) {
                List<String> sslist = list.getList();
                if (sslist != null && !sslist.isEmpty()) {
                    saves.addAll(sslist);
                }                
            }
            return saves;
        }
        
        public void setSelected(SaveRow row) {
            for (int i = 0; i < rows.size(); i++) {
                ((SaveRow) rows.get(i)).setActive(false);
            }
            this.selected = row;
            this.selected.setActive(true);
        }
    
    }
    
    private class SaveRow extends Row<String> {
        private SaveList saveList;
        private Text saveName;
        
        public SaveRow(SaveList sl) {
            saveList = sl;
            saveName = new Text("");
            setLayout(Layout.horizontal);
            addView(saveName);
            
            addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    saveList.setSelected(SaveRow.this);
                }
            });
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
        }

        @Override
        public void displayRow(String text) {
            saveName.setText(text);
        }
        
        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            saveName.setWidth(width);
            saveName.setHeight(height);
            saveName.setVerticalAlignment(BitmapFont.VAlign.Center);
            saveName.setAlignment(BitmapFont.Align.Center);
        }
        
        public void setActive(boolean actived) {
            setBackgroundVisible(actived);
        }

        @Override
        protected void clickEffect(boolean isPressed) {
            // ignore
        }
        
    }
    
    // class ---- 操作按钮
    private class ButtonPanel extends LinearLayout {
        private Button save;    // 新存档
        private Button load;
        private Button delete;
        // 注意该格式可能成为文件名，所以要注意各个系统的卷材语法，比如在win下不要用":"作为文件名
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        
        public ButtonPanel() {
            super();
            save = new Button(get("save.new"));
            load = new Button(get("save.load"));
            delete = new Button(get("save.delete"));
            
            this.setLayout(Layout.horizontal);
            addView(save);
            addView(load);
            addView(delete);
            
            save.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    SaveHelper.saveStoryNew();
                    saveList.refreshPageData();
                }
            });
            load.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    if (saveList.selected == null) {
                        return;
                    }
                    SaveStory saveStory = SaveHelper.loadStory(saveList.selected.saveName.getText());
                    startState.showStoryPanel(saveStory);
                }
            });
            delete.addClickListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (isPress) return;
                    if (saveList.selected == null) {
                        return;
                    }
                    String saveName = saveList.selected.saveName.getText();
                    if (saveName.equals("SaveLast")) {
                        return;
                    }
                    SaveHelper.deleteStory(saveName);
                    saveList.refreshPageData();
                }
            });
        }
        
        @Override
        public void updateViewChildren() {
            super.updateViewChildren();
            List<UI> uis = getViews();
            float btnWidth = width / uis.size();
            for (UI ui : uis) {
                ui.setWidth(btnWidth);
                ui.setHeight(UIFactory.getUIConfig().getButtonHeight());
            }
        }
    }
    
//    private class WrapButton extends Button {
//
//        public WrapButton(String text) {
//            super(text);
//            this.setBackground(UIFactory.getUIConfig().getBackground(), true);
//            this.setBackgroundColor(UIFactory.getUIConfig().getDesColor(), true);
//        }
//
//        @Override
//        protected void clickEffect(boolean isPress) {
//            background.setColor(isPress ? UIFactory.getUIConfig().getActiveColor() : UIFactory.getUIConfig().getDesColor());
//        }
//        
//        
//    }
    
}
