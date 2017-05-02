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

import com.jme3.ai.navmesh.NavMesh;
import com.jme3.ai.navmesh.NavMeshPathfinder;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.optimize.GeometryBatchFactory;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.GeometryUtils;

/**
 * NavMeshEntity用于作为场景的寻路网格而存在。这个实体可以载入一个模形并用这个模型的Mesh作为寻路网格.
 * 注：所指定的模型中的网格将<strong>直接</strong>作为寻路网格, 如果模型中存在多个网格，则这些网格会被合并在一起，
 * 然后作为用于生成NavMesh的网格，其它不会再做优化处理。
 * @author huliqing
 */
public class NavMeshEntity extends NonModelEntity{

    // 这个File指向一个j3o模型文件，这个文件中的mesh将作为寻路网格用于NavMesh
    private String file;
    
    // ---- inner
    //  原始网格
    private Mesh mesh;
    // 导航网格
    private NavMesh navMesh;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        file = data.getAsString("file");
    }
    
    @Override
    protected void initEntity() {
        if (file == null) {
            return;
        }
        mesh = null;
        // 如果存在多个网格, 则把网格合并到一起
        Spatial meshFile = Loader.loadModel(file);
        LinkedList<Geometry> meshGeos = new LinkedList<Geometry>();
        GeometryUtils.findAllGeometry(meshFile, meshGeos);
        
        if (meshGeos.size() <= 0) {
            Logger.getLogger(NavMeshEntity.class.getName()).log(Level.WARNING, "No mesh found for NavMesh"
                    + ", NavMeshEntityId={0}, entityName={1}, uniqueId={2}"
                    , new Object[] {data.getId(), data.getName(), data.getUniqueId()});
            return;
        } else if (meshGeos.size() == 1) {
            mesh = meshGeos.get(0).getMesh();
        } else {
            GeometryBatchFactory.mergeGeometries(meshGeos, mesh);
        }
        navMesh = new NavMesh(mesh);
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
    }
    
    /**
     * 获取用于寻路的网格
     * @return 
     */
    public Mesh getNavMesh() {
        return mesh;
    }
    
    public NavMeshPathfinder createPathfinder() {
        if (navMesh == null) {
            return null;
        }
        return new NavMeshPathfinder(navMesh);
    }
    
//    public void findPath(final Vector3f start, final Vector3f end
//            , final NavMeshPathfinder finder, Callable<Boolean> cb) {
//        Future temp = ThreadHelper.submit(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                finder.warp(start);
//                return finder.computePath(end.clone());
//            }
//        });
//    }
   
}
