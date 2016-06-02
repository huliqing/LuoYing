/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.loader;

import name.huliqing.fighter.object.logic.FightLogic;
import name.huliqing.fighter.data.LogicData;
import name.huliqing.fighter.object.logic.FollowLogic;
import name.huliqing.fighter.object.logic.ActorLogic;
import name.huliqing.fighter.object.logic.NotifyLogic;
import name.huliqing.fighter.object.logic.PlayerLogic;
import name.huliqing.fighter.object.logic.PositionLogic;
import name.huliqing.fighter.object.logic.SearchEnemyLogic;
import name.huliqing.fighter.object.logic.AttributeChangeLogic;
import name.huliqing.fighter.object.logic.DefendLogic;
import name.huliqing.fighter.object.logic.IdleLogic;
import name.huliqing.fighter.object.logic.ShopLogic;

/**
 *
 * @author huliqing
 */
class LogicLoader {
    
    public static ActorLogic load(LogicData data) {
        
        String tagName = data.getProto().getTagName();
        
        if (tagName.equals("logicFight")) {
            return new FightLogic(data);
        }
        
        if (tagName.equals("logicFollow")) {
            return new FollowLogic(data);
        } 
        
        if (tagName.equals("logicNotify")) {
            return new NotifyLogic(data);
        } 
        
        if (tagName.equals("logicPlayer")) {
            return new PlayerLogic(data);
        }  
        
        if (tagName.equals("logicPosition")) {
            return new PositionLogic(data);
        } 
        
        if (tagName.equals("logicSearchEnemy")) {
            return new SearchEnemyLogic(data);
        } 
        
        if (tagName.equals("logicAttributeChange")) {
            return new AttributeChangeLogic(data);
        } 
        
        if (tagName.equals("logicDefend")) {
            return new DefendLogic(data);
        } 
        
        if (tagName.equals("logicIdle")) {
            return new IdleLogic(data);
        }
        
        if (tagName.equals("logicShop")) {
            return new ShopLogic(data);
        }
        
        throw new UnsupportedOperationException("Unknow Logic type, tagName=" + tagName);
    }
}
