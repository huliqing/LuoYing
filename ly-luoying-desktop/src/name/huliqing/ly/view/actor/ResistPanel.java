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
package name.huliqing.ly.view.actor;

import com.jme3.font.BitmapFont;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import name.huliqing.luoying.data.ResistData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;

/**
 * 抗性面板
 * @author huliqing
 */
public class ResistPanel extends LinearLayout implements ActorPanel{
    
    private Entity actor;
    private final Text text;
    private final ResistListPanel listPanel;
    
    public ResistPanel(float width, float height) {
        this.width = width;
        this.height = height;
        listPanel = new ResistListPanel(width, height);
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
        
        text.setText("");
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
    
    private class ResistListPanel extends ListView<ResistData>{
        private final List<ResistData> datas = new ArrayList<ResistData>();
        public ResistListPanel(float width, float height) {
            super(width, height);
        }
        @Override
        protected Row createEmptyRow() {
            return new ResistRow(this);
        }

        @Override
        public List<ResistData> getDatas() {
            datas.clear();
            if (actor != null) {
                actor.getData().getObjectDatas(ResistData.class, datas);
                Iterator<ResistData> it = datas.iterator();
                while (it.hasNext()) {
                    if (it.next().getTagName().equals("resistGroup")) {
                        it.remove();
                    }
                }
            }
            return datas;
        }

        @Override
        public boolean removeItem(ResistData data) {
            throw new UnsupportedOperationException("不能删除！");
        }
    }
    
}
