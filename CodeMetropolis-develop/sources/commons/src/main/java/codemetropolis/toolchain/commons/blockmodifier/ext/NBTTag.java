package codemetropolis.toolchain.commons.blockmodifier.ext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NBTTag {
    private final Type type;
    private Type listType = null;
    private final String name;
    private Object value;

    public enum Type {
        TAG_End,
        TAG_Byte,
        TAG_Short,
        TAG_Int,
        TAG_Long,
        TAG_Float,
        TAG_Double,
        TAG_Byte_Array,
        TAG_String,
        TAG_List,
        TAG_Compound,
        TAG_Int_Array
    }

    /**
     * Create a new TAG_List or TAG_Compound NBT tag.
     *
     * @param type either TAG_List or TAG_Compound
     * @param name name for the new tag or null to create an unnamed tag.
     * @param value list of tags to add to the new tag.
     */
    public NBTTag(Type type, String name, NBTTag[] value) {
        this(type, name, (Object) value);
    }

    /**
     * Create a new TAG_List with an empty list. Use {@link NBTTag#addTag(NBTTag)} to add tags later.
     *
     * @param name name for this tag or null to create an unnamed tag.
     * @param listType type of the elements in this empty list.
     */
    public NBTTag(String name, Type listType) {
        this(Type.TAG_List, name, listType);
    }

    /**
     * Create a new NBT tag.
     *
     * @param type any value from the {@link Type} enum.
     * @param name name for the new tag or null to create an unnamed tag.
     * @param value an object that fits the tag type or a {@link Type} to create an empty TAG_List with this list type.
     */
    public NBTTag(Type type, String name, Object value) {
        switch (type) {
        case TAG_End:
            if (value != null)
                throw new IllegalArgumentException();
            break;
        case TAG_Byte:
            if (!(value instanceof Byte))
                throw new IllegalArgumentException();
            break;
        case TAG_Short:
            if (!(value instanceof Short))
                throw new IllegalArgumentException();
            break;
        case TAG_Int:
            if (!(value instanceof Integer))
                throw new IllegalArgumentException();
            break;
        case TAG_Long:
            if (!(value instanceof Long))
                throw new IllegalArgumentException();
            break;
        case TAG_Float:
            if (!(value instanceof Float))
                throw new IllegalArgumentException();
            break;
        case TAG_Double:
            if (!(value instanceof Double))
                throw new IllegalArgumentException();
            break;
        case TAG_Byte_Array:
            if (!(value instanceof byte[]))
                throw new IllegalArgumentException();
            break;
        case TAG_String:
            if (!(value instanceof String))
                throw new IllegalArgumentException();
            break;
        case TAG_List:
            if (value instanceof Type) {
                this.listType = (Type) value;
                value = new NBTTag[0];
            } else {
                if (!(value instanceof NBTTag[]))
                    throw new IllegalArgumentException();
                this.listType = (((NBTTag[]) value)[0]).getType();
            }
            break;
        case TAG_Compound:
            if (!(value instanceof NBTTag[]))
                throw new IllegalArgumentException();
            break;
        case TAG_Int_Array:
            if (!(value instanceof int[]))
                throw new IllegalArgumentException();
            break;
        default:
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object newValue)
    {
        switch (type) {
        case TAG_End:
            if (value != null)
                throw new IllegalArgumentException();
            break;
        case TAG_Byte:
            if (!(value instanceof Byte))
                throw new IllegalArgumentException();
            break;
        case TAG_Short:
            if (!(value instanceof Short))
                throw new IllegalArgumentException();
            break;
        case TAG_Int:
            if (!(value instanceof Integer))
                throw new IllegalArgumentException();
            break;
        case TAG_Long:
            if (!(value instanceof Long))
                throw new IllegalArgumentException();
            break;
        case TAG_Float:
            if (!(value instanceof Float))
                throw new IllegalArgumentException();
            break;
        case TAG_Double:
            if (!(value instanceof Double))
                throw new IllegalArgumentException();
            break;
        case TAG_Byte_Array:
            if (!(value instanceof byte[]))
                throw new IllegalArgumentException();
            break;
        case TAG_String:
            if (!(value instanceof String))
                throw new IllegalArgumentException();
            break;
        case TAG_List:
            if (value instanceof Type) {
                this.listType = (Type) value;
                value = new NBTTag[0];
            } else {
                if (!(value instanceof NBTTag[]))
                    throw new IllegalArgumentException();
                this.listType = (((NBTTag[]) value)[0]).getType();
            }
            break;
        case TAG_Compound:
            if (!(value instanceof NBTTag[]))
                throw new IllegalArgumentException();
            break;
        case TAG_Int_Array:
            if (!(value instanceof int[]))
                throw new IllegalArgumentException();
            break;
        default:
            throw new IllegalArgumentException();
        }

        value = newValue;
    }
	
	public void clearList() {
		value = new NBTTag[0];
	}

    public Type getListType() {
        return listType;
    }

    /**
     * Add a tag to a TAG_List or a TAG_Compound.
     */
    public void addTag(NBTTag tag) {
        if (type != Type.TAG_List && type != Type.TAG_Compound)
            throw new RuntimeException();
        NBTTag[] subtags = (NBTTag[]) value;

        int index = subtags.length;

        //For TAG_Compund entries, we need to add the tag BEFORE the end,
        //or the new tag gets placed after the TAG_End, messing up the data.
        //TAG_End MUST be kept at the very end of the TAG_Compound.
        if(type == Type.TAG_Compound) index--;
        insertTag(tag, index);
    }

    /**
     * Add a tag to a TAG_List or a TAG_Compound at the specified index.
     */
    public void insertTag(NBTTag tag, int index) {
        if (type != Type.TAG_List && type != Type.TAG_Compound)
            throw new RuntimeException();
        NBTTag[] subtags = (NBTTag[]) value;
        if (subtags.length > 0) {
            if (type == Type.TAG_List && tag.getType() != getListType()) {
                throw new IllegalArgumentException();
			}
		} else {
			if (type == Type.TAG_List) {
				listType = tag.getType(); // qqDPS
			}
		}
        if (index > subtags.length)
            throw new IndexOutOfBoundsException();
        NBTTag[] newValue = new NBTTag[subtags.length + 1];
        System.arraycopy(subtags, 0, newValue, 0, index);
        newValue[index] = tag;
        System.arraycopy(subtags, index, newValue, index + 1, subtags.length - index);
        value = newValue;
    }

    /**
     * Remove a tag from a TAG_List or a TAG_Compound at the specified index.
     *
     * @return the removed tag
     */
    public NBTTag removeTag(int index) {
        if (type != Type.TAG_List && type != Type.TAG_Compound)
            throw new RuntimeException();
        NBTTag[] subtags = (NBTTag[]) value;
        NBTTag victim = subtags[index];
        NBTTag[] newValue = new NBTTag[subtags.length - 1];
        System.arraycopy(subtags, 0, newValue, 0, index);
        index++;
        System.arraycopy(subtags, index, newValue, index - 1, subtags.length - index);
        value = newValue;
        return victim;
    }

    /**
     * Remove a tag from a TAG_List or a TAG_Compound. If the tag is not a child of this tag then nested tags are searched.
     *
     * @param tag tag to look for
     */
    public void removeSubtag(NBTTag tag) {
        if (type != Type.TAG_List && type != Type.TAG_Compound)
            throw new RuntimeException();
        if (tag == null)
            return;
        NBTTag[] subtags = (NBTTag[]) value;
        for (int i = 0; i < subtags.length; i++) {
            if (subtags[i] == tag) {
                removeTag(i);
                return;
            } else {
                if (subtags[i].type == Type.TAG_List || subtags[i].type == Type.TAG_Compound) {
                    subtags[i].removeSubtag(tag);
                }
            }
        }
    }

    /**
     * Find the first nested tag with specified name in a TAG_Compound.
     *
     * @param name the name to look for. May be null to look for unnamed tags.
     * @return the first nested tag that has the specified name.
     */
    
    public NBTTag getSubtagByName(String name) {
        for(NBTTag t : getSubtags()) {
        	if(name == null || name.equals("")) {
        		if(t.getName() == null || t.getName().equals("")) return t;
        	} else {
        		if(t.getName() != null && t.getName().equals(name)) return t;
        	}
        }
        return null;
    }

    /**
     * Returns an array of all nested tags.
     */
    public NBTTag[] getSubtags() {
        if (type != Type.TAG_List && type != Type.TAG_Compound)
            return null;
        return (NBTTag[]) value;
    }

    /**
     * Read a tag and its nested tags from an InputStream.
     *
     * @param is stream to read from, like a FileInputStream
     * @return NBT tag or structure read from the InputStream
     * @throws IOException if there was no valid NBT structure in the InputStream or if another IOException occurred.
     */
    public static NBTTag readFrom(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        byte type = dis.readByte();
        NBTTag tag = null;

        if (type == 0) {
            tag = new NBTTag(Type.TAG_End, null, null);
        } else {
            tag = new NBTTag(Type.values()[type], dis.readUTF(), readPayload(dis, type));
        }

        dis.close();

        return tag;
    }

    private static Object readPayload(DataInputStream dis, byte type) throws IOException {
        switch (type) {
        case 0:
            return null;
        case 1:
            return dis.readByte();
        case 2:
            return dis.readShort();
        case 3:
            return dis.readInt();
        case 4:
            return dis.readLong();
        case 5:
            return dis.readFloat();
        case 6:
            return dis.readDouble();
        case 7:
            int length = dis.readInt();
            byte[] ba = new byte[length];
            dis.readFully(ba);
            return ba;
        case 8:
            return dis.readUTF();
        case 9:
            byte lt = dis.readByte();
            int ll = dis.readInt();
            NBTTag[] lo = new NBTTag[ll];
            for (int i = 0; i < ll; i++) {
                lo[i] = new NBTTag(Type.values()[lt], null, readPayload(dis, lt));
            }
            if (lo.length == 0)
                return Type.values()[lt];
            else
                return lo;
        case 10:
            byte stt;
            NBTTag[] tags = new NBTTag[0];
            do {
                stt = dis.readByte();
                String name = null;
                if (stt != 0) {
                    name = dis.readUTF();
                }
                NBTTag[] newTags = new NBTTag[tags.length + 1];
                System.arraycopy(tags, 0, newTags, 0, tags.length);
                newTags[tags.length] = new NBTTag(Type.values()[stt], name, readPayload(dis, stt));
                tags = newTags;             } while (stt != 0);
            return tags;
        case 11:
            int len = dis.readInt();
            int[] ia = new int[len];
			for (int qq = 0; qq < len; qq++) {
				ia[qq] = dis.readInt();
			}
            return ia;

        }
        return null;
    }

    /**
     * Write a tag and its nested tags to an OutputStream.
     *
     * @param os stream to write to, like a FileOutputStream
     * @throws IOException if this is not a valid NBT structure or if any IOException occurred.
     */
    public void writeTo(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeByte(type.ordinal());
        if (type != Type.TAG_End) {
            if (name != null) { dos.writeUTF(name); } // qqDPS
            writePayload(dos);
        }
        dos.flush();
        dos.close();
    }

    private void writePayload(DataOutputStream dos) throws IOException {
        switch (type) {
        case TAG_End:
            break;
        case TAG_Byte:
            dos.writeByte((Byte) value);
            break;
        case TAG_Short:
            dos.writeShort((Short) value);
            break;
        case TAG_Int:
            dos.writeInt((Integer) value);
            break;
        case TAG_Long:
            dos.writeLong((Long) value);
            break;
        case TAG_Float:
            dos.writeFloat((Float) value);
            break;
        case TAG_Double:
            dos.writeDouble((Double) value);
            break;
        case TAG_Byte_Array:
            byte[] ba = (byte[]) value;
            dos.writeInt(ba.length);
            dos.write(ba);
            break;
        case TAG_String:
            dos.writeUTF((String) value);
            break;
        case TAG_List:
            NBTTag[] list = (NBTTag[]) value;
            dos.writeByte(getListType().ordinal());
            dos.writeInt(list.length);
            for (NBTTag tt : list) {
                tt.writePayload(dos);
            }
            break;
        case TAG_Compound:
            NBTTag[] subtags = (NBTTag[]) value;
            for (NBTTag subtag : subtags) {
                Type type = subtag.getType();
                dos.writeByte(type.ordinal());
                if (type != Type.TAG_End) {
                    dos.writeUTF(subtag.getName());
                    subtag.writePayload(dos);
                }
            }
            break;
        case TAG_Int_Array:
            int[] ia = (int[]) value;
            dos.writeInt(ia.length);
			for (int qq = 0; qq < ia.length; qq++) {
				dos.writeInt(ia[qq]);
			}
            break;

        }
    } 

	@Override
	public String toString() {
		return toString(0);
	}
	
	private String toString(int indentLevel) {
		if (type == Type.TAG_End) return "";
		NBTTag[] nestedTags = getSubtags();
		StringBuilder sb = new StringBuilder();
		indent(sb, indentLevel);
        sb.append(type.toString() + "(");
        if(name != null && !name.equals("")) sb.append("\"" + name + "\"");
        sb.append(")");
        switch(type) {
        	case TAG_Byte_Array:
        		sb.append(" [" + ((byte[])value).length + " bytes]");
        		break;
        	case TAG_Int_Array:
        		sb.append(" [" + ((int[])value).length + " * 4 bytes]");
        		break;
        	case TAG_List:
        		sb.append(": " + (nestedTags.length) + " entries");
        		break;
        	case TAG_Compound:
        		sb.append(": " + (nestedTags.length - 1) + " entries");
        		break;
        	default:
        		sb.append(": " + value);
        }
        sb.append("\n");
        if(type == Type.TAG_Compound || type == Type.TAG_List) {
        	indent(sb, indentLevel);
        	sb.append("{\n");
        	for(NBTTag t : nestedTags) sb.append(t.toString(indentLevel + 1));
        	indent(sb, indentLevel);
        	sb.append("}\n");
        }
        return sb.toString();
	}
	
	private StringBuilder indent(StringBuilder sb, int indentLevel) {
		String indent = "   ";
		for(int i = 0; i < indentLevel; i++) sb.append(indent);
		return sb;
	}

}
