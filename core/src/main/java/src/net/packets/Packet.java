package src.net.packets;

public class Packet {
    public static enum Types{
        CONNECT, DISCONNECTPLAYER, POSITION, NEWPLAYER
    }

    public static Object[] connect(String name){
        return new Object[]{Types.CONNECT, name};
    }
    public static Object[] disconnectPlayer(Integer id){
        return new Object[]{Types.DISCONNECTPLAYER, id};
    }

    public static Object[] position(Integer id,Float x, Float y){
        return new Object[]{Types.POSITION, id, x, y};
    }

    public static Object[] newPlayer(Integer id,String name){
        return new Object[]{Types.NEWPLAYER, id, name};
    }

}
