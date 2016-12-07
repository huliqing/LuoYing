/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view;

import java.util.List;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.luoying.ui.UIFactory;
import name.huliqing.luoying.ui.Button;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.Window;
import name.huliqing.ly.LyConfig;

/**
 *
 * @author huliqing
 */
public class AboutView extends Window {
    
    private Text version;
    private Text home;
//    private Text email;
//    private Text engine;
    private Text more;
    
    private Button close;

    public AboutView(float width, float height) {
        super(width, height);
        setTitle(ResourceManager.get("setting.title"));
        setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
        
        version = new Text(ResourceManager.get("about.version", new Object[] {LyConfig.getVersionName()}));
        home = new Text(ResourceManager.get("about.home"));
//        email = new Text(ResourceManager.get("about.email"));
//        engine = new Text(ResourceManager.get("about.engine"));
        more = new Text(ResourceManager.get("about.more"));
        close = new Button(ResourceManager.get("common.close"));
        
        addView(version);
        addView(home);
//        addView(email);
//        addView(engine);
        addView(more);
        addView(close);
        
        close.addClickListener(new Listener() {
            @Override
            public void onClick(UI ui, boolean isPress) {
                if (!isPress) {
                    AboutView.this.removeFromParent();
                }
            }
        });
        
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        
        float w = getContentWidth();
        float h = getContentHeight();
        float margin = 10;
        
        List<UI> cuis = getViews();
        for (UI ui : cuis) {
            if (ui == close) {
                continue;
            }
            ui.setWidth(w - margin * 2);
            ui.setMargin(margin, margin, 0, 0);
        }
        
        close.setWidth(w);
        close.setHeight(UIFactory.getUIConfig().getButtonHeight());
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout();
        setToCorner(Corner.CC);
        close.setToCorner(Corner.CB);
    }
    
    
}
