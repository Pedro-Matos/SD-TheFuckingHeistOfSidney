package ConcorrentVersion.Regioes.gruposAssalto;

import ConcorrentVersion.ParametrosDoProblema.GeneralRepository;
import ConcorrentVersion.Regioes.museu.Museum;

/**
 * @author Pedro Matos and Tiago Bastos
 */
public class GestorGruposAssalto  {

    /**
     * Assault Groups
     */
    private GrupoAssalto grupo[] = new GrupoAssalto[2];
    /**
     * Museum
     */
    private Museum museum;
    /**
     * General Repository
     */
    private GeneralRepository general;

    /**
     *
     * @param museum Museum
     * @param general General Repository
     */
    public GestorGruposAssalto(Museum museum, GeneralRepository general) {
        this.museum = museum;
        this.general = general;
    }

    /**
     * Group Formation
     * @param idGrupo group id
     * @param nrSala room id
     * @return true if creation is successful
     */
    public synchronized boolean formarGrupo(int idGrupo, int nrSala) {
        if (grupo[idGrupo] == null) {
            grupo[idGrupo] = new GrupoAssalto(museum, nrSala,idGrupo, general);
            return true;
        }

        return false;
    }

    public int getPos(int ladraoID, int idGrupo){
        return grupo[idGrupo].getPos(ladraoID);
    }


    /**
     * Destroys the group
     * @param idGrupo group id
     */
    public synchronized void desfazerGrupo(int idGrupo) {
        grupo[idGrupo] = null;
    }

    /**
     * Places thief in the group
     * @param ladraoID thief id
     * @param idGrupo group id
     */
    public void entrar(int ladraoID, int idGrupo, int pos_grupo) {
         grupo[idGrupo].entrar(ladraoID,pos_grupo);
    }

    /**
     * Calls crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public int crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo){
        return grupo[idGrupo].rastejarIn(ladraoID, agilidade,idGrupo,posgrupo);
    }

    /**
     * Calls crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @return final position
     */
    public int crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo){
        return grupo[idGrupo].rastejarOut(ladraoID, agilidade,idGrupo, posgrupo);
    }


    /**
     * Get distance of room
     * @param idGrupo group id
     * @return distance
     */
    public int getDistanciaSala(int idGrupo){
        return grupo[idGrupo].getDistanciaSala();
    }

    /**
     * Steal paiting
     * @param idGrupo group id
     * @return true if paiting was stolen
     */
    public boolean rollACanvas(int idGrupo){
        return grupo[idGrupo].roubarQuadro();
    }

    /**
     * Thief is waiting for it's turn
     * @param id Thief id
     * @param idGrupo Group id
     */
    public void waitMinhaVez(int id, int idGrupo) {
        grupo[idGrupo].waitMinhaVez(id);
    }

    
}