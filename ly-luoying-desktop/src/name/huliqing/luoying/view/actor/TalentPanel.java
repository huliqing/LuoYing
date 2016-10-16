/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.actor;

import com.jme3.font.BitmapFont;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.layer.network.UserCommandNetwork;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.TalentService;
import name.huliqing.luoying.object.talent.Talent;
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
    
    private class TalentListPanel extends ListView<Talent>{
        private List<Talent> datas;
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
                        userCommandNetwork.addTalentPoints(actor, row.getData().getData().getId(), 1);
                        setPanelUpdate(actor);
                    }
                }
            });
            return row;
        }

        @Override
        public List<Talent> getDatas() {
            if (actor != null) {
                datas = talentService.getTalents(actor);
            }
            if (datas == null) {
                datas = Collections.EMPTY_LIST;
            }
            return datas;
        }

        @Override
        public boolean removeItem(Talent data) {
            throw new UnsupportedOperationException();
        }
    }
    
}
