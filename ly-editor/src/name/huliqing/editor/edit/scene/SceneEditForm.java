/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import com.jme3.math.Ray;
import com.jme3.util.SafeArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.SimpleEditForm;
import name.huliqing.editor.edit.select.EntitySelectObj;
import name.huliqing.editor.edit.select.SelectObjManager;
import name.huliqing.editor.select.SelectObj;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.scene.Scene;
import name.huliqing.luoying.object.scene.SceneListener;

/**
 *
 * @author huliqing
 */
public class SceneEditForm extends SimpleEditForm implements SceneListener {

    private static final Logger LOG = Logger.getLogger(SceneEditForm.class.getName());
    private final PlayService playService = Factory.get(PlayService.class);

    private Game game;
    private Scene scene;
    private final SafeArrayList<EntitySelectObj> entityObjs = new SafeArrayList<EntitySelectObj>(EntitySelectObj.class);
    
    public SceneEditForm() {}
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        setToolbar(new EditToolbar());
    }
    
    @Override
    public void cleanup() {
        if (game != null && game.isInitialized()) {
            game.cleanup();
            game = null;
        }
        getEditRoot().detachAllChildren();
        super.cleanup(); 
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public void setScene(Scene newScene) {
        if (game != null) {
            game.cleanup();
            game = null;
            entityObjs.clear();
        }
        scene = newScene;
        scene.addSceneListener(this);
        
        game = Loader.load(IdConstants.SYS_GAME);
        playService.registerGame(game); // game.initialize必须先调用
        
        game.setScene(scene);
        game.initialize(editor);
        getEditRoot().attachChild(game.getScene().getRoot());
    }

    public void addEntity(Entity entity) {
        if (scene != null) {
            scene.addEntity(entity);
        }
    }

    @Override
    public SelectObj doPick(Ray ray) {
        if (scene == null)
            return null;
        
        EntitySelectObj result = null;
        float minDistance = Float.MAX_VALUE;
        Float temp;
        for (EntitySelectObj seo : entityObjs.getArray()) {
            temp = seo.distanceOfPick(ray);
            if (temp != null && temp < minDistance) {
                minDistance = temp;
                result = seo;
            }
        }
        if (result != null) {
            LOG.log(Level.INFO, "doPick, selectObj={0}", result.getObject().getData().getId());
        }
        return result;
    }

    @Override
    public void onSceneLoaded(Scene scene) {} // ignore

    @Override
    public void onSceneEntityAdded(Scene scene, Entity entityAdded) {
        EntitySelectObj eso = SelectObjManager.createSelectObj(entityAdded.getData().getTagName());
        eso.setObject(entityAdded);
        entityObjs.add(eso);
        eso.initialize(this);
    }
    
    @Override
    public void onSceneEntityRemoved(Scene scene, Entity entityRemoved) {
        for (EntitySelectObj seo : entityObjs.getArray()) {
            if (seo.getObject().getData() == entityRemoved.getData()) {
                entityObjs.remove(seo);
                seo.cleanup();
                break;
            }
        }
    }
}
