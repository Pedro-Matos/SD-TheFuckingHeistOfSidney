package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface ConcentrationSiteInterface extends Remote {

    public Tuple<VectorTimestamp, Integer> getThiefGroup(int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> imReady(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getNumberOfThieves(int id, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getThiefState(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> amINeeded(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> callThief(int grupo, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Boolean> getBusyThief(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> prepareExcursion(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> reverseDirection(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> atARoom(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> flagArrival(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getAgility(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;;

    public Tuple<VectorTimestamp, Integer> waitForThieves(VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> waitForThievesEnd(VectorTimestamp vectorTimestamp) throws RemoteException;

}
