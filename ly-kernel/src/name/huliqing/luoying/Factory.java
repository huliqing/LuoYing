/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
 */
package name.huliqing.luoying;

import java.util.HashMap;
import java.util.Map;
import name.huliqing.luoying.layer.network.ActionNetwork;
import name.huliqing.luoying.layer.network.ActionNetworkImpl;
import name.huliqing.luoying.layer.network.ActorNetwork;
import name.huliqing.luoying.layer.network.ActorNetworkImpl;
import name.huliqing.luoying.layer.network.EntityNetwork;
import name.huliqing.luoying.layer.network.EntityNetworkImpl;
import name.huliqing.luoying.layer.network.PlayNetwork;
import name.huliqing.luoying.layer.network.PlayNetworkImpl;
import name.huliqing.luoying.layer.network.SkillNetwork;
import name.huliqing.luoying.layer.network.SkillNetworkImpl;
import name.huliqing.luoying.layer.network.SkinNetwork;
import name.huliqing.luoying.layer.network.SkinNetworkImpl;
import name.huliqing.luoying.layer.network.TalentNetwork;
import name.huliqing.luoying.layer.network.TalentNetworkImpl;
import name.huliqing.luoying.layer.network.TaskNetwork;
import name.huliqing.luoying.layer.network.TaskNetworkImpl;
import name.huliqing.luoying.layer.service.ActionService;
import name.huliqing.luoying.layer.service.ActionServiceImpl;
import name.huliqing.luoying.layer.service.ActorService;
import name.huliqing.luoying.layer.service.ActorServiceImpl;
import name.huliqing.luoying.layer.service.ConfigService;
import name.huliqing.luoying.layer.service.ConfigServiceImpl;
import name.huliqing.luoying.layer.service.DefineService;
import name.huliqing.luoying.layer.service.DefineServiceImpl;
import name.huliqing.luoying.layer.service.SystemServiceImpl;
import name.huliqing.luoying.layer.service.ElService;
import name.huliqing.luoying.layer.service.ElServiceImpl;
import name.huliqing.luoying.layer.service.LogicService;
import name.huliqing.luoying.layer.service.LogicServiceImpl;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.layer.service.PlayServiceImpl;
import name.huliqing.luoying.layer.service.SaveService;
import name.huliqing.luoying.layer.service.SaveServiceImpl;
import name.huliqing.luoying.layer.service.SceneService;
import name.huliqing.luoying.layer.service.SceneServiceImpl;
import name.huliqing.luoying.layer.service.SkillService;
import name.huliqing.luoying.layer.service.SkillServiceImpl;
import name.huliqing.luoying.layer.service.SkinService;
import name.huliqing.luoying.layer.service.SkinServiceImpl;
import name.huliqing.luoying.layer.service.TalentService;
import name.huliqing.luoying.layer.service.TalentServiceImpl;
import name.huliqing.luoying.layer.service.TaskService;
import name.huliqing.luoying.layer.service.TaskServiceImpl;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.layer.service.EntityService;
import name.huliqing.luoying.layer.service.EntityServiceImpl;
import name.huliqing.luoying.layer.service.MathService;
import name.huliqing.luoying.layer.service.MathServiceImpl;
import name.huliqing.luoying.layer.service.MessageService;
import name.huliqing.luoying.layer.service.MessageServiceImpl;
import name.huliqing.luoying.layer.service.ResistService;
import name.huliqing.luoying.layer.service.ResistServiceImpl;
import name.huliqing.luoying.layer.service.SoundService;
import name.huliqing.luoying.layer.service.SoundServiceImpl;

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
        CLASS_MAP.put(EntityNetwork.class, EntityNetworkImpl.class);
        CLASS_MAP.put(PlayNetwork.class, PlayNetworkImpl.class);
        CLASS_MAP.put(SkillNetwork.class, SkillNetworkImpl.class);
        CLASS_MAP.put(SkinNetwork.class, SkinNetworkImpl.class);
        CLASS_MAP.put(TalentNetwork.class, TalentNetworkImpl.class);
        CLASS_MAP.put(TaskNetwork.class, TaskNetworkImpl.class);
        
        // service
        CLASS_MAP.put(ActionService.class, ActionServiceImpl.class);
        CLASS_MAP.put(ActorService.class, ActorServiceImpl.class);
        CLASS_MAP.put(ConfigService.class, ConfigServiceImpl.class);
        CLASS_MAP.put(DefineService.class, DefineServiceImpl.class);
        CLASS_MAP.put(ElService.class, ElServiceImpl.class);
        CLASS_MAP.put(EntityService.class, EntityServiceImpl.class);
        CLASS_MAP.put(LogicService.class, LogicServiceImpl.class);
        CLASS_MAP.put(MathService.class, MathServiceImpl.class);
        CLASS_MAP.put(MessageService.class, MessageServiceImpl.class);
        CLASS_MAP.put(PlayService.class, PlayServiceImpl.class);
        CLASS_MAP.put(ResistService.class, ResistServiceImpl.class);
        CLASS_MAP.put(SaveService.class, SaveServiceImpl.class);
        CLASS_MAP.put(SceneService.class, SceneServiceImpl.class);
        CLASS_MAP.put(SkillService.class, SkillServiceImpl.class);
        CLASS_MAP.put(SkinService.class, SkinServiceImpl.class);
        CLASS_MAP.put(SoundService.class, SoundServiceImpl.class);
        CLASS_MAP.put(SystemService.class, SystemServiceImpl.class);
        CLASS_MAP.put(TalentService.class, TalentServiceImpl.class);
        CLASS_MAP.put(TaskService.class, TaskServiceImpl.class);
        
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
