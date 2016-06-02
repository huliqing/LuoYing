/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.fighter.object.position;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.util.clone.Cloner;
import java.io.IOException;

/**
 * 
 * @author huliqing
 */
public class EmitterShapeWrap implements EmitterShape {

    private Position inner;
    
    public EmitterShapeWrap(Position position) {
        inner = position;
    }
    
    @Override
    public void getRandomPoint(Vector3f store) {
        inner.getPoint(store);
    }

    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        inner.getPoint(store);
    }

    @Override
    public EmitterShape deepClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //-----------

    @Override
    public Object jmeClone() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
