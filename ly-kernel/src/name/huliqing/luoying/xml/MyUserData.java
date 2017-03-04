
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MyUserData implements Savable {

    /**
     * Boolean type on Geometries to indicate that physics collision
     * shape generation should ignore them.
     */
    public static final String JME_PHYSICSIGNORE = "JmePhysicsIgnore";

    /**
     * For geometries using shared mesh, this will specify the shared
     * mesh reference.
     */
    public static final String JME_SHAREDMESH    = "JmeSharedMesh";

    private static final int   TYPE_INTEGER      = 0;
    private static final int   TYPE_FLOAT        = 1;
    private static final int   TYPE_BOOLEAN      = 2;
    private static final int   TYPE_STRING       = 3;
    private static final int   TYPE_LONG         = 4;
    private static final int   TYPE_SAVABLE      = 5;
    private static final int   TYPE_LIST         = 6;
    private static final int   TYPE_MAP          = 7;
    private static final int   TYPE_ARRAY        = 8;

    protected byte             type;
    protected Object           value;

    public MyUserData() {
    }

    /**
     * Creates a new <code>UserData</code> with the given
     * type and value.
     * 
     * @param type
     *            Type of data, should be between 0 and 8.
     * @param value
     *            Value of the data
     */
    public MyUserData(byte type, Object value) {
        assert type >= 0 && type <= 8;
        this.type = type;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static byte getObjectType(Object type) {
        if (type instanceof Integer) {
            return TYPE_INTEGER;
        } else if (type instanceof Float) {
            return TYPE_FLOAT;
        } else if (type instanceof Boolean) {
            return TYPE_BOOLEAN;
        } else if (type instanceof String) {
            return TYPE_STRING;
        } else if (type instanceof Long) {
            return TYPE_LONG;
        } else if (type instanceof Savable) {
            return TYPE_SAVABLE;
        } else if (type instanceof List) {
            return TYPE_LIST;
        } else if (type instanceof Map) {
            return TYPE_MAP;
        } else if (type.getClass().isArray()) {
            return TYPE_ARRAY;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type.getClass().getName());
        }
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "type", (byte) 0);

        switch (type) {
            case TYPE_INTEGER:
                int i = (Integer) value;
                oc.write(i, "intVal", 0);
                break;
            case TYPE_FLOAT:
                float f = (Float) value;
                oc.write(f, "floatVal", 0f);
                break;
            case TYPE_BOOLEAN:
                boolean b = (Boolean) value;
                oc.write(b, "boolVal", false);
                break;
            case TYPE_STRING:
                String s = (String) value;
                oc.write(s, "strVal", null);
                break;
            case TYPE_LONG:
                Long l = (Long) value;
                oc.write(l, "longVal", 0l);
                break;
            case TYPE_SAVABLE:
                Savable sav = (Savable) value;
                oc.write(sav, "savableVal", null);
                break;
            case TYPE_LIST:
                this.writeList(oc, (List<?>) value, "0");
                break;
            case TYPE_MAP:
                Map<?, ?> map = (Map<?, ?>) value;
                this.writeList(oc, map.keySet(), "0");
                this.writeList(oc, map.values(), "1");
                break;
            case TYPE_ARRAY:
                this.writeList(oc, asList((Object[]) value), "0");
                break;
            default:
                throw new UnsupportedOperationException("Unsupported value type: " + value.getClass());
        }
    }
    
    private List asList(Object[] obj) {
        List list = new ArrayList();
        for (Object o : obj) {
            list.add(o);
        }
        return list;
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        type = ic.readByte("type", (byte) 0);
        switch (type) {
            case TYPE_INTEGER:
                value = ic.readInt("intVal", 0);
                break;
            case TYPE_FLOAT:
                value = ic.readFloat("floatVal", 0f);
                break;
            case TYPE_BOOLEAN:
                value = ic.readBoolean("boolVal", false);
                break;
            case TYPE_STRING:
                value = ic.readString("strVal", null);
                break;
            case TYPE_LONG:
                value = ic.readLong("longVal", 0l);
                break;
            case TYPE_SAVABLE:
                value = ic.readSavable("savableVal", null);
                break;
            case TYPE_LIST:
                value = this.readList(ic, "0");
                break;
            case TYPE_MAP:
                Map<Object, Object> map = new HashMap<Object, Object>();
                List<?> keys = this.readList(ic, "0");
                List<?> values = this.readList(ic, "1");
                for (int i = 0; i < keys.size(); ++i) {
                    map.put(keys.get(i), values.get(i));
                }
                value = map;
                break;
            case TYPE_ARRAY:
                value = this.readList(ic, "0").toArray();
                break;
            default:
                throw new UnsupportedOperationException("Unknown type of stored data: " + type);
        }
    }

    /**
     * The method stores a list in the capsule.
     * @param oc
     *            output capsule
     * @param list
     *            the list to be stored
     * @throws IOException
     */
    private void writeList(OutputCapsule oc, Collection<?> list, String listName) throws IOException {
        if (list != null) {
            oc.write(list.size(), listName + "size", 0);
            int counter = 0;
            for (Object o : list) {
                // t is for 'type'; v is for 'value'
                if (o instanceof Integer) {
                    oc.write(TYPE_INTEGER, listName + "t" + counter, 0);
                    oc.write((Integer) o, listName + "v" + counter, 0);
                } else if (o instanceof Float) {
                    oc.write(TYPE_FLOAT, listName + "t" + counter, 0);
                    oc.write((Float) o, listName + "v" + counter, 0f);
                } else if (o instanceof Boolean) {
                    oc.write(TYPE_BOOLEAN, listName + "t" + counter, 0);
                    oc.write((Boolean) o, listName + "v" + counter, false);
                } else if (o instanceof String || o == null) {// treat null's like Strings just to store them and keep the List like the user intended
                    oc.write(TYPE_STRING, listName + "t" + counter, 0);
                    oc.write((String) o, listName + "v" + counter, null);
                } else if (o instanceof Long) {
                    oc.write(TYPE_LONG, listName + "t" + counter, 0);
                    oc.write((Long) o, listName + "v" + counter, 0L);
                } else if (o instanceof Savable) {
                    oc.write(TYPE_SAVABLE, listName + "t" + counter, 0);
                    oc.write((Savable) o, listName + "v" + counter, null);
                } else if(o instanceof Object[]) {
                    oc.write(TYPE_ARRAY, listName + "t" + counter, 0);
                    this.writeList(oc, Arrays.asList((Object[]) o), listName + "v" + counter);
                } else if(o instanceof List) {
                    oc.write(TYPE_LIST, listName + "t" + counter, 0);
                    this.writeList(oc, (List<?>) o, listName + "v" + counter);
                } else if(o instanceof Map) {
                    oc.write(TYPE_MAP, listName + "t" + counter, 0);
                    Map<?, ?> map = (Map<?, ?>) o;
                    this.writeList(oc, map.keySet(), listName + "v(keys)" + counter);
                    this.writeList(oc, map.values(), listName + "v(vals)" + counter);
                } else {
                    throw new UnsupportedOperationException("Unsupported type stored in the list: " + o.getClass());
                }
                
                ++counter;
            }
        } else {
            oc.write(0, "size", 0);
        }
    }

    /**
     * The method loads a list from the given input capsule.
     * @param ic
     *            the input capsule
     * @return loaded list (an empty list in case its size is 0)
     * @throws IOException
     */
    private List<?> readList(InputCapsule ic, String listName) throws IOException {
        int size = ic.readInt(listName + "size", 0);
        List<Object> list = new ArrayList<Object>(size);
        for (int i = 0; i < size; ++i) {
            int type = ic.readInt(listName + "t" + i, 0);
            switch (type) {
                case TYPE_INTEGER:
                    list.add(ic.readInt(listName + "v" + i, 0));
                    break;
                case TYPE_FLOAT:
                    list.add(ic.readFloat(listName + "v" + i, 0));
                    break;
                case TYPE_BOOLEAN:
                    list.add(ic.readBoolean(listName + "v" + i, false));
                    break;
                case TYPE_STRING:
                    list.add(ic.readString(listName + "v" + i, null));
                    break;
                case TYPE_LONG:
                    list.add(ic.readLong(listName + "v" + i, 0L));
                    break;
                case TYPE_SAVABLE:
                    list.add(ic.readSavable(listName + "v" + i, null));
                    break;
                case TYPE_ARRAY:
                    list.add(this.readList(ic, listName + "v" + i).toArray());
                    break;
                case TYPE_LIST:
                    list.add(this.readList(ic, listName + "v" + i));
                    break;
                case TYPE_MAP:
                    Map<Object, Object> map = new HashMap<Object, Object>();
                    List<?> keys = this.readList(ic, listName + "v(keys)" + i);
                    List<?> values = this.readList(ic, listName + "v(vals)" + i);
                    for (int j = 0; j < keys.size(); ++j) {
                        map.put(keys.get(j), values.get(j));
                    }
                    list.add(map);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown type of stored data in a list: " + type);
            }
        }
        return list;
    }
}