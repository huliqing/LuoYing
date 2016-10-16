/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.state.lan;

import name.huliqing.luoying.state.GameState;
import name.huliqing.luoying.mess.MessPlayLoadSavedActor;
import name.huliqing.luoying.mess.MessPlayLoadSavedActorResult;
import name.huliqing.luoying.object.actor.Actor;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.game.Game.GameListener;
import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import name.huliqing.luoying.network.GameServer;
import name.huliqing.luoying.state.NetworkServerPlayState;

/**
 * 局域网模式下的游戏服务端。不保存服务端和客户端的资料，每都都需要选择角色进行游戏。
 * @author huliqing
 */
public abstract class LanServerPlayState extends NetworkServerPlayState  {
    
    public LanServerPlayState(Application app, GameServer gameServer) {
        super(app, gameServer);
    }
    
    @Override
    public void changeGameState(final GameState newGameState) {
        super.changeGameState(newGameState);
        newGameState.getGame().addListener(new GameListener() {
            @Override
            public void onGameStarted(Game game) {
                // 隐藏其它UI界面
                setUIVisiable(false);
                // 显示角色选择面板
                showSelectPanel(newGameState.getGame().getData().getAvailableActors());
            }
        });
    }

    @Override
    protected void addPlayer(Actor actor) {
        super.addPlayer(actor);
        gameState.getGame().onPlayerSelected(actor);
    }

    @Override
    protected boolean processMessage(GameServer gameServer, HostedConnection source, Message m) {
        // 局域网模式下的服务端不保存客户端资料，所以这里直接返回false，以便让客户端去弹出角色选择窗口，选择一个角色进行游戏
        if (m instanceof MessPlayLoadSavedActor) {
            gameServer.send(source, new MessPlayLoadSavedActorResult(false));
            return true;
        }
        
        return super.processMessage(gameServer, source, m); 
    }
    
}
