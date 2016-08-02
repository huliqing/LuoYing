/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.game.view.actor;

import com.jme3.font.BitmapFont;
import java.util.Collections;
import java.util.List;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.ResConstants;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.TalentData;
import name.huliqing.core.game.network.UserCommandNetwork;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.TalentService;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.LinearLayout;
import name.huliqing.core.ui.ListView;
import name.huliqing.core.ui.Row;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;

/**
 * 天赋面板
 * @author huliqing
 */
public class TalentPanel extends LinearLayout implements ActorPanel{
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
//    private final PlayService playService = Factory.get(PlayService.class);
//    private final SkinService skinService = Factory.get(SkinService.class);
    private final TalentService talentService = Factory.get(TalentService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    private Actor actor;
    private Text text;
    private TalentListPanel listPanel;
    
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
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        listPanel.getDatas();
        listPanel.refreshPageData();
        
        int talentRemain = 0;
        if (actor != null) {
            talentRemain = actorService.getTalentPoints(actor);
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
        private List<TalentData> datas;
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
                        userCommandNetwork.addTalentPoints(actor, row.getData().getId(), 1);
                        setPanelUpdate(actor);
                    }
                }
            });
            return row;
        }

        @Override
        public List<TalentData> getDatas() {
            if (actor != null) {
                datas = talentService.getTalents(actor);
            }
            if (datas == null) {
                datas = Collections.EMPTY_LIST;
            }
            return datas;
        }

        @Override
        public boolean removeItem(TalentData data) {
            throw new UnsupportedOperationException();
        }
    }
    
}
