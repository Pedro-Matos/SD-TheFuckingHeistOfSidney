package interfaces;

import support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that defines the operations available over the objects that
 * represent the General Repository
 *
 * @author Pedro Matos
 * @author Tiago Bastos
 */
public interface GeneralRepositoryInterface extends Remote {
    /**
     * The first lines to be written in the file.
     *
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void startLog(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Function that writes when a change occurs. It checks for repetitions and do not allow them.
     *
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void add_log(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Function called to finish the report. It shows the number of stollen paitings.
     *
     * @param total           total of paitings
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void finalizarRelatorio(int total, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for the distance
     *
     * @param nrSala          id of room
     * @param distancia       distance from the room to the CollectionSite
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setDistanciaSala(int nrSala, int distancia, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for the number of paitings
     *
     * @param nrSala          id of room
     * @param nrQuadrosSala   number of paitings
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setNrQuadrosSala(int nrSala, int nrQuadrosSala, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for Master Thief state
     *
     * @param state           Master Thief state
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setMasterThiefState(int state, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for Ordinary Thief state
     *
     * @param id              id of thief
     * @param state           state of thief
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setThiefState(int id, int state, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for Ordinary Thief situation
     *
     * @param id              id of thief
     * @param situation       situation of thief
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setThiefSituation(int id, int situation, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Ordinary Thief Agility
     *
     * @param id              id of thief
     * @param disp            agility of thief
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setThiefDisplacement(int id, int disp, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set room for assault party n1
     *
     * @param room            id of room
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAssaultParty1_room(int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set room for assault party n2
     *
     * @param room            id of room
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAssaultParty2_room(int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set Ordinary Thief's position
     *
     * @param pos_grupo       thief position in group
     * @param pos             thief position to the room
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP1_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set for the presence of paiting
     *
     * @param pos_grupo       thief position in group
     * @param cv              paiting
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP1_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Multiple Set
     *
     * @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param pos             thief's position
     * @param cv              paiting
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP1_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Reset
     *
     * @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP1_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set position
     *
     * @param pos_grupo       thief position in group
     * @param pos             position to the room
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP2_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Set paiting
     *
     * @param pos_grupo       thief position in group
     * @param cv              paiting
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP2_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Multiple Set
     *
     * @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param pos             thief's position
     * @param cv              paiting
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP2_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Reset
     *
     * @param pos_grupo       thief position in group
     * @param id              thief's id
     * @param vectorTimestamp
     * @throws RemoteException
     */
    public void setAP2_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Function that flags the end of the program
     *
     * @throws RemoteException
     */
    public void finished() throws RemoteException;
}