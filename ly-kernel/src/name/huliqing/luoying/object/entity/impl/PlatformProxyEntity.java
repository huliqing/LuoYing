/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.object.entity.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.layer.service.SystemService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.ProxyEntity;
import name.huliqing.luoying.object.entity.Entity;

/**
 * 依赖于平台的Entity,根据平台的匹配关系来确定要使用哪一个环境。
 * @author huliqing
 */
public class PlatformProxyEntity extends ProxyEntity {
    private final SystemService systemService = Factory.get(SystemService.class);

    // 默认的EntityId,如果找不到匹配当前平台的Env则使用这个id的Env作为代理Env
    private String defaultEntityId;
    
    // ---- inner
    private Entity proxyEntity;
    private List<EntityMatcher> matchers;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        defaultEntityId = data.getAsString("defaultEntity");
        // 格式：“entity1|platform1|platform2, entityId2|platform3|platform4,...”
        String[] mpArr = data.getAsArray("mapping");
        if (mpArr != null) {
            matchers = new ArrayList<EntityMatcher>(mpArr.length);
            for (String mp : mpArr) {
                String[] entityMap = mp.split("\\|");
                if (entityMap.length < 2) { // 长度小于2就意味着：指定了entity，但是没有指定匹配的平台，这就没有意义了。不需要处理
                    continue;
                }
                String entityId = entityMap[0];
                Set<String> platforms = new HashSet<String>(entityMap.length - 1);
                for (int i = 1; i < entityMap.length; i++) {
                    platforms.add(entityMap[i]);
                }
                matchers.add(new EntityMatcher(entityId, platforms));
            }
        }
    }
    
    @Override
    public void initEntity() {
        EntityMatcher matcher = findMatcher(systemService.getPlatformName());
        String proxyId = matcher != null ? matcher.entityId : defaultEntityId;
        proxyEntity = Loader.load(proxyId);
    }

    @Override
    public Entity getProxyEntity() {
        return proxyEntity;
    }
    
    private EntityMatcher findMatcher(String platform) {
        if (matchers == null)
            return null;
        for (EntityMatcher em : matchers) {
            if (em.isMatch(platform)) {
                return em;
            }
        }
        return null;
    }
    
    private class EntityMatcher {
        // EnvId
        private final String entityId;
        // 匹配的平台列表
        private final Set<String> platforms;
        
        public EntityMatcher(String entityId, Set<String> platforms) {
            this.entityId = entityId;
            this.platforms = platforms;
        }
        
        public boolean isMatch(String platform) {
            return platforms.contains(platform);
        }
    }
    
}
