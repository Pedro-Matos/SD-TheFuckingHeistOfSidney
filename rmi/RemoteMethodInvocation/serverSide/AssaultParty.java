package RemoteMethodInvocation.serverSide;

import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.interfaces.MuseumInterface;
import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.RemoteException;
import java.util.Arrays;

import static RemoteMethodInvocation.support.Constantes.*;

/**
 * Assault Party
 * @author Pedro Matos and Tiago Bastos
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
    private int[][] posicao = new int[2][NUM_GROUP];  // 1- linha posicao 2- linha IDLadrao
    /**
     * Next thief to crawl
     */
    private boolean[] minhaVez = new boolean[NUM_GROUP];
    /**
     * Number of thiefs at the room
     */
    private int naSala;
    /**
     * Number of thiefs in the CollectionSite
     */
    private int noTerreiro;
    /**
     * Number of thiefs in the assault party
     */
    private int nrElementos;
    /**
     * Thiefs in the room
     */
    private boolean[] cheguei = new boolean[NUM_GROUP];
    /**
     * Thiefs in the CollectionSite
     */
    private boolean[] voltei = new boolean[NUM_GROUP];
    /**
     * Room id
     */
    private int nrSala;
    /**
     * Room distance
     */
    private int distanciaSala = -1;

    /**
     * Group id
     */
    private int id = -1;


    private VectorTimestamp local;


    /**
     *
     * @param museum  Museum
     * @param nrSala room id
     * @param id thief id
     * @param general General Repository
     */
    public AssaultParty(MuseumInterface museum, int nrSala, int id, GeneralRepositoryInterface general) {

        this.gen = general;
        this.museum = museum;
        this.id = id;
        this.nrElementos = 0;
        this.naSala = 0;
        this.noTerreiro = 0;
        this.nrSala = nrSala;

        for (int i = 0; i < NUM_GROUP; i++) {
            this.posicao[0][i] = 0;
            this.posicao[1][i] = -1;
            this.minhaVez[i] = false;
            this.cheguei[i] = false;
            this.voltei[i] = false;
        }
    }

    /**
     * Enter the assault party
     * @param ladraoID thief id
     * @param pos_grupo group position
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp entrar(int ladraoID, int pos_grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.posicao[1][nrElementos] = ladraoID;
        int realID = ladraoID+1;
        if (nrElementos == NUM_GROUP - 1) {
            this.minhaVez[nrElementos] = true;
        }

        if (id == 0) {
            setAP1_pos_id_canvas(pos_grupo, ladraoID, 0, false);

        } else if (id == 1) {
            setAP2_pos_id_canvas(pos_grupo, ladraoID, 0, false);
        }

        this.nrElementos++;
        return local.clone();
    }


    public synchronized Tuple<VectorTimestamp, Integer> getPos(int ladraoID, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        for(int i = 0; i<nrElementos; i++){
            if(this.posicao[1][i] == ladraoID){
                return new Tuple<>(local.clone(), i);
            }
        }
        return new Tuple<>(local.clone(), -1);
    }

    /**
     * crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public synchronized Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        int indiceNoGrupo = this.getPosicao(ladraoID);

        int getDistanciaSala = this.getRoomDistance(local.clone()).getSecond();


        if (this.naSala != nrElementos) {

            if (this.minhaVez[indiceNoGrupo]) {


                setThiefState(ladraoID,CRAWLING_INWARDS);

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do {
                    int myPosition = 0;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (posicao[1][k] != ladraoID) {
                            partnersPos[i++] = posicao[0][k];
                        } else {
                            myPosition = posicao[0][k];
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
                                posicao[0][indiceNoGrupo] = getDistanciaSala;
                                naSala++;
                                cheguei[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,getDistanciaSala);
                                    setThiefState(ladraoID,AT_A_ROOM);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,getDistanciaSala);
                                    setThiefState(ladraoID,AT_A_ROOM);
                                }
                            } else {
                                posicao[0][indiceNoGrupo] = myPosition + i;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,myPosition + i);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,myPosition + i);
                                }
                            }

                            break;
                        } else {
                            if (i == 1) repetir = false;
                        }
                    }
                } while (repetir);

                this.minhaVez[indiceNoGrupo] = false;
                int j = 0;
                for (j = 0; j < partnersPos.length; j++) {
                    if (partnersPos[j] > posicao[0][indiceNoGrupo]) ;
                    {
                        break;
                    }
                }
                for (int k = 0; k < nrElementos; k++) {
                    if (posicao[0][k] == partnersPos[j]) {
                        this.minhaVez[k] = true;
                    }
                }


            }
        }
        notifyAll();

        return new Tuple<>(local.clone(), this.posicao[0][indiceNoGrupo]);

    }


    /**
     * Crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public synchronized Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        int indiceNoGrupo = this.getPosicao(ladraoID);

        int getDistanciaSala = this.getRoomDistance(local.clone()).getSecond();

        if(naSala ==nrElementos){
            if (this.minhaVez[indiceNoGrupo]) {

                boolean repetir = true;
                int[] partnersPos = new int[NUM_GROUP - 1];
                do{
                    int myPosition = -1;

                    int i = 0;
                    for (int k = 0; k < NUM_GROUP; k++) {

                        if (posicao[1][k] != ladraoID) {
                            partnersPos[i++] = posicao[0][k];
                        } else {
                            myPosition = posicao[0][k];
                        }

                    }

                    if(myPosition == getDistanciaSala)
                        setThiefState(ladraoID,CRAWLING_OUTWARDS);

                    Arrays.sort(partnersPos);
                    for (i = agilidade; i > 0; i--) {
                        boolean tooFarOrOcupada = false;

                        int minha = myPosition - i;
                        int first = partnersPos[0];
                        int second = partnersPos[1];


                        if ((first - minha   > DIST_THIEVES) || (first-minha == 0 && (minha != getDistanciaSala))) {
                            tooFarOrOcupada = true;
                        }

                        if (minha == second) {
                            tooFarOrOcupada = true;
                        }


                        if ((!tooFarOrOcupada)) {
                            if (myPosition - i <= 0) {
                                posicao[0][indiceNoGrupo] = 0;
                                noTerreiro++;
                                voltei[indiceNoGrupo] = true;
                                repetir = false;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,0);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,0);
                                }
                                setThiefState(ladraoID, OUTSIDE);
                            } else {
                                posicao[0][indiceNoGrupo] = myPosition - i;
                                if(idGrupo == 0){
                                    setAP1_pos(posgrupo,myPosition - i);
                                }
                                else if(idGrupo== 1){
                                    setAP2_pos(posgrupo,myPosition - i);
                                }
                            }
                            break;
                        }
                        else {
                            if (i == 1) repetir = false;
                        }
                    }
                }while(repetir);

                this.minhaVez[indiceNoGrupo] = false;
                int j = 0;
                for (j = partnersPos.length - 1; j >= 0; j--) {
                    if (partnersPos[j] < posicao[0][indiceNoGrupo]);
                    {
                        break;
                    }

                }
                for (int k = 0; k < nrElementos; k++) {
                    if (posicao[0][k] == partnersPos[j]) {
                        this.minhaVez[k] = true;
                    }
                }
            }
        }
        notifyAll();
        return new Tuple<>(local.clone(), this.posicao[0][indiceNoGrupo]);
    }

    /**
     * Get position
     * @param ladraoID thief id
     * @return position
     */
    public synchronized int getPosicao(int ladraoID) {
        int j = -1;

        for (int i = 0; i < NUM_GROUP; i++) {
            if (this.posicao[1][i] == ladraoID) {
                j = i;
                break;
            }
        }
        return j;
    }

    /**
     * Get distance
     * @return distance
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Integer> getRoomDistance(VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        if (distanciaSala == -1) {
            distanciaSala = getMuseumRoomDistance(nrSala);
        }

        return new Tuple<>(local.clone(), distanciaSala);
    }


    /**
     * Steals paiting
     *
     * @return true if success
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Boolean> rollACanvas(VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        boolean ret = rollACanvas(nrSala);

        return new Tuple<>(local.clone(), ret);

    }

    /**
     * Waiting for it's turn
     * @param id thief id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp waitMyTurn(int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        notifyAll();
        int pos = this.getPosicao(id);
        while (!minhaVez[pos]) {

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return local.clone();
    }

    private boolean rollACanvas(int nrSala){
        boolean ret = false;

        try {
            local.increment();
            Tuple<VectorTimestamp, Boolean> tuple =
                    this.museum.rollACanvas(nrSala, local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    private int getMuseumRoomDistance(int nrSala) {
        int ret = -1;

        try {
            local.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.museum.getMuseumRoomDistance(nrSala, local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        try {
            this.gen.setAP2_pos(pos_grupo, posicao, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        try {
            this.gen.setAP1_pos(pos_grupo, posicao, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setThiefState(int id, int stat) {
        try {
            this.gen.setThiefState(id, stat, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos_id_canvas(int pos_grupo, int ladraoID, int i, boolean b) {
        try {
            this.gen.setAP1_pos_id_canvas(pos_grupo, ladraoID, i, b, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_pos_id_canvas(int pos_grupo, int ladraoID, int i, boolean b) {
        try {
            this.gen.setAP2_pos_id_canvas(pos_grupo, ladraoID, i, b, local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
