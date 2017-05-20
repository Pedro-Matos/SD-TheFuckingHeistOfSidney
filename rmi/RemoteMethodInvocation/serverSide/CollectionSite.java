package RemoteMethodInvocation.serverSide;

import RemoteMethodInvocation.interfaces.*;
import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import static RemoteMethodInvocation.support.Constantes.*;

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
     * Museum
     */
    private final MuseumInterface museum;
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
     *  @param museum Museum
     * @param concentrationSite CollectionSite
     * @param gestorGrupos Manager of assault partys
     * @param generalRepository General Repository
     */
    public CollectionSite(MuseumInterface museum, ConcentrationSiteInterface concentrationSite,
                          AssaultPartyManagerInterface gestorGrupos, GeneralRepositoryInterface generalRepository) {
        this.concentrationSite = concentrationSite;
        this.museum = museum;
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

    }

    /**
     * Checks if the groups are done
     *
     * @return -1 if they are or 0/1 if they aren't.
     * @param id
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
                    this.gestorGrupos.entrar(ladraoID,grupo,i);
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
    public synchronized Tuple<VectorTimestamp, Integer> startOperations(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.estadoChefe = DECIDING_WHAT_TO_DO;
        general.setMasterThiefState(this.estadoChefe, local.clone());
        return new Tuple<>(local.clone(), -1);
    }

    /**
     * Creates assault party
     * @param idGrupo if of party
     * @return true if formed, false if not.
     */
    public synchronized Tuple<VectorTimestamp, Boolean>  prepareAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp){

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

        if(idGrupo == 0) general.setAssaultParty1_room(j, );
        if(idGrupo == 1) general.setAssaultParty2_room(j, );


        Tuple<VectorTimestamp, Boolean> tuple = gestorGrupos.formarGrupo(idGrupo, j);


        return tuple;
    }


    /**
     * Master is resting
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Boolean> takeARest(VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        int nrLadroes = concentrationSite.getNumberOfThieves(id, vectorTimestamp);

        int checkGrupos = this.checkGroups(vectorTimestamp);

        if (checkGrupos == -1 || nrLadroes < NUM_GROUP) {
            if (this.estadoChefe == ASSEMBLING_A_GROUP) {
                this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
                general.setMasterThiefState(this.estadoChefe, );
                return new Tuple<>(local.clone(), false);
            }
            this.estadoChefe = WAITING_FOR_GROUP_ARRIVAL;
            general.setMasterThiefState(this.estadoChefe, );

            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            this.estadoChefe = DECIDING_WHAT_TO_DO;
            general.setMasterThiefState(this.estadoChefe, );

        }
        return new Tuple<>(local.clone(), false);
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
    public synchronized Tuple<VectorTimestamp, Integer> handACanvas(int ladraoID, int sala, int grupo, int pos, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        this.quadrosRoubados++;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            this.gestorGrupos.desfazerGrupo(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                general.setMasterThiefState(this.estadoChefe, );
            }
            notifyAll();
        }

        if(grupo == 0){
            general.setAP1_reset(pos,ladraoID, );
        }
        else if (grupo == 1){
            general.setAP2_reset(pos,ladraoID, );
        }

        return new Tuple<>(local.clone(), -1);
    }

    /**
     * Room is empty
     * @param sala room id
     * @param grupo group id
     * @param pos position in the group
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Integer> flagEmptyRoom(int sala, int grupo, int pos, VectorTimestamp vectorTimestamp){

        local.update(vectorTimestamp);

        this.salaVazia[sala] = true;
        this.grupos[grupo][pos] = -1;
        if (--this.nrElemGrupo[grupo] == 0) {
            this.salaAssalto[sala] = -1;
            this.gestorGrupos.desfazerGrupo(grupo);
            this.grupoOcup[grupo] = false;
            if (this.estadoChefe == WAITING_FOR_GROUP_ARRIVAL) {
                this.estadoChefe = DECIDING_WHAT_TO_DO;
                general.setMasterThiefState(this.estadoChefe, );
            }
            notifyAll();
        }


        return new Tuple<>(local.clone(), -1);
    }

    /**
     * Verifies the position in the group
     * @param id thief id
     * @param grupo group id
     * @return position in the group
     */
    public int getPosGrupo(int id, int grupo) {

        for (int i = 0; i < NUM_GROUP; i++) {
            if (grupos[grupo][i] == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Verifies if there are paitings in the museum
     * @return true if empty
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Boolean> checkEmptyMuseum(VectorTimestamp vectorTimestamp) {
        for (int i = 0; i < salaVazia.length; i++) {
            if (!salaVazia[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Show the results of the assault
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Boolean> sumUpResults(VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        this.estadoChefe = PRESENTING_THE_REPORT;
        general.setMasterThiefState(this.estadoChefe, );
        return new Tuple<>(local.clone(), false);
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

    /**
     * Get the number of paitings in the room
     * @param sala room id
     * @return number of paitings
     */
    public int get_paitings(int sala){
       return museum.getNumeroQuadros(sala);
    }

}
