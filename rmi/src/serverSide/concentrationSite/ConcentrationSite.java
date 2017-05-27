package serverSide.concentrationSite;

import interfaces.ConcentrationSiteInterface;
import support.Constantes;
import support.MemFIFO;
import interfaces.GeneralRepositoryInterface;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.RemoteException;

import static support.Constantes.*;

/**
 * Monitor Concentration Site.
 * @author Pedro Matos and Tiago Bastos
 */
public class ConcentrationSite implements ConcentrationSiteInterface {

    /**
     * FIFO used when the Thiefs arrive to the CollectionSite.
     */
    private MemFIFO fifo;
    /**
     * State of thief's
     */
    private int[] estadoLadrao = new int[NUM_THIEVES];
    /**
     * Flag if the thief is busy
     */
    private boolean[] busyLadrao = new boolean[NUM_THIEVES];
    /**
     * Number of thiefs in the CollectionSite
     */
    private int nrLadroes;
    /**
     * Agility of each thief
     */
    private int agilidadeLadroes[] = new int[NUM_THIEVES];
    /**
     * Group of each thief
     */
    private int grupoLadrao[] = new int[NUM_THIEVES];
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;
    /**
     * Thief's situation
     */
    private int situacaoLadrao[] = new int[NUM_THIEVES];


    private VectorTimestamp local;


    /**
     * @param generalRepository General Repository
     */
    public ConcentrationSite(GeneralRepositoryInterface generalRepository) {

        this.nrLadroes = 0;

        for (int i = 0; i < NUM_THIEVES; i++) {
            estadoLadrao[i] = OUTSIDE;
            this.busyLadrao[i] = false;
            grupoLadrao[i] = -1;
            agilidadeLadroes[i] = ((int) (Math.random() * (MAX_AGIL - 1))) + MIN_AGIL;
            this.situacaoLadrao[i] = WAITING;
        }

        fifo = new MemFIFO(NUM_THIEVES);

        this.general = generalRepository;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);

    }

    /**
     * Get for the thief's group
     * @param id thief's id
     * @param vectorTimestamp
     * @return thief's group
     */
    public synchronized Tuple<VectorTimestamp, Integer> getThiefGroup(int id, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), grupoLadrao[id]);

    }

    /**
     * The thief uses this method when he is ready.
     * The thief is added to the FIFO
     * @param ladraoID thief's id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp imReady(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (!fifo.full()) {
            this.busyLadrao[ladraoID] = false;
            fifo.write(ladraoID);
            this.nrLadroes++;
            this.grupoLadrao[ladraoID] = -1;
        } else {
            System.out.println("ERRO!!");
        }

        if (this.nrLadroes >= NUM_GROUP) {

            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return local.clone();
    }

    /**
     * Get for the number of thiefs in the CollectionSite
     * @return number of thiefs
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Integer> getNumberOfThieves(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.nrLadroes);
    }

    /**
     * Get for the thief state
     * @param ladraoID thief id
     * @param vectorTimestamp
     * @return thief state
     */

    public synchronized Tuple<VectorTimestamp, Integer> getThiefState(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.estadoLadrao[ladraoID]);

    }

    /**
     * Thief uses this when he is waiting for orders
     * @param ladraoID thief id
     * @param vectorTimestamp
     */

    public synchronized VectorTimestamp amINeeded(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        notifyAll();
        while (!this.busyLadrao[ladraoID] && this.estadoLadrao[ladraoID] != HEIST_END) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return local.clone();

    }

    /**
     *  Calls a thief from the FIFO and adds to the group
     * @param grupo group id
     * @param vectorTimestamp
     * @return id of thief, or -1 if FIFO is empty
     */
    public synchronized Tuple<VectorTimestamp, Integer> callThief(int grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        int id = -1;

        if (!fifo.empty()) {
            id = (Integer) fifo.read();
            this.nrLadroes--;
            this.busyLadrao[id] = true;
            this.grupoLadrao[id] = grupo;
            setMasterThiefState(ASSEMBLING_A_GROUP);
            setThiefSituation(id,IN_PARTY);
            notifyAll();
        }

        return new Tuple<>(local.clone(), id);
    }


    /**
     * Verifies if the thief is busy
     *
     * @param ladraoID thief id
     * @param vectorTimestamp
     * @return true if busy, false is free
     */
    public synchronized Tuple<VectorTimestamp, Boolean> getBusyThief(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        return new Tuple<>(local.clone(), this.busyLadrao[ladraoID]);
    }

    /**
     * State changes to Crawl IN
     * @param ladraoID thief id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp prepareExcursion(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.estadoLadrao[ladraoID] = CRAWLING_INWARDS;
        return local.clone();
    }

    /**
     * State changes to Crawl OUT
     * @param ladraoID thief id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp reverseDirection(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        this.estadoLadrao[ladraoID] = CRAWLING_OUTWARDS;
        return local.clone();
    }

    /**
     * State changes to At a Room
     * @param ladraoID thief id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp atARoom(int ladraoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        this.estadoLadrao[ladraoID] = AT_A_ROOM;
        return local.clone();
    }

    /**
     * Thief arrives at the CollectionSite
     * @param ladraoID thief id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp flagArrival(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.busyLadrao[ladraoID] = false;
        this.grupoLadrao[ladraoID] = -1;
        this.estadoLadrao[ladraoID] = OUTSIDE;
        setThiefState(ladraoID,this.estadoLadrao[ladraoID]);
        setThiefSituation(ladraoID,WAITING);
        this.imReady(ladraoID, local.clone());


        return local.clone();
    }



    /**
     * GET for the agility
     * @param ladraoID thief id
     * @param vectorTimestamp
     * @return thief agility
     */
    public synchronized Tuple<VectorTimestamp, Integer> getAgility(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), agilidadeLadroes[ladraoID]);
    }

    /**
     * Waiting for the correct number of thiefs in order to create an assault party
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp waitForThieves(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        while (this.nrLadroes < NUM_GROUP) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        notifyAll();

        return local.clone();
    }

    /**
     * Waits for all the thiefs in order to finish operation
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp waitForThievesEnd(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        while (this.nrLadroes < NUM_THIEVES) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        for (int i = 0; i < NUM_THIEVES; i++) {
            estadoLadrao[i] = HEIST_END;
        }
        notifyAll();
        return local.clone();
    }

    private void setThiefSituation(int id, int inParty) {
        try {
            this.general.setThiefSituation(id, inParty,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setMasterThiefState(int stat) {
        try {
            this.general.setMasterThiefState(stat,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setThiefState(int ladraoID, int i) {
        try {
            this.general.setThiefState(ladraoID, i,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
