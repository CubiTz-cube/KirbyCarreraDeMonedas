package src.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import src.screens.GameScreen;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import src.utils.variables.ConsoleColor;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    public final GameScreen game;
    private final String ip;
    private final Integer port;
    private Boolean running;
    private final CopyOnWriteArrayList<ClientListener> users;
    private final ExecutorService pool = Executors.newFixedThreadPool(32);
    private final ServerSocket serverSocket;

    public Server(GameScreen game, String ip, Integer port){
        this.game = game;
        this.ip = ip;
        this.port = port;
        this.users = new CopyOnWriteArrayList<>();
        this.running = false;

        game.main.setIds(0);

        ServerSocketHints hints = new ServerSocketHints();
        hints.acceptTimeout = 0;
        serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, hints);
        running = true;
    }

    public CopyOnWriteArrayList<ClientListener> getUsers() {
        return users;
    }

    /**
     * Envia un mensaje a todos los clientes conectados
     * @param data Paquete
     * @param id Id del User que envia el mensaje
     */
    public void sendAll(Object[] data, Integer id){
        synchronized (users) {
            for (ClientListener user : users) {
                if (!user.isRunning() || user.getId().equals(id)) continue;
                user.send(data);
            }
        }
    }

    public void removeUser(ClientListener user){
        users.remove(user);
    }

    @Override
    public void run() {
        System.out.println( ConsoleColor.BLUE + "Server abierto en " + ip + ":" + port + ConsoleColor.RESET);

        try {
            while (running) {
                Socket clientSocket = serverSocket.accept(null);
                ClientListener newUser = new ClientListener(this,clientSocket, game.main.getIds());
                pool.execute(newUser);
                users.add(newUser);
            }
        } catch (GdxRuntimeException e) {
            Gdx.app.log("Server", "socket cerrado");
        }

    }

    public void close(){
        running = false;
        for (ClientListener user : users) {
            user.closeNoPacket();
        }
        serverSocket.dispose();
        pool.shutdownNow();
    }
}
