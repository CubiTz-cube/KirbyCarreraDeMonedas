package src.net;

import com.badlogic.gdx.Gdx;
import src.net.packets.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import com.badlogic.gdx.net.Socket;

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
            while (running) {
                Object[] pack = (Object[]) in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                System.out.println("[User] Recibido: " + type);
                switch (type){
                    case CONNECT:
                        name = (String) pack[1];
                        server.sendAll(Packet.newPlayer(id, name), id);
                        for (ClientListener u : server.getUsers()){
                            if (u.id.equals(id)) continue;
                            send(Packet.newPlayer(u.id, u.name));
                        }
                        break;

                    case POSITION:
                        //Integer id = (Integer) pack[1];
                        Float x = (Float) pack[2];
                        Float y = (Float) pack[3];
                        server.sendAll(Packet.position(this.id, x, y), this.id);
                        break;
                    case GAMESTART:
                        server.sendAll(Packet.gameStart(), id);
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
