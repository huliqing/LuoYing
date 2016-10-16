/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import name.huliqing.luoying.data.DropData;
import name.huliqing.luoying.data.ModuleData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.drop.Drop;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.sound.SoundManager;

/**
 *
 * @author huliqing
 */
public class DropModule extends AbstractModule {

    private List<Drop> drops;
    
    // 掉落物品时的默认提示声效
    private String[] sounds; 

    @Override
    public void setData(ModuleData data) {
        super.setData(data); 
        sounds = data.getAsArray("sounds");
    }

    @Override
    public void updateDatas() {
        // xxx updateDatas.
    }
    
    @Override
    public void initialize(Entity actor) {
        super.initialize(actor); 
        
        List<DropData> dropDatas = actor.getData().getObjectDatas(DropData.class, null);
        if (dropDatas != null) {
            for (DropData id : dropDatas) {
                addDrop((Drop) Loader.load(id));
            }
        }
    }

    @Override
    public void cleanup() {
        if (drops != null) {
            drops.clear();
        }
        super.cleanup(); 
    }
    
    /**
     * 添加一个掉落设置
     * @param drop 
     */
    public void addDrop(Drop drop) {
        if (drops == null) {
            drops = new ArrayList<Drop>(5);
        }
        if (drops.contains(drop))
            return;
        
        drops.add(drop);
        entity.getData().addObjectData(drop.getData());
    }
    
    /**
     * 移除一个掉落设置
     * @param drop
     * @return 
     */
    public boolean removeDrop(Drop drop) {
        if (drops == null || !drops.contains(drop)) 
            return false;
        
        drops.remove(drop);
        entity.getData().removeObjectData(drop.getData());
        return true;
    }
    
    /**
     * 获取掉落设置, 返回的列表只允许只读。
     * @return 
     */
    public List<Drop> getDrops() {
        if (drops != null) {
            return Collections.unmodifiableList(drops);
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * 处理掉落物品给指定角色, 注：物品是从当前角色掉落到指定角色(target)身上。
     * @param target
     */
    public void doDrop(Entity target) {
        if (drops == null) {
            return;
        }
        boolean hasDrop = false;
        for (int i = 0; i < drops.size(); i++) {
            if (drops.get(i).doDrop(entity, target)) {
                hasDrop = true;
            }
        }
        if (hasDrop) {
            playDefaultDropSound();
        }
    }
    
    // 播放默认的掉落声音
    private void playDefaultDropSound() {
        if (sounds == null)
            return;
        
        for (String s : sounds) {
            SoundManager.getInstance().playSound(s, entity.getSpatial().getWorldTranslation());
        }
    }
}
