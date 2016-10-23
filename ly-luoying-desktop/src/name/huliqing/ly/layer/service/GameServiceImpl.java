/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.layer.service;

import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.data.ObjectData;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.SyncData;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.GameAppState;
import name.huliqing.ly.enums.MessageType;
import name.huliqing.ly.object.NetworkObject;
import name.huliqing.ly.object.game.SimpleRpgGame;
import name.huliqing.ly.view.talk.SpeakManager;
import name.huliqing.ly.view.talk.Talk;
import name.huliqing.ly.view.talk.TalkManager;

/**
 *
 * @author huliqing
 */
public class GameServiceImpl implements GameService {

    private PlayService playService;
    private ActionService actionService;
    
    @Override
    public void inject() {
        playService = Factory.get(PlayService.class);
        actionService = Factory.get(ActionService.class);
    }

    @Override
    public void addMessage(String message, MessageType type) {
    }

    @Override
    public void speak(Entity actor, String mess, float useTime) {
        SpeakManager.getInstance().doSpeak(actor, mess, useTime);
    }
    
    @Override
    public void talk(Talk talk) {
        // 不要在这里设置setNetwork(false),这会覆盖掉gameNetwork.talk的设置
        // 因为gameNetwork.talk是直接调用gameService.talk的方法
//        talk.setNetwork(false);
        
        TalkManager.getInstance().startTalk(talk);
    }

    @Override
    public NetworkObject findSyncObject(long objectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entity getPlayer() {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        return game.getPlayer();
    }

    @Override
    public Entity getTarget() {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        return game.getTarget();
    }

    @Override
    public void setTarget(Entity target) {
        SimpleRpgGame game = (SimpleRpgGame) playService.getGame();
        game.setTarget(target);
    }

    @Override
    public void exitGame() {
        GameAppState gameApp = LuoYing.getApp().getStateManager().getState(GameAppState.class);
        if (gameApp != null) {
            LuoYing.getApp().getStateManager().detach(gameApp);
        }
    }

    @Override
    public void saveCompleteStage(int storyNum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncGameInitToClient(HostedConnection client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void syncObject(NetworkObject object, SyncData syncData, boolean reliable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addShortcut(Entity actor, ObjectData data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void playRunToPos(Entity actor, Vector3f worldPos) {
        actionService.playRun(actor, worldPos);
    }


}
