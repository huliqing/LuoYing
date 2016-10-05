/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.utils.modifier;

import com.jme3.animation.AnimControl;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.ly.Ly;
import name.huliqing.ly.utils.GeometryUtils;
import name.huliqing.ly.utils.ModelFileUtils;

/**
 *  
 * @author huliqing
 */
public class OutfitUtils {
    private final static Logger logger = Logger.getLogger(OutfitUtils.class.getName());
    private final static String REDIRECT_BONE_INDEX_OK = "redirect_bone_index_ok";
    private final static String RIG_SKE_PATH = "Models/actor/ske.mesh.j3o";
    
    public static void redirectBoneIndex(String outfitFile, String rigSkeFile) {
        AssetManager am = Ly.getAssetManager();
        Spatial outfit = am.loadModel(outfitFile);
        if (outfit.getUserData(REDIRECT_BONE_INDEX_OK) != null) {
            logger.log(Level.WARNING, "Outfit name={0} has already Redirect bone index!", outfit.getName());
            return;
        }
        AnimControl outfitAC = outfit.getControl(AnimControl.class);
        SkeletonControl outfitSC = outfit.getControl(SkeletonControl.class);
        if (outfitSC == null) {
            return;
        }
        // 移除control
        outfit.removeControl(outfitAC);
        outfit.removeControl(outfitSC);
        
        // 重定向boneIndex
        Skeleton rigSke = am.loadModel(rigSkeFile).getControl(SkeletonControl.class).getSkeleton();
        // skin中可能存在多个Geometry,每一个都要进行处理.
        List<Geometry> geos = GeometryUtils.findAllGeometry(outfit);
        Skeleton outfitSke = outfitSC.getSkeleton();  // skin中的骨骼(已丢弃)
        for (Geometry geo : geos) {
            // 找到Mesh中的骨骼索引
            // 这里需要检测并初始化一次就可以, 不能重复做,即使skin是重新load进来的
            // 因为geometry或mesh可能进行了缓存,所以即使重新Loader.loadSkin(),可能
            // 载入的对象仍然引用相同的mesh.所以这里需要通过判断,避免对skin mesh
            // 中的骨骼索引重定向多次,只有第一次是正确的,第二次及后续一定错误,因为数据覆盖了.
            Mesh mesh = geo.getMesh();
            logger.log(Level.INFO, "==MaxNumWeights={0}", mesh.getMaxNumWeights());
            VertexBuffer indices = mesh.getBuffer(VertexBuffer.Type.BoneIndex);
            if (!indices.getData().hasArray()) {
                // Prepares the mesh for software skinning by converting the bone index and weight buffers to heap buffers. 
                // 另参考: SkeletonControl => void resetToBind() 
                mesh.prepareForAnim(true);
            }
            
            // 重定向
            ByteBuffer ib = (ByteBuffer) indices.getData();
            ib.rewind();
            byte[] fib = ib.array();
            for (int i = 0; i < fib.length; i++) {
                int bIndex = fib[i] & 0xff; // bIndex是skin中子骨骼

                // 这里一般不会发生, 除非做了第二次骨骼索引的重定向,
                // 否则skin中的初始骨骼索引不可能会大于或等于它的骨骼数(最大索引为BoneCount-1)
                if (bIndex >= outfitSke.getBoneCount()) {
                    logger.log(Level.WARNING, "SkinSke bone index big than boneCount, bIndex={0}, totalBone={1}"
                            , new Object[] {bIndex, outfitSke.getBoneCount()});
                    continue;
                }

                String boneName = outfitSke.getBone(bIndex).getName();
                // 从父骨骼中找出与skin中当前骨头相同名称的骨头.
                int rootBoneIndex = rigSke.getBoneIndex(boneName);
                if (rootBoneIndex != -1) {

                    logger.log(Level.INFO, "update bone index, skin={0}, index update: {1} to {2}"
                            , new Object[]{outfit.getName(), fib[i], rootBoneIndex});

                    fib[i] = (byte) rootBoneIndex;
                } else {
                    // 如果skinNode中的骨骼没有在父骨骼中找到,则随便直接绑定到父骨骼的根节点中.
                    // 出现这种情况主要是skin中存在额外的骨骼,这个骨头不知道要绑定到哪里?!!?
                    fib[i] = 0;
                    logger.log(Level.WARNING, "SkinSke found a extra bone, but not know where to bind to! boneName={0}"
                            , boneName);
                }
            }
            indices.updateData(ib);
        }
        outfit.setUserData(REDIRECT_BONE_INDEX_OK, "1");
        ModelFileUtils.saveTo(outfit, outfitFile);
    }
    
//    1.注意:如果skin中存在骨骼及动画信息,这些信息
//    将被丢弃,相应的骨骼索引信息会重定向到actor中的主骨骼上.这要求skinNode
//    骨骼中的所有骨头的名称都应该与主rig中骨骼中的骨头名称相匹配.
    
//    2.需要重定向skin中的骨骼索引,这里稍微复杂,需要详细记住:
//    比如:主骨骼存在 root - hips - hand 这三个骨头, 而skin中的骨骼可能只有
//    hand这个骨头, 为了把skin绑定到主骨骼中,也就是是将skin中的mesh的控制权
//    从skin自身的hand骨头交由主骨骼中的hand骨头来控制,这样skin就不需要AnimControl
//    和SkeletonControl.
//    因为skin自身的mesh中包含有权重和骨骼索引的信息.
//    但是因为skin自身的骨骼索引和主骨骼索引是不一致的,如上:主骨骼中的hand
//    的索引为2, 而skin因为hand只有一个骨头,索引为0, 所以在丢弃了skin中的
//    AnimControl和SkeletonControl之后,需要把skin中hand的骨头索引重定向到
//    主骨骼中的hand的索引.而权重信息不需要改变,也就是如上:skin中mesh的关于
//    hand的骨骼索引需要从0重定向到主骨骼中的hand索引2. 
//    这样主骨骼中的SkeletonControl在控制skin执行变换的时候才能正确从主骨骼
//    中找到hand的骨头,(否则找到的就是root了).
//    关于骨骼与Mesh的具体逻辑可参考: SkeletonControl.controlRenderSoftware
    public static void main(String[] args) {
//        重要：模型（j3o）只需要重定向一次就可以了，不能多次重定向，除非模型修改后重新转换成j3o.
//        redirectBoneIndex("Models/actor/female/eye.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/face.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/foot.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.000.mesh.j3o", RIG_SKE_PATH);
        
//        redirectBoneIndex("Models/actor/female/foot.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.001.mesh.j3o", RIG_SKE_PATH);
        
//        redirectBoneIndex("Models/actor/female/foot.002.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.002.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.002.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.002.mesh.j3o", RIG_SKE_PATH);
//        
//        redirectBoneIndex("Models/actor/female/foot.003.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.003.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.003.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.003.mesh.j3o", RIG_SKE_PATH);
//        
//        redirectBoneIndex("Models/actor/female/foot.004.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.004.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.004.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.004.mesh.j3o", RIG_SKE_PATH);     
//        
//        redirectBoneIndex("Models/actor/female/foot.005.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.005.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/lowerBody.005.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.005.mesh.j3o", RIG_SKE_PATH);
//        
//        redirectBoneIndex("Models/actor/female/foot.006.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/hand.006.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/female/upperBody.006.mesh.j3o", RIG_SKE_PATH);
        
        // ---- Male
//        redirectBoneIndex("Models/actor/male/foot.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/hand.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/lowerBody.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/upperBody.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/face.000.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/hair.000.mesh.j3o", RIG_SKE_PATH);
//        
//        redirectBoneIndex("Models/actor/male/foot.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/hand.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/lowerBody.001.mesh.j3o", RIG_SKE_PATH);
//        redirectBoneIndex("Models/actor/male/upperBody.001.mesh.j3o", RIG_SKE_PATH);
    }
    
    
}
