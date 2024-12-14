package src.net;

import com.badlogic.gdx.Gdx;
import src.net.packets.Packet;

import java.io.*;

import com.badlogic.gdx.net.Socket;
import src.utils.constants.ConsoleColor;
import src.world.entities.Entity;
import src.world.entities.blocks.BreakBlock;
import src.world.entities.enemies.Enemy;
import src.world.entities.player.Player;
import src.world.entities.player.powers.PowerUp;

public class ClientListener implements Runnable{
    private final Server server;
    private final Integer id;
    private String name;
    private final Socket socket;
    private Boolean running = false;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientListener(Server server ,Socket socket, Integer id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
        } catch (IOException e) {
            Gdx.app.log("User", "Error al crear cliente: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        try {
            int packId;
            float x, y, fx, fy;
            boolean flipX;
            while (running) {
                try {
                    Object[] pack = (Object[]) in.readObject();
                    Packet.Types type = (Packet.Types) pack[0];
                    if (!type.equals(Packet.Types.ACTENTITYPOSITION) &&
                        !type.equals(Packet.Types.ACTOTHERPLAYER) &&
                        !type.equals(Packet.Types.ACTENEMY) &&
                        !type.equals(Packet.Types.ACTBREAKBLOCK))
                        System.out.println(ConsoleColor.PURPLE + "[User " + id + "] Recibido: " + type + ConsoleColor.RESET);

                    switch (type) {
                        case CONNECTPLAYER:
                            name = (String) pack[1];
                            server.sendAll(Packet.newPlayer(id, name), id);
                            for (ClientListener u : server.getUsers()) {
                                if (u.id.equals(id)) continue;
                                send(Packet.newPlayer(u.id, u.name));
                            }
                            break;

                        case GAMESTART:
                            server.sendAll(Packet.gameStart(), -1);
                            break;

                        case ACTENTITYPOSITION:
                            packId = (int) pack[1]; //Devuelve -1 si es la pos de un player
                            x = (float) pack[2];
                            y = (float) pack[3];
                            fx = (float) pack[4];
                            fy = (float) pack[5];
                            if (packId == -1) server.sendAll(Packet.actEntityPosition(id, x, y, fx, fy), id);
                            else server.sendAll(Packet.actEntityPosition(packId, x, y, fx, fy), id);
                            break;

                        case NEWENTITY:
                            packId = (int) pack[1];
                            Entity.Type packType = (Entity.Type) pack[2];
                            x = (float) pack[3];
                            y = (float) pack[4];
                            fx = (float) pack[5];
                            fy = (float) pack[6];
                            flipX = (Boolean) pack[7];
                            server.sendAll(Packet.newEntity(packId, packType, x, y, fx, fy, flipX), id);
                            break;

                        case REMOVEENTITY:
                            packId = (int) pack[1];
                            server.sendAll(Packet.removeEntity(packId), id);
                            break;

                        case ACTENEMY:
                            packId = (int) pack[1];
                            Enemy.StateType state = (Enemy.StateType) pack[2];
                            Float cronno = (Float) pack[3];
                            flipX = (boolean) pack[4];
                            server.sendAll(Packet.actEnemy(packId, state, cronno, flipX), id);
                            break;

                        case ACTOTHERPLAYER:
                            //Integer packId = (Integer) pack[1]; Devuelve -1
                            Player.AnimationType animationType = (Player.AnimationType) pack[2];
                            flipX = (boolean) pack[3];
                            Player.StateType playerStateType = (Player.StateType) pack[4];
                            PowerUp.Type powerType = (PowerUp.Type) pack[5];
                            server.sendAll(Packet.actOtherPlayer(id, animationType, flipX, playerStateType, powerType), id);
                            break;

                        case ACTBREAKBLOCK:
                            packId = (int) pack[1];
                            BreakBlock.StateType blockStateType = (BreakBlock.StateType) pack[2];

                            server.sendAll(Packet.actBlock(packId, blockStateType), id);
                            break;

                        case ACTSCORE:
                            //Integer packId = (Integer) pack[1]; Devuelve -1
                            int score = (int) pack[2];
                            server.sendAll(Packet.actScore(id, score), id);
                            break;

                        case ACTENTITYCOLOR:
                            packId = (int) pack[1];
                            float r = (float) pack[2];
                            float g = (float) pack[3];
                            float b = (float) pack[4];
                            float a = (float) pack[5];
                            if (packId == -1) server.sendAll(Packet.actEntityColor(id, r, g, b, a), -1);
                            else server.sendAll(Packet.actEntityColor(packId, r, g, b, a), id);
                            break;

                        case MESSAGE:
                            String name = (String) pack[1];
                            String message = (String) pack[2];
                            server.sendAll(Packet.message(name, message), id);
                            break;
                    }
                }catch (StreamCorruptedException | ClassNotFoundException | ClassCastException e2) {
                    Gdx.app.log("User", "Error con el paquete");
                }
            }
        }catch (IOException e) {
            Gdx.app.log("User", "socket cerrado");
            close();
        }
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean isRunning(){
        return running;
    }

    public void close(){
        if (!running) return;
        server.sendAll(Packet.disconnectPlayer(id), id);
        server.sendAll(Packet.message("Server", name + " se ha desconectado"), id);
        closeNoPacket();
    }

    public void closeNoPacket(){
        if (!running) return;
        running = false;
        server.removeUser(this);
        try {
            in.close();
            out.close();
            socket.dispose();
        } catch (IOException e) {
            Gdx.app.log("User", "Error al cerrar socket de User " + name, e);
        }
    }

    public void send(Object[] data){
        try {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("[USER] Falla al enviar mensaje: " + e.getMessage());
        }
    }
}
