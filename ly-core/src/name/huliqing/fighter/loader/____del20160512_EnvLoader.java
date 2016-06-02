///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package name.huliqing.fighter.loader;
//
//import name.huliqing.fighter.data.EnvData;
//import name.huliqing.fighter.object.env.Env;
//import name.huliqing.fighter.object.env.GrassEnv;
//import name.huliqing.fighter.object.env.SkyEnv;
//import name.huliqing.fighter.object.env.TerrainEnv;
//import name.huliqing.fighter.object.env.TreeEnv;
//
///**
// *
// * @author huliqing
// */
//class EnvLoader {
//
//    public static Env load(EnvData data) {
//        String tagName = data.getTagName();
//        
//        Env env = null;
//        if (tagName.equals("sky")) {
//            env = new SkyEnv(data);
//        } else if (tagName.equals("terrain")) {
//            env = new TerrainEnv(data);
//        } else if (tagName.equals("tree")) {
//            env = new TreeEnv(data);
//        } else if (tagName.equals("grass")) {
//            env = new GrassEnv(data);
//        }
//        // 触发模型载入
//        if (env != null) {
//            env.getModel();
//            return env;
//        }
//        
//        // 无法识别的tagName
//        throw new UnsupportedOperationException("Unknow tagName=" + tagName);
//    }
//}
