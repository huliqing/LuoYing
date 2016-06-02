/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.fighter.game.dao.ItemDao;
import name.huliqing.fighter.game.dao.ItemDaoImpl;
import name.huliqing.fighter.game.dao.SkillDao;
import name.huliqing.fighter.game.dao.SkillDaoImpl;
import name.huliqing.fighter.game.dao.SkinDao;
import name.huliqing.fighter.game.dao.SkinDaoImpl;
import name.huliqing.fighter.game.network.ActionNetwork;
import name.huliqing.fighter.game.network.ActionNetworkImpl;
import name.huliqing.fighter.game.network.ActorNetwork;
import name.huliqing.fighter.game.network.ActorNetworkImpl;
import name.huliqing.fighter.game.network.AttributeNetwork;
import name.huliqing.fighter.game.network.AttributeNetworkImpl;
import name.huliqing.fighter.game.network.ChatNetwork;
import name.huliqing.fighter.game.network.ChatNetworkImpl;
import name.huliqing.fighter.game.network.HandlerNetwork;
import name.huliqing.fighter.game.network.HandlerNetworkImpl;
import name.huliqing.fighter.game.network.ItemNetwork;
import name.huliqing.fighter.game.network.ItemNetworkImpl;
import name.huliqing.fighter.game.network.PlayNetwork;
import name.huliqing.fighter.game.network.PlayNetworkImpl;
import name.huliqing.fighter.game.network.SkillNetwork;
import name.huliqing.fighter.game.network.SkillNetworkImpl;
import name.huliqing.fighter.game.network.SkinNetwork;
import name.huliqing.fighter.game.network.SkinNetworkImpl;
import name.huliqing.fighter.game.network.StateNetwork;
import name.huliqing.fighter.game.network.StateNetworkImpl;
import name.huliqing.fighter.game.network.TalentNetwork;
import name.huliqing.fighter.game.network.TalentNetworkImpl;
import name.huliqing.fighter.game.network.TaskNetwork;
import name.huliqing.fighter.game.network.TaskNetworkImpl;
import name.huliqing.fighter.game.network.UserCommandNetwork;
import name.huliqing.fighter.game.network.UserCommandNetworkImpl;
import name.huliqing.fighter.game.service.ActionService;
import name.huliqing.fighter.game.service.ActionServiceImpl;
import name.huliqing.fighter.game.service.ActorAnimService;
import name.huliqing.fighter.game.service.ActorAnimServiceImpl;
import name.huliqing.fighter.game.service.ActorService;
import name.huliqing.fighter.game.service.ActorServiceImpl;
import name.huliqing.fighter.game.service.AttributeService;
import name.huliqing.fighter.game.service.AttributeServiceImpl;
import name.huliqing.fighter.game.service.BulletService;
import name.huliqing.fighter.game.service.BulletServiceImpl;
import name.huliqing.fighter.game.service.ChatService;
import name.huliqing.fighter.game.service.ChatServiceImpl;
import name.huliqing.fighter.game.service.ConfigService;
import name.huliqing.fighter.game.service.ConfigServiceImpl;
import name.huliqing.fighter.game.service.DropService;
import name.huliqing.fighter.game.service.DropServiceImpl;
import name.huliqing.fighter.game.service.EffectService;
import name.huliqing.fighter.game.service.EffectServiceImpl;
import name.huliqing.fighter.game.service.EnvService;
import name.huliqing.fighter.game.service.EnvServiceImpl;
import name.huliqing.fighter.game.service.HandlerService;
import name.huliqing.fighter.game.service.HandlerServiceImpl;
import name.huliqing.fighter.game.service.ElService;
import name.huliqing.fighter.game.service.ElServiceImpl;
import name.huliqing.fighter.game.service.HitCheckerService;
import name.huliqing.fighter.game.service.HitCheckerServiceImpl;
import name.huliqing.fighter.game.service.ItemService;
import name.huliqing.fighter.game.service.ItemServiceImpl;
import name.huliqing.fighter.game.service.LogicService;
import name.huliqing.fighter.game.service.LogicServiceImpl;
import name.huliqing.fighter.game.service.MagicService;
import name.huliqing.fighter.game.service.MagicServiceImpl;
import name.huliqing.fighter.game.service.PlayService;
import name.huliqing.fighter.game.service.PlayServiceImpl;
import name.huliqing.fighter.game.service.ResistService;
import name.huliqing.fighter.game.service.ResistServiceImpl;
import name.huliqing.fighter.game.service.SaveService;
import name.huliqing.fighter.game.service.SaveServiceImpl;
import name.huliqing.fighter.game.service.SceneService;
import name.huliqing.fighter.game.service.SceneServiceImpl;
import name.huliqing.fighter.game.service.SkillService;
import name.huliqing.fighter.game.service.SkillServiceImpl;
import name.huliqing.fighter.game.service.SkinService;
import name.huliqing.fighter.game.service.SkinServiceImpl;
import name.huliqing.fighter.game.service.StateService;
import name.huliqing.fighter.game.service.StateServiceImpl;
import name.huliqing.fighter.game.service.TalentService;
import name.huliqing.fighter.game.service.TalentServiceImpl;
import name.huliqing.fighter.game.service.TaskService;
import name.huliqing.fighter.game.service.TaskServiceImpl;
import name.huliqing.fighter.game.service.ViewService;
import name.huliqing.fighter.game.service.ViewServiceImpl;

/**
 *
 * @author huliqing
 */
public class Factory {
    
    private final static Map<Class, Class> claMaps = new HashMap<Class, Class>();
    private final static Map<Class, Object> insMaps = new HashMap<Class, Object>();
    
    static {
        // network
        claMaps.put(ActionNetwork.class, ActionNetworkImpl.class);
        claMaps.put(ActorNetwork.class, ActorNetworkImpl.class);
        claMaps.put(AttributeNetwork.class, AttributeNetworkImpl.class);
        claMaps.put(ChatNetwork.class, ChatNetworkImpl.class);
        claMaps.put(HandlerNetwork.class, HandlerNetworkImpl.class);
        claMaps.put(ItemNetwork.class, ItemNetworkImpl.class);
        claMaps.put(PlayNetwork.class, PlayNetworkImpl.class);
        claMaps.put(SkillNetwork.class, SkillNetworkImpl.class);
        claMaps.put(SkinNetwork.class, SkinNetworkImpl.class);
        claMaps.put(StateNetwork.class, StateNetworkImpl.class);
        claMaps.put(TalentNetwork.class, TalentNetworkImpl.class);
        claMaps.put(TaskNetwork.class, TaskNetworkImpl.class);
        claMaps.put(UserCommandNetwork.class, UserCommandNetworkImpl.class);
        
        // service
        claMaps.put(ActionService.class, ActionServiceImpl.class);
        claMaps.put(ActorAnimService.class, ActorAnimServiceImpl.class);
        claMaps.put(ActorService.class, ActorServiceImpl.class);
        claMaps.put(AttributeService.class, AttributeServiceImpl.class);
        claMaps.put(BulletService.class, BulletServiceImpl.class);
        claMaps.put(ChatService.class, ChatServiceImpl.class);
        claMaps.put(ConfigService.class, ConfigServiceImpl.class);
        claMaps.put(DropService.class, DropServiceImpl.class);
        claMaps.put(EffectService.class, EffectServiceImpl.class);
        claMaps.put(ElService.class, ElServiceImpl.class);
        claMaps.put(EnvService.class, EnvServiceImpl.class);
        claMaps.put(HandlerService.class, HandlerServiceImpl.class);
        claMaps.put(HitCheckerService.class, HitCheckerServiceImpl.class);
        claMaps.put(ItemService.class, ItemServiceImpl.class);
        claMaps.put(LogicService.class, LogicServiceImpl.class);
        claMaps.put(MagicService.class, MagicServiceImpl.class);
        claMaps.put(PlayService.class, PlayServiceImpl.class);
        claMaps.put(ResistService.class, ResistServiceImpl.class);
        claMaps.put(SaveService.class, SaveServiceImpl.class);
        claMaps.put(SceneService.class, SceneServiceImpl.class);
        claMaps.put(SkillService.class, SkillServiceImpl.class);
        claMaps.put(SkinService.class, SkinServiceImpl.class);
        claMaps.put(StateService.class, StateServiceImpl.class);
        claMaps.put(TalentService.class, TalentServiceImpl.class);
        claMaps.put(TaskService.class, TaskServiceImpl.class);
        claMaps.put(ViewService.class, ViewServiceImpl.class);
        
        // dao
        claMaps.put(ItemDao.class, ItemDaoImpl.class);
        claMaps.put(SkillDao.class, SkillDaoImpl.class);
        claMaps.put(SkinDao.class, SkinDaoImpl.class);
    }
    
    public static <T extends Object> void register(Class<T> c, Class ins) {
        if (!c.isAssignableFrom(ins)) {
            throw new IllegalArgumentException("Could not register service. service class=" + c + ", object class=" + ins);
        }
        claMaps.put(c, ins);
    }
    
    public static <T extends Object> T get(Class<T> cla) {
        T ins = (T) insMaps.get(cla);
        if (ins != null) {
            return ins;
        }
        Class clazz = claMaps.get(cla);
        if (clazz != null) {
            try {
                // 1.实例化
                ins = (T) clazz.newInstance();
                // 2.存入缓存
                insMaps.put(cla, ins);
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
