package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface MuseumInterface extends Remote {

    public Tuple<VectorTimestamp, Boolean> rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getMuseumRoomDistance(int nr_sala, VectorTimestamp clone) throws RemoteException;

}
