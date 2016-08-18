/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.core.object.env;

import com.jme3.app.Application;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import name.huliqing.core.Factory;
import name.huliqing.core.data.env.EnvData;
import name.huliqing.core.object.scene.Scene;
import name.huliqing.core.mvc.service.SystemService;
import name.huliqing.core.mvc.service.EnvService;

/**
 * 依赖于平台的Env环境,根据平台的匹配关系来确定要使用哪一个环境。
 * @author huliqing
 * @param <T>
 */
public class ProxyPlatformEnv <T extends EnvData> extends AbstractEnv<T> implements ProxyEnv {
    private final SystemService systemService = Factory.get(SystemService.class);
    private final EnvService envService = Factory.get(EnvService.class);

    // 默认的EnvId,如果找不到匹配当前平台的Env则使用这个id的Env作为代理Env
    private String defaultEnv;
    
    // ---- inner
    private List<EnvMatcher> matchers;
    private Env proxyEnv;
    
    @Override
    public void setData(T data) {
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
    }

    @Override
    public void initialize(Application app, Scene scene) {
        super.initialize(app, scene);
        EnvMatcher matcher = findMatcher(systemService.getPlatformName());
        String proxyEnvId = matcher != null ? matcher.envId : defaultEnv;
        EnvData envData = envService.loadEnvData(proxyEnvId);
        proxyEnv = envService.loadEnv(envData);
        proxyEnv.initialize(app, scene);
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
    public Env getProxyEnv() {
        return proxyEnv;
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
