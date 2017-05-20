package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface CollectionSiteInterface extends Remote {

    public Tuple<VectorTimestamp, Integer> checkGroups(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> joinAssaultParty(int ladraoID, int grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public boolean grupoCheio(int grupoID)  throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getMasterThiefState(int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> startOperations(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> prepareAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> takeARest(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getNumberElemGroup(int grupoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> handACanvas(int ladraoID, int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> flagEmptyRoom(int sala, int grupo, int pos, VectorTimestamp vectorTimestamp) throws RemoteException;

    public int getPosGrupo(int id, int grupo) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> checkEmptyMuseum(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> sumUpResults(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getNumberofStolenPaints(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> checkEmptyRooms(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getAssaultRoom(int grupoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public int get_paitings(int sala) throws RemoteException;

}
