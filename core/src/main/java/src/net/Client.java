package src.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import src.net.packets.Packet;
import src.screens.worldScreens.GameScreen;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class Client implements Runnable{
    private final GameScreen game;
    private final String ip;
    private final Integer port;
    private Boolean running = false;

    private final String name;
    private final HashMap<Integer, String> playersConnected;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Client(GameScreen game, String ip, int port, String name){
        this.game = game;
        this.ip = ip;
        this.port = port;
        this.name = name;
        playersConnected = new HashMap<>();
        playersConnected.put(-1, name);
        try {
            SocketHints hints = new SocketHints();
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, hints);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
        } catch (IOException e){
            System.out.println("Error al crear cliente: " + e.getMessage());
        }
    }

    public ArrayList<String> getPlayersConnected() {
        return new ArrayList<>(this.playersConnected.values());
    }

    public String getName() {
        return name;
    }

    public Boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        Integer id;
        send(Packet.connect(name));
        try {
            while (running) {
                Object[] pack = (Object[])in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                System.out.println("[Client] Recibido: " + type);
                switch (type){
                    case NEWPLAYER:
                        id = (Integer) pack[1];
                        String name = (String) pack[2];
                        playersConnected.put(id, name);
                        break;

                    case DISCONNECTPLAYER:
                        id = (Integer) pack[1];
                        playersConnected.remove(id);

                        break;

                    case POSITION:
                        Integer packId = (Integer) pack[1];
                        Float packX = (Float) pack[2];
                        Float packY= (Float) pack[3];
                        //server.game.changePosition(packId, packX, packY);
                        break;
                }
            }
        } catch (SocketException | EOFException e) {
            Gdx.app.log("Client", "Socket cerrado");
        }catch (IOException e) {
            Gdx.app.log("Client", "Error al recibir mensaje", e);
        } catch (ClassNotFoundException e){
            Gdx.app.log("Client", "Error al procesar mensaje", e);
        } finally {
            close();
        }
    }

    public void send(Object[] data){
        try {
            out.writeObject(data);
        }catch (IOException e){
            Gdx.app.log("Client", "Error al enviar mensaje", e);
        }
    }

    public void close(){
        if (!running) return;
        running = false;
        socket.dispose();
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            Gdx.app.log("Client", "Error al cerrar socket: ", e);
        }

    }
}
