package src.net.packets;

public class Packet {
    public static enum Types{
        CONNECT, DISCONNECT, POSITION
    }

    public static Object[] connect(){
        return new Object[]{Types.CONNECT};
    }
    public static Object[] disconnect(Integer id){
        return new Object[]{Types.DISCONNECT, id};
    }

    public static Object[] vector2(Integer id,Float x, Float y){
        return new Object[]{Types.POSITION, id, x, y};
    }
}
