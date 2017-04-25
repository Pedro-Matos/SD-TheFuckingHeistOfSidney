package VersaoDistribuida.Regioes.escritorioChefe;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

import static VersaoDistribuida.ComInfo.ComPorts.portCollectionSite;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerCollectionSite {

        public static void main(String[] args) {

            CollectionSite escritorio = null;
            CollectionSiteInterface escritorioInterface;
            ServerCom scon, sconi;
            ClientProxy cliProxy;

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor Museu? ");
            String museu = GenericIO.readlnString();

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor ConcentrationSite? ");
            String concentrationSite = GenericIO.readlnString();

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor Grupo de Asalto? ");
            String assaultGroup = GenericIO.readlnString();

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor General Repository? ");
            String generalRepository = GenericIO.readlnString();

            scon = new ServerCom(portCollectionSite);
            scon.start();
            escritorio = new CollectionSite(museu,concentrationSite, assaultGroup, generalRepository);
            escritorioInterface = new CollectionSiteInterface(escritorio);
            GenericIO.writelnString("O serviço escritorio foi estabelecido!");
            GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

            while (true) {
                sconi = scon.accept();                            // entrada em processo de escuta
                cliProxy = new ClientProxy(sconi, escritorioInterface); // lançamento do agente prestador do serviço
                cliProxy.start();
            }

        }
}

