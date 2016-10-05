/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Animation;
import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bounding.BoundingVolume.Type;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Camera.FrustumIntersect;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.util.TempVars;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Ly;

/**
 *
 * @author huliqing
 */
public class GeometryUtils {
    private final static Logger logger = Logger.getLogger(GeometryUtils.class.getName());
    
    /**
     * 判断child是否和parent是同一个对象，如果是同一个对象，或者是其子对象
     * 都视为true.否则返回false
     * @param child
     * @param parent
     * @return 
     */
    public static boolean isSelfOrChild(Spatial child, Spatial parent) {
        if (child == parent) {
            return true;
        } else {
            if (child.getParent() != null) {
                return isSelfOrChild(child.getParent(), parent);
            } else {
                return false;
            }
        }
    }
    
    /**
     * 查找spatial的根父节点
     * @param spatial
     * @return 
     */
    public static Spatial findRootNode(Spatial spatial) {
        if (spatial.getParent() == null) {
            return spatial;
        } else {
            return findRootNode(spatial.getParent());
        }
    }
    
    /**
     * 转换为unshade模式
     * @param node
     */
    public static void makeUnshaded(Spatial node) {

        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            private Geometry tempGeom;
            private Material tempMat;
            @Override
            public void visit(Spatial spatial) {
                if (!(spatial instanceof Geometry))
                    return;
                
                tempGeom = (Geometry) spatial;
                tempMat = tempGeom.getMaterial();
                if (tempGeom.getName().equals("Invisible")) {
                    return;
                }
                if ("Unshaded".equals(tempMat.getMaterialDef().getName())) {
                    return;
                }
                
                // 地形物体
                if (tempGeom.getMaterial().getTextureParam("DiffuseMap_1") != null) {

                    Material tat = new Material(Ly.getAssetManager(), "Common/MatDefs/Terrain/Terrain.j3md");
                    tat.setTexture("Alpha", tempGeom.getMaterial().getTextureParam("AlphaMap").getTextureValue());
                    if (tempGeom.getMaterial().getTextureParam("DiffuseMap") != null) {
                        tat.setTexture("Tex1", tempGeom.getMaterial().getTextureParam("DiffuseMap").getTextureValue());
                        tat.getTextureParam("Tex1").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                        tat.setFloat("Tex1Scale", Float.valueOf(tempGeom.getMaterial().getParam("DiffuseMap_0_scale").getValueAsString()));
                    }
                    if (tempGeom.getMaterial().getTextureParam("DiffuseMap_1") != null) {
                        tat.setTexture("Tex2", tempGeom.getMaterial().getTextureParam("DiffuseMap_1").getTextureValue());
                        tat.getTextureParam("Tex2").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                        tat.setFloat("Tex2Scale", Float.valueOf(tempGeom.getMaterial().getParam("DiffuseMap_1_scale").getValueAsString()));
                    }
                    if (tempGeom.getMaterial().getTextureParam("DiffuseMap_2") != null) {
                        tat.setTexture("Tex3", tempGeom.getMaterial().getTextureParam("DiffuseMap_2").getTextureValue());
                        tat.getTextureParam("Tex3").getTextureValue().setWrap(Texture.WrapMode.Repeat);
                        tat.setFloat("Tex3Scale", Float.valueOf(tempGeom.getMaterial().getParam("DiffuseMap_2_scale").getValueAsString()));
                    }
                    tempGeom.setMaterial(tat);
                    return;
                } 
                
                // 普通物体
                if (tempGeom.getMaterial().getTextureParam("DiffuseMap") != null) {
                    Material mat = new Material(Ly.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                    // 防止一些OBJECT被处理掉透明样式
                    RenderState rs = tempGeom.getMaterial().getAdditionalRenderState();
                    if (rs != null) {
                        mat.getAdditionalRenderState().setAlphaFallOff(rs.getAlphaFallOff());
                        mat.getAdditionalRenderState().setAlphaTest(rs.isAlphaTest());
                        mat.getAdditionalRenderState().setBlendMode(rs.getBlendMode());
                        mat.getAdditionalRenderState().setColorWrite(rs.isColorWrite());
                        mat.getAdditionalRenderState().setDepthTest(rs.isDepthTest());
                        mat.getAdditionalRenderState().setDepthWrite(rs.isDepthWrite());
                        mat.getAdditionalRenderState().setFaceCullMode(rs.getFaceCullMode());
                        mat.getAdditionalRenderState().setPointSprite(rs.isPointSprite());
                        mat.getAdditionalRenderState().setPolyOffset(rs.getPolyOffsetFactor(), rs.getPolyOffsetUnits());
//                      mat.getAdditionalRenderState().setStencil(...);
                        mat.getAdditionalRenderState().setWireframe(rs.isWireframe());
                        mat.getAdditionalRenderState().setAlphaFallOff(rs.getAlphaFallOff());
                        // FIX树叶透明
                        mat.setFloat("AlphaDiscardThreshold", rs.getAlphaFallOff());
                    }

                    mat.setTexture("ColorMap", tempGeom.getMaterial().getTextureParam("DiffuseMap").getTextureValue());
                    tempGeom.setMaterial(mat);
                }
            }
        };
        node.depthFirstTraversal(sgv);
    }

    /**
     * 从spatial中找出所有geometry
     * @param spatial
     * @return 
     */
    public static List<Geometry> findAllGeometry(Spatial spatial) {
        return findAllGeometry(spatial, null);
    }
    
    public static List<Geometry> findAllGeometry(Spatial spatial, List<Geometry> store) {
        if (store == null) {
            store = new ArrayList<Geometry>();
        }
        final List<Geometry> results = store;
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    results.add((Geometry) spatial);
                }
            }
        };
        spatial.depthFirstTraversal(sgv);
        return results;
    }
    
    /**
     * 查找物体中的所有材质
     * @param spatial
     * @return 
     */
    public static List<Material> findAllMaterial(Spatial spatial) {
        final List<Material> results = new ArrayList<Material>();
        SceneGraphVisitor sgv = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial instanceof Geometry) {
                    Material material = ((Geometry) spatial).getMaterial();
                    if (material != null && !results.contains(material)) {
                        results.add(material);
                    }
                }
            }
        };
        spatial.depthFirstTraversal(sgv);
        return results;
    }
    
    /**
     * 获取包围盒的z向长度，
     * 1.如果是AABB则取ZExtent。
     * 2.如果是Sphere则取半径
     * 3.其它不支持，一率返回0
     * @param spatial
     * @return 
     */
    public static float getBoundingVolumeZExtent(Spatial spatial) {
        BoundingVolume bv = spatial.getWorldBound();
        float zExtent = 0;
        if (bv.getType() == BoundingVolume.Type.AABB) {
            zExtent = ((BoundingBox) bv).getZExtent();
        } else if (bv.getType() == BoundingVolume.Type.Sphere) {
            zExtent = ((BoundingSphere) bv).getRadius();
        } else {
            Logger.getLogger(GeometryUtils.class.getName())
                    .log(Level.WARNING, "Unsupported BoundingVolume type:{0}", bv.getType());
        }
        return zExtent;
    }
    
    /**
     * 获取模型的中点所在的世界坐标。该方法只是简单整合模型的原点及模板bound
     * 的中心点。即：将模型的世界坐标点的Y移动到modelBound所在的Y坐标值。
     * @param spatial
     * @param store
     * @return 
     */
    public static Vector3f getModelBoundCenter(Spatial spatial, Vector3f store) {
        store.set(spatial.getWorldTranslation()).setY(spatial.getWorldBound().getCenter().y);
        return store;
    }
 
    /**
     * 绑定骨骼将childBone添加到parentBone中
     * @param parent
     * @param child
     * @param parentBone
     * @param childBone 
     */
    public static void bindBone(Skeleton parent, Skeleton child, String parentBone, String childBone) {
        Bone pBone = parent.getBone(parentBone);
        Bone cBone = child.getBone(childBone);
        if (pBone != null && cBone != null && cBone.getParent() != pBone) {
            pBone.addChild(cBone);
            cBone.setUserControl(true);
        }
    }
    
    /**
     * 获得terrain的高位位置，并将结果存放到position返回
     * @param terrain
     * @param position 指定的地点
     * @return 
     */
    public static Vector3f getTerrainHeight(Spatial terrain, Vector3f position) {
        // 设置随机点的高度值
        TempVars tv = TempVars.get();
        Temp tp = Temp.get();
        Vector3f ori = tv.vect1.set(position).setY(999999);
        Vector3f dir = tv.vect2.set(position).setY(-999999).subtractLocal(ori);
        CollisionResults store = tp.results;
        store.clear();
        RayUtils.collideWith(ori, dir, terrain, store);
        CollisionResult result = store.getFarthestCollision();
        if (result != null) {
            position.set(result.getContactPoint());
        }
        tv.release();
        tp.release();
        return position;
    }
    
    /**
     * 获取Model的高度,主要是获取包围盒的高度
     * @param spatial
     * @return 
     */
    public static float getModelHeight(Spatial spatial) {
        float height = 0;
        BoundingVolume bv = spatial.getWorldBound();
        if (bv.getType() == Type.AABB) {
            BoundingBox bb = (BoundingBox) bv;
            height = bb.getYExtent() * 2;
        } else if (bv.getType() == Type.Sphere) {
            BoundingSphere bs = (BoundingSphere) bv;
            height = bs.getRadius() * 2;
        } else {
            logger.log(Level.WARNING, "Unsupported BoundingVolume type={0}", bv.getType());
        }
        return height;
    }
    
    /**
     * 获取一个对象的bound的顶部的世界位置，主要是计算bound的中心点世界坐标
     * 加上YExtend(box)或radius(sphere)即为该bound的top坐标。
     * @param spatial
     * @param store 存放结果(not null)
     * @return 
     */
    public static Vector3f getBoundTopPosition(Spatial spatial, Vector3f store) {
        BoundingVolume bv = spatial.getWorldBound();
        // 要注意：一些没有mesh的Spatial可能没有包围盒
        if (bv == null) {
            return store;
        }
        if (bv.getType() == Type.AABB) {
            BoundingBox bb = (BoundingBox) bv;
            store.set(bb.getCenter()).addLocal(0, bb.getYExtent(), 0);
        } else if (bv.getType() == Type.Sphere) {
            BoundingSphere bs = (BoundingSphere) bv;
            store.set(bs.getCenter()).addLocal(0, bs.getRadius(), 0);
        } else {
            logger.log(Level.WARNING, "Unsupported BoundingVolume type={0}", bv.getType());
        }
        return store;
    }
    
    /**
     * 将一个世界坐标点的位置转换为屏幕坐标位置。
     * @param worldPos
     * @param store (not null)
     * @return 
     */
    public static Vector3f convertWorldToScreen(Vector3f worldPos, Vector3f store) {
        Ly.getApp().getCamera().getScreenCoordinates(worldPos, store);
        return store;
    }
    
    /**
     * 给指定的spatial指定一个颜色，该方法主要改变spatial的material的Color
     * 属性，当没有该属性时会偿试添加一个，这要求该material的原形必须有一
     * 个"Color"属性的定义，该属性的verType必须是vector4.
     * @param spatial
     * @param color 
     */
    public static void setColor(Spatial spatial, final ColorRGBA color) {
        spatial.depthFirstTraversal(new SceneGraphVisitorAdapter() {
            @Override
            public void visit(Geometry geom) {
                // 存在Color属性时更改颜色。
                Material mat = geom.getMaterial();
                if (mat != null) {
                    MatParam colorParam = mat.getParam("Color");
                    if (colorParam != null && colorParam.getVarType() == VarType.Vector4) {
                        ((ColorRGBA)colorParam.getValue()).set(color);
                        return;
                    }
                    // 不存在Color颜色时先看是否有Color属性的定义，如果有则添加该属性。
                    MatParam colorDef = mat.getMaterialDef().getMaterialParam("Color");
                    if (colorDef != null && colorDef.getVarType() == VarType.Vector4) {
                        // 这里需要重新创建一个，否则会引用同一个实例。
                        mat.setColor("Color", new ColorRGBA(color));
                    }
                }
            }
        });
    }
    
    /**
     * 判断目标是否在摄像机视景体之内或者与视景体交叉
     * @param spatial
     * @return 
     */
    public static boolean intersectCamera(Spatial spatial) {
        Camera cam = Ly.getApp().getCamera();
        BoundingVolume bv = spatial.getWorldBound();
        int checkPlane = bv.getCheckPlane();
        bv.setCheckPlane(0);
        Camera.FrustumIntersect interset = cam.contains(bv);
        boolean result = interset == FrustumIntersect.Outside ? false : true;
        bv.setCheckPlane(checkPlane);
        return result;
    }
    
    /**
     * 将from中的所有动画添加到to身上,如果已经存在同名动画则替换之.注意:该方
     * 法只是简单将Animation从from添加到to上,而to仍然保留了所有Animation的
     * 引用,这时改变to的Animation可能会影响from身上添加的Animation
     * @param from 
     * @param to 
     */
    public static void addSkeletonAnim(Spatial from, Spatial to) {
        AnimControl acFrom = from.getControl(AnimControl.class);
        AnimControl acTo = to.getControl(AnimControl.class);
        if (acFrom == null || acTo == null) {
            logger.log(Level.WARNING, "from and to add need a AnimControl. from={0}, to={1}", new Object[] {from, to});
            return;
        }
        Collection<String> namesFrom = acFrom.getAnimationNames();
        if (namesFrom == null || namesFrom.isEmpty())
            return;
//        logger.log(Level.INFO, "Before add Animations:{0}", Arrays.toString(acTo.getAnimationNames().toArray()));
        for (String name : namesFrom) {
            Animation anim = acFrom.getAnim(name);
            acTo.addAnim(anim);
        }
//        logger.log(Level.INFO, "After add Animations:{0}", Arrays.toString(acTo.getAnimationNames().toArray()));
    }
    
    /**
     * 根据给定的顶点位置及变换来计算一个BoundingBox.
     * @param pts
     * @param transform 变换
     * @param store 存取最终的结果
     * @return 
     */
    public static BoundingBox computeBoundForPoints(Vector3f[] pts, Transform transform, BoundingBox store) {
        if (store == null) {
            store = new BoundingBox();
        }
        TempVars tv = TempVars.get();
        Vector3f min = tv.vect1.set(Vector3f.POSITIVE_INFINITY);
        Vector3f max = tv.vect2.set(Vector3f.NEGATIVE_INFINITY);
        Vector3f temp = tv.vect3;
        for (int i = 0; i < pts.length; i++) {
            transform.transformVector(pts[i], temp);
            min.minLocal(temp);
            max.maxLocal(temp);
        }
        Vector3f center = min.add(max).multLocal(0.5f);
        Vector3f extent = max.subtract(min).multLocal(0.5f);
        store.setCenter(center);
        store.setXExtent(extent.x);
        store.setYExtent(extent.y);
        store.setZExtent(extent.z);
        tv.release();
        return store;
    }
    
}






