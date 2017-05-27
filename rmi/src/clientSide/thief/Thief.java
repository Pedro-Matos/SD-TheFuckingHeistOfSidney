package clientSide.thief;

import interfaces.AssaultPartyManagerInterface;
import interfaces.CollectionSiteInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.GeneralRepositoryInterface;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.RemoteException;

import static support.Constantes.*;

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

        // clock ????
        this.agilidade = getAgility(vt.clone());
        setThiefDisplacement(this.id, this.agilidade, vt.clone());

    }




    @Override
    public void run() {
        int posicao = 0;

        imReady(this.id);

        boolean quadro = false;
        boolean heistOver = false;

        while (!heistOver) {
            // no clock
            int stat = getThiefState(this.id);

            switch (stat) {

                case OUTSIDE:
                    setThiefState(this.id, stat);
                    // no clock
                    boolean getBusyLadrao = getBusyThief(this.id);

                    if (!getBusyLadrao && this.meuGrupo == -1) {
                        amINeeded(this.id);
                    } else {

                        this.meuGrupo = getThiefGroup(this.id);

                        // no clock
                        this.pos_grupo = getGroupPosition(this.id, this.meuGrupo);

                        if(this.meuGrupo != -1 && this.pos_grupo != -1){
                            prepareExcursion(this.id);
                        }

                    }
                    break;


                case CRAWLING_INWARDS:

                    waitMyTurn(this.id, this.meuGrupo);


                    posicao = crawlIn(this.id, this.agilidade, this.meuGrupo, this.pos_grupo);

                    // no clock
                    int getDistanciaSala = getRoomDistance(this.meuGrupo);


                    if (posicao == getDistanciaSala) {
                        atARoom(this.id);
                    }
                    break;

                case AT_A_ROOM:

                    quadro = rollACanvas(this.meuGrupo);

                    // no clock
                    int room = getAssaultRoom(this.meuGrupo);

                    if (this.meuGrupo == 0) {
                        setAP1_canvas(this.pos_grupo, quadro, room);
                    } else if (this.meuGrupo == 1) {
                        setAP2_canvas(this.pos_grupo, quadro, room);
                    }

                    reverseDirection(this.id);
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        waitMyTurn(this.id, this.meuGrupo);
                        posicao = crawlOut(this.id, this.agilidade, this.meuGrupo, this.pos_grupo);
                    }

                    if (this.meuGrupo == 0) {
                        setAP1_pos(this.pos_grupo, posicao);
                    } else if (this.meuGrupo == 1) {
                        setAP2_pos(this.pos_grupo, posicao);
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            // no clock
                            int getSalaAssalto = getAssaultRoom(this.meuGrupo);
                            // no clock
                            int getPosGrupo = getGroupPosition(this.id, this.meuGrupo);
                            handACanvas(this.id, getSalaAssalto, this.meuGrupo, getPosGrupo);

                        } else {
                            // no clock
                            int getSalaAssalto = getAssaultRoom(this.meuGrupo);
                            // no clock
                            int getPosGrupo = getGroupPosition(this.id, this.meuGrupo);
                            flagEmptyRoom(getSalaAssalto, this.meuGrupo, getPosGrupo);
                        }

                        if (this.meuGrupo == 0) setAP1_reset(this.pos_grupo, this.id);
                        else if (this.meuGrupo == 1) setAP2_reset(this.pos_grupo, this.id);

                        this.meuGrupo = -1;
                        flagArrival(this.id);
                    }

                    break;

                case HEIST_END:

                    heistOver = true;
                    break;
            }
        }
    }

    private void setAP2_reset(int pos_grupo, int id) {
        try {
            this.generalRepository.setAP2_reset(pos_grupo, id, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_reset(int pos_grupo, int id) {
        try {
            this.generalRepository.setAP1_reset(pos_grupo, id, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void flagArrival(int id) {
        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.flagArrival(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void flagEmptyRoom(int getSalaAssalto, int meuGrupo, int getPosGrupo) {

        try {
            vt.increment();
            VectorTimestamp clock =
                    collectionSiteInterface.flagEmptyRoom(getSalaAssalto, meuGrupo, getPosGrupo, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void handACanvas(int id, int getSalaAssalto, int meuGrupo, int getPosGrupo) {

        try {
            vt.increment();
            VectorTimestamp clock =
                    collectionSiteInterface.handACanvas(id, getSalaAssalto, meuGrupo, getPosGrupo, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        try {
            this.generalRepository.setAP2_pos(pos_grupo, posicao, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        try {
            this.generalRepository.setAP1_pos(pos_grupo, posicao, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int crawlOut(int id, int agilidade, int meuGrupo, int pos_grupo) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.crawlOut(id, agilidade, meuGrupo, pos_grupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void reverseDirection(int id) {
        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.reverseDirection(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_canvas(int pos_grupo, boolean quadro, int room) {
        try {
            this.generalRepository.setAP2_canvas(pos_grupo, quadro, room, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_canvas(int pos_grupo, boolean quadro, int room) {
        try {
            this.generalRepository.setAP1_canvas(pos_grupo, quadro, room, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getAssaultRoom(int meuGrupo) {
        int ret = -1;
        try {
//            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getAssaultRoom(meuGrupo, vt.clone());
            ret = tuple.getSecond();
//            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private boolean rollACanvas(int meuGrupo) {
        boolean ret = false;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Boolean> tuple =
                    this.grupo.rollACanvas(meuGrupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void atARoom(int id) {
        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.atARoom(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getRoomDistance(int meuGrupo) {
        int ret = -1;

        try {
//            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.getRoomDistance(meuGrupo, vt.clone());
            ret = tuple.getSecond();
//            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int crawlIn(int id, int agilidade, int meuGrupo, int pos_grupo) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.grupo.crawlIn(id, agilidade, meuGrupo, pos_grupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void waitMyTurn(int id, int meuGrupo) {

        try {
            vt.increment();
            VectorTimestamp clock = this.grupo.waitMyTurn(id, meuGrupo, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void prepareExcursion(int id) {
        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.prepareExcursion(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getGroupPosition(int id, int meuGrupo) {
        int ret = -1;

        try {
//            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = this.grupo.getPos(id, meuGrupo, vt.clone());
            ret = tuple.getSecond();
//            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int getThiefGroup(int id) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getThiefGroup(id, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void amINeeded(int id) {

        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.amINeeded(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private boolean getBusyThief(int id) {
        boolean ret = false;
        try {
//            vt.increment();
            Tuple<VectorTimestamp, Boolean> tuple = concentrationSite.getBusyThief(id, vt.clone());
            ret = tuple.getSecond();
//            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void setThiefState(int id, int stat) {
        try {
            this.generalRepository.setThiefState(id, stat, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getThiefState(int id) {
        int state = -1;
        try {
//            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getThiefState(id, vt.clone());
//            vt.update(tuple.getClock());
            state = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return state;
    }

    private void imReady(int id) {
        try {
            vt.increment();
            VectorTimestamp clock = concentrationSite.imReady(id, vt.clone());
            vt.update(clock);
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
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getAgility(id, vt.clone());
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
            this.generalRepository.setThiefDisplacement(id, agilidade, vt.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
