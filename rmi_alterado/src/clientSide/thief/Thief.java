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
 * Represents the Ordinary Thief
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public class Thief extends Thread {

    /**
     * General Repository
     */
    private GeneralRepositoryInterface generalRepository;
    /**
     * Assault Party's manager
     */
    private AssaultPartyManagerInterface group;
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
    private int myGroup;
    /**
     * Thief Agility
     */
    private int agility;
    /**
     * Thief position in the group
     */
    private int group_position;

    /**
     * Thief local VectorTimestamp
     */
    private VectorTimestamp vt;


    /**
     * Creates a ORdinary Thief instance for runnning in a distributed enviroment
     *
     * @param id                      Thief ID
     * @param name                    Thief name
     * @param generalRepository       General Repository Interface
     * @param group                   Assault Party manager Interface
     * @param collectionSiteInterface Concentration Site Interface
     * @param concentrationSite       CollectionSite Interface
     */
    public Thief(int id, String name, GeneralRepositoryInterface generalRepository, AssaultPartyManagerInterface group,
                 CollectionSiteInterface collectionSiteInterface, ConcentrationSiteInterface concentrationSite) {

        super(name);
        this.name = name;
        this.id = id;

        this.generalRepository = generalRepository;
        this.collectionSiteInterface = collectionSiteInterface;
        this.group = group;
        this.concentrationSite = concentrationSite;


        this.myGroup = -1;
        this.group_position = -1;

        vt = new VectorTimestamp(VECTOR_TIMESTAMP_SIZE, (id + 1));

        // clock ????
        this.agility = getAgility();
        setThiefDisplacement(this.id, this.agility);

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
                    boolean getBusyLadrao = getBusyThief(this.id);

                    if (!getBusyLadrao && this.myGroup == -1) {
                        amINeeded(this.id);
                    } else {
                        this.myGroup = getThiefGroup(this.id);
                        this.group_position = getGroupPosition(this.id, this.myGroup);

                        if (this.myGroup != -1 && this.group_position != -1) {
                            prepareExcursion(this.id);
                        }

                    }
                    break;


                case CRAWLING_INWARDS:
                    waitMyTurn(this.id, this.myGroup);

                    posicao = crawlIn(this.id, this.agility, this.myGroup, this.group_position);

                    int getDistanciaSala = getRoomDistance(this.myGroup);


                    if (posicao == getDistanciaSala) {
                        atARoom(this.id);
                    }
                    break;

                case AT_A_ROOM:

                    quadro = rollACanvas(this.myGroup);

                    int room = getAssaultRoom(this.myGroup);

                    if (this.myGroup == 0) {
                        setAP1_canvas(this.group_position, quadro, room);
                    } else if (this.myGroup == 1) {
                        setAP2_canvas(this.group_position, quadro, room);
                    }

                    reverseDirection(this.id);
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        waitMyTurn(this.id, this.myGroup);
                        posicao = crawlOut(this.id, this.agility, this.myGroup, this.group_position);
                    }

                    if (this.myGroup == 0) {
                        setAP1_pos(this.group_position, posicao);
                    } else if (this.myGroup == 1) {
                        setAP2_pos(this.group_position, posicao);
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            // no clock
                            int getSalaAssalto = getAssaultRoom(this.myGroup);
                            // no clock
                            int getPosGrupo = getGroupPosition(this.id, this.myGroup);
                            handACanvas(this.id, getSalaAssalto, this.myGroup, getPosGrupo);

                        } else {
                            // no clock
                            int getSalaAssalto = getAssaultRoom(this.myGroup);
                            // no clock
                            int getPosGrupo = getGroupPosition(this.id, this.myGroup);
                            flagEmptyRoom(getSalaAssalto, this.myGroup, getPosGrupo);
                        }

                        if (this.myGroup == 0) setAP1_reset(this.group_position, this.id);
                        else if (this.myGroup == 1) setAP2_reset(this.group_position, this.id);

                        this.myGroup = -1;
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_reset(int pos_grupo, int id) {
        try {
            this.generalRepository.setAP1_reset(pos_grupo, id, vt.clone());
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        try {
            this.generalRepository.setAP2_pos(pos_grupo, posicao, vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        try {
            this.generalRepository.setAP1_pos(pos_grupo, posicao, vt.clone());
        } catch (RemoteException e) {
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
                    this.group.crawlOut(id, agilidade, meuGrupo, pos_grupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_canvas(int pos_grupo, boolean quadro, int room) {
        try {
            this.generalRepository.setAP2_canvas(pos_grupo, quadro, room, vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP1_canvas(int pos_grupo, boolean quadro, int room) {
        try {
            this.generalRepository.setAP1_canvas(pos_grupo, quadro, room, vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getAssaultRoom(int meuGrupo) {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getAssaultRoom(meuGrupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
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
            vt.increment();
            Tuple<VectorTimestamp, Boolean> tuple =
                    this.group.rollACanvas(meuGrupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getRoomDistance(int meuGrupo) {
        int ret = -1;

        try {
            vt.increment();
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.group.getRoomDistance(meuGrupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
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
                    this.group.crawlIn(id, agilidade, meuGrupo, pos_grupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void waitMyTurn(int id, int meuGrupo) {

        try {
//            vt.increment();
            VectorTimestamp clock = this.group.waitMyTurn(id, meuGrupo, vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getGroupPosition(int id, int meuGrupo) {
        int ret = -1;

        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = this.group.getGroupPosition(id, meuGrupo, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int getThiefGroup(int id) {
        int ret = -1;
        try {
//            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getThiefGroup(id, vt.clone());
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void amINeeded(int id) {

        try {
//            vt.increment();
            VectorTimestamp clock = concentrationSite.amINeeded(id, vt.clone());
            vt.update(clock);
        } catch (RemoteException e) {
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
            vt.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void setThiefState(int id, int stat) {
        try {
            this.generalRepository.setThiefState(id, stat, vt.clone());
        } catch (RemoteException e) {
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
            vt.update(tuple.getClock());
            state = tuple.getSecond();
        } catch (RemoteException e) {
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
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getAgility() {
        int ret = -1;
        try {
            vt.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getAgility(id, vt.clone());
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
            this.agility = tuple.getSecond();
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }


    private void setThiefDisplacement(int id, int agilidade) {
        try {
            this.generalRepository.setThiefDisplacement(id, agilidade, vt.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
