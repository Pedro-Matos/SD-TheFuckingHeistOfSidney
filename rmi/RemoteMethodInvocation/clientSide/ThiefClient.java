package RemoteMethodInvocation.clientSide;

import RemoteMethodInvocation.interfaces.AssaultPartyManagerInterface;
import RemoteMethodInvocation.interfaces.CollectionSiteInterface;
import RemoteMethodInvocation.interfaces.ConcentrationSiteInterface;
import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.registry.RegistryConfig;
import genclass.GenericIO;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static RemoteMethodInvocation.support.Constantes.NUM_THIEVES;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public class ThiefClient {

    public static void main(String[] args) {

        // Initialise RMI configurations
        String rmiRegHostName = RegistryConfig.RMI_REGISTRY_HOSTNAME;
        int rmiRegPortNumb = RegistryConfig.RMI_REGISTRY_PORT;
        Registry registry = null;

        // Initialise RMI invocations
        GeneralRepositoryInterface generalRepositoryInterface = null;
        AssaultPartyManagerInterface assaultPartyManagerInterface = null;
        CollectionSiteInterface collectionSiteInterface = null;
        ConcentrationSiteInterface concentrationSiteInterface = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        /*
         * localização por nome do objecto remoto no serviço de registos RMI
         */
        try {
            assaultPartyManagerInterface = (AssaultPartyManagerInterface)
                    registry.lookup(RegistryConfig.RMI_REGISTRY_ASSGMAN_NAME);

        } catch (RemoteException e) {
            GenericIO.writelnString("Excepção na localização do GestorGruposAssalto: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("O GestorGruposAssalto não está registada: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }


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


        Thief[] thief = new Thief[NUM_THIEVES];
        for (int i = 0; i < NUM_THIEVES; i++) {
            String thiefName = "Thief - " + i;
            thief[i] = new Thief(i, thiefName, generalRepositoryInterface, assaultPartyManagerInterface,
                    collectionSiteInterface, concentrationSiteInterface);
            thief[i].start();
        }

        for (int i = 0; i < NUM_THIEVES; i++) {
            try {
                thief[i].join();
            } catch (InterruptedException e) {
                GenericIO.writelnString("O Thief " + i + " terminou o seu trabalho");
            }
        }

    }

}
