package src.main;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import src.world.entities.player.powers.PowerUp;

public class Modificable {
    //Variables del juego

    public static final int MONEDAS_POR_MINIJUEGO = 6; // Cantidad maxima de monedas que se pueden obtener en un minijuego
    public static final int TIEMPO_MINUTOS_JUEGO = 8; // Duración de la partida en minutos

    // Variables del jugador
    public static final String NOMBRE_JUGADOR = "NOMBRE";

    public static final int DANO_DASH = 1; // Daño que ocaciona el dash del jugador
    public static final float FUERZA_IMPULSO_DASH = 15f; // Impulso al hacer el dash
    public static final float ACELERACION_CAMINAR = 14f;
    public static final float VELOCIDAD_MAXIMA_CAMINAR = 5f;
    public static final float ACELERACION_CORRER = 18f;
    public static final float VELOCIDAD_MAXIMA_CORRER = 6.5f;
    public static final float TIEMPO_MAXIMO_SALTO_MANTENIDO = 0.3f;
    public static final float FUERZA_SALTO = 8f;
    public static final float FUERZA_SALTO_SOSTENIDO = 25f;
    public static final float FUERZA_ABSORBER = 12f;

    public static final PowerUp.Type PODER_INICIAL = PowerUp.Type.NONE; // Poder inicial del jugador
    
    // Controles

    public static int CONTROL_JUGADOR_IZQUIERDA = Input.Keys.A;
    public static int CONTROL_JUGADOR_DERECHA = Input.Keys.D;
    public static int CONTROL_JUGADOR_SALTO = Input.Keys.W;
    public static int CONTROL_JUGADOR_AGACHARSE = Input.Keys.S;
    public static int CONTROL_JUGADOR_CORRER = Input.Keys.SHIFT_LEFT;
    public static int CONTROL_JUGADOR_ACCION = Input.Keys.P;
    public static int CONTROL_JUGADOR_SOLTAR_PODER = Input.Keys.CONTROL_LEFT;
}
