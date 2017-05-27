package serverSide.collectionSite;

import interfaces.*;
import registry.RegistryConfig;
import support.Constantes;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static support.Constantes.*;

/**
 * Class that descibes the Collection Site
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public class CollectionSite implements CollectionSiteInterface {
    /**
     * CollectionSite
     */
    private final ConcentrationSiteInterface concentrationSite;
    /**
     * Assault partys manager
     */
    private final AssaultPartyManagerInterface groupManager;
    /**
     * Number of groups
     */
    private int countGroups = 0;
    /**
     * Master Thief state
     */
    private int masterThiefState;
    /**
     * Empty rooms
     */
    private boolean[] emptyRooms = new boolean[NUM_ROOMS];
    /**
     * Assault Partys
     */
    private int[][] groups = new int[2][NUM_GROUP];
    /**
     * Nr of elements in the groups
     */
    private int[] numberElemGroup = new int[2];
    /**
     * Total of stolen paintings
     */
    private int stolenPaintings;
    /**
     * Rooms occupied
     */
    private int[] assaultRoom = new int[NUM_ROOMS];
    /**
     * Groups occupied
     */
    private boolean groupsOccu[] = new boolean[2];
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;
    /**
     * Local Vector Timestamp
     */
    private VectorTimestamp local;

    /**
     * @param concentrationSite CollectionSite Interface
     * @param gestorGrupos      Manager of assault partys Interface
     * @param generalRepository General Repository Interface
     */
    public CollectionSite(ConcentrationSiteInterface concentrationSite, AssaultPartyManagerInterface gestorGrupos,
                          GeneralRepositoryInterface generalRepository) {
        this.concentrationSite = concentrationSite;
        this.groupManager = gestorGrupos;

        // Estado inicial do chefe
        masterThiefState = PLANNING_THE_HEIST;


        numberElemGroup[0] = 0;
        numberElemGroup[1] = 0;
        countGroups = 0;
        groupsOccu[0] = false;
        groupsOccu[1] = false;

        this.general = generalRepository;

        for (int i = 0; i < NUM_GROUP; i++) {
            groups[0][i] = -1;
            groups[1][i] = -1;
        }

        for (int i = 0; i < NUM_ROOMS; i++) {
            this.emptyRooms[i] = false;
            this.assaultRoom[i] = -1;
        }

        this.stolenPaintings = 0;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);


    }

    /**
     * Checks if the groups are done
     *
     * @param vectorTimestamp clock
     * @return clock and int with the id of the group
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> checkGroups(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        if (!this.groupsOccu[0]) {

            return new Tuple<>(local.clone(), 0);
        } else if (!this.groupsOccu[1]) {

            return new Tuple<>(local.clone(), 1);
        } else {

            return new Tuple<>(local.clone(), -1);
        }
    }

    /**
     * Adds thief to an assault party
     *
     * @param ladraoID        thief id
     * @param grupo           group id
     * @param vectorTimestamp clock
     * @return clock and the Position in the group. -1 if the group is full
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> joinAssaultParty(int ladraoID, int grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        boolean cond = this.grupoCheio(grupo);

        if (!cond) {
            for (int i = 0; i < NUM_GROUP; i++) {
                if (this.groups[grupo][i] == -1) {
                    this.numberElemGroup[grupo]++;
                    this.groups[grupo][i] = ladraoID;
                    //break;
                    joinAssaultParty(ladraoID, grupo, i);
                    return new Tuple<>(local.clone(), i);
                }
            }
        }

        return new Tuple<>(local.clone(), -1);
    }

    /**
     * Verifies master thief state
     *
     * @param id
     * @param vectorTimestamp clock
     * @return clock and the master thief state
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getMasterThiefState(int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.masterThiefState);
    }

    /**
     * Starts the assault
     *
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp startOperations(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.masterThiefState = DECIDING_WHAT_TO_DO;
        setMasterThiefState(this.masterThiefState);
        return local.clone();
    }


    /**
     * Creates assault party
     *
     * @param idGrupo if of party
     * @return clock and true if formed, false if not.
     */
    @Override
    public synchronized VectorTimestamp prepareAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.groupsOccu[idGrupo] = true;
        this.countGroups++;
        this.masterThiefState = ASSEMBLING_A_GROUP;
        //general.setMasterThiefState(this.masterThiefState);

        int j;
        for (j = 0; j < NUM_ROOMS; j++) {

            if (this.assaultRoom[j] == -1 && !this.emptyRooms[j]) {
                this.assaultRoom[j] = idGrupo;
                break;
            }
        }

        if (idGrupo == 0) setAssaultParty1_room(j);
        if (idGrupo == 1) setAssaultParty2_room(j);


        createAssaultParty(idGrupo, j);


        return local.clone();
    }


    /**
     * Master is resting
     *
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp takeARest(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        // no clock
        int nrLadroes = getNumberOffThieves();

        int checkGrupos = this.checkGroups(local.clone()).getSecond();

        if (checkGrupos == -1 || nrLadroes < NUM_GROUP) {
            if (this.masterThiefState == ASSEMBLING_A_GROUP) {
                this.masterThiefState = WAITING_FOR_GROUP_ARRIVAL;
                setMasterThiefState(this.masterThiefState);
                return local.clone();
            }
            this.masterThiefState = WAITING_FOR_GROUP_ARRIVAL;
            setMasterThiefState(this.masterThiefState);

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            this.masterThiefState = DECIDING_WHAT_TO_DO;
            setMasterThiefState(this.masterThiefState);

        }
        return local.clone();
    }


    /**
     * Returns the number of elements in the group
     *
     * @param grupoID         id of group
     * @param vectorTimestamp clock
     * @return clock number of elements
     */
    @Override
    public synchronized Tuple<VectorTimestamp, Integer> getNumberElemGroup(int grupoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.numberElemGroup[grupoID]);
    }


    /**
     * Hand canvas to the thief and leave the group
     *
     * @param ladraoID        thief id
     * @param sala            room id
     * @param grupo           group id
     * @param pos             position in group
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp handACanvas(int ladraoID, int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.stolenPaintings++;
        this.groups[grupo][pos] = -1;
        if (--this.numberElemGroup[grupo] == 0) {
            this.assaultRoom[sala] = -1;
            destroyAssaultParty(grupo);
            this.groupsOccu[grupo] = false;
            if (this.masterThiefState == WAITING_FOR_GROUP_ARRIVAL) {
                this.masterThiefState = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.masterThiefState);
            }
            notifyAll();
        }

        if (grupo == 0) {
            setAP1_reset(pos, ladraoID);
        } else if (grupo == 1) {
            setAP2_reset(pos, ladraoID);
        }

        return local.clone();
    }


    /**
     * Room is empty
     *
     * @param sala            room id
     * @param grupo           group id
     * @param pos             position in the group
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public synchronized VectorTimestamp flagEmptyRoom(int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.emptyRooms[sala] = true;
        this.groups[grupo][pos] = -1;
        if (--this.numberElemGroup[grupo] == 0) {
            this.assaultRoom[sala] = -1;
            destroyAssaultParty(grupo);
            this.groupsOccu[grupo] = false;
            if (this.masterThiefState == WAITING_FOR_GROUP_ARRIVAL) {
                this.masterThiefState = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.masterThiefState);
            }
            notifyAll();
        }


        return local.clone();
    }


    /**
     * Verifies if there are paitings in the museum
     *
     * @param vectorTimestamp clock
     * @return clock and true if museum is empty
     */
    @Override
    public Tuple<VectorTimestamp, Boolean> checkEmptyMuseum(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        for (int i = 0; i < emptyRooms.length; i++) {
            if (!emptyRooms[i]) {
                return new Tuple<>(local.clone(), false);
            }
        }
        return new Tuple<>(local.clone(), true);
    }

    /**
     * Show the results of the assault
     *
     * @param vectorTimestamp clock
     * @return clock
     */
    @Override
    public VectorTimestamp sumUpResults(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.masterThiefState = PRESENTING_THE_REPORT;
        setMasterThiefState(this.masterThiefState);
        return local.clone();
    }

    /**
     * Number of stollen paitings
     *
     * @param vectorTimestamp clock
     * @return clock and number of paintings
     */
    @Override
    public Tuple<VectorTimestamp, Integer> getNumberofStolenPaints(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        return new Tuple<>(local.clone(), this.stolenPaintings);
    }

    /**
     * Checks if there are rooms with paitings
     *
     * @param vectorTimestamp clock
     * @return clock and true if the rooms aren't empty
     */
    @Override
    public Tuple<VectorTimestamp, Boolean> checkEmptyRooms(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (!emptyRooms[i] && assaultRoom[i] == -1) {
                //salasLivres = true;
                return new Tuple<>(local.clone(), true);
            }
        }
        return new Tuple<>(local.clone(), false);
    }


    /**
     * Returns the room the group is assaulting
     *
     * @param grupoID         group id
     * @param vectorTimestamp clock
     * @return clock and room id
     */
    @Override
    public Tuple<VectorTimestamp, Integer> getAssaultRoom(int grupoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (this.assaultRoom[i] == grupoID) {
                return new Tuple<>(local.clone(), i);
            }
        }
        return new Tuple<>(local.clone(), -1);
    }

    /**
     * This function is used for the log to signal the AssaultPartyManager to shutdown.
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro ao localizar o registo");
            ex.printStackTrace();
            System.exit(1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_COLSITE_NAME;


        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Collection registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Collection not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println("Collection Site closed.");
    }


    private void joinAssaultParty(int ladraoID, int grupo, int i) {
        try {
            local.increment();
            VectorTimestamp clock = this.groupManager.joinAssaultParty(ladraoID, grupo, i, local.clone());
            local.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setMasterThiefState(int stat) {
        try {
            this.general.setMasterThiefState(stat, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private synchronized boolean grupoCheio(int grupoID) {

        if (this.numberElemGroup[grupoID] != NUM_GROUP) {
            return false;
        } else {
            return true;
        }
    }

    private boolean createAssaultParty(int idGrupo, int j) {
        boolean ret = false;

        try {
            local.increment();
            Tuple<VectorTimestamp, Boolean> tuple = this.groupManager.createAssaultParty(idGrupo, j, local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }


    private void setAssaultParty1_room(int j) {
        try {
            this.general.setAssaultParty1_room(j, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAssaultParty2_room(int j) {
        try {
            this.general.setAssaultParty2_room(j, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void destroyAssaultParty(int grupo) {

        try {
            local.increment();
            VectorTimestamp clock = this.groupManager.destroyAssaultParty(grupo, local.clone());
            local.update(clock);
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void setAP1_reset(int pos, int ladraoID) {
        try {
            this.general.setAP1_reset(pos, ladraoID, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_reset(int pos, int ladraoID) {
        try {
            this.general.setAP2_reset(pos, ladraoID, local.clone());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getNumberOffThieves() {
        int ret = -1;

        try {
//            local.increment();
            Tuple<VectorTimestamp, Integer> tuple =
                    this.concentrationSite.getNumberOfThieves(local.clone());
            ret = tuple.getSecond();
//            local.update(tuple.getClock());
        } catch (RemoteException e) {
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

}
