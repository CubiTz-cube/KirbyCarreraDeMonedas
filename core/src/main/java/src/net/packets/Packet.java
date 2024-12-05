package src.net.packets;

import src.world.entities.Entity;
import src.world.entities.blocks.Block;
import src.world.entities.enemies.Enemy;
import src.world.entities.player.Player;

public class Packet {
    public enum Types{
        CONNECTPLAYER, DISCONNECTPLAYER, NEWPLAYER, GAMESTART,
        ACTENTITYPOSITION, NEWENTITY, REMOVEENTITY,
        ACTENEMY, ACTOTHERPLAYER, ACTBREAKBLOCK, ACTSCORE,
        MESSAGE,
    }

    public static Object[] connectPlayer(String name){
        return new Object[]{Types.CONNECTPLAYER, name};
    }

    public static Object[] disconnectPlayer(int id){
        return new Object[]{Types.DISCONNECTPLAYER, id};
    }

    public static Object[] newPlayer(int id,String name){
        return new Object[]{Types.NEWPLAYER, id, name};
    }

    public static Object[] gameStart(){
        return new Object[]{Types.GAMESTART};
    }

    public static Object[] actEntityPosition(int id, float x, float y, float fx, float fy){
        return new Object[]{Types.ACTENTITYPOSITION, id, x, y, fx, fy};
    }
    public static Object[] actEntityPosition(int id, float x, float y){
        return new Object[]{Types.ACTENTITYPOSITION, id, x, y, 0f, 0f};
    }

    public static Object[] newEntity(int id, Entity.Type type, float x, float y, float fx, float fy, boolean flipX){
        return new Object[]{Types.NEWENTITY, id, type, x, y, fx, fy, flipX};
    }

    public static Object[] actEnemy(int id, Enemy.StateType state, float cronno, boolean flipX){
        return new Object[]{Types.ACTENEMY, id, state, cronno, flipX};
    }

    public static Object[] removeEntity(int id){
        return new Object[]{Types.REMOVEENTITY, id};
    }

    public static Object[] actOtherPlayer(int id, Player.AnimationType animationType, boolean flipX){
        return new Object[]{Types.ACTOTHERPLAYER, id, animationType, flipX};
    }

    public static Object[] actBlock(int id, Block.StateType stateType){
        return new Object[]{Types.ACTBREAKBLOCK, id, stateType};
    }

    public static Object[] actScore(int id,int score){
        return new Object[]{Types.ACTSCORE, id, score};
    }

    public static Object[] message(String name ,String message){
        return new Object[]{Types.MESSAGE, name, message};
    }
}
