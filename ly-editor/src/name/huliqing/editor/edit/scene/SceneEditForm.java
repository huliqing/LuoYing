/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor.edit.scene;

import name.huliqing.editor.Editor;
import name.huliqing.editor.edit.SimpleEditForm;
import name.huliqing.editor.toolbar.EditToolbar;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.IdConstants;
import name.huliqing.luoying.layer.service.PlayService;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.game.Game;
import name.huliqing.luoying.object.scene.Scene;

/**
 *
 * @author huliqing
 */
public class SceneEditForm extends SimpleEditForm {
    private final PlayService playService = Factory.get(PlayService.class);

    private Game game;
    private Scene editScene;
    private String editSceneId;
    
    public SceneEditForm() {}
    
    @Override
    public void initialize(Editor editor) {
        super.initialize(editor);
        if (editSceneId == null) {
            return;
        }
        editScene = Loader.load(editSceneId);
        game = Loader.load(IdConstants.SYS_GAME);
        game.setScene(editScene);
        game.initialize(editor);
        
        playService.registerGame(game);
        
        getEditRoot().attachChild(game.getScene().getRoot());
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
    
    public void setScene(String sceneId) {
        this.editSceneId = sceneId;
    }

}
