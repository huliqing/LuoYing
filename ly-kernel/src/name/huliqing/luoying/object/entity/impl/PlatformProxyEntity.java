/*
 * LuoYing is a program used to make 3D RPG game.
 * Copyright (c) 2014-2016 Huliqing <31703299@qq.com>
 * 
 * This file is part of LuoYing.
 *
 * LuoYing is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LuoYing is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with LuoYing.  If not, see <http://www.gnu.org/licenses/>.
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (proxyEntity != null) {
            proxyEntity.setEnabled(enabled);
        }
    }
    
    @Override
    public void initEntity() {
        EntityMatcher matcher = findMatcher(systemService.getPlatformName());
        String proxyId = matcher != null ? matcher.entityId : defaultEntityId;
        proxyEntity = Loader.load(proxyId);
        proxyEntity.setEnabled(isEnabled());
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
