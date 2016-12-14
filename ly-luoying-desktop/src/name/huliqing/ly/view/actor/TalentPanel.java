/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.luoying.data.TalentData;
import name.huliqing.luoying.layer.network.TalentNetwork;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.layer.service.TalentService;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;

/**
 * 天赋面板
 * @author huliqing
 */
public class TalentPanel extends LinearLayout implements ActorPanel{
    private final TalentService talentService = Factory.get(TalentService.class);
    private final TalentNetwork talentNetwork = Factory.get(TalentNetwork.class);
    
    private Entity actor;
    private final Text text;
    private final TalentListPanel listPanel;
    
    public TalentPanel(float width, float height) {
        this.width = width;
        this.height = height;
        listPanel = new TalentListPanel(width, height);
        text = new Text("");
        text.setFontColor(UIFactory.getUIConfig().getDesColor());
        text.setFontSize(UIFactory.getUIConfig().getDesSize());
        text.setHeight(UIFactory.getUIConfig().getTitleHeight());
        text.setVerticalAlignment(BitmapFont.VAlign.Center);
        text.setBackground(UIFactory.getUIConfig().getBackground(), true);
        text.setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), true);
        addView(listPanel);
        addView(text);
    }

    @Override
    public void setPanelVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public void setPanelUpdate(Entity actor) {
        this.actor = actor;
        listPanel.getDatas();
        listPanel.refreshPageData();
        
        int talentRemain = 0;
        if (actor != null) {
            talentRemain = talentService.getTalentPoints(actor);
        }
        text.setText(ResourceManager.get(ResConstants.TALENT_TALENT_POINTS_REMAIN, new Object[] {talentRemain}));
    }
    
    public void setPageSize(int pageSize) {
        listPanel.setPageSize(pageSize);
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        text.setWidth(width);
        text.setMargin(5, 0, 0, 0);
        
        listPanel.setWidth(width);
        listPanel.setHeight(height - text.getHeight());
    }
    
    // ---------------------------
    
    private class TalentListPanel extends ListView<TalentData>{
        private final List<TalentData> datas = new ArrayList<TalentData>();
        public TalentListPanel(float width, float height) {
            super(width, height);
        }
        @Override
        protected Row createEmptyRow() {
            final TalentRow row = new TalentRow(this);
            row.setUpListener(new Listener() {
                @Override
                public void onClick(UI ui, boolean isPress) {
                    if (!isPress) {
                        talentNetwork.addTalentPoints(actor, row.getData().getId(), 1);
                        setPanelUpdate(actor);
                    }
                }
            });
            return row;
        }

        @Override
        public List<TalentData> getDatas() {
            datas.clear();
            if (actor != null) {
                actor.getData().getObjectDatas(TalentData.class, datas);
            }
            return datas;
        }

        @Override
        public boolean removeItem(TalentData data) {
            throw new UnsupportedOperationException("天赋不能删除！");
        }
    }
    
}
