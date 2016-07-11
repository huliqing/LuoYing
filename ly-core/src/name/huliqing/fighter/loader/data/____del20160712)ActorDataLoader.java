///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader.data;
//
//import name.huliqing.fighter.object.DataLoaderFactory;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.data.ActorData;
//import name.huliqing.fighter.data.AttributeData;
//import name.huliqing.fighter.data.DropData;
//import name.huliqing.fighter.data.LogicData;
//import name.huliqing.fighter.data.Proto;
//import name.huliqing.fighter.data.ProtoData;
//import name.huliqing.fighter.data.ResistData;
//import name.huliqing.fighter.data.SkillData;
//import name.huliqing.fighter.data.SkinData;
//import name.huliqing.fighter.data.TalentData;
//import name.huliqing.fighter.enums.Sex;
//import name.huliqing.fighter.manager.ResourceManager;
//import name.huliqing.fighter.object.actor.ItemStore;
//import name.huliqing.fighter.object.actor.SkillStore;
//
///**
// *
// * @author huliqing
// */
//public class ActorDataLoader implements DataLoader<ActorData> {
//    
//    @Override
//    public ActorData loadData(Proto proto) {
//        // ==== 2.items 
//        ItemStore itemStore = new ItemStore();
//        String[] itemsTemp = proto.getAsArray("items");
//        if (itemsTemp != null && itemsTemp.length > 0) {
//            for (String item : itemsTemp) {
//                if (item == null || item.trim().equals("")) {
//                    continue;
//                }
//                String[] itemArr = item.split("\\|");
//                String itemId = itemArr[0];
//                int itemTotal = 1;
//                if (itemArr.length >= 2) {
//                    itemTotal = Integer.parseInt(itemArr[1]);
//                }
//                ProtoData itemData = DataLoaderFactory.createData(itemId);
//                itemStore.addItem(itemData, itemTotal);
//            }
//        }
//        
//        // skinBase 基本皮肤,
//        // 注1：可能部分角色没有基本皮肤，如不可换装备的角色类型
//        // 注2: 基本皮肤不会存放在itemStore中，而skinOutfit会存放在itemStore
//        List<SkinData> skinBases = null;
//        String[] skinBasesTemp = proto.getAsArray("skinBase");
//        if (skinBasesTemp != null) {
//            skinBases = new ArrayList<SkinData>(skinBasesTemp.length);
//            for (String sbt : skinBasesTemp) {
//                SkinData sdb = DataLoaderFactory.createSkinData(sbt);
//                sdb.setUsing(true);// 对于skinBase来说设置using=true没有太大意义，因为skinBase不会在界面上显示
//                skinBases.add(sdb);
//            }            
//        }
//        
//        // skinOutfit
//        String[] skinOutfitTemp = proto.getAsArray("skinOutfit");
//        if (skinOutfitTemp != null) {
//            for (String skinId : skinOutfitTemp) {
//                itemStore.addItem(DataLoaderFactory.createSkinData(skinId), 1);
//                SkinData skinOutfit = (SkinData) itemStore.getItem(skinId);
//                skinOutfit.setUsing(true);
//            }
//        }
//        
//        // items - weapon
//        String[] weaponIds = proto.getAsArray("weapon");
//        if (weaponIds != null) {
//            for (String wid : weaponIds) {
//                itemStore.addItem(DataLoaderFactory.createSkinData(wid), 1);
//                SkinData weaponData = (SkinData) itemStore.getItem(wid);
//                weaponData.setUsing(true);
//            }
//        }
//        
//        // ==== 载入物品掉落设置
//        String drop = proto.getAttribute("drop");
//        DropData dropData = null;
//        if (drop != null) {
//            dropData = DataLoaderFactory.createDropData(drop);
//        }
//        
//        // ==== 载入技能
//        String[] skillIds = proto.getAsArray("skills");
//        SkillStore skillStore = new SkillStore();
//        if (skillIds != null && skillIds.length > 0) {
//            for (String skillId : skillIds) {
//                SkillData skillData = DataLoaderFactory.createSkillData(skillId);
//                if (skillData != null) {
//                    skillStore.add(skillData);
//                } else {
//                    Logger.getLogger(ActorDataLoader.class.getName())
//                            .log(Level.WARNING
//                            , "Skill not found, actorId={0}, skillId={1}"
//                            , new Object[]{proto.getId(), skillId});
//                }
//            }
//        }
//        
//        // ==== 载入逻辑
//        String[] logicIds = proto.getAsArray("logic");
//        List<LogicData> logics = null;
//        if (logicIds != null && logicIds.length > 0) {
//            logics = new ArrayList<LogicData>(logicIds.length);
//            for (String logicId : logicIds) {
//                logics.add(DataLoaderFactory.createLogicData(logicId));
//            }
//        }
//        
//        ActorData data = new ActorData(proto.getId());
//        data.setLevel(proto.getAsInteger("level", 1));
//        
//        // remove201602xx
////        // physics
////        boolean physicsEnabled = proto.getAsBoolean("physicsEnabled", true);
////        data.setPhysicsEnabled(physicsEnabled);
//        
//        // 武器插槽
//        List<String> slots = proto.getAsList("slots");
//        
//        // 角色属性
//        String[] attributeArr = proto.getAsArray("attributes");
//        String lifeAttribute = proto.getAttribute("lifeAttribute");
//        String viewAttribute = proto.getAttribute("viewAttribute");
//        Map<String, AttributeData> temp = new LinkedHashMap<String, AttributeData>();
//        if (attributeArr != null) {
//            for (String attrId : attributeArr) {
//                AttributeData attData = DataLoaderFactory.createAttributeData(attrId);
//                temp.put(attData.getId(), attData);
//            }
//        }
//        if (lifeAttribute != null) {
//            temp.put(lifeAttribute, DataLoaderFactory.createAttributeData(lifeAttribute));
//        }
//        if (viewAttribute != null) {
//            temp.put(viewAttribute, DataLoaderFactory.createAttributeData(viewAttribute));
//        }
//        Map<String, AttributeData> attributes = null;
//        if (temp.size() > 0) {
//            attributes = new LinkedHashMap<String, AttributeData>(temp.size());
//            attributes.putAll(temp);
//        }
//        
//        // Resist
//        ResistData resistData = null;
//        String tempResist = proto.getAttribute("resist");
//        if (tempResist != null) {
//            resistData = DataLoaderFactory.createResistData(tempResist);
//        }
//        
//        // talents
//        String[] talentArr = proto.getAsArray("talents");
//        ArrayList<TalentData> talents = null;
//        if (talentArr != null) {
//            talents = new ArrayList<TalentData>(talentArr.length);
//            for (String talent : talentArr) {
//                talents.add(DataLoaderFactory.createTalentData(talent));
//            }
//        }
//        
//        // 等级及经验值掉落设置
//        data.setLevelUpEl(proto.getAttribute("levelUpEl"));
//        data.setXpDropEl(proto.getAttribute("xpDropEl"));
//        data.setName(ResourceManager.getObjectName(proto.getId()));
//        data.setGroup(proto.getAsInteger("group", 0));
//        data.setSex(Sex.identifyByName(proto.getAttribute("sex", "2")));
//        data.setRace(proto.getAttribute("race"));
//        data.setEssential(proto.getAsBoolean("essential", false));
//        data.setSkinBase(skinBases);
//        data.setItemStore(itemStore);
//        data.setSkillStore(skillStore);
//        data.setDrop(dropData);
//        data.setLogics(logics);
//        data.setSlots(slots);
//        data.setAttributes(attributes);
//        data.setLifeAttribute(lifeAttribute);
//        data.setViewAttribute(viewAttribute);
//        data.setResist(resistData);
//        data.setTalents(talents);
//        data.setTalentPoints(proto.getAsInteger("talentPoints", 0));
//        data.setTalentPointsLevelEl(proto.getAttribute("talentPointsLevelEl"));
//        data.setTeam(proto.getAsInteger("team", 0));
//        data.setLiving(proto.getAsBoolean("living", false));
//        data.setFollowTarget(proto.getAsInteger("followTarget", -1));
//        data.setChat(proto.getAttribute("chat"));
//        return data;
//    }
//    
//}
