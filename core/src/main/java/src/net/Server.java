package src.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private final String ip;
    private final Integer port;
    private ServerSocket serverSocket;
    private Boolean running;
    private final CopyOnWriteArrayList<ClientListener> users;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    public Server(String ip, Integer port){
        this.ip = ip;
        this.port = port;
        this.users = new CopyOnWriteArrayList<>();
        this.running = false;
        try {
            this.serverSocket = new ServerSocket(port);
            running = true;
        } catch (IOException e) {
            System.out.println("Error al crear server: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Server abierto en " + ip + ":" + port);
        try{
            while (running) {
                Socket clientSocket = serverSocket.accept();
                ClientListener newUser = new ClientListener(this,clientSocket, users.size()+1);
                pool.execute(newUser);
                users.add(newUser);
            }
        }catch (IOException e){
            System.out.println("Error al aceptar cliente: " + e.getMessage());
        }
    }

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

    public void close(){
        running = false;
        try {
            synchronized (users) {
                for (ClientListener user : users) {
                    user.close();
                    //user.send(Packet.serverClose());
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Falla al cerrar el servidor: " + e.getMessage());
        }
        pool.shutdown();
    }
}
