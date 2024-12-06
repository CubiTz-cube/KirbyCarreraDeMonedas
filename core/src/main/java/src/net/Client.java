package src.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import src.net.packets.Packet;
import src.screens.GameScreen;
import src.utils.constants.ConsoleColor;
import src.world.entities.Entity;
import src.world.entities.blocks.BreakBlock;
import src.world.entities.enemies.Enemy;
import src.world.entities.otherPlayer.OtherPlayer;
import src.world.entities.player.Player;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;

public class Client implements Runnable{
    private final GameScreen game;
    private final String ip;
    private final Integer port;

    private final String name;
    private final HashMap<Integer, String> playersConnected;
    public Boolean gameStart = false;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Boolean running = false;

    public Client(GameScreen game, String ip, int port, String name){
        this.game = game;
        this.ip = ip;
        this.port = port;
        if (name.isEmpty()) this.name = "Sin nombre";
        else this.name = name;
        playersConnected = new HashMap<>();
        playersConnected.put(-1, name);
    }

    public HashMap<Integer, String> getPlayersConnected() {
        return this.playersConnected;
    }

    public String getName() {
        return name;
    }

    public Boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        try {
            SocketHints hints = new SocketHints();
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, hints);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            running = true;
        } catch (IOException e){
            System.out.println("Error al conectar cliente: " + e.getMessage());
        }

        int packId;
        float x,y ,fx, fy;
        boolean flipX;
        send(Packet.connectPlayer(name));
        System.out.println(ConsoleColor.BLUE + "[Client] Conectado a servidor: " + socket.getRemoteAddress() + ConsoleColor.RESET);
        try {
            while (running) {
                Object[] pack = (Object[])in.readObject();
                Packet.Types type = (Packet.Types) pack[0];
                if (!type.equals(Packet.Types.ACTENTITYPOSITION) &&
                    !type.equals(Packet.Types.ACTOTHERPLAYER) &&
                    !type.equals(Packet.Types.ACTENEMY) &&
                    !type.equals(Packet.Types.ACTBREAKBLOCK)) System.out.println(ConsoleColor.CYAN + "[Client] Recibido: " + type + ConsoleColor.RESET);

                switch (type){
                    case NEWPLAYER:
                        packId = (Integer) pack[1];
                        String name = (String) pack[2];
                        playersConnected.put(packId, name);
                        game.addActor(new OtherPlayer(game.getWorld(), game.main.getAssetManager(), new Rectangle(0, 10, 1.5f, 1.5f), packId, name));
                        break;

                    case DISCONNECTPLAYER:
                        packId = (Integer) pack[1];
                        playersConnected.remove(packId);
                        game.removeEntityNoPacket(packId);
                        break;

                    case GAMESTART:
                        gameStart = true;
                        break;

                    case NEWENTITY:
                        packId = (Integer) pack[1];
                        Entity.Type packType = (Entity.Type) pack[2];
                        x = (Float) pack[3];
                        y = (Float) pack[4];
                        fx = (Float) pack[5];
                        fy = (Float) pack[6];
                        flipX = (Boolean) pack[7];
                        game.addEntityNoPacket(packType, new Vector2(x,y), new Vector2(fx,fy), packId, flipX);
                        break;

                    case REMOVEENTITY:
                        packId = (Integer) pack[1];
                        game.removeEntityNoPacket(packId);
                        break;

                    case ACTENTITYPOSITION:
                        packId = (Integer) pack[1];
                        x = (Float) pack[2];
                        y = (Float) pack[3];
                        fx = (Float) pack[4];
                        fy = (Float) pack[5];
                        //System.out.println("Actualizar " + packId + " en " + x+ ", "+ y + " FX: " + fx + " | FY" + fy);
                        game.actEntityPos(packId, x, y, fx, fy);
                        break;

                    case ACTENEMY:
                        packId = (Integer) pack[1];
                        Enemy.StateType state = (Enemy.StateType) pack[2];
                        Float cronno = (Float) pack[3];
                        flipX = (Boolean) pack[4];
                        game.actEnemy(packId, state,cronno, flipX);
                        break;

                    case ACTOTHERPLAYER:
                        packId = (Integer) pack[1];
                        Player.AnimationType animationType = (Player.AnimationType) pack[2];
                        flipX = (Boolean) pack[3];
                        Player.StateType playerStateType = (Player.StateType) pack[4];
                        game.actOtherPlayerAnimation(packId, animationType, flipX, playerStateType);
                        break;

                    case ACTBREAKBLOCK:
                        packId = (Integer) pack[1];
                        BreakBlock.StateType blockStateType = (BreakBlock.StateType) pack[2];
                        game.actBlock(packId, blockStateType);
                        break;

                    case ACTSCORE:
                        packId = (Integer) pack[1];
                        int score = (int) pack[2];
                        game.actScore(packId, score);
                        break;

                    case ACTENTITYCOLOR:
                        packId = (Integer) pack[1];
                        float r = (float) pack[2];
                        float g = (float) pack[3];
                        float b = (float) pack[4];
                        float a = (float) pack[5];
                        game.actEntityColor(packId, r, g, b, a);
                        break;

                    case MESSAGE:
                        String packName = (String) pack[1];
                        String message = (String) pack[2];
                        game.addMessage(packName, message);
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
        if (!running) return;
        try {
            out.writeObject(data);
        }catch (IOException e){
            Gdx.app.log("Client", "Error al enviar mensaje", e);
        }
    }

    public void close(){
        if (!running) return;
        running = false;
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            Gdx.app.log("Client", "Error al cerrar socket: ", e);
        }
        socket.dispose();
    }
}
