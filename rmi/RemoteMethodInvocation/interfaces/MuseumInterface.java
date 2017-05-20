package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface MuseumInterface extends Remote {

    public boolean rollACanvas(int nr_sala, VectorTimestamp vectorTimestamp) throws RemoteException;

    public int getDistancia(int nr_sala) throws RemoteException;

    public int getNumeroQuadros(int nr_sala) throws RemoteException;

}
