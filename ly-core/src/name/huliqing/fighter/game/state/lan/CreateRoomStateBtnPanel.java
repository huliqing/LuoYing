/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.game.state.lan;

import com.jme3.math.ColorRGBA;
import java.util.List;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.ui.UI;

/**
 *
 * @author huliqing
 */
public class CreateRoomStateBtnPanel extends LinearLayout {
    private CreateRoomState createRoomState;
    
    private SimpleBtn btnCreate;
    private SimpleBtn btnBack;
    
    public CreateRoomStateBtnPanel(float width, float height, CreateRoomState _createRoomState) {
        super();
        this.width = width;
        this.height = height;
        this.createRoomState = _createRoomState;
        setBackground(UIFactory.getUIConfig().getBackground(), true);
        setBackgroundColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f), true);
        setLayout(Layout.horizontal);
        
        btnCreate = new SimpleBtn(ResourceManager.get("lan.createRoom"));
        btnBack = new SimpleBtn(ResourceManager.get("lan.back"));
        addView(btnBack);
        addView(btnCreate);
        
        btnCreate.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                createRoomState.createRoom();
            }
        });
        
        btnBack.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (isPress) return;
                createRoomState.backToLanState();
            }
        });
    }

    @Override
    public void updateViewChildren() {
        super.updateViewChildren();
        float btnHeight = height;
        float mgTop = btnHeight * 0.1f;
        float mgLeft = 20;
        List<UI> cuis = getViews();

        for (UI ui : cuis) {
            ui.setHeight(btnHeight - mgTop * 2);
            ui.setMargin(mgLeft, mgTop, 0, 0);
        }
    }
}
