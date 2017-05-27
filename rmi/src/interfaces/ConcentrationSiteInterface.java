package interfaces;

import support.Tuple;
import support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface ConcentrationSiteInterface extends Remote {

    public Tuple<VectorTimestamp, Integer> getThiefGroup(int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp imReady(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getNumberOfThieves(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getThiefState(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp amINeeded(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> callThief(int grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> getBusyThief(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp prepareExcursion(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp reverseDirection(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp atARoom(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp flagArrival(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getAgility(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;;

    public VectorTimestamp waitForThieves(VectorTimestamp vectorTimestamp) throws RemoteException;

    public VectorTimestamp waitForThievesEnd(VectorTimestamp vectorTimestamp) throws RemoteException;

}
