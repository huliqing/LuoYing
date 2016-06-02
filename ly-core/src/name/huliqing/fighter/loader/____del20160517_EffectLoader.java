///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.EffectData;
//import name.huliqing.fighter.object.effect.Effect;
//import name.huliqing.fighter.object.effect.EncircleHaloEffect;
//import name.huliqing.fighter.object.effect.HaloEffect;
//import name.huliqing.fighter.object.effect.ModelEffect;
//import name.huliqing.fighter.object.effect.GroupEffect;
//import name.huliqing.fighter.object.effect.ParticleEffect;
//import name.huliqing.fighter.object.effect.ProjectionEffect;
//import name.huliqing.fighter.object.effect.SimpleGroupEffect;
//import name.huliqing.fighter.object.effect.SlideColorEffect;
//import name.huliqing.fighter.object.effect.SlideColorIOSplineEffect;
//import name.huliqing.fighter.object.effect.SlideColorSplineEffect;
//import name.huliqing.fighter.object.effect.TextureCylinderEffect;
//import name.huliqing.fighter.object.effect.TextureEffect;
//
///**
// *
// * @author huliqing
// */
//class EffectLoader {
//    
//    public static Effect loadEffect(EffectData data) {
//        String tagName = data.getProto().getTagName();
//        
//        if ("effectHalo".equals(tagName)) {
//            return new HaloEffect(data);
//        } 
//        
//        if ("effectParticle".equals(tagName)) {
//            return new ParticleEffect(data);
//        } 
//        
//        if ("effectSimpleGroup".equals(tagName)) { // outdate
//            return new SimpleGroupEffect(data);
//        } 
//        
//        if ("effectGroup".equals(tagName)) {
//            return new GroupEffect(data);
//        } 
//        
//        if ("effectEncircleHalo".equals(tagName)) { // outdate
//            return new EncircleHaloEffect(data);
//        } 
//        
//        if ("effectTexture".equals(tagName)) {
//            return new TextureEffect(data);
//        } 
//        
//        if ("effectTextureCylinder".equals(tagName)) {
//            return new TextureCylinderEffect(data);
//        } 
//        
//        if ("effectModel".equals(tagName)) {
//            return new ModelEffect(data);
//        }
//        
//        if ("effectSlideColor".equals(tagName)) {
//            return new SlideColorEffect(data);
//        } 
//        
//        if ("effectSlideColorSpline".equals(tagName)) {
//            return new SlideColorSplineEffect(data);
//        } 
//        
//        if ("effectSlideColorIOSpline".equals(tagName)) {
//            return new SlideColorIOSplineEffect(data);
//        } 
//        
//        if ("effectProjection".equals(tagName)) {
//            return new ProjectionEffect(data);
//        } 
//
//        return null;
//    }
//    
//    // remove,不能用反射
////    private final static Map<String, Class> tagMap = new HashMap<String, Class>();
////    
////    static {
////        tagMap.put("effectGroup", GroupEffect.class);
////        tagMap.put("effectParticle", ParticleEffect.class);
////        tagMap.put("effectTexture", TextureEffect.class);
////        tagMap.put("effectModel", ModelEffect.class);
////        tagMap.put("effectTextureCylinder", TextureCylinderEffect.class);
////        tagMap.put("effectSplineSurface", SlideColorSplineEffect.class);
////        
////        // ----outdate
////        tagMap.put("effectEncircleHalo", EncircleHaloEffect.class);
////        tagMap.put("effectSimpleGroup", SimpleGroupEffect.class);
////        tagMap.put("effectHalo", HaloEffect.class);
////    }
////    public static Effect loadEffect(EffectData data) {
////        String tagName = data.getProto().getTagName();
////        Class clazz = tagMap.get(tagName);
////        if (clazz != null) {
////            try {
////                Constructor<Effect> con = clazz.getConstructor(EffectData.class);
////                Effect effect = con.newInstance(data);
////                return effect;
////            } catch (Exception ex) {
////                throw new NullPointerException("Could not instance for class=" + clazz + ", error=" + ex.getMessage());
////            }
////        }
////        throw new NullPointerException("Could not loadEffect");
////    }
//    
//}
