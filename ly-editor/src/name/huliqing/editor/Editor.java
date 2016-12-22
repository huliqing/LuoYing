/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.editor;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import name.huliqing.editor.tiles.Coord;
import name.huliqing.editor.tiles.Grid;

/**
 *
 * @author huliqing
 */
public class Editor extends SimpleApplication{
    
    private static Application app;
    private Grid grid;
    private Coord coord;
    private EditorCamera editorCam;
    
    public final static Application getApp() {
        return app;
    }

    @Override
    public void simpleInitApp() {
        app = this;
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        app.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);
        
        grid = new Grid();
        coord = new Coord();
        rootNode.attachChild(grid);
        rootNode.attachChild(coord);
        
        editorCam = new EditorCamera(getCamera(), inputManager);
        editorCam.setChase(coord);
    }
}
