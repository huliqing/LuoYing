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
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.luoying.data.EntityData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.NonModelEntity;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.utils.MaterialUtils;

/**
 * NavMeshEntity用于作为场景的寻路网格而存在。这个实体可以载入一个模形并用这个模型的Mesh作为寻路网格.
 * 也可以指定一个已经生成好的寻路网格模型
 * @author huliqing
 */
public class NavMeshEntity extends NonModelEntity{

    // 这个File指向一个j3o模型文件，这个文件中的mesh将作为寻路网格用于NavMesh
    private String file;
    private boolean debug;
    
    // ---- inner
    //  原始网格
    private Mesh mesh;
    // 导航网格
    private NavMesh navMesh;
    // Debug
    private Geometry debugGeo;
    
    @Override
    public void setData(EntityData data) {
        super.setData(data);
        file = data.getAsString("file");
        debug = data.getAsBoolean("debug", debug);
    }
    
    @Override
    protected void initEntity() {
        if (file == null) {
            return;
        }
        // 如果存在多个网格，则最后找到的一个为准。
        Spatial meshFile = Loader.loadModel(file);
        meshFile.depthFirstTraversal(new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    mesh = ((Geometry) spatial).getMesh();
                }
            }
        });
        if (mesh != null) {
            navMesh = new NavMesh(mesh);
            if (debug) {
                debugGeo = new Geometry("NavMesh", mesh);
                debugGeo.setCullHint(Spatial.CullHint.Never);
                debugGeo.setMaterial(MaterialUtils.createUnshaded(ColorRGBA.Green));
            }
        } else {
            Logger.getLogger(NavMeshEntity.class.getName()).log(Level.WARNING, "No mesh found for NavMesh"
                    + ", NavMeshEntityId={0}, entityName={1}, uniqueId={2}"
                    , new Object[] {data.getId(), data.getName(), data.getUniqueId()});
        }
    }

    @Override
    public void cleanup() {
        mesh = null;
        if (debugGeo != null) {
            debugGeo.removeFromParent();
            debugGeo = null;
        }
        super.cleanup();
    }

    @Override
    public void onInitScene(Scene scene) {
        super.onInitScene(scene);
        if (debug && debugGeo != null) {
            scene.getRoot().attachChild(debugGeo);
        }
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
