package VersaoDistribuida.Regioes.gruposAssalto;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

import static VersaoDistribuida.ComInfo.ComPorts.portAssaultGroup;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerAssaultParty {

    /**
     * Programa Principal servidor Grupo de Assalto
     *
     * @param args
     */
    public static void main(String[] args) {

        /*
         * Obtenção dos parâmetros do problema
         */
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor museu? ");
        String museu = GenericIO.readlnString();

        GenericIO.writeString("\nNome do sistema computacional onde está o servidor General Repository? ");
        String generalRepository = GenericIO.readlnString();

        GrupoAssaltoInterface grupoAssaltoInter;
        ServerCom scon, sconi;

        ClientProxy cliProxy;

        scon = new ServerCom(portAssaultGroup);
        scon.start();
        GrupoAssalto[] grupo = new GrupoAssalto[2];
        grupo[0] = null;
        grupo[1] = null;
        grupoAssaltoInter = new GrupoAssaltoInterface(grupo, museu, generalRepository);
        GenericIO.writelnString("O serviço grupo de assalto foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, grupoAssaltoInter);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

