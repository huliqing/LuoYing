///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.object.magic;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import name.huliqing.fighter.Factory;
//import name.huliqing.fighter.data.MagicData;
//import name.huliqing.fighter.game.service.AttributeService;
//
///**
// * 操作目标属性的魔法
// * @author huliqing
// */
//public class AttributeMagic extends AbstractMagic {
//    private static final Logger LOG = Logger.getLogger(AttributeMagic.class.getName());
//    private final static AttributeService attributeService = Factory.get(AttributeService.class);
//
//    // 属性ID
//    private String applyAttribute;
//    // 值
//    private float value;
//    
//    public AttributeMagic(MagicData data) {
//        super(data);
//        applyAttribute = data.getProto().getAttribute("applyAttribute");
//        value = data.getProto().getAsFloat("value");
//    }
//
//    @Override
//    protected void doInit() {
//        super.doInit();
//        if (target == null) {
//            LOG.log(Level.INFO, "Target not found!");
//            return;
//        }
//        attributeService.applyDynamicValue(target, applyAttribute, value);
//        attributeService.clampDynamicValue(target, applyAttribute);
//    }
//    
//    @Override
//    protected void doLogic(float tpf) {
//    }
//    
//}
