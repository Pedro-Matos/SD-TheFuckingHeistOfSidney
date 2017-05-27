package serverSide.museum;

import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import support.Constantes;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.RemoteException;
import java.util.Random;

import static support.Constantes.*;

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
        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);


        for (int i = 0; i < salas.length; i++) {
            Random r = new Random();
            salas[i][0] = r.nextInt((MAX_PAINTS+1)-MIN_PAINTS) + MIN_PAINTS;
            setNrQuadrosSala(i, salas[i][0]);
            Random r2 = new Random();

            salas[i][1] = r2.nextInt((MAX_DIST+1)-MIN_DIST) + MIN_DIST;
            setDistanciaSala(i, salas[i][1]);
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
    public synchronized Tuple<VectorTimestamp, Boolean> rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        if (salas[nr_sala][0] > 0) {
            salas[nr_sala][0]--;
            //generalRepository.setNrQuadrosSala(nr_sala, salas[nr_sala][0]);
            return new Tuple<>(local.clone(), true);
        } else {
            return new Tuple<>(local.clone(), false);
        }
    }


    /**
     * Distance of the room
     * @param nr_sala room id
     * @param clone
     * @return distance
     */
    public synchronized Tuple<VectorTimestamp, Integer> getMuseumRoomDistance(int nr_sala, VectorTimestamp clone) {
        local.update(clone);

        return new Tuple<>(local.clone(), salas[nr_sala][1]);
    }


    private void setDistanciaSala(int i, int i1) {
        try {
            this.generalRepository.setDistanciaSala(i, i1, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setNrQuadrosSala(int i, int i1) {
        try {
            this.generalRepository.setNrQuadrosSala(i, i1,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
