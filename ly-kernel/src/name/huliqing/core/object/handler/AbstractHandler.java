/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import name.huliqing.core.Factory;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ObjectData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.mvc.service.ProtoService;
import name.huliqing.core.object.effect.Effect;
import name.huliqing.core.object.sound.SoundManager;

/**
 * 基本Handler类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractHandler<T extends HandlerData> implements Handler<T> {
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
//    private final ItemService itemService = Factory.get(ItemService.class);
    private final ProtoService protoService = Factory.get(ProtoService.class);
    
    protected T data;
    protected String[] effects;
    protected String[] sounds;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
        effects = data.getAsArray("effects");
        sounds = data.getAsArray("sounds");
    }

    @Override
    public boolean canUse(Actor actor, ObjectData data) {
        if (actor == null || data == null) {
            throw new NullPointerException("could not use data, actor=" + actor + ", data=" + data);
        }
        
        // 物品数量不够
        if (data.getTotal() <= 0) {
            return false;
        }
        
        // 角色死亡
        if (actorService.isDead(actor)) {
            return false;
        }
   
        return true;
    }
    
    @Override
    public final void useForce(Actor actor, ObjectData data) {
        useObject(actor, data);
        playEffects(actor, effects);
        playSounds(actor, sounds);
    }
    
    /**
     * 播放特效
     * @param actor
     * @param effects 
     */
    private void playEffects(Actor actor, String[] effects) {
        if (effects != null) {
            Effect effect;
            for (String eid : effects) {
                 effect = effectService.loadEffect(eid);
                 effect.setTraceObject(actor.getSpatial());
                 playService.addEffect(effect);
            }
        }
    }
    
    /**
     * 播放声效
     * @param actor
     * @param sounds 
     */
    private void playSounds(Actor actor, String[] sounds) {
        if (sounds != null) {
            for (String sid : sounds) {
                SoundManager.getInstance().playSound(sid, actor.getSpatial().getWorldTranslation());
            }
        }
    }
    
    @Override
    public boolean remove(Actor actor, ObjectData data, int count) {
        
        // remove20160831
        // 一些物品是不能删除的,如地图
//        if (data instanceof PkgItemData) {
//            PkgItemData pkgData = (PkgItemData) data;
//            if (!pkgData.isDeletable()) {
//                return false;
//            }
//        }
        
        // 删除并提示,只提示当前场景中的主玩家
        int oldCount = data.getTotal();
        protoService.removeData(actor, data.getId(), count);
        ObjectData od = protoService.getData(actor, data.getId());
        int trueRemoved = od != null ? oldCount - od.getTotal() : oldCount;
        
        if (trueRemoved > 0 && actor == playService.getPlayer()) {
            playService.addMessage(ResourceManager.get("common.remove"
                            , new Object[] {ResourceManager.getObjectName(data.getId())
                                    , trueRemoved > 1 ? "(" + trueRemoved + ")" : ""})
                            , MessageType.notice);
        }
        return trueRemoved > 0;
    }
    
    /**
     * 立即使用物品
     * @param actor
     * @param data 
     */
    protected abstract void useObject(Actor actor, ObjectData data);

}
