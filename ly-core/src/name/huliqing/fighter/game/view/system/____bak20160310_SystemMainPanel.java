///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.game.view.system;
//
//import com.jme3.font.BitmapFont;
//import java.util.List;
//import java.util.logging.Logger;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.ui.Button;
//import name.huliqing.fighter.ui.LinearLayout;
//import name.huliqing.fighter.ui.LinearLayout.Layout;
//import name.huliqing.fighter.ui.Text;
//import name.huliqing.fighter.ui.UIFactory;
//import name.huliqing.fighter.ui.UI;
//import name.huliqing.fighter.ui.UI.Corner;
//import name.huliqing.fighter.ui.UI.Listener;
//import name.huliqing.fighter.ui.Window;
//
///**
// *
// * @author huliqing
// */
//public class SystemMainPanel extends Window {
////    private final static Logger logger = Logger.getLogger(SystemMainPanel.class.getName());
//    
//    private LinearLayout tabPanel;
//    private LinearLayout bodyPanel;
//    
//    // ==== tab
//    // 声音设置
//    private TabButton tabSound;
//    // 快捷方式设置
//    private TabButton tabUI;
//    // 图形设置
//    private TabButton tabPerformance;
//    // Other:暂不开放，debug功能会导致关卡列表在debug模式下被自动打开。
//    private TabButton tabOther;
//    
//    // ==== tab panel
//    private SystemSoundPanel soundPanel;
//    private SystemUIPanel shortcutPanel;
//    private SystemOtherPanel otherPanel;
//    private SystemPerformancePanel performancePanel;
//    
//    // ==== other
//    // 当前激活的tab
//    private int index = 0;
//    // 每个tab的默认可显示行数
//    private int globalSize = 5;
//
//    public SystemMainPanel(float width, float height) {
//        super(width, height);
//        setTitle(ResourceManager.get("system.main"));
//        setLayout(Layout.horizontal);
//        
//        init();
//    }
//    
//    private void init() {
//        
//        tabPanel = new LinearLayout();
//        bodyPanel = new LinearLayout();
//        addView(tabPanel);
//        addView(bodyPanel);
//        
//        // ==== Panels 
//        soundPanel = new SystemSoundPanel(getContentWidth(), getContentHeight());
//        soundPanel.setVisible(false);
//        soundPanel.setPageSize(globalSize);
//        bodyPanel.addView(soundPanel);
//        soundPanel.setToCorner(Corner.CC);
//        
//        shortcutPanel = new SystemUIPanel(getContentWidth(), getContentHeight());
//        shortcutPanel.setVisible(false);
//        shortcutPanel.setPageSize(globalSize);
//        bodyPanel.addView(shortcutPanel);
//        shortcutPanel.setToCorner(Corner.CC);
//        
//        performancePanel = new SystemPerformancePanel(getContentWidth(), getContentHeight());
//        performancePanel.setVisible(false);
//        performancePanel.setPageSize(globalSize);
//        bodyPanel.addView(performancePanel);
//        performancePanel.setToCorner(Corner.CC);
//        
//        // 暂不开放，debug功能会导致关卡列表在debug模式下被自动打开。
////        otherPanel = new SystemOtherPanel(getContentWidth(), getContentHeight());
////        otherPanel.setToCorner(Corner.CC);
////        otherPanel.setVisible(false);
////        otherPanel.setPageSize(globalSize);
////        bodyPanel.addView(otherPanel);
//        
//        // ==== tabs
//        tabSound = new TabButton(ResourceManager.get("system.sound"), soundPanel);
//        tabUI = new TabButton(ResourceManager.get("system.ui"), shortcutPanel);
//        tabPerformance = new TabButton(ResourceManager.get("system.performance"), performancePanel);
////        tabOther = new TabButton(ResourceManager.get("system.other"), otherPanel);
//        
//        tabPanel.addView(tabSound);
//        tabPanel.addView(tabUI);
//        tabPanel.addView(tabPerformance);
////        tabPanel.addView(tabOther);
//        
//    }
//    
//    public void showTab() {
//        if (!isVisible()) {
//            setVisible(true);
//        }
//        TabButton tab = (TabButton) tabPanel.getViews().get(index);
//        showTab(tab);
//    }
//    
//    private void showTab(TabButton tabButton) {
//        List<UI> bodyChildren = tabPanel.getViews();
//        for (UI child : bodyChildren) {
//            TabButton temp = (TabButton) child;
//            temp.setActive(false);
//        }
//        index = bodyChildren.indexOf(tabButton);
//        tabButton.setActive(true);
//    }
//    
//    @Override
//    public void setVisible(boolean bool) {
//        super.setVisible(bool);
//        
//        // remove20150725,由于现在面板隐藏时是从父节点detach,这个方法可能无法
//        // 调用到，所以不再在这里保存配置，现在移到SimplePlayState.cleanup方法中
////        // 关闭面板时保存设置
////        if (!bool) {
////            logger.log(Level.INFO, "Save Config.");
////            SaveService saveService = Factory.get(SaveService.class);
////            saveService.saveConfig();
////            saveService.commitLast(null);
////        }
//    }
//    
//    @Override
//    public void updateViewChildren() {
//        super.updateViewChildren();
//        float tabWidth = getContentWidth() * 0.2f;
//        tabPanel.setWidth(tabWidth);
//        tabPanel.setHeight(getContentHeight());
//        
//        List<UI> tabBtns = tabPanel.getViews();
//        float tabHeight = getContentHeight() / tabBtns.size();
//        for (UI btn : tabBtns) {
//            btn.setWidth(tabWidth);
//            btn.setHeight(tabHeight);
//        }
//        
//        bodyPanel.setWidth(getContentWidth() * 0.8f);
//        bodyPanel.setHeight(getContentHeight());
//        bodyPanel.setMargin(0, 0, 0, 0);
//        List<UI> bodyChildren = bodyPanel.getViews();
//        for (UI child : bodyChildren) {
//            child.setWidth(bodyPanel.getWidth());
//            child.setHeight(bodyPanel.getHeight());
//        }
//    }
//    
//    private class TabButton extends LinearLayout {
//        
//        private Text name;
//        private UI contentPanel;
//        
//        public TabButton(String text, UI contentPanel) {
//            super();
//            this.name = new Text(text);
//            this.name.setAlignment(BitmapFont.Align.Center);
//            this.name.setVerticalAlignment(BitmapFont.VAlign.Center);
//            this.contentPanel = contentPanel;
//            
//            setBackground(UIFactory.getUIConfig().getBackground(), true);
//            setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
//            addClickListener(new Listener() {
//                @Override
//                public void onClick(UI ui, boolean isPress) {
//                    if (!isPress) {
//                        showTab(TabButton.this);
//                    }
//                }
//            });
//            addView(name);
//        }
//
//        @Override
//        public void updateViewChildren() {
//            super.updateViewChildren();
//            name.setWidth(width);
//            name.setHeight(height);
//        }
//        
//        /**
//         * 激活当前面板
//         * @param active 
//         */
//        public void setActive(boolean active) {
//            contentPanel.setVisible(active);
//            if (active) {
//                setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
//            } else {
//                setBackgroundColor(UIFactory.getUIConfig().getBodyBgColor(), true);
//            }
//        }
//        
//        @Override
//        protected void clickEffect(boolean isPress) {
//           // ignore
//        }
//    }
//}
