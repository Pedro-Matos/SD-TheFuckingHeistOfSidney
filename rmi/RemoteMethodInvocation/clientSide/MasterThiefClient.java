package RemoteMethodInvocation.clientSide;

import RemoteMethodInvocation.interfaces.CollectionSiteInterface;
import RemoteMethodInvocation.interfaces.ConcentrationSiteInterface;
import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.registry.RegistryConfig;
import genclass.GenericIO;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public class MasterThiefClient {

    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        // Initialise RMI invocations
        GeneralRepositoryInterface generalRepositoryInterface = null;
        CollectionSiteInterface collectionSiteInterface = null;
        ConcentrationSiteInterface concentrationSiteInterface = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /*
         * localização por nome do objecto remoto no serviço de registos RMI
         */

        try {
            concentrationSiteInterface = (ConcentrationSiteInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_CONSITE_NAME);

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do ConcentrationSite: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O ConcentrationSite não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }


        try {
            collectionSiteInterface = (CollectionSiteInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_COLSITE_NAME);

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do CollectionSite: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O CollectionSite não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }


        try {
            generalRepositoryInterface = (GeneralRepositoryInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_GENREPO_NAME);

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do GeneralRepository: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O GeneralRepository não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }


        MasterThief master = new MasterThief("MasterThief", generalRepositoryInterface, collectionSiteInterface,
                concentrationSiteInterface);
        GenericIO.writelnString("O MasterThief iniciou o seu trabalho");
        master.start();

        try {
            master.join();
        } catch (InterruptedException e) {
            GenericIO.writelnString("O MasterThief terminou o seu trabalho");
        }

    }
}