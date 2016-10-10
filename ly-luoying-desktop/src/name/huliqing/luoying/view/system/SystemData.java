/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.view.system;

/**
 *
 * @author huliqing
 */
public class SystemData {
    
    private String name;
    private String des;
    
    public SystemData() {
    }

    public SystemData(String name, String des) {
        this.name = name;
        this.des = des;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
    
}
