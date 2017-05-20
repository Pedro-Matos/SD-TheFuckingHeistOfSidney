package RemoteMethodInvocation.clientSide;

import RemoteMethodInvocation.interfaces.AssaultPartyManagerInterface;
import RemoteMethodInvocation.interfaces.CollectionSiteInterface;
import RemoteMethodInvocation.interfaces.ConcentrationSiteInterface;
import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.RemoteException;

import static RemoteMethodInvocation.support.Constantes.*;

/**
 * Ordinary Thief
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Thief extends Thread {

    /**
     * General Repository
     */
    private GeneralRepositoryInterface generalRepository;
    /**
     * Assault Party's manager
     */
    private AssaultPartyManagerInterface grupo;
    /**
     * Concentration Site
     */
    private CollectionSiteInterface collectionSiteInterface;
    /**
     * CollectionSite
     */
    private ConcentrationSiteInterface concentrationSite;
    /**
     * Thief ID
     */
    private int id;
    /**
     * Thief name
     */
    private String name;
    /**
     * Thief Group
     */
    private int meuGrupo;
    /**
     * Thief Agility
     */
    private int agilidade;
    /**
     * Thief position in the group
     */
    private int pos_grupo;

    private VectorTimestamp vt;


    /**
     * @param id Thief ID
     * @param name Thief name
     * @param generalRepository General Repository
     * @param grupo Assault Party manager
     * @param collectionSiteInterface Concentration Site
     * @param concentrationSite CollectionSite
     */
    public Thief(int id, String name, GeneralRepositoryInterface generalRepository, AssaultPartyManagerInterface grupo,
                 CollectionSiteInterface collectionSiteInterface, ConcentrationSiteInterface concentrationSite) {

        super(name);
        this.name = name;
        this.id = id;

        this.generalRepository = generalRepository;
        this.collectionSiteInterface = collectionSiteInterface;
        this.grupo = grupo;
        this.concentrationSite = concentrationSite;


        this.meuGrupo = -1;
        this.pos_grupo = -1;

        vt = new VectorTimestamp(VECTOR_TIMESTAMP_SIZE, (id + 1));

        this.agilidade = getAgility(vt.clone());

        setThiefDisplacement(this.id, this.agilidade, vt.clone());

    }




    @Override
    public void run() {
        int posicao = 0;

        imReady(this.id, vt.clone());

        boolean quadro = false;
        boolean heistOver = false;

        while (!heistOver) {
            int stat = getThiefState(this.id, vt.clone());

            switch (stat) {

                case OUTSIDE:
                    setThiefState(this.id, stat, vt.clone());

                    boolean getBusyLadrao = getBusyThief(this.id, vt.clone());

                    if (!getBusyLadrao && this.meuGrupo == -1) {
                        amINeeded(this.id, vt.clone());
                    } else {

                        this.meuGrupo = getThiefGroup(this.id, vt.clone());

                        this.pos_grupo = getGroupPosition(this.id, this.meuGrupo, vt.clone());

                        if(this.meuGrupo != -1 && this.pos_grupo != -1){
                            prepareExcursion(this.id, vt.clone());
                        }

                    }
                    break;


                case CRAWLING_INWARDS:

                    waitMyTurn(this.id, this.meuGrupo, vt.clone());


                    posicao = crawlIn(this.id, this.agilidade, this.meuGrupo, this.pos_grupo, vt.clone());
                    int getDistanciaSala = getRoomDistance(this.meuGrupo, vt.clone());


                    if (posicao == getDistanciaSala) {
                        atARoom(this.id, vt.clone());
                    }
                    break;

                case AT_A_ROOM:

                    quadro = rollACanvas(this.meuGrupo, vt.clone());

                    getAssaultRoom(this.meuGrupo, vt.clone());
                    int room = getAssaultRoom(this.meuGrupo, vt.clone());

                    if (this.meuGrupo == 0) {
                        setAP1_canvas(this.pos_grupo, quadro, room, vt.clone());
                    } else if (this.meuGrupo == 1) {
                        setAP2_canvas(this.pos_grupo, quadro, room, vt.clone());
                    }

                    reverseDirection(this.id, vt.clone());
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        waitMyTurn(this.id, this.meuGrupo, vt.clone());
                        posicao = crawlOut(this.id, this.agilidade, this.meuGrupo, this.pos_grupo, vt.clone());
                    }

                    if (this.meuGrupo == 0) {
                        setAP1_pos(this.pos_grupo, posicao, vt.clone());
                    } else if (this.meuGrupo == 1) {
                        setAP2_pos(this.pos_grupo, posicao, vt.clone());
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            int getSalaAssalto = getAssaultRoom(this.meuGrupo, vt.clone());
                            int getPosGrupo = getGroupPosition(this.id, this.meuGrupo, vt.clone());
                            handACanvas(this.id, getSalaAssalto, this.meuGrupo, getPosGrupo, vt.clone());

                        } else {

                            int getSalaAssalto = getAssaultRoom(this.meuGrupo, vt.clone());
                            int getPosGrupo = getGroupPosition(this.id, this.meuGrupo, vt.clone());
                            flagEmptyRoom(getSalaAssalto, this.meuGrupo, getPosGrupo, vt.clone());
                        }

                        if (this.meuGrupo == 0) setAP1_reset(this.pos_grupo, this.id, vt.clone());
                        else if (this.meuGrupo == 1) setAP2_reset(this.pos_grupo, this.id, vt.clone());

                        this.meuGrupo = -1;
                        flagArrival(this.id, vt.clone());
                    }

                    break;

                case HEIST_END:

                    heistOver = true;
                    break;
            }
        }
    }

    private void setAP2_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP2_reset(pos_grupo, id, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP1_reset(pos_grupo, id, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void flagArrival(int id, VectorTimestamp vectorTimestamp) {
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.flagArrival(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void flagEmptyRoom(int getSalaAssalto, int meuGrupo, int getPosGrupo, VectorTimestamp vectorTimestamp) {

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    collectionSiteInterface.flagEmptyRoom(getSalaAssalto, meuGrupo, getPosGrupo, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handACanvas(int id, int getSalaAssalto, int meuGrupo, int getPosGrupo, VectorTimestamp vectorTimestamp ) {

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    collectionSiteInterface.handACanvas(id, getSalaAssalto, meuGrupo, getPosGrupo, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_pos(int pos_grupo, int posicao, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP2_pos(pos_grupo, posicao, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos(int pos_grupo, int posicao, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP1_pos(pos_grupo, posicao, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int crawlOut(int id, int agilidade, int meuGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.crawlOut(id, agilidade, meuGrupo, pos_grupo, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void reverseDirection(int id, VectorTimestamp vectorTimestamp) {
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.reverseDirection(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_canvas(int pos_grupo, boolean quadro, int room, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP2_canvas(pos_grupo, quadro, room, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_canvas(int pos_grupo, boolean quadro, int room, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setAP1_canvas(pos_grupo, quadro, room, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getAssaultRoom(int meuGrupo, VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getAssaultRoom(id, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private boolean rollACanvas(int meuGrupo, VectorTimestamp vectorTimestamp) {
        boolean ret = false;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Boolean> tuple =
                    this.grupo.rollACanvas(meuGrupo, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void atARoom(int id, VectorTimestamp vectorTimestamp) {
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.atARoom(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getRoomDistance(int meuGrupo, VectorTimestamp vectorTimestamp) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.getRoomDistance(meuGrupo, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int crawlIn(int id, int agilidade, int meuGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.crawlIn(id, agilidade, meuGrupo, pos_grupo, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void waitMyTurn(int id, int meuGrupo, VectorTimestamp vectorTimestamp) {

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = this.grupo.waitMyTurn(id, meuGrupo, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void prepareExcursion(int id, VectorTimestamp vectorTimestamp) {
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.prepareExcursion(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getGroupPosition(int id, int meuGrupo, VectorTimestamp vectorTimestamp) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = this.grupo.getPos(id, meuGrupo, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int getThiefGroup(int id, VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getThiefGroup(id, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void amINeeded(int id, VectorTimestamp vectorTimestamp) {

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.amINeeded(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private boolean getBusyThief(int id, VectorTimestamp vectorTimestamp) {
        boolean ret = false;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Boolean> tuple = concentrationSite.getBusyThief(id, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void setThiefState(int id, int stat, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setThiefState(id, stat, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getThiefState(int id, VectorTimestamp vectorTimestamp) {
        int state = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getThiefState(id, vectorTimestamp);
            vt.update(tuple.getClock());
            state = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return state;
    }

    private void imReady(int id, VectorTimestamp vectorTimestamp) {
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.imReady(id, vectorTimestamp);
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getAgility(VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getAgility(id, vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
            this.agilidade = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }


    private void setThiefDisplacement(int id, int agilidade, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setThiefDisplacement(id, agilidade, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
