package VersaoDistribuida.Regioes.escritorioChefe;

import VersaoDistribuida.ComInfo.ClientCom;
import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerCollectionSite {
        private static final int portNumb = ;

        public static void main(String[] args) {

            CollectionSite escritorio = null;
            CollectionSiteInterface escritorioInterface;
            ServerCom scon, sconi;
            ClientProxy cliProxy;

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor base? ");
            String base = GenericIO.readlnString();
            ClientCom cConBase = new ClientCom(base, );

            GenericIO.writeString("\nNome do sistema computacional onde está o servidor grupo de assalto? ");
            String grupoAssalto = GenericIO.readlnString();
            ClientCom cConGrupo = new ClientCom(grupoAssalto, );

            scon = new ServerCom(portNumb);
            scon.start();
            escritorio = new CollectionSite(cConBase,cConGrupo);
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

