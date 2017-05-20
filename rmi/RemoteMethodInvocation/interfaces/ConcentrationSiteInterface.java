package RemoteMethodInvocation.interfaces;

import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface ConcentrationSiteInterface extends Remote {

    public int getGrupoLadrao(int id) throws RemoteException;

    public void estouPronto(int ladraoID) throws RemoteException;

    public int getNrLadroes() throws RemoteException;

    public int getStateLadrao(int ladraoID) throws RemoteException;

    public void amINeeded(int ladraoID) throws RemoteException;

    public int chamaLadrao(int grupo) throws RemoteException;

    public boolean getBusyLadrao(int ladraoID) throws RemoteException;

    public void prepareExcursion(int ladraoID) throws RemoteException;

    public void reverseDirection(int ladraoID) throws RemoteException;

    public void naSala(int ladraoID) throws RemoteException;

    public void indicarChegada(int ladraoID) throws RemoteException;

    public Tuple<VectorTimestamp, Integer> getAgilidade(int ladraoID, VectorTimestamp vectorTimestamp) throws RemoteException;;

    public void esperaLadroes() throws RemoteException;

    public void esperaLadroesFim() throws RemoteException;

}
