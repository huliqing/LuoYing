/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.handler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Config;
import name.huliqing.core.Factory;
import name.huliqing.core.data.HandlerData;
import name.huliqing.core.data.PkgItemData;
import name.huliqing.core.object.actor.Actor;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.enums.MessageType;
import name.huliqing.core.mvc.network.PlayNetwork;
import name.huliqing.core.mvc.service.ActorService;
import name.huliqing.core.mvc.service.EffectService;
import name.huliqing.core.mvc.service.ItemService;
import name.huliqing.core.mvc.service.PlayService;
import name.huliqing.core.manager.ResourceManager;
import name.huliqing.core.manager.SoundManager;
import name.huliqing.core.object.effect.Effect;

/**
 * 基本Handler类
 * @author huliqing
 * @param <T>
 */
public abstract class AbstractHandler<T extends HandlerData> implements Handler<T> {
    private final PlayNetwork playNetwork = Factory.get(PlayNetwork.class);
    private final PlayService playService = Factory.get(PlayService.class);
    private final ActorService actorService = Factory.get(ActorService.class);
    private final EffectService effectService = Factory.get(EffectService.class);
    private final ItemService itemService = Factory.get(ItemService.class);
    
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
    public boolean canUse(Actor actor, ProtoData data) {
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
        
        if (!checkRaceAndSex(actor, data)) {
            playNetwork.addMessage(actor, ResourceManager.get("handler.notRaceAndSex"), MessageType.notice);
            return false;
        }
        return true;
    }
    
    /**
     * 检查物品的种族限制和性别限制是否允许使用该物品。
     * @param actor
     * @param data
     * @return 
     */
    protected final boolean checkRaceAndSex(Actor actor, ProtoData data) {
        // 只检查PkgItemData类型的物品，其它物品默认为无限制
        if (!(data instanceof PkgItemData)) {
            return true;
        }
        PkgItemData pkgData = (PkgItemData) data;
        List<String> raceLimit = pkgData.getRaceLimit();
        // 种族限制
        if (!raceLimit.isEmpty() && !raceLimit.contains(actorService.getRace(actor))) {
            if (Config.debug) {
                Logger.getLogger(AbstractHandler.class.getName()).log(Level.INFO, "Check race limit failure, found race={0}, expect={1}"
                        , new Object[] {actorService.getRace(actor),  raceLimit.toString()});
            }
            return false;
        }
        if (pkgData.getSexLimit() != null && pkgData.getSexLimit() != actorService.getSex(actor)) {
            if (Config.debug) {
                Logger.getLogger(AbstractHandler.class.getName()).log(Level.INFO, "Check sex limit failure, found sex={0}, expect={1}"
                        , new Object[] {actorService.getRace(actor),  pkgData.getSexLimit()});
            }
            return false;
        }
        return true;
    }

    @Override
    public final void useForce(Actor actor, ProtoData data) {
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
                 effect.setTraceObject(actor.getModel());
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
                SoundManager.getInstance().playSound(sid, actor.getModel().getWorldTranslation());
            }
        }
    }
    
    @Override
    public boolean remove(Actor actor, ProtoData data, int count) {
        // 一些物品是不能删除的,如地图
        if (data instanceof PkgItemData) {
            PkgItemData pkgData = (PkgItemData) data;
            if (!pkgData.isDeletable()) {
                return false;
            }
        }
        
        // 删除并提示,只提示当前场景中的主玩家
        int trueRemoved = itemService.removeItem(actor, data.getId(), count);
        if (actor == playService.getPlayer()) {
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
    protected abstract void useObject(Actor actor, ProtoData data);

}
