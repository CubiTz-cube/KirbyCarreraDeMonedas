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
                        break;

                    case POSITION:
                        Integer packId = (Integer) pack[1];
                        Float packX = (Float) pack[2];
                        Float packY= (Float) pack[3];
                        //server.game.changePosition(packId, packX, packY);
                        break;
                }
            }
        }catch (EOFException | SocketException e) {
            Gdx.app.log("User", "socket cerrado");
        } catch (IOException e) {
            Gdx.app.log("User", "Error al leer paquete", e);
        } catch (ClassNotFoundException e) {
            Gdx.app.log("User", "Error al convertir paquete", e);
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
        //server.sendAll(Packet.disconnect(id), id);
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
