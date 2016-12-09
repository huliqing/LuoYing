///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.luoying.message;
//
//import name.huliqing.luoying.data.ItemData;
//import name.huliqing.luoying.object.entity.Entity;
//
///**
// * ItemDataUseMessage用于实体在使用物体时的状态消息。 
// * @author huliqing
// */
//public class ItemDataUseMessage extends DataUseMessage<ItemData> {
//    
//    // 物品使用状态
//    private final int state;
//    
//    /**
//     * @param message 基本消息
//     * @param entity 使用物品的实体
//     * @param item 被使用的物品
//     * @param state 状态码，物品成功使用必须设置为{@link ItemDataUseMessage#STATE_OK}。
//     */
//    public ItemDataUseMessage(String message, Entity entity, ItemData item, int state) {
//        super(message, entity, item, state == STATE_OK);
//        this.state = state;
//    }
//
//    /**
//     * 获取物品的使用状态码,每个状态码表示的物品使用是否正确。只有当状态码为{@link ItemDataUseMessage#STATE_OK}
//     * 时才表示物品正常使用，其它状态码分别表示不同的异常状态。
//     * @return 
//     */
//    public int getState() {
//        return state;
//    }
//    
//    
//}
