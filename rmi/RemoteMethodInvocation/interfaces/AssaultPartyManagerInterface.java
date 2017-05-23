package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface AssaultPartyManagerInterface extends Remote {

    public Tuple<VectorTimestamp, Boolean> formarGrupo(int idGrupo, int nrSala, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getPos(int ladraoID, int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp desfazerGrupo(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp entrar(int ladraoID, int idGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getRoomDistance(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> rollACanvas(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp waitMyTurn(int id, int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;



}
