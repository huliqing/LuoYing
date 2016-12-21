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
package name.huliqing.ly.test;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import name.huliqing.luoying.object.Loader;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.ly.Init;

/**
 *
 * @author huliqing
 */
public class EmitterAndFilter extends SimpleApplication {

    public static void main(String[] args) {
        EmitterAndFilter app = new EmitterAndFilter();
        app.setShowSettings(false);
        app.start();
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (keyPressed) return;
            if (name.equals("do") && !keyPressed) {
                Entity effect = Loader.load("effectTonic");
                rootNode.attachChild(effect.getSpatial());
            } 
        }
    };
    
    @Override
    public void simpleInitApp() {
        Init.initialize(this);
        this.flyCam.setMoveSpeed(20);
        inputManager.addMapping("do", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "do");
        
        Entity sky = Loader.load("envSky");
        rootNode.attachChild(sky.getSpatial());
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-1, -1, -1));
        this.rootNode.addLight(dl);
        
        FilterPostProcessor processor = new FilterPostProcessor(this.assetManager);
        this.viewPort.addProcessor(processor);
        
        DirectionalLightShadowFilter filter = new DirectionalLightShadowFilter(assetManager, 1024, 1);
        filter.setLambda(0.55f);
        filter.setShadowIntensity(0.75f);
        filter.setShadowCompareMode(CompareMode.Hardware);
        filter.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        filter.setLight(dl);
        processor.addFilter(filter);
        
        Spatial terrain = this.assetManager.loadModel("Models/env/terrain/scene.j3o");
        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
        this.rootNode.attachChild(terrain);
        
        Spatial tree = this.assetManager.loadModel("Models/trees/tree/tree978.j3o");
        tree.setShadowMode(RenderQueue.ShadowMode.Cast);
        this.rootNode.attachChild(tree);
    }
    
}
