package serverSide.assaultParty;

import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.RemoteException;
import java.util.Arrays;

import static support.Constantes.*;

/**
 * Assault Party
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public class AssaultParty {

    /**
     * Museum
     */
    private MuseumInterface museum;

    /**
     * General Repository
     */
    private GeneralRepositoryInterface gen;
    /**
     * Thief's positions
     */
    private int[][] position = new int[2][NUM_GROUP];  // 1- linha position 2- linha IDLadrao
    /**
     * Next thief to crawl
     */
    private boolean[] myTurn = new boolean[NUM_GROUP];
    /**
     * Number of thiefs at the room
     */
    private int atRoom;
    /**
     * Number of thiefs in the CollectionSite
     */
    private int atCollection;
    /**
     * Number of thiefs in the assault party
     */
    private int numberElements;
    /**
     * Thiefs in the room
     */
    private boolean[] arrived = new boolean[NUM_GROUP];
    /**
     * Thiefs in the CollectionSite
     */
    private boolean[] imBack = new boolean[NUM_GROUP];
    /**
     * Room id
     */
    private int roomNumber;
    /**
     * Room distance
     */
    private int roomDistance = -1;

    /**
     * Group id
     */
    private int id = -1;

    /**
     * Local Vector Timestamp
     */
    private VectorTimestamp local;


    /**
     * @param museum  Museum
     * @param roomNumber  room id
     * @param id      thief id
     * @param general General Repository
     */
    public AssaultParty(MuseumInterface museum, int roomNumber, int id, GeneralRepositoryInterface general) {

        this.gen = general;
        this.museum = museum;
        this.id = id;
        this.numberElements = 0;
        this.atRoom = 0;
        this.atCollection = 0;
        this.roomNumber = roomNumber;

        for (int i = 0; i < NUM_GROUP; i++) {
            this.position[0][i] = 0;
            this.position[1][i] = -1;
            this.myTurn[i] = false;
            this.arrived[i] = false;
            this.imBack[i] = false;
        }

        local = new VectorTimestamp(VECTOR_TIMESTAMP_SIZE, 0);
    }

    /**
     * Enter the assault party
     *
     * @param ladraoID        thief id
     * @param pos_grupo       group position
     * @param vectorTimestamp clock
     * @return VectorTimestamp clock
     */
    public synchronized VectorTimestamp joinAssaultParty(int ladraoID, int pos_grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.position[1][numberElements] = ladraoID;
        if (numberElements == NUM_GROUP - 1) {
            this.myTurn[numberElements] = true;
        }

        if (id == 0) {
            setAP1_pos_id_canvas(pos_grupo, ladraoID, 0, false);

        } else if (id == 1) {
            setAP2_pos_id_canvas(pos_grupo, ladraoID, 0, false);
        }

        this.numberElements++;
        return local.clone();
    }

    /**
     * Method that returns the thief position inside the Assault Party
     *
     * @param ladraoID        thief id
     * @param vectorTimestamp clock
     * @return clock and int with the position of the thief in the assault party
     */
    public synchronized Tuple<VectorTimestamp, Integer> getGroupPosition(int ladraoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        for (int i = 0; i < numberElements; i++) {
            if (this.position[1][i] == ladraoID) {
                return new Tuple<>(local.clone(), i);
            }
        }
        return new Tuple<>(local.clone(), -1);
    }

    /**
     * crawl in
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock and final position
     */
    public synchronized Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        int indiceNoGrupo = this.getPosicao(ladraoID);

        // no clock
        int getDistanciaSala = this.getRoomDistance(local.clone()).getSecond();


        if (this.atRoom != numberElements) {

            if (this.myTurn[indiceNoGrupo]) {


                setThiefState(ladraoID, CRAWLING_INWARDS);
                System.out.println(Arrays.toString(local.toIntArray()) + "THIEF STATE");

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do {
                    int myPosition = 0;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (position[1][k] != ladraoID) {
                            partnersPos[i++] = position[0][k];
                        } else {
                            myPosition = position[0][k];
                        }

                    }
                    Arrays.sort(partnersPos);
                    for (i = agilidade; i > 0; i--) {
                        boolean tooFarOrOcupada = false;

                        int minha = myPosition + i;
                        int first = partnersPos[1];
                        int second = partnersPos[0];


                        if ((minha - first > DIST_THIEVES) || (minha - first == 0 && (minha != 0) && minha != getDistanciaSala)) {
                            tooFarOrOcupada = true;
                        }

                        if (minha == second) {
                            tooFarOrOcupada = true;
                        }

                        if (!tooFarOrOcupada) {
                            if (myPosition + i >= getDistanciaSala) {
                                position[0][indiceNoGrupo] = getDistanciaSala;
                                atRoom++;
                                arrived[indiceNoGrupo] = true;
                                repetir = false;
                                if (idGrupo == 0) {
                                    setAP1_pos(posgrupo, getDistanciaSala);
                                    setThiefState(ladraoID, AT_A_ROOM);
                                } else if (idGrupo == 1) {
                                    setAP2_pos(posgrupo, getDistanciaSala);
                                    setThiefState(ladraoID, AT_A_ROOM);
                                }
                            } else {
                                position[0][indiceNoGrupo] = myPosition + i;
                                if (idGrupo == 0) {
                                    setAP1_pos(posgrupo, myPosition + i);
                                } else if (idGrupo == 1) {
                                    setAP2_pos(posgrupo, myPosition + i);
                                }
                            }

                            break;
                        } else {
                            if (i == 1) repetir = false;
                        }
                    }
                } while (repetir);

                this.myTurn[indiceNoGrupo] = false;
                int j = 0;
                for (j = 0; j < partnersPos.length; j++) {
                    if (partnersPos[j] > position[0][indiceNoGrupo]) ;
                    {
                        break;
                    }
                }
                for (int k = 0; k < numberElements; k++) {
                    if (position[0][k] == partnersPos[j]) {
                        this.myTurn[k] = true;
                    }
                }


            }
        }
        notifyAll();

        return new Tuple<>(local.clone(), this.position[0][indiceNoGrupo]);

    }


    /**
     * crawl out
     *
     * @param ladraoID        thief id
     * @param agilidade       thief agility
     * @param idGrupo         group id
     * @param posgrupo        position in the group
     * @param vectorTimestamp clock
     * @return clock and final position
     */
    public synchronized Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);


        int indiceNoGrupo = this.getPosicao(ladraoID);

        // no clock
        int getDistanciaSala = this.getRoomDistance(local.clone()).getSecond();

        if (atRoom == numberElements) {
            if (this.myTurn[indiceNoGrupo]) {

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do {
                    int myPosition = -1;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (position[1][k] != ladraoID) {
                            partnersPos[i++] = position[0][k];
                        } else {
                            myPosition = position[0][k];
                        }

                    }

                    if (myPosition == getDistanciaSala)
                        setThiefState(ladraoID, CRAWLING_OUTWARDS);

                    Arrays.sort(partnersPos);
                    for (i = agilidade; i > 0; i--) {
                        boolean tooFarOrOcupada = false;

                        int minha = myPosition - i;
                        int first = partnersPos[0];
                        int second = partnersPos[1];

                        if ((first - minha > DIST_THIEVES) || (first - minha == 0 && (minha != getDistanciaSala))) {
                            tooFarOrOcupada = true;
                        }

                        if (minha == second) {
                            tooFarOrOcupada = true;
                        }

                        if ((!tooFarOrOcupada)) {
                            if (myPosition - i <= 0) {
                                position[0][indiceNoGrupo] = 0;
                                atCollection++;
                                imBack[indiceNoGrupo] = true;
                                repetir = false;
                                if (idGrupo == 0) {
                                    setAP1_pos(posgrupo, 0);
                                } else if (idGrupo == 1) {
                                    setAP2_pos(posgrupo, 0);
                                }
                                setThiefState(ladraoID, OUTSIDE);
                            } else {
                                position[0][indiceNoGrupo] = myPosition - i;
                                if (idGrupo == 0) {
                                    setAP1_pos(posgrupo, myPosition - i);
                                } else if (idGrupo == 1) {
                                    setAP2_pos(posgrupo, myPosition - i);
                                }
                            }
                            break;
                        } else {
                            if (i == 1) repetir = false;
                        }
                    }
                } while (repetir);

                this.myTurn[indiceNoGrupo] = false;
                int j = 0;
                for (j = partnersPos.length - 1; j >= 0; j--) {
                    if (partnersPos[j] < position[0][indiceNoGrupo]) ;
                    {
                        break;
                    }

                }
                for (int k = 0; k < numberElements; k++) {
                    if (position[0][k] == partnersPos[j]) {
                        this.myTurn[k] = true;
                    }
                }
            }
        }
        notifyAll();
        return new Tuple<>(local.clone(), this.position[0][indiceNoGrupo]);
    }


    /**
     * Get distance
     *
     * @param vectorTimestamp clock
     * @return clock and distance
     */
    public Tuple<VectorTimestamp, Integer> getRoomDistance(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (roomDistance == -1) {
            roomDistance = getMuseumRoomDistance(roomNumber);
        }

        return new Tuple<>(local.clone(), roomDistance);
    }


    /**
     * Steals paiting
     *
     * @param vectorTimestamp clock
     * @return clock and true if success
     */
    public synchronized Tuple<VectorTimestamp, Boolean> rollACanvas(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), rollACanvas(roomNumber));

    }

    /**
     * Waiting for it's turn
     *
     * @param id              thief id
     * @param vectorTimestamp clock
     * @return clock
     */
    public synchronized VectorTimestamp waitMyTurn(int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        notifyAll();
        int pos = this.getPosicao(id);
        while (!myTurn[pos]) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return local.clone();
    }


    private synchronized int getPosicao(int ladraoID) {
        int j = -1;

        for (int i = 0; i < NUM_GROUP; i++) {
            if (this.position[1][i] == ladraoID) {
                j = i;
                break;
            }
        }
        return j;
    }

    private boolean rollACanvas(int nrSala) {
        boolean ret = false;

        try {
            Tuple<VectorTimestamp, Boolean> tuple =
                    this.museum.rollACanvas(nrSala, local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    private int getMuseumRoomDistance(int nrSala) {
        int ret = -1;

        try {
            Tuple<VectorTimestamp, Integer> tuple =
                    this.museum.getMuseumRoomDistance(nrSala, local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        try {
            this.gen.setAP2_pos(pos_grupo, posicao, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        try {
            this.gen.setAP1_pos(pos_grupo, posicao, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setThiefState(int id, int stat) {
        try {
            this.gen.setThiefState(id, stat, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos_id_canvas(int pos_grupo, int ladraoID, int i, boolean b) {
        try {
            this.gen.setAP1_pos_id_canvas(pos_grupo, ladraoID, i, b, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_pos_id_canvas(int pos_grupo, int ladraoID, int i, boolean b) {
        try {
            this.gen.setAP2_pos_id_canvas(pos_grupo, ladraoID, i, b, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
