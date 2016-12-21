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
package name.huliqing.luoying.object.effect;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.TempVars;
import name.huliqing.luoying.LuoYing;
import name.huliqing.luoying.constants.AssetConstants;
import name.huliqing.luoying.data.EffectData;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.position.Position;
import name.huliqing.luoying.shape.MySpline;
import name.huliqing.luoying.shape.SplineSurface;

/**
 * 滑动的颜色特效,设置两个材质: mask和tex
 * tex会在mask下不停的滑动，从Mesh的一端进入，然后从另一端出去。
 * @author huliqing
 */
public class SlideColorIOSplineEffect extends Effect {
    private ColorRGBA startColor = new ColorRGBA(1, 1, 1, 1);
    private ColorRGBA endColor = new ColorRGBA(1, 1, 1, 1);
    
    private String mask = AssetConstants.TEXTURE_MASK;
    private String tex = AssetConstants.TEXTURE_PARTICLES;
    private final boolean texAnimY = true;
    private boolean texChangeDir;
    
    // 路径点，注：fixedPoints和randomPoints必须选择一个
    // fixedPoints是固定点
    // randomPoints是动态点，指向的是Position.xml的id.
    private Vector3f[] fixedPoints;
    // 绑定位置点Position
    private String[] randomPoints;
    private float width = 1;
    // 至少30个分段，否则转折太明显
    private int segments = 30;
    private float tension = 0.5f;
    private boolean cycle;
    private Vector3f up = Vector3f.UNIT_Z;
    // 维度
    private int dimension = 1;
    // 循环展示多少次动画
    private float repeatTimes = 1.0f;    
    
    // ---- inner
    private Spatial animObj;
    private Material mat;
//    private float fullUseTime;

    @Override
    public void setData(EffectData data) {
        super.setData(data); 
        startColor = data.getAsColor("startColor", startColor);
        endColor = data.getAsColor("endColor", endColor);
        
        mask = data.getAsString("mask", mask);
        tex = data.getAsString("tex", tex);
//        texAnimY = data.getAsBoolean("texAnimY", texAnimY); // 暂不作为参数，默认true.
        texChangeDir = data.getAsBoolean("texChangeDir", texChangeDir);
        
        fixedPoints = data.getAsVector3fArray("fixedPoints");
        randomPoints = data.getAsArray("randomPoints");
        width = data.getAsFloat("width", width);
        segments = data.getAsInteger("segments", segments);
        tension = data.getAsFloat("tension", tension);
        cycle = data.getAsBoolean("cycle", cycle);
        up = data.getAsVector3f("up", up).normalizeLocal();
        dimension = data.getAsInteger("dimension", dimension);
        repeatTimes = data.getAsFloat("repeatTimes", repeatTimes);
        if (fixedPoints == null && randomPoints == null)
            throw new NullPointerException("need fixedPoints or randomPoints");
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        create();
    }
    
    @Override
    protected void effectUpdate(float tpf) {
        super.effectUpdate(tpf);
        float inter;
        if (repeatTimes > 1) {
            inter = timeUsed / trueTimeTotal * repeatTimes;
            inter %= 1.0f;
        } else {
            inter = timeUsed / trueTimeTotal;
        }
        mat.setFloat("offsetY", FastMath.interpolateLinear(inter, -1f, 1f));
    }
    
    private void create() {
        if (mat == null) {
            AssetManager am = LuoYing.getAssetManager();
            mat = new Material(am, AssetConstants.MATERIAL_SLIDE_COLOR_IO);
            mat.setColor("StartColor", startColor);
            mat.setColor("EndColor", endColor);

            Texture maskMap = am.loadTexture(mask);
            mat.setTexture("MaskMap", maskMap);

            Texture texMap = am.loadTexture(tex);
            mat.setTexture("TexMap", texMap);
            mat.setBoolean("TexAnimY", texAnimY);
            mat.setBoolean("TexChangeDir", texChangeDir);
            mat.setFloat("offsetY", -1.0f);

            mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off); // Allow to see both sides of a face
            mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Additive);
            mat.getAdditionalRenderState().setColorWrite(true);
            mat.getAdditionalRenderState().setDepthTest(true);
            mat.getAdditionalRenderState().setDepthWrite(false);
        }
        if (animObj == null) {
            animObj = loadAnimModel();
            animObj.setMaterial(mat);
            animNode.attachChild(animObj);
            
//            // for debug
//            Material debugMat = new Material(Common.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//            debugMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
//            debugMat.getAdditionalRenderState().setWireframe(true);
//            Spatial debugObj = (animObj instanceof Geometry) ? animObj.clone() : ((Node) animObj).getChild(0).clone();
//            debugObj.setMaterial(debugMat);
//            attachChild(debugObj);
        }
    }
    
    private Spatial loadAnimModel() {
        // 生成路径点
        if (fixedPoints == null) {
            fixedPoints = new Vector3f[randomPoints.length];
            for (int i = 0; i < randomPoints.length; i++) {
                Vector3f point = new Vector3f();
                ((Position)Loader.load(randomPoints[i])).getPoint(point);
                fixedPoints[i] = point;
            }
        }
        MySpline spline = createSpline(fixedPoints, tension, cycle);
        if(dimension > 1) {
            // 计算出实际的维度旋转轴
            TempVars tv = TempVars.get();
            Vector3f nextPoint = tv.vect1;
            Vector3f forward = tv.vect2;
            Vector3f left = tv.vect3;
            Vector3f rotAxis = tv.vect4;
            spline.getSplinePoint(spline.getTotalLength() / segments, nextPoint);
            nextPoint.subtract(spline.getControlPoints().get(0), forward).normalizeLocal();
            up.cross(forward, left).normalizeLocal();
            left.cross(up, rotAxis).normalizeLocal();
            
            Quaternion qua = new Quaternion();
            Vector3f tempUp = tv.vect5;
            float angle = FastMath.PI / dimension;
            Node animNode = new Node();
            for (int i = 0; i < dimension; i++) {
                qua.fromAngleAxis(angle * i, rotAxis);
                qua.mult(up, tempUp);
                animNode.attachChild(createSurface(spline, tempUp));
            }
            tv.release();
            return animNode;
        } else {
            return createSurface(spline, up);
        }
    }
    
    private Geometry createSurface(MySpline spline, Vector3f up) {
        SplineSurface surface = new SplineSurface(spline, width, segments, up);
        Geometry geo = new Geometry("SplineSurface", surface);
        return geo;
    }
    
    private MySpline createSpline(Vector3f[] waypoints, float tension, boolean cycle) {
        MySpline spline = new MySpline();
        for (int i = 0; i < waypoints.length - 1; i++) {
            spline.getControlPoints().add(waypoints[i]);
        }
        spline.addControlPoint(waypoints[waypoints.length - 1]);
        spline.setCurveTension(tension);
        spline.setCycle(cycle);
        return spline;
    }
}
