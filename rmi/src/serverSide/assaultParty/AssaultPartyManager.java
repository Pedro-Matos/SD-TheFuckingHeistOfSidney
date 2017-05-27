package serverSide.assaultParty;

import interfaces.AssaultPartyManagerInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import interfaces.Register;
import registry.RegistryConfig;
import support.Constantes;
import support.Tuple;
import support.VectorTimestamp;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaultPartyManager implements AssaultPartyManagerInterface {

    /**
     * Assault Groups
     */
    private AssaultParty grupo[] = new AssaultParty[2];
    /**
     * Museum
     */
    private MuseumInterface museum;
    /**
     * General Repository
     */
    private GeneralRepositoryInterface general;

    private VectorTimestamp local;

    /**
     *
     * @param museum Museum
     * @param general General Repository
     */
    public AssaultPartyManager(MuseumInterface museum, GeneralRepositoryInterface general) {
        this.museum = museum;
        this.general = general;

        local = new VectorTimestamp(Constantes.VECTOR_TIMESTAMP_SIZE, 0);
    }

    /**
     * Group Formation
     * @param idGrupo group id
     * @param nrSala room id
     * @param vectorTimestamp
     * @return true if creation is successful
     */
    public synchronized Tuple<VectorTimestamp, Boolean> createAssaultParty(int idGrupo, int nrSala, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        if (grupo[idGrupo] == null) {
            grupo[idGrupo] = new AssaultParty(museum, nrSala,idGrupo, general);
            return new Tuple<>(local.clone(), true);
        }

        return new Tuple<>(local.clone(), false);
    }

    public Tuple<VectorTimestamp, Integer> getPos(int ladraoID, int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);


        Tuple<VectorTimestamp, Integer> tuple = grupo[idGrupo].getPos(ladraoID, local.clone());

        return tuple;
    }


    /**
     * Destroys the group
     * @param idGrupo group id
     * @param vectorTimestamp
     */
    public synchronized VectorTimestamp destroyAssaultParty(int idGrupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        grupo[idGrupo] = null;
        return local.clone();
    }

    /**
     * Places thief in the group
     * @param ladraoID thief id
     * @param idGrupo group id
     * @param pos_grupo group position
     * @param vectorTimestamp
     */
    public VectorTimestamp joinAssaultParty(int ladraoID, int idGrupo, int pos_grupo, VectorTimestamp vectorTimestamp) {

        local.update(vectorTimestamp);

        return grupo[idGrupo].entrar(ladraoID,pos_grupo, vectorTimestamp);

    }

    /**
     * Calls crawl in
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public Tuple<VectorTimestamp, Integer> crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        return grupo[idGrupo].crawlIn(ladraoID, agilidade, idGrupo, posgrupo, vectorTimestamp);
    }

    /**
     * Calls crawl out
     * @param ladraoID thief id
     * @param agilidade thief agility
     * @param idGrupo group id
     * @param posgrupo position in the group
     * @param vectorTimestamp
     * @return final position
     */
    public Tuple<VectorTimestamp, Integer> crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        return grupo[idGrupo].crawlOut(ladraoID, agilidade, idGrupo, posgrupo, vectorTimestamp);
    }


    /**
     * Get distance of room
     * @param idGrupo group id
     * @param vectorTimestamp
     * @return distance
     */
    public Tuple<VectorTimestamp, Integer> getRoomDistance(int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        return grupo[idGrupo].getRoomDistance(local.clone());
    }

    /**
     * Steal paiting
     * @param idGrupo group id
     * @param vectorTimestamp
     * @return true if paiting was stolen
     */
    public Tuple<VectorTimestamp, Boolean> rollACanvas(int idGrupo, VectorTimestamp vectorTimestamp){
        local.update(vectorTimestamp);

        return grupo[idGrupo].rollACanvas(local.clone());
    }

    /**
     * Thief is waiting for it's turn
     * @param id Thief id
     * @param idGrupo Group id
     * @param vectorTimestamp
     */
    public VectorTimestamp waitMyTurn(int id, int idGrupo, VectorTimestamp vectorTimestamp) {
        local.update(vectorTimestamp);

        return grupo[idGrupo].waitMyTurn(id, local.clone());
    }

    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;

        rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            System.out.println("Erro ao localizar o registo");
            ex.printStackTrace();
            System.exit (1);
        }

        String nameEntryBase = RegistryConfig.RMI_REGISTER_NAME;
        String nameEntryObject = RegistryConfig.RMI_REGISTRY_ASSGMAN_NAME;


        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Assault Group registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        } catch (NotBoundException e) {
            System.out.println("Assault Group not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit (1);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            ex.printStackTrace();
            System.exit (1);
        }

        System.out.println("AssaultGroupManager closed.");
    }


}
