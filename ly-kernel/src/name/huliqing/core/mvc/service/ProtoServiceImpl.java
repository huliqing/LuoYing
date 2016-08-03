/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.mvc.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.core.Factory;
import name.huliqing.core.constants.DataTypeConstants;
import name.huliqing.core.xml.Proto;
import name.huliqing.core.data.ProtoData;
import name.huliqing.core.object.DataFactory;
import name.huliqing.core.object.ProtoUtils;
import name.huliqing.core.object.actor.Actor;

/**
 *
 * @author huliqing
 */
public class ProtoServiceImpl implements ProtoService {
    private static final Logger LOG = Logger.getLogger(ProtoServiceImpl.class.getName());
    
    private ItemService itemService;
    private SkinService skinService;
    private SkillService skillService;
    private HandlerService handlerService;

    @Override
    public void inject() {
        itemService = Factory.get(ItemService.class);
        skinService = Factory.get(SkinService.class);
        skillService = Factory.get(SkillService.class);
        handlerService = Factory.get(HandlerService.class);
    }

    @Override
    public ProtoData createData(String id) {
        ProtoData data = DataFactory.createData(id);
        return data;
    }

    @Override
    public ProtoData getData(Actor actor, String id) {
        Proto proto = ProtoUtils.getProto(id);
        if (proto == null)
            return null;
        
        int dt = proto.getDataType();
        ProtoData data = null;
        switch (dt) {
            case DataTypeConstants.ITEM:
            case DataTypeConstants.SKIN:
                data = itemService.getItem(actor, id);
                break;
                
            case DataTypeConstants.SKILL:
                data = skillService.getSkill(actor, id);
                break;
                
            default :
                LOG.log(Level.WARNING, "Unsupported getData type dataType={0}, dataId={1}", new Object[] {dt, id});
                break;
        }
        return data;
    }

    @Override
    public void addData(Actor actor, ProtoData data, int count) {
        if (data == null)
            return;
        
        int dt = data.getDataType();
        switch (dt) {
            case DataTypeConstants.ITEM:
            case DataTypeConstants.SKIN:
                itemService.addItem(actor, data.getId(), count);
                break;
            
            case DataTypeConstants.SKILL:
                skillService.addSkill(actor, data.getId());
                break;
                
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void useData(Actor actor, ProtoData data) {
        if (data == null)
            return;
        
        handlerService.useObject(actor, data);
    }

    @Override
    public void removeData(Actor actor, ProtoData data, int count) {
        if (data == null)
            return;
        
        handlerService.removeObject(actor, data, count);
    }

    @Override
    public void syncDataTotal(Actor actor, String objectId, int total) {
        Proto proto = ProtoUtils.getProto(objectId);
        int dt = proto.getDataType();
        switch (dt) {
            case DataTypeConstants.ITEM:
            case DataTypeConstants.SKIN:
                itemService.syncItemTotal(actor, objectId, total);
                break;
                
            default:
                LOG.log(Level.WARNING, "Unsupported syncDataTotal for, objectId={0}, dataType={1}", new Object[] {objectId, dt});
                break;
        }
    }
    
}
