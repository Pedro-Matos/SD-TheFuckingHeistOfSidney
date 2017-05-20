package RemoteMethodInvocation.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface MuseumInterface extends Remote {

    public boolean roubarQuadro(int nr_sala) throws RemoteException;

    public int getDistancia(int nr_sala) throws RemoteException;

    public int getNumeroQuadros(int nr_sala) throws RemoteException;

}
