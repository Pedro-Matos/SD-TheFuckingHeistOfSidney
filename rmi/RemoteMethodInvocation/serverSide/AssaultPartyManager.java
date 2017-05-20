package RemoteMethodInvocation.serverSide;

import RemoteMethodInvocation.interfaces.AssaultPartyManagerInterface;
import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.interfaces.MuseumInterface;
import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

/**
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaultPartyManager implements AssaultPartyManagerInterface {

    /**
     * Assault Groups
     */
    private AssaultParty grupo[] = new AssaultParty[2];
    /**
     * Museum
     */
    private MuseumInterface museum;
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;

    private VectorTimestamp local;

    /**
     *
     * @param museum Museum
     * @param general General Repository
     */
    public AssaultPartyManager(MuseumInterface museum, GeneralRepositoryInterface general) {
        this.museum = museum;
        this.general = general;
    }

    /**
     * Group Formation
     * @param idGrupo group id
     * @param nrSala room id
     * @param vectorTimestamp
     * @return true if creation is successful
     */
    public synchronized Tuple<VectorTimestamp, Boolean> formarGrupo(int idGrupo, int nrSala, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (grupo[idGrupo] == null) {
            grupo[idGrupo] = new AssaultParty(museum, nrSala,idGrupo, general);
            return new Tuple<>(local.clone(), true);
        }

        return new Tuple<>(local.clone(), false);
    }

    public Tuple<VectorTimestamp, Integer> getPos(int ladraoID, int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);


        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].getPos(ladraoID, local.clone());

        return tuple;
    }


    /**
     * Destroys the group
     * @param idGrupo group id
     * @param vectorTimestamp
     */
    public synchronized Tuple<VectorTimestamp, Boolean> desfazerGrupo(int idGrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        grupo[idGrupo] = null;
        return new Tuple<>(local.clone(), false);
    }

    /**
     * Places thief in the group
     * @param ladraoID thief id
     * @param idGrupo group id
     * @param pos_grupo group position
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Integer> entrar(int ladraoID, int idGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        grupo[idGrupo].entrar(ladraoID,pos_grupo, vectorTimestamp);
        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].entrar(ladraoID,pos_grupo, vectorTimestamp);

        return tuple;
    }

    /**
     * Calls crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].crawlIn(ladraoID, agilidade, idGrupo, posgrupo, vectorTimestamp);

        return tuple;
    }

    /**
     * Calls crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].crawlOut(ladraoID, agilidade, idGrupo, posgrupo, vectorTimestamp);

        return tuple;    }


    /**
     * Get distance of room
     * @param idGrupo group id
     * @param vectorTimestamp
     * @return distance
     */
    public Tuple<VectorTimestamp, Integer> getRoomDistance(int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].getRoomDistance(local.clone());
        return tuple;
    }

    /**
     * Steal paiting
     * @param idGrupo group id
     * @param vectorTimestamp
     * @return true if paiting was stolen
     */
    public Tuple<VectorTimestamp, Boolean> rollACanvas(int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        Tuple<VectorTimestamp, Boolean> tuple = grupo[idGrupo].rollACanvas(local.clone());
        return tuple;
    }

    /**
     * Thief is waiting for it's turn
     * @param id Thief id
     * @param idGrupo Group id
     * @param vectorTimestamp
     */
    public Tuple<VectorTimestamp, Integer> waitMyTurn(int id, int idGrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].waitMinhaVez(id, local.clone());
        return tuple;
    }

    
}
