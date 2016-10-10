/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.ly.layer.network.ActionNetwork;
import name.huliqing.ly.layer.network.ActionNetworkImpl;
import name.huliqing.ly.layer.network.ActorNetwork;
import name.huliqing.ly.layer.network.ActorNetworkImpl;
import name.huliqing.ly.layer.network.AttributeNetwork;
import name.huliqing.ly.layer.network.AttributeNetworkImpl;
import name.huliqing.ly.layer.network.ChatNetwork;
import name.huliqing.ly.layer.network.ChatNetworkImpl;
import name.huliqing.ly.layer.network.DropNetwork;
import name.huliqing.ly.layer.network.DropNetworkImpl;
import name.huliqing.ly.layer.network.ItemNetwork;
import name.huliqing.ly.layer.network.ItemNetworkImpl;
import name.huliqing.ly.layer.network.PlayNetwork;
import name.huliqing.ly.layer.network.PlayNetworkImpl;
import name.huliqing.ly.layer.network.ObjectNetworkImpl;
import name.huliqing.ly.layer.network.SkillNetwork;
import name.huliqing.ly.layer.network.SkillNetworkImpl;
import name.huliqing.ly.layer.network.SkinNetwork;
import name.huliqing.ly.layer.network.SkinNetworkImpl;
import name.huliqing.ly.layer.network.StateNetwork;
import name.huliqing.ly.layer.network.StateNetworkImpl;
import name.huliqing.ly.layer.network.TalentNetwork;
import name.huliqing.ly.layer.network.TalentNetworkImpl;
import name.huliqing.ly.layer.network.TaskNetwork;
import name.huliqing.ly.layer.network.TaskNetworkImpl;
import name.huliqing.ly.layer.service.ActionService;
import name.huliqing.ly.layer.service.ActionServiceImpl;
import name.huliqing.ly.layer.service.ActorAnimService;
import name.huliqing.ly.layer.service.ActorAnimServiceImpl;
import name.huliqing.ly.layer.service.ActorService;
import name.huliqing.ly.layer.service.ActorServiceImpl;
import name.huliqing.ly.layer.service.AttributeService;
import name.huliqing.ly.layer.service.AttributeServiceImpl;
import name.huliqing.ly.layer.service.BulletService;
import name.huliqing.ly.layer.service.BulletServiceImpl;
import name.huliqing.ly.layer.service.ChatService;
import name.huliqing.ly.layer.service.ChatServiceImpl;
import name.huliqing.ly.layer.service.ConfigService;
import name.huliqing.ly.layer.service.ConfigServiceImpl;
import name.huliqing.ly.layer.service.DropService;
import name.huliqing.ly.layer.service.DropServiceImpl;
import name.huliqing.ly.layer.service.EffectService;
import name.huliqing.ly.layer.service.EffectServiceImpl;
import name.huliqing.ly.layer.service.SystemServiceImpl;
import name.huliqing.ly.layer.service.ElService;
import name.huliqing.ly.layer.service.ElServiceImpl;
import name.huliqing.ly.layer.service.GameService;
import name.huliqing.ly.layer.service.GameServiceImpl;
import name.huliqing.ly.layer.service.HitCheckerService;
import name.huliqing.ly.layer.service.HitCheckerServiceImpl;
import name.huliqing.ly.layer.service.ItemService;
import name.huliqing.ly.layer.service.ItemServiceImpl;
import name.huliqing.ly.layer.service.LogicService;
import name.huliqing.ly.layer.service.LogicServiceImpl;
import name.huliqing.ly.layer.service.MagicService;
import name.huliqing.ly.layer.service.MagicServiceImpl;
import name.huliqing.ly.layer.service.PlayService;
import name.huliqing.ly.layer.service.PlayServiceImpl;
import name.huliqing.ly.layer.service.ObjectServiceImpl;
import name.huliqing.ly.layer.service.ResistService;
import name.huliqing.ly.layer.service.ResistServiceImpl;
import name.huliqing.ly.layer.service.SaveService;
import name.huliqing.ly.layer.service.SaveServiceImpl;
import name.huliqing.ly.layer.service.SceneService;
import name.huliqing.ly.layer.service.SceneServiceImpl;
import name.huliqing.ly.layer.service.SkillService;
import name.huliqing.ly.layer.service.SkillServiceImpl;
import name.huliqing.ly.layer.service.SkinService;
import name.huliqing.ly.layer.service.SkinServiceImpl;
import name.huliqing.ly.layer.service.StateService;
import name.huliqing.ly.layer.service.StateServiceImpl;
import name.huliqing.ly.layer.service.TalentService;
import name.huliqing.ly.layer.service.TalentServiceImpl;
import name.huliqing.ly.layer.service.TaskService;
import name.huliqing.ly.layer.service.TaskServiceImpl;
import name.huliqing.ly.layer.service.ViewService;
import name.huliqing.ly.layer.service.ViewServiceImpl;
import name.huliqing.ly.layer.service.SystemService;
import name.huliqing.ly.layer.service.GameLogicService;
import name.huliqing.ly.layer.service.GameLogicServiceImpl;
import name.huliqing.ly.layer.service.ObjectService;
import name.huliqing.ly.layer.network.ObjectNetwork;

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
        CLASS_MAP.put(DropNetwork.class, DropNetworkImpl.class);
        CLASS_MAP.put(ItemNetwork.class, ItemNetworkImpl.class);
        CLASS_MAP.put(PlayNetwork.class, PlayNetworkImpl.class);
        CLASS_MAP.put(ObjectNetwork.class, ObjectNetworkImpl.class);
        CLASS_MAP.put(SkillNetwork.class, SkillNetworkImpl.class);
        CLASS_MAP.put(SkinNetwork.class, SkinNetworkImpl.class);
        CLASS_MAP.put(StateNetwork.class, StateNetworkImpl.class);
        CLASS_MAP.put(TalentNetwork.class, TalentNetworkImpl.class);
        CLASS_MAP.put(TaskNetwork.class, TaskNetworkImpl.class);
        
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
        CLASS_MAP.put(GameService.class, GameServiceImpl.class);
        CLASS_MAP.put(GameLogicService.class, GameLogicServiceImpl.class);
        CLASS_MAP.put(HitCheckerService.class, HitCheckerServiceImpl.class);
        CLASS_MAP.put(ItemService.class, ItemServiceImpl.class);
        CLASS_MAP.put(LogicService.class, LogicServiceImpl.class);
        CLASS_MAP.put(MagicService.class, MagicServiceImpl.class);
        CLASS_MAP.put(PlayService.class, PlayServiceImpl.class);
        CLASS_MAP.put(ObjectService.class, ObjectServiceImpl.class);
        CLASS_MAP.put(ResistService.class, ResistServiceImpl.class);
        CLASS_MAP.put(SaveService.class, SaveServiceImpl.class);
        CLASS_MAP.put(SceneService.class, SceneServiceImpl.class);
        CLASS_MAP.put(SkillService.class, SkillServiceImpl.class);
        CLASS_MAP.put(SkinService.class, SkinServiceImpl.class);
        CLASS_MAP.put(StateService.class, StateServiceImpl.class);      
        CLASS_MAP.put(SystemService.class, SystemServiceImpl.class);
        CLASS_MAP.put(TalentService.class, TalentServiceImpl.class);
        CLASS_MAP.put(TaskService.class, TaskServiceImpl.class);
        CLASS_MAP.put(ViewService.class, ViewServiceImpl.class);
        
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
            } catch (InstantiationException ex) {
                throw new RuntimeException("Could not instance for class=" + cla + ", error=" + ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Could not instance for class=" + cla + ", error=" + ex.getMessage());
            }
            // 2.存入缓存
            INSTANCE_MAP.put(cla, ins);
            // 3.注入其它引用
            ((Inject) ins).inject();
        } else {
            throw new NullPointerException("No instance register for class:" + cla);
        }
        return ins;
    }
    
}
