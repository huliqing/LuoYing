/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.chat;

import com.jme3.app.Application;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.constants.InterfaceConstants;
import name.huliqing.fighter.data.ChatData;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ChatService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.view.tiles.SimpleRow;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.ui.AbstractUI;
import name.huliqing.fighter.ui.Icon;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.ListView;
import name.huliqing.fighter.ui.Row;
import name.huliqing.fighter.ui.Text;
import name.huliqing.fighter.ui.UI;
import name.huliqing.fighter.ui.UI.Listener;
import name.huliqing.fighter.ui.UIFactory;

/**
 * Chat群组
 * @author huliqing
 * @param <T>
 */
public class GroupChat<T extends ChatData> extends Chat<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ChatService chatService = Factory.get(ChatService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    
    private List<Chat> chats; 
    
    // ---- inner
    private LinearLayout group;
    private TitlePanel title;
    private Icon separate;
    private ChatList chatList;

    @Override
    public void initData(T data) {
        super.initData(data);
        String[] tempChats = data.getAsArray("chats");
        chats = new ArrayList<Chat>(tempChats.length);
        for (int i = 0; i < tempChats.length; i++) {
            Chat chat = chatService.loadChat(tempChats[i]);
            chat.parent = this;
            chats.add(chat);
        }
    }

    @Override
    protected UI createChatUI(float width, float height) {
        group = new LinearLayout(width, height);
        group.setBackground("Interface/ui/bg_chat.png", true);
        group.setBackgroundColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 0.25f), true);
        group.setPadding(10, 10, 10, 10);
        group.setToCorner(UI.Corner.CC);
        
        float cw = group.getContentWidth();
        float titleHeight = UIFactory.getUIConfig().getTitleHeight();
        float iconHeight = 2;
        float margin = 10;
        float listHeight = group.getContentHeight() - titleHeight - iconHeight - margin;
        
        title = new TitlePanel(ResourceManager.getObjectName(data), cw, titleHeight);
        
        separate = new Icon(InterfaceConstants.UI_LINE_H);
        separate.setWidth(cw);
        separate.setHeight(iconHeight);
        separate.setMargin(0, 0, 0, margin);
        
        chatList = new ChatList(cw, listHeight);
        
        group.addView(title);
        group.addView(separate);
        group.addView(chatList);
        // 添加一个空的事件阻止事件穿透
        group.addClickListener(AbstractUI.EMPTY_LISTENER);
        group.setEffectEnabled(false);

        return group;
    }
   

    @Override
    public void initialize(Application app) {
        super.initialize(app);
        
        // 更新title
        title.setTitle(getChatName() + "-" + actorService.getName(actor));
        // 列表要刷新一下，因为一些Chat可能需要动态过滤以确定是否要对当前player显示
        chatList.refreshPageData();
        
        // 如果只有一个子对话框则直接弹出子对话框
        if (chats.size() == 1) {
            if (chats.get(0).isVisibleForPlayer()) {
                displayChat(chats.get(0));
            }
            cleanup();
        }
    }

    @Override
    public void setActor(Actor actor) {
        super.setActor(actor);
        if (!chats.isEmpty()) {
            for (Chat c : chats) {
                c.setActor(actor);
            }
        }
    }
    
    private void displayChat(Chat chat) {
        chat.setActor(actor);
        playService.addObject(chat);
    }
    
    // ---- 列表显示所有子chat
    private class ChatList extends ListView<Chat> {

        public ChatList(float width, float height) {
            super(width, height);
            setPageSize(5);
        }
        
        @Override
        protected Row createEmptyRow() {
            return new ChatRow(this);
        }

        @Override
        public List getDatas() {
            return chats;
        }

        @Override
        protected boolean filter(Chat chat) {
            // 如果子chat对玩家不可见则要过滤掉，不显示
            return !chat.isVisibleForPlayer();
        }
        
    }
    
    private class ChatRow extends SimpleRow<Chat> implements Listener {
        private Text text;
        private Chat chat;
        
        public ChatRow(ListView parentView) {
            super(parentView);
            text = new Text("");
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
            addView(text);
            addClickListener(this);
        }
        
        @Override
        public void displayRow(Chat data) {
            this.chat = data;
            text.setText(chat.getChatName());
        }
        
        @Override
        public void onClick(UI view, boolean isPressed) {
            if (isPressed) return;
            displayChat(chat);
        }
        
        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            text.setWidth(width);
            text.setHeight(height);
        }
    }
    
    private class TitlePanel extends LinearLayout {

        private Text title;
        private Icon btn;
        
        public TitlePanel(String titleText, float width, float height) {
            super(width, height);
            float btnHeight = height * 0.75f;
            
            title = new Text(titleText);
            title.setFontSize(UIFactory.getUIConfig().getTitleSize());
            title.setWidth(width - btnHeight);
            title.setHeight(height);
            
            btn = new Icon(UIFactory.getUIConfig().getButtonClose());
            btn.setWidth(btnHeight);
            btn.setHeight(btnHeight);
            btn.addClickListener(new Listener() {

                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed) return;
                    playService.removeObject(GroupChat.this);
                }
            });
            
            setLayout(Layout.horizontal);
            addView(title);
            addView(btn);
        }
        
        void setTitle(String titleText) {
            title.setText(titleText);
            title.setFontSize(UIFactory.getUIConfig().getTitleSize());
        }
    }
}
