/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.env;

import name.huliqing.luoying.object.entity.ProxyEntity;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.AbstractEntity;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.xml.DataFactory;

/**
 * 依赖于平台的Env环境,根据平台的匹配关系来确定要使用哪一个环境。
 * @author huliqing
 */
public class PlatformProxyEnv extends AbstractEntity implements ProxyEntity {
    private final SystemService systemService = Factory.get(SystemService.class);

    // 默认的EnvId,如果找不到匹配当前平台的Env则使用这个id的Env作为代理Env
    private String defaultEnv;
    
    // ---- inner
    private List<EnvMatcher> matchers;
    private EntityData proxyEnvData;
    private Entity proxyEnv;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        defaultEnv = data.getAsString("defaultEnv");
        // 格式：“env1|platform1|platform2, envId2|platform3|platform4,...”
        String[] mpArr = data.getAsArray("mapping");
        if (mpArr != null) {
            matchers = new ArrayList<EnvMatcher>(mpArr.length);
            for (String mp : mpArr) {
                String[] envMap = mp.split("\\|");
                if (envMap.length < 2) { // 长度小于2就意味着：指定了env，但是没有指定匹配的平台，这就没有意义了。不需要处理
                    continue;
                }
                String envId = envMap[0];
                Set<String> platforms = new HashSet<String>(envMap.length - 1);
                for (int i = 1; i < envMap.length; i++) {
                    platforms.add(envMap[i]);
                }
                matchers.add(new EnvMatcher(envId, platforms));
            }
        }
        
        EnvMatcher matcher = findMatcher(systemService.getPlatformName());
        String proxyEnvId = matcher != null ? matcher.envId : defaultEnv;
        proxyEnvData = DataFactory.createData(proxyEnvId);
    }
    
    @Override
    public void updateDatas() {
        if (proxyEnv != null) {
            proxyEnv.updateDatas();
        }
    }
    
    @Override
    public void initEntity() {
        proxyEnv = Loader.load(proxyEnvData);
    }
    
    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        proxyEnv.onInitScene(scene);
    }

    @Override
    public void cleanup() {
        if (proxyEnv != null) {
            proxyEnv.cleanup();
            proxyEnv = null;
        }
        super.cleanup(); 
    }

    @Override
    public Entity getProxyEnv() {
        return proxyEnv;
    }

    @Override
    public Spatial getSpatial() {
        return proxyEnv.getSpatial();
    }
    
    private EnvMatcher findMatcher(String platform) {
        if (matchers == null)
            return null;
        for (EnvMatcher em : matchers) {
            if (em.isMatch(platform)) {
                return em;
            }
        }
        return null;
    }
    
    private class EnvMatcher {
        // EnvId
        private final String envId;
        // 匹配的平台列表
        private final Set<String> platforms;
        
        public EnvMatcher(String envId, Set<String> platforms) {
            this.envId = envId;
            this.platforms = platforms;
        }
        
        public boolean isMatch(String platform) {
            return platforms.contains(platform);
        }
    }
    
}
