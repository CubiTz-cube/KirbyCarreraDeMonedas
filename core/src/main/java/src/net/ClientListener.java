package src.net;

import src.net.packets.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientListener implements Runnable{
    private final Integer id;
    private String name;
    private final Socket socket;
    private Boolean running = false;
    private ObjectOutputStream out;
    private Server server;

    public ClientListener(Server server ,Socket socket, Integer id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Boolean isRunning(){
        return running;
    }

    public void close(){
        if (!running) return;
        System.out.println("USER Usuario desconectado " + name + " "+ id);
        //server.sendAll(Packet.disconnect(id), id);
        running = false;
        server.removeUser(this);
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar socket de User" + name + " : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("USER Nuevo usuario conectado " + id);
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            running = true;
            while (running) {
                Object[] pack = (Object[]) in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                switch (type){
                    case POSITION:
                        Integer packId = (Integer) pack[1];
                        Float packX = (Float) pack[2];
                        Float packY= (Float) pack[3];
                        //server.game.changePosition(packId, packX, packY);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer/escribir mensaje: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error al leer/escribir mensaje (clase no encontrada): " + e.getMessage());
        } finally {
            close();
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
