package src.net;

import com.badlogic.gdx.Gdx;
import src.net.packets.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import com.badlogic.gdx.net.Socket;
import src.world.entities.Entity;
import src.world.entities.enemies.Enemy;
import src.world.player.Player;

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
        System.out.println("[User] Nuevo usuario conectado " + id);
        try {
            int packId;
            float x, y;
            boolean flipX;
            while (running) {
                Object[] pack = (Object[]) in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                if (!type.equals(Packet.Types.POSITION) &&
                    !type.equals(Packet.Types.ACTOTHERPLAYER) &&
                    !type.equals(Packet.Types.ACTENEMY)) System.out.println("[User " + id + "] Recibido: " + type);

                switch (type){
                    case CONNECT:
                        name = (String) pack[1];
                        server.sendAll(Packet.newPlayer(id, name), id);
                        for (ClientListener u : server.getUsers()){
                            if (u.id.equals(id)) continue;
                            send(Packet.newPlayer(u.id, u.name));
                        }
                        break;

                    case DISCONNECTPLAYER:
                        //Integer packId = (Integer) pack[1]; Devuelve -1
                        server.sendAll(Packet.disconnectPlayer(id), id);
                        break;

                    case POSITION:
                        packId = (Integer) pack[1]; //Devuelve -1 si es la pos de un player
                        x = (Float) pack[2];
                        y = (Float) pack[3];
                        if (packId == -1) server.sendAll(Packet.position(id, x, y), id);
                        else server.sendAll(Packet.position(packId, x, y), id);
                        break;
                    case GAMESTART:
                        server.sendAll(Packet.gameStart(), -1);
                        break;

                    case NEWENTITY:
                        // Cuando recibe el paquete newEntity del cliente que creao el servidor recorre su lista de entidades
                        // por eso no hace falta revisar lso valores del paquete
                        for (Entity e : server.game.getEntities().values()){
                            if (!(e instanceof Enemy enemy)) continue;
                            x = e.getBody().getPosition().x;
                            y = e.getBody().getPosition().y;
                            server.sendAll(Packet.newEnemy(e.getId(), enemy.getType(), x, y), id);
                        }
                        break;
                    case ACTENEMY:
                        packId = (Integer) pack[1];
                        Enemy.StateType state = (Enemy.StateType) pack[2];
                        float cronno = (Float) pack[3];
                        flipX = (Boolean) pack[4];
                        server.sendAll(Packet.actEnemy(packId, state, cronno, flipX), id);
                        break;
                    case ACTOTHERPLAYER:
                        //Integer packId = (Integer) pack[1]; Devuelve -1
                        Player.AnimationType animationType = (Player.AnimationType) pack[2];
                        flipX = (Boolean) pack[3];
                        server.sendAll(Packet.actOtherPlayer(id, animationType, flipX), id);
                        break;
                }
            }
        }catch (EOFException | SocketException e) {
            Gdx.app.log("User", "socket cerrado");
        } catch (IOException e) {
            Gdx.app.log("User", "Error al leer paquete", e);
        } catch (ClassNotFoundException e) {
            Gdx.app.log("User", "Error al convertir paquete", e);
        }finally {
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
        System.out.println("[USER] Usuario desconectado " + name + " "+ id);
        server.sendAll(Packet.disconnectPlayer(id), id);
        running = false;
        server.removeUser(this);
        try {
            in.close();
            out.close();
            socket.dispose();
        } catch (IOException e) {
            Gdx.app.log("User", "Error al cerrar socket de User" + name, e);
        }
    }

    public void send(Object[] data){
        try {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("USER Falla al enviar mensaje: " + e.getMessage());
        }
    }
}
