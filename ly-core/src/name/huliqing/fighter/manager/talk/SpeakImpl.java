/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.manager.talk;

import com.jme3.math.ColorRGBA;
import com.jme3.util.TempVars;
import name.huliqing.fighter.Common;
import name.huliqing.fighter.Factory;
import name.huliqing.fighter.object.actor.Actor;
import name.huliqing.fighter.enums.MessageType;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.manager.ResourceManager;
import name.huliqing.fighter.ui.UIFactory;
import name.huliqing.fighter.utils.GeometryUtils;
import name.huliqing.fighter.utils.MathUtils;
import name.huliqing.fighter.ui.LinearLayout;
import name.huliqing.fighter.ui.Text;

/**
 * 显示对话框
 * @author huliqing
 */
public class SpeakImpl extends Speak {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final static ColorRGBA bgColor = new ColorRGBA(0,0,0,0.85f);
    
    // 说话的角色
    private Actor actor;
    // 说话的内容
    private String mess;
    // 对话面板的最大宽度
    private float panelWidth;
    // 对话面板
    private SpeakPanel speakPanel;
    
    // 如果目标角色离摄像头多远就不显示谈话内容.(使用平方比较）
    private float maxDistanceSquared = 50 * 50;
    // 超过一定距离时开始缩小对话框（使用平方比较)
    private float distanceSquaredToScale = 20 * 20;
    
    /**
     * 添加一个“说话”
     * @param actor 说话者
     * @param mess 说话内容 
     * @param useTime 如果时间小于或等于0，则该时间会被重新自动计算 
     */
    public SpeakImpl(Actor actor, String mess, float useTime) {
        this.actor = actor;
        this.mess = mess;
        this.useTime = useTime;
    }

    @Override
    public Actor getActor() {
        return actor;
    }
    
    @Override
    protected void doInit() {
        // 如果没有指定时间，则自动计算一个时间
        int worldLen = 0;
        if (useTime <= 0) {
            worldLen = ResourceManager.getWorldLength(mess);
            useTime = MathUtils.clamp(configService.getSpeakTimeWorld() * worldLen
                    , configService.getSpeakTimeMin()
                    , configService.getSpeakTimeMax());
        }
//        if (Config.debug) {
//            System.out.println("speak useTime=" + useTime 
//                    + ",world=" + mess 
//                    + ",worldLen=" + worldLen 
//                    + ",speakTimeWorld=" + configService.getSpeakTimeWorld()
//                    + ", timeMin=" + configService.getSpeakTimeMin()
//                    + ", timeMax=" + configService.getSpeakTimeMax()
//                    );
//        }
        
        this.panelWidth = playService.getScreenWidth() * 0.3f;
        if (speakPanel == null) {
            speakPanel = new SpeakPanel(mess);
        }
        speakPanel.resize();
        maxDistanceSquared = configService.getSpeakMaxDistance() *  configService.getSpeakMaxDistance();
        
        playService.addObject(speakPanel, true);
        
        // 在HUD上添加谈话内容,只要求角色在场景内，并且距离允许即可。不需要摄像机剔除检测。
        float distanceSquared = actor.getModel().getWorldTranslation()
                .distanceSquared(Common.getApp().getCamera().getLocation());
        if (playService.isInScene(actor.getModel()) && checkDistance(distanceSquared)) {
            playService.addMessage(actor.getModel().getName() + ": " + mess, MessageType.talk);
        }
    }
    
    @Override
    protected void doLogic(float tpf) {
        // 1.角色已经不在场景中则直接结束
        if (!playService.isInScene(actor)) {
            cleanup();
            return;
        }
        
        float distanceSquared = actor.getModel().getWorldTranslation()
                .distanceSquared(Common.getApp().getCamera().getLocation());
        
        // 2.如果距离太远则不显示
        if (!checkDistance(distanceSquared)) {
            speakPanel.setVisible(false);
            return;
        }
        
        // 3.如果角色完全在视景体之外，则不显示。
        if (!checkInCamera()) {
            speakPanel.setVisible(false);
            return;
        }
        
        // 缩小对话框
        // 当缩小对话框时，text的坐标X坐标需要修正，否则不是在角色的正头顶
        // 因为text的原点在左下角。
        float fixXScalePos = 0; 
        if (distanceSquared > distanceSquaredToScale) {
            float scaleDistance = maxDistanceSquared - distanceSquaredToScale;
            float scale = (scaleDistance - (distanceSquared - distanceSquaredToScale)) / scaleDistance;
            fixXScalePos = speakPanel.getWidth() * (1 - scale) * 0.5f;
            speakPanel.setLocalScale(scale);
        }
        
        // 更新text 位置
        TempVars tv = TempVars.get();
        GeometryUtils.getBoundTopPosition(actor.getModel(), tv.vect1);
        GeometryUtils.convertWorldToScreen(tv.vect1, tv.vect2);
        speakPanel.setVisible(true);
        speakPanel.setLocalTranslation(tv.vect2.addLocal(speakPanel.getWidth() * -0.5f + fixXScalePos, 10, 0));
        tv.release();
    }
    
    @Override
    public void cleanup() {
        if (speakPanel != null) {
            speakPanel.removeFromParent();
            speakPanel = null;
        }
        mess = null;
        actor = null;
        super.cleanup();
    }
    
    /**
     * 判断说话的角色是否在视景体范围内，包含交叉也算在内
     * @return 
     */
    private boolean checkInCamera() {
        return GeometryUtils.intersectCamera(actor.getModel());
    }
    
    /**
     * 检查距离是否在设置的允许范围内
     * @return 
     */
    private boolean checkDistance(float distanceSquared) {
        if (distanceSquared <= maxDistanceSquared) {
            return true;
        } 
//            Logger.getLogger(TalkLogicSpeak.class.getName()).log(Level.INFO
//                    , "Distance {0}(Squared) more than maxDistanceSquared {1}(Squared),hide the talk, content={2}"
//                    , new Object[] {distanceSquared, maxDistanceSquared, mess});
        return false;
    }

    private class SpeakPanel extends LinearLayout {
        private Text text;
        
        public SpeakPanel(String mess) {
            super();
            setPadding(10, 5, 10, 5);
            text = new Text(mess);
            text.setFontSize(UIFactory.getUIConfig().getBodyFontSize() * 0.85f);
            float tw = text.getWidth();
            if (tw > panelWidth) {
                text.setWidth(panelWidth);
                this.width = panelWidth + paddingLeft + paddingRight;
                this.height = text.getHeight() + paddingTop + paddingBottom;
            } else {
                this.width = text.getWidth() + paddingLeft + paddingRight;
                this.height = text.getHeight() + paddingTop + paddingBottom;
            }
            addView(text);
            this.setBackground(UIFactory.getUIConfig().getBackground(), true);
            this.setBackgroundColor(bgColor, true);
        }

    }
}
