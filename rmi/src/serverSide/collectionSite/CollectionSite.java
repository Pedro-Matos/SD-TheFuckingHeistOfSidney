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
 * Monitor Collection Site
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class CollectionSite implements CollectionSiteInterface {
    /**
     * CollectionSite
     */
    private final ConcentrationSiteInterface concentrationSite;
    /**
     * Assault partys manager
     */
    private final AssaultPartyManagerInterface gestorGrupos;
    /**
     * Master Thief state
     */
    private int estadoChefe;
    /**
     * Empty rooms
     */
    private boolean[] salaVazia = new boolean[NUM_ROOMS];
    /**
     * Assault Partys
     */
    private int[][] grupos = new int[2][NUM_GROUP];
    /**
     * Nr of elements in the groups
     */
    private int[] nrElemGrupo = new int[2];
    /**
     * Total of stollen paintings
     */
    private int quadrosRoubados;
    /**
     * Rooms occupied
     */
    private int[] salaAssalto = new int[NUM_ROOMS];

    /**
     * Groups occupied
     */
    private boolean grupoOcup[] = new boolean[2];

    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;


    private VectorTimestamp local;


    /**
     * Number of groups
     */
    int countGrupos = 0;

    /**
     * @param concentrationSite CollectionSite
     * @param gestorGrupos Manager of assault partys
     * @param generalRepository General Repository
     */
    public CollectionSite(ConcentrationSiteInterface concentrationSite, AssaultPartyManagerInterface gestorGrupos,
                          GeneralRepositoryInterface generalRepository) {
        this.concentrationSite = concentrationSite;
        this.gestorGrupos = gestorGrupos;

        // Estado inicial do chefe
        estadoChefe = PLANNING_THE_HEIST;


        nrElemGrupo[0] = 0;
        nrElemGrupo[1] = 0;
        countGrupos = 0;
        grupoOcup[0] = false;
        grupoOcup[1] = false;

        this.general = generalRepository;

        for (int i = 0; i < NUM_GROUP; i++) {
            grupos[0][i] = -1;
            grupos[1][i] = -1;
        }

        for (int i = 0; i < NUM_ROOMS; i++) {
            this.salaVazia[i] = false;
            this.salaAssalto[i] = -1;
        }

        this.quadrosRoubados = 0;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);


    }

    /**
     * Checks if the groups are done
     *
     * @return -1 if they are or 0/1 if they aren't.
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Integer> checkGroups(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        if (!this.grupoOcup[0]) {

            return new Tuple<>(local.clone(), 0);
        } else if (!this.grupoOcup[1]) {

            return new Tuple<>(local.clone(), 1);
        } else {

            return new Tuple<>(local.clone(), -1);
        }
    }

    /**
     * Adds thief to an assault party
     *
     * @param ladraoID thief id
     * @param grupo group id
     * @param vectorTimestamp
     * @return Position in the group. -1 if the group is full
     */
    public synchronized Tuple<VectorTimestamp, Integer> joinAssaultParty(int ladraoID, int grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        boolean cond = this.grupoCheio(grupo);

        if (!cond) {
            for (int i = 0; i < NUM_GROUP; i++) {
                if (this.grupos[grupo][i] == -1) {
                    this.nrElemGrupo[grupo]++;
                    this.grupos[grupo][i] = ladraoID;
                    //break;
                    joinAssaultParty(ladraoID,grupo,i);
                    return new Tuple<>(local.clone(), i);
                }
            }
        }

        return new Tuple<>(local.clone(), -1);
    }


    /**
     * Verifies if the group is full
     *
     * @param grupoID group id
     * @return false if it isn't full; true if is
     */
    public synchronized boolean grupoCheio(int grupoID) {

        if (this.nrElemGrupo[grupoID] != NUM_GROUP) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifies master thief state
     *
     * @return master thief state
     * @param id
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Integer> getMasterThiefState(int id, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.estadoChefe);
    }

    /**
     * Starts the assault
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp startOperations(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.estadoChefe = DECIDING_WHAT_TO_DO;
        setMasterThiefState(this.estadoChefe);
        return local.clone();
    }



    /**
     * Creates assault party
     * @param idGrupo if of party
     * @return true if formed, false if not.
     */
    public synchronized VectorTimestamp prepareAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        this.grupoOcup[idGrupo] = true;
        this.countGrupos++;
        this.estadoChefe = ASSEMBLING_A_GROUP;
        //general.setMasterThiefState(this.estadoChefe);

        int j;
        for (j = 0; j < NUM_ROOMS; j++) {

            if (this.salaAssalto[j] == -1 && !this.salaVazia[j]) {
                this.salaAssalto[j] = idGrupo;
                break;
            }
        }

        if(idGrupo == 0) setAssaultParty1_room(j);
        if(idGrupo == 1) setAssaultParty2_room(j);


        createAssaultParty(idGrupo, j);


        return local.clone();
    }



    /**
     * Master is resting
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp takeARest(VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        // no clock
        int nrLadroes = getNumberOffThieves();

        int checkGrupos = this.checkGroups(local.clone()).getSecond();

        if (checkGrupos == -1 || nrLadroes < NUM_GROUP) {
            if (this.estadoChefe == ASSEMBLING_A_GROUP) {
                this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
                setMasterThiefState(this.estadoChefe);
                return local.clone();
            }
            this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
            setMasterThiefState(this.estadoChefe);

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            this.estadoChefe = DECIDING_WHAT_TO_DO;
            setMasterThiefState(this.estadoChefe);

        }
        return local.clone();
    }


    /**
     * Returns the number of elements in the group
     * @param grupoID id of group
     * @param vectorTimestamp
     * @return number of elements
     */
    public synchronized Tuple<VectorTimestamp, Integer> getNumberElemGroup(int grupoID, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return new Tuple<>(local.clone(), this.nrElemGrupo[grupoID]);
    }



    /**
     * Hand canvas to the thief and leave the group
     * @param ladraoID thief id
     * @param sala room id
     * @param grupo group id
     * @param pos position in group
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp handACanvas(int ladraoID, int sala, int grupo, int pos, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        this.quadrosRoubados++;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            destroyAssaultParty(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.estadoChefe);
            }
            notifyAll();
        }

        if(grupo == 0){
            setAP1_reset(pos, ladraoID);
        }
        else if (grupo == 1){
            setAP2_reset(pos,ladraoID);
        }

        return local.clone();
    }



    /**
     * Room is empty
     * @param sala room id
     * @param grupo group id
     * @param pos position in the group
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp flagEmptyRoom(int sala, int grupo, int pos, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        this.salaVazia[sala] = true;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            destroyAssaultParty(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                setMasterThiefState(this.estadoChefe);
            }
            notifyAll();
        }


        return local.clone();
    }


    /**
     * Verifies if there are paitings in the museum
     * @return true if empty
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Boolean> checkEmptyMuseum(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        for (int i = 0; i < salaVazia.length; i++) {
            if (!salaVazia[i]) {
                return new Tuple<>(local.clone(), false);
            }
        }
        return new Tuple<>(local.clone(), true);
    }

    /**
     * Show the results of the assault
     * @param vectorTimestamp
     */
    public VectorTimestamp sumUpResults(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.estadoChefe = PRESENTING_THE_REPORT;
        setMasterThiefState(this.estadoChefe);
        return local.clone();
    }

    /**
     * Number of stollen paitings
     *
     * @return Number of stollen paitings
     * @param vectorTimestamp
     */

    public Tuple<VectorTimestamp, Integer> getNumberofStolenPaints(VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);
        return new Tuple<>(local.clone(), this.quadrosRoubados);
    }

    /**
     * Checks if there are rooms with paitings
     *
     * @return true if there are rooms with paitings
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Boolean> checkEmptyRooms(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (!salaVazia[i] && salaAssalto[i] == -1) {
                //salasLivres = true;
                return new Tuple<>(local.clone(), true);
            }
        }
        return new Tuple<>(local.clone(), false);
    }


    /**
     * Returns the room the group is assaulting
     *
     * @param grupoID group id
     * @param vectorTimestamp
     * @return room id
     */
    public Tuple<VectorTimestamp, Integer> getAssaultRoom(int grupoID, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        for (int i = 0; i < NUM_ROOMS; i++) {
            if (this.salaAssalto[i] == grupoID) {
                return new Tuple<>(local.clone(), i);
            }
        }
        return new Tuple<>(local.clone(), -1);
    }

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
            System.exit (1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_COLSITE_NAME;


        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Collection registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) {
            System.out.println("Collection not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            ex.printStackTrace();
            System.exit (1);
        }

        System.out.println("Collection Site closed.");
    }


    private void joinAssaultParty(int ladraoID, int grupo, int i){
        try {
            local.increment();
            VectorTimestamp clock = this.gestorGrupos.joinAssaultParty(ladraoID, grupo, i,  local.clone());
            local.update(clock);
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

    private boolean createAssaultParty(int idGrupo, int j){
        boolean ret = false;

        try {
            local.increment();
            Tuple<VectorTimestamp, Boolean> tuple = this.gestorGrupos.createAssaultParty(idGrupo, j,  local.clone());
            ret = tuple.getSecond();
            local.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }


    private void setAssaultParty1_room(int j){
        try {
            this.general.setAssaultParty1_room(j,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAssaultParty2_room(int j){
        try {
            this.general.setAssaultParty2_room(j,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void destroyAssaultParty(int grupo) {

        try {
            local.increment();
            VectorTimestamp clock = this.gestorGrupos.destroyAssaultParty(grupo,  local.clone());
            local.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    private void setAP1_reset(int pos, int ladraoID){
        try {
            this.general.setAP1_reset(pos, ladraoID,  local.clone());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void setAP2_reset(int pos, int ladraoID){
        try {
            this.general.setAP2_reset(pos, ladraoID,  local.clone());
        } catch (RemoteException e){
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
                    this.concentrationSite.getNumberOfThieves( local.clone());
            ret = tuple.getSecond();
//            local.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

}
