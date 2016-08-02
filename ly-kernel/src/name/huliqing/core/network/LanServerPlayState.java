/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.network;

import name.huliqing.core.state.GameState;
import name.huliqing.core.Factory;
import name.huliqing.core.mess.MessPlayLoadSavedActor;
import name.huliqing.core.mess.MessPlayLoadSavedActorResult;
import name.huliqing.core.network.GameServer;
import name.huliqing.core.mvc.network.ActorNetwork;
import name.huliqing.core.mvc.network.StateNetwork;
import name.huliqing.core.mvc.network.UserCommandNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.ConfigService;
import name.huliqing.core.mvc.service.LogicService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.mvc.service.SkillService;
import name.huliqing.core.mvc.service.StateService;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.object.game.Game;
import name.huliqing.core.object.game.Game.GameListener;
import com.jme3.app.Application;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import name.huliqing.core.state.NetworkServerPlayState;

/**
 * 局域网模式下的游戏服务端。不保存服务端和客户端的资料，每都都需要选择角色进行游戏。
 * @author huliqing
 */
public class LanServerPlayState extends NetworkServerPlayState  {
    private final UserCommandNetwork userCommandNetwork = Factory.get(UserCommandNetwork.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ConfigService configService = Factory.get(ConfigService.class);
    private final StateService stateService = Factory.get(StateService.class);
    private final LogicService logicService = Factory.get(LogicService.class);
    private final SkillService skillService = Factory.get(SkillService.class);
    private final StateNetwork stateNetwork = Factory.get(StateNetwork.class);
    private final ActorNetwork actorNetwork = Factory.get(ActorNetwork.class);
    
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
