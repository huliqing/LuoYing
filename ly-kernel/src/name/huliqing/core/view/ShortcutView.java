/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.view;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import name.huliqing.core.LY;
import name.huliqing.core.Factory;
import name.huliqing.core.data.ItemData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.data.SkinData;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkinService;
import name.huliqing.core.object.module.ItemListener;
import name.huliqing.core.object.module.SkinListener;
import name.huliqing.core.object.skin.Skin;
import name.huliqing.core.ui.UIFactory;
import name.huliqing.core.ui.FrameLayout;
import name.huliqing.core.ui.Icon;
import name.huliqing.core.ui.Text;
import name.huliqing.core.ui.UI;
import name.huliqing.core.ui.UI.Listener;

/**
 *
 * @author huliqing
 */
public class ShortcutView extends FrameLayout implements ItemListener, SkinListener {
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    private final SkinService skinService = Factory.get(SkinService.class);
//    private final ActorService actorService = Factory.get(ActorService.class);
//    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    private final static float NUM_COUNT_ALPHA = 0.75f;
    
    private ObjectData data;
    private final Actor actor;
    private final Icon icon;
    private final Text numCount;
    
    // 开始拖动的时间点
    private long startDragTime;
    private boolean bucketVisible;
    
    public ShortcutView(float width, float height, Actor actor, ObjectData data) {
        super(width, height);
        
        this.actor = actor;
        this.data = data;
        
        icon = new Icon(data.getIcon());
        
        numCount = new Text("");
        addView(icon);
        addView(numCount);
        
        this.setDragEnabled(true);
        this.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPressed) {
                onShortcutClick(isPressed);
            }
        });
        
        setBackground("Interface/icon/shortcut2.png", true);

        // 注册侦听器
        itemService.addItemListener(actor, this);
        skinService.addSkinListener(actor, this);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        
        icon.setWidth(width * 0.85f);
        icon.setHeight(height * 0.85f);
        // 用颜色区别来区分skin装备是否正在使用中
        if (data instanceof SkinData) {
            SkinData sd = (SkinData) data;
            if (sd.isUsing()) {
                icon.setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            } else {
                icon.setBackgroundColor(ColorRGBA.White, true);
            }
        }
        
        resetNumCount(data.getTotal());
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout(); 
        icon.setToCorner(Corner.CC);
        numCount.setToCorner(Corner.CC);
    }
    
    /**
     * 当shortcut被点击时执行操作。
     * @param isPress 
     */
    protected void onShortcutClick(boolean isPress) {
        if (!isPress) {
            // 物品使用
            userCommandNetwork.useObject(actor, ShortcutView.this.data);
        }
    }
    
    private void resetNumCount(int total) {
        if (total > 999) {
            numCount.setText("999+");
        } else {
            numCount.setText(String.valueOf(total));
        }
        // 只有刚好只有一件物品时不显示数量，提高UI美观度。
        // 剩0件时要显示，以告诉player没有该物品了
        boolean visible = total != 1;
        numCount.setVisible(visible);
        
        // remove
        numCount.setWidth(width);
        numCount.setHeight(height);
        numCount.setVerticalAlignment(BitmapFont.VAlign.Center);
        numCount.setAlignment(BitmapFont.Align.Center);
        numCount.setAlpha(NUM_COUNT_ALPHA);
        
    }

    @Override
    protected void onDragStart() {
        startDragTime = LY.getGameTime();
    }

    @Override
    protected void onDragMove(float xAmount, float yAmount) {
        super.onDragMove(xAmount, yAmount);
        // 拖动时间超过一定时间时才显示recycle,以避免在正常点击的时候也显示recycle图标。
        if (!bucketVisible) {
            if (LY.getGameTime() - startDragTime > 100) {
                ShortcutManager.setBucketVisible(true);
                setOnTop();
                bucketVisible = true;
            }
        }
    }
    
    /**
     * 更新shortcut
     * @param tpf 
     */
    public void updateShortcut(float tpf) {
        // do something.
    }
    
    @Override
    public void onRelease() {
        super.onRelease();
        // 检测是否销毁shortcut
        ShortcutManager.checkProcess(this);
        ShortcutManager.setBucketVisible(false);
        bucketVisible = false;
        startDragTime = 0;
    }

    /**
     * 快捷删除时进行清理
     */
    public void cleanup() {
        itemService.removeItemListener(actor, this);
        skinService.removeSkinListener(actor, this);
    }

    public Actor getActor() {
        return actor;
    }

    public ObjectData getData() {
        return data;
    }

    @Override
    public void onItemAdded(Actor actor, ItemData item, int trueAdded) {
        if (!item.getId().equals(data.getId()))
            return;
        
        itemUpdate();
    }

    @Override
    public void onItemRemoved(Actor actor, ItemData item, int trueRemoved) {
        if (!item.getId().equals(data.getId()))
            return;
        
        itemUpdate();
    }
    
    private void itemUpdate() {
        ObjectData temp = itemService.getItem(actor, this.data.getId());
        // 物品可能用完
        if (temp == null) {
            this.data.setTotal(0);
        } else {
            this.data = temp;
        }
        resetNumCount(this.data.getTotal());
    }

    @Override
    public void onSkinAttached(Actor actor, Skin skin) {
        if (skin.getData() == this.data) {
            setNeedUpdate();
        }
    }

    @Override
    public void onSkinDetached(Actor actor, Skin skin) {
        if (skin.getData() == this.data) {
            setNeedUpdate();
        }
    }

    @Override
    public void onSkinAdded(Actor actor, SkinData data) {
        if (data == this.data) {
            setNeedUpdate();
        }
    }

    @Override
    public void onSkinRemoved(Actor actor, SkinData data) {
        if (data == this.data) {
            setNeedUpdate();
        }
    }
    
    
}
