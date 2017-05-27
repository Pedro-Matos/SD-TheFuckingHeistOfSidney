package interfaces;

import support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface GeneralRepositoryInterface extends Remote {

    public void startLog(VectorTimestamp vectorTimestamp) throws RemoteException;

    public void add_log(VectorTimestamp vectorTimestamp) throws RemoteException;

    public void finalizarRelatorio(int total, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setDistanciaSala(int nrSala, int distancia, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setNrQuadrosSala(int nrSala, int nrQuadrosSala, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setMasterThiefState(int state, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setThiefState(int id, int state, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setThiefSituation(int id, int situation, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setThiefDisplacement(int id, int disp, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAssaultParty1_room(int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAssaultParty2_room(int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP1_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP1_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP1_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP1_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP2_pos(int pos_grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP2_canvas(int pos_grupo, boolean cv, int room, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP2_pos_id_canvas(int pos_grupo, int id, int pos, boolean cv, VectorTimestamp vectorTimestamp) throws RemoteException;

    public void setAP2_reset(int pos_grupo, int id, VectorTimestamp vectorTimestamp) throws RemoteException;

}