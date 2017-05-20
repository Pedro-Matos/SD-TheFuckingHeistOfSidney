package RemoteMethodInvocation.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface CollectionSiteInterface extends Remote {

    public int checkGrupos() throws RemoteException;

    public int entrarGrupo(int ladraoID, int grupo) throws RemoteException;

    public boolean grupoCheio(int grupoID)  throws RemoteException;

    public int getEstadoChefe() throws RemoteException;

    public void startOperations() throws RemoteException;

    public boolean prepareAssaultParty(int idGrupo) throws RemoteException;

    public boolean takeARest() throws RemoteException;

    public int getNrElemGrupo(int grupoID) throws RemoteException;

    public void handACanvas(int ladraoID, int sala, int grupo, int pos) throws RemoteException;

    public void indicarSalaVazia(int sala, int grupo, int pos) throws RemoteException;

    public int getPosGrupo(int id, int grupo) throws RemoteException;

    public boolean checkEmptyMuseu() throws RemoteException;

    public void sumUpResults() throws RemoteException;

    public int getQuadrosRoubados() throws RemoteException;

    public boolean checkSalasLivres() throws RemoteException;

    public int getSalaAssalto(int grupoID) throws RemoteException;

    public int get_paitings(int sala) throws RemoteException;

}
