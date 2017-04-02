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
package name.huliqing.editor.edit.terrain;

import com.jme3.terrain.heightmap.HillHeightMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import name.huliqing.editor.constants.ResConstants;
import name.huliqing.editor.manager.Manager;

/**
 *
 * @author huliqing
 */
public class HillPanel extends GridPane {

    private static final Logger LOG = Logger.getLogger(HillPanel.class.getName());
    
    private final BasePanel basePanel;
    
    public final Label iterationsLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_ITERATIONS));
    public final TextField iterationsField = new TextField("2000");
    
    public final Label seedLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_SEED));
    public final TextField flatteningField = new TextField("4");
    
    public final Label minRadiusLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_RADIUS_MIN));
    public final TextField minRadiusField = new TextField("20");
    
    public final Label maxRadiusLabel = new Label(Manager.getRes(ResConstants.FORM_CREATE_TERRAIN_TYPE_HILL_RADIUS_MAX));
    public final TextField maxRadiusField = new TextField("50");
    
    public HillPanel(BasePanel basePanel) {
        this.basePanel = basePanel;
        addRow(0, iterationsLabel, iterationsField, seedLabel, flatteningField);
        addRow(1, minRadiusLabel, minRadiusField, maxRadiusLabel, maxRadiusField);
        getColumnConstraints().add(new ColumnConstraints(110));
        GridPane.setHalignment(iterationsLabel, HPos.RIGHT);
        GridPane.setHalignment(seedLabel, HPos.RIGHT);
        GridPane.setHalignment(minRadiusLabel, HPos.RIGHT);
        GridPane.setHalignment(maxRadiusLabel, HPos.RIGHT);
        GridPane.setHgrow(seedLabel, Priority.ALWAYS);
        GridPane.setHgrow(maxRadiusLabel, Priority.ALWAYS);
        setPadding(new Insets(0, 10, 0, 0));
        setVgap(10);
    }
    
    public float[] getHeightMap() {
        try {
            int terrainTotalSize = new Integer(basePanel.totalSizeField.getText());
            int iterations = new Integer(iterationsField.getText());
            long seed = new Long(flatteningField.getText());
            float min = new Float(minRadiusField.getText());
            float max = new Float(maxRadiusField.getText());
            HillHeightMap heightMap = new HillHeightMap(terrainTotalSize, iterations, min, max, seed);
            return heightMap.getHeightMap();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "", ex);
        }
        return null;
    }
}
