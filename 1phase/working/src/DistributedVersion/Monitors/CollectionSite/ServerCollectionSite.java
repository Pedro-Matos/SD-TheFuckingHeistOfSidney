package DistributedVersion.Monitors.CollectionSite;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.portCollectionSite;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerCollectionSite {

        public static void main(String[] args) {

            CollectionSite escritorio = null;
            CollectionSiteInterface escritorioInterface;
            ServerCom scon, sconi;
            ClientProxy cliProxy;

            GenericIO.writeString("\nWhich machine is Museum on? ");
            String museu = GenericIO.readlnString();

            GenericIO.writeString("\nWhich machine is ConcentrationSite on? ");
            String concentrationSite = GenericIO.readlnString();

            GenericIO.writeString("\nWhich machine is AssaultParty on? ");
            String assaultGroup = GenericIO.readlnString();

            GenericIO.writeString("\nWhich machine is General Repository on? ");
            String generalRepository = GenericIO.readlnString();

            scon = new ServerCom(portCollectionSite);
            scon.start();
            escritorio = new CollectionSite(museu,concentrationSite, assaultGroup, generalRepository);
            escritorioInterface = new CollectionSiteInterface(escritorio);
            GenericIO.writelnString("Service CollectionSite has been established!");
            GenericIO.writelnString("Server listening.");


        /*
         * processamento de pedidos
         */

            boolean keepAlive = true;

            while (keepAlive) {
                try{
                    sconi = scon.accept();
                    cliProxy = new ClientProxy(sconi, escritorioInterface);
                    cliProxy.start();
                }catch(SocketTimeoutException e){
                    if(!escritorioInterface.isAlive()){
                        keepAlive = false;
                    }
                }

            }

            GenericIO.writelnString("CollectionSite has been terminated!");

        }
}

