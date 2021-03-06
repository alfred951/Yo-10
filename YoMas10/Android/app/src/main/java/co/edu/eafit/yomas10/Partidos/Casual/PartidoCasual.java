package co.edu.eafit.yomas10.Partidos.Casual;

import java.util.ArrayList;

import co.edu.eafit.yomas10.Jugador.Jugador;
import co.edu.eafit.yomas10.Partidos.Partido;

/**
 * Created by Usuario on 24/10/2015.
 */
public class PartidoCasual extends Partido {

    private ArrayList<Jugador> integrantes;

//    public PartidoCasual(String fecha, String hora, String cancha){
//        super(fecha, hora, cancha);
//        integrantes = new ArrayList<>();
//    }

    public PartidoCasual(String fecha, String hora, String cancha, ArrayList<Jugador> integrantes){
        super(fecha, hora, cancha);
        this.integrantes = integrantes;
    }


    public PartidoCasual(String fecha, String hora, String cancha, ArrayList<Jugador> integrantes, int id){
        super(fecha, hora, cancha, id);
        this.integrantes = integrantes;
    }

    public void agregarParticipante(Jugador jugador){
        if (!integrantes.contains(jugador)){
            integrantes.add(jugador);
        }
    }

    public ArrayList<Jugador> getIntegrantes() {
        return integrantes;
    }

    @Override
    public String toString() {
        return "Partido el " + fecha + "\nLugar: " + cancha;
    }
}
