/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.core.game.dao.ItemDao;
import name.huliqing.core.game.dao.ItemDaoImpl;
import name.huliqing.core.game.dao.SkillDao;
import name.huliqing.core.game.dao.SkillDaoImpl;
import name.huliqing.core.game.dao.SkinDao;
import name.huliqing.core.game.dao.SkinDaoImpl;
import name.huliqing.core.game.network.ActionNetwork;
import name.huliqing.core.game.network.ActionNetworkImpl;
import name.huliqing.core.game.network.ActorNetwork;
import name.huliqing.core.game.network.ActorNetworkImpl;
import name.huliqing.core.game.network.AttributeNetwork;
import name.huliqing.core.game.network.AttributeNetworkImpl;
import name.huliqing.core.game.network.ChatNetwork;
import name.huliqing.core.game.network.ChatNetworkImpl;
import name.huliqing.core.game.network.PlayNetwork;
import name.huliqing.core.game.network.PlayNetworkImpl;
import name.huliqing.core.game.network.ProtoNetwork;
import name.huliqing.core.game.network.ProtoNetworkImpl;
import name.huliqing.core.game.network.SkillNetwork;
import name.huliqing.core.game.network.SkillNetworkImpl;
import name.huliqing.core.game.network.SkinNetwork;
import name.huliqing.core.game.network.SkinNetworkImpl;
import name.huliqing.core.game.network.StateNetwork;
import name.huliqing.core.game.network.StateNetworkImpl;
import name.huliqing.core.game.network.TalentNetwork;
import name.huliqing.core.game.network.TalentNetworkImpl;
import name.huliqing.core.game.network.TaskNetwork;
import name.huliqing.core.game.network.TaskNetworkImpl;
import name.huliqing.core.game.network.UserCommandNetwork;
import name.huliqing.core.game.network.UserCommandNetworkImpl;
import name.huliqing.core.game.service.ActionService;
import name.huliqing.core.game.service.ActionServiceImpl;
import name.huliqing.core.game.service.ActorAnimService;
import name.huliqing.core.game.service.ActorAnimServiceImpl;
import name.huliqing.core.game.service.ActorService;
import name.huliqing.core.game.service.ActorServiceImpl;
import name.huliqing.core.game.service.AttributeService;
import name.huliqing.core.game.service.AttributeServiceImpl;
import name.huliqing.core.game.service.BulletService;
import name.huliqing.core.game.service.BulletServiceImpl;
import name.huliqing.core.game.service.ChatService;
import name.huliqing.core.game.service.ChatServiceImpl;
import name.huliqing.core.game.service.ConfigService;
import name.huliqing.core.game.service.ConfigServiceImpl;
import name.huliqing.core.game.service.DropService;
import name.huliqing.core.game.service.DropServiceImpl;
import name.huliqing.core.game.service.EffectService;
import name.huliqing.core.game.service.EffectServiceImpl;
import name.huliqing.core.game.service.SystemServiceImpl;
import name.huliqing.core.game.service.HandlerService;
import name.huliqing.core.game.service.HandlerServiceImpl;
import name.huliqing.core.game.service.ElService;
import name.huliqing.core.game.service.ElServiceImpl;
import name.huliqing.core.game.service.EnvServiceImpl;
import name.huliqing.core.game.service.GameService;
import name.huliqing.core.game.service.GameServiceImpl;
import name.huliqing.core.game.service.HitCheckerService;
import name.huliqing.core.game.service.HitCheckerServiceImpl;
import name.huliqing.core.game.service.ItemService;
import name.huliqing.core.game.service.ItemServiceImpl;
import name.huliqing.core.game.service.LogicService;
import name.huliqing.core.game.service.LogicServiceImpl;
import name.huliqing.core.game.service.MagicService;
import name.huliqing.core.game.service.MagicServiceImpl;
import name.huliqing.core.game.service.PlayService;
import name.huliqing.core.game.service.PlayServiceImpl;
import name.huliqing.core.game.service.ProtoService;
import name.huliqing.core.game.service.ProtoServiceImpl;
import name.huliqing.core.game.service.ResistService;
import name.huliqing.core.game.service.ResistServiceImpl;
import name.huliqing.core.game.service.SaveService;
import name.huliqing.core.game.service.SaveServiceImpl;
import name.huliqing.core.game.service.SceneService;
import name.huliqing.core.game.service.SceneServiceImpl;
import name.huliqing.core.game.service.SkillService;
import name.huliqing.core.game.service.SkillServiceImpl;
import name.huliqing.core.game.service.SkinService;
import name.huliqing.core.game.service.SkinServiceImpl;
import name.huliqing.core.game.service.SoundService;
import name.huliqing.core.game.service.SoundServiceImpl;
import name.huliqing.core.game.service.StateService;
import name.huliqing.core.game.service.StateServiceImpl;
import name.huliqing.core.game.service.TalentService;
import name.huliqing.core.game.service.TalentServiceImpl;
import name.huliqing.core.game.service.TaskService;
import name.huliqing.core.game.service.TaskServiceImpl;
import name.huliqing.core.game.service.ViewService;
import name.huliqing.core.game.service.ViewServiceImpl;
import name.huliqing.core.game.service.SystemService;
import name.huliqing.core.game.service.EnvService;
import name.huliqing.core.game.service.GameLogicService;
import name.huliqing.core.game.service.GameLogicServiceImpl;

/**
 *
 * @author huliqing
 */
public class Factory {
    
    private final static Map<Class, Class> CLASS_MAP = new HashMap<Class, Class>();
    private final static Map<Class, Object> INSTANCE_MAP = new HashMap<Class, Object>();
    
    static {
        // network
        CLASS_MAP.put(ActionNetwork.class, ActionNetworkImpl.class);
        CLASS_MAP.put(ActorNetwork.class, ActorNetworkImpl.class);
        CLASS_MAP.put(AttributeNetwork.class, AttributeNetworkImpl.class);
        CLASS_MAP.put(ChatNetwork.class, ChatNetworkImpl.class);
        CLASS_MAP.put(PlayNetwork.class, PlayNetworkImpl.class);
        CLASS_MAP.put(ProtoNetwork.class, ProtoNetworkImpl.class);
        CLASS_MAP.put(SkillNetwork.class, SkillNetworkImpl.class);
        CLASS_MAP.put(SkinNetwork.class, SkinNetworkImpl.class);
        CLASS_MAP.put(StateNetwork.class, StateNetworkImpl.class);
        CLASS_MAP.put(TalentNetwork.class, TalentNetworkImpl.class);
        CLASS_MAP.put(TaskNetwork.class, TaskNetworkImpl.class);
        CLASS_MAP.put(UserCommandNetwork.class, UserCommandNetworkImpl.class);
        
        // service
        CLASS_MAP.put(ActionService.class, ActionServiceImpl.class);
        CLASS_MAP.put(ActorAnimService.class, ActorAnimServiceImpl.class);
        CLASS_MAP.put(ActorService.class, ActorServiceImpl.class);
        CLASS_MAP.put(AttributeService.class, AttributeServiceImpl.class);
        CLASS_MAP.put(BulletService.class, BulletServiceImpl.class);
        CLASS_MAP.put(ChatService.class, ChatServiceImpl.class);
        CLASS_MAP.put(ConfigService.class, ConfigServiceImpl.class);
        CLASS_MAP.put(DropService.class, DropServiceImpl.class);
        CLASS_MAP.put(EffectService.class, EffectServiceImpl.class);
        CLASS_MAP.put(ElService.class, ElServiceImpl.class);
        CLASS_MAP.put(EnvService.class, EnvServiceImpl.class);
        CLASS_MAP.put(GameService.class, GameServiceImpl.class);
        CLASS_MAP.put(GameLogicService.class, GameLogicServiceImpl.class);
        CLASS_MAP.put(HandlerService.class, HandlerServiceImpl.class);
        CLASS_MAP.put(HitCheckerService.class, HitCheckerServiceImpl.class);
        CLASS_MAP.put(ItemService.class, ItemServiceImpl.class);
        CLASS_MAP.put(LogicService.class, LogicServiceImpl.class);
        CLASS_MAP.put(MagicService.class, MagicServiceImpl.class);
        CLASS_MAP.put(PlayService.class, PlayServiceImpl.class);
        CLASS_MAP.put(ProtoService.class, ProtoServiceImpl.class);
        CLASS_MAP.put(ResistService.class, ResistServiceImpl.class);
        CLASS_MAP.put(SaveService.class, SaveServiceImpl.class);
        CLASS_MAP.put(SceneService.class, SceneServiceImpl.class);
        CLASS_MAP.put(SkillService.class, SkillServiceImpl.class);
        CLASS_MAP.put(SkinService.class, SkinServiceImpl.class);
        CLASS_MAP.put(SoundService.class, SoundServiceImpl.class);
        CLASS_MAP.put(StateService.class, StateServiceImpl.class);      
        CLASS_MAP.put(SystemService.class, SystemServiceImpl.class);
        CLASS_MAP.put(TalentService.class, TalentServiceImpl.class);
        CLASS_MAP.put(TaskService.class, TaskServiceImpl.class);
        CLASS_MAP.put(ViewService.class, ViewServiceImpl.class);
        
        // dao
        CLASS_MAP.put(ItemDao.class, ItemDaoImpl.class);
        CLASS_MAP.put(SkillDao.class, SkillDaoImpl.class);
        CLASS_MAP.put(SkinDao.class, SkinDaoImpl.class);
    }
    
    public static <T extends Object> void register(Class<T> c, Class ins) {
        if (!c.isAssignableFrom(ins)) {
            throw new IllegalArgumentException("Could not register service. service class=" + c + ", object class=" + ins);
        }
        CLASS_MAP.put(c, ins);
    }
    
    public static <T extends Object> T get(Class<T> cla) {
        T ins = (T) INSTANCE_MAP.get(cla);
        if (ins != null) {
            return ins;
        }
        Class clazz = CLASS_MAP.get(cla);
        if (clazz != null) {
            try {
                // 1.实例化
                ins = (T) clazz.newInstance();
                // 2.存入缓存
                INSTANCE_MAP.put(cla, ins);
                // 3.注入其它引用
                ((Inject) ins).inject();
            } catch (Exception ex) {
                throw new NullPointerException("Could not instance for class=" + cla + ", error=" + ex.getMessage());
            }
        } else {
            throw new NullPointerException("No instance register for class:" + cla);
        }
        return ins;
    }
    
}
