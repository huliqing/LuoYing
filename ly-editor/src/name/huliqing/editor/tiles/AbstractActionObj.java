/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.tiles;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import name.huliqing.editor.Editor;

/**
 *
 * @author huliqing
 */
public abstract class AbstractActionObj extends Node implements ActionObj {

    private final Ray ray = new Ray();
    private final Vector2f tempPick2d = new Vector2f();
    private final Vector3f tempPick3d = new Vector3f();
    
    private boolean actionStarted;
    
    public AbstractActionObj() {}

    @Override
    public boolean isVisible() {
        return getCullHint() == CullHint.Never;
    }

    @Override
    public void setVisible(boolean visible) {
        setCullHint(visible ? CullHint.Never : CullHint.Always);
    }

    @Override
    public void onActionStart() {
        actionStarted = true;
        if (!isVisible()) {
            return;
        }
        BoundingVolume bv = getWorldBound();
        if (bv == null) {
            return;
        }
        getPickRay();
        if (!bv.intersects(ray)) {
            return;
        }
        doAction(ray);
    }

    @Override
    public void onActionEnd() {
        actionStarted = false;
    }
    
    /**
     * 获取点击的射线
     * @return 
     */
    private Ray getPickRay() {
        tempPick2d.set(Editor.getEditor().getInputManager().getCursorPosition());
        Editor.getEditor().getCamera().getWorldCoordinates(tempPick2d, 0, tempPick3d);
        // 向量必须归一化
        Vector3f dir = Editor.getEditor().getCamera().getWorldCoordinates(tempPick2d, 1)
                .subtract(tempPick3d).normalizeLocal();
        ray.setOrigin(tempPick3d);
        ray.setDirection(dir);
        return ray;
    }
    
    /**
     * 执行行为
     * @param ray 
     */
    protected abstract void doAction(Ray ray);
}
