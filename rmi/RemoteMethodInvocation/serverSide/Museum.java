package RemoteMethodInvocation.serverSide;

import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.interfaces.MuseumInterface;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.util.Random;

import static RemoteMethodInvocation.support.Constantes.*;

/**
 * Monitor Museum
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Museum implements MuseumInterface {

    /**
     * Rooms of the museum
     */
    int[][] salas = new int[NUM_ROOMS][2];
    /**
     * General Repository
     */
    GeneralRepositoryInterface generalRepository;


    private VectorTimestamp local;


    /**
     * @param generalRepository General Repository
     */
    public Museum(GeneralRepositoryInterface generalRepository){

        this.generalRepository = generalRepository;

        for (int i = 0; i < salas.length; i++) {
            Random r = new Random();
            salas[i][0] = r.nextInt((MAX_PAINTS+1)-MIN_PAINTS) + MIN_PAINTS;
            this.generalRepository.setNrQuadrosSala(i, salas[i][0], );
            Random r2 = new Random();

            salas[i][1] = r2.nextInt((MAX_DIST+1)-MIN_DIST) + MIN_DIST;
            this.generalRepository.setDistanciaSala(i, salas[i][1], );
        }



    }

    /**
     *
     * Method to steal the paiting
     *
     * @param nr_sala room id
     * @param vectorTimestamp
     * @return true if success
     */
    public synchronized boolean rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) {
        if (salas[nr_sala][0] > 0) {
            salas[nr_sala][0]--;
            //generalRepository.setNrQuadrosSala(nr_sala, salas[nr_sala][0]);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Distance of the room
     * @param nr_sala room id
     * @return distance
     */
    public synchronized int getDistancia(int nr_sala) {
        return salas[nr_sala][1];
    }

    /**
     * Number of paitings
     * @param nr_sala room id
     * @return nr of paitings
     */
    public synchronized int getNumeroQuadros(int nr_sala) {
        return salas[nr_sala][0];
    }
}
