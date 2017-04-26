package DistributedVersion.Monitors.AssaultParty;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.portAssaultGroup;

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
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor Museum? ");
        String museu = GenericIO.readlnString();

        GenericIO.writeString("\nNome do sistema computacional onde está o servidor General Repository? ");
        String generalRepository = GenericIO.readlnString();

        AssaultPartyInterface grupoAssaltoInter;
        ServerCom scon, sconi;

        ClientProxy cliProxy;

        scon = new ServerCom(portAssaultGroup);
        scon.start();
        AssaultParty[] grupo = new AssaultParty[2];
        grupo[0] = null;
        grupo[1] = null;
        grupoAssaltoInter = new AssaultPartyInterface(grupo, museu, generalRepository);
        GenericIO.writelnString("O serviço grupo de assalto foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();                            // entrada em processo de escuta
                cliProxy = new ClientProxy(sconi, grupoAssaltoInter);    // lançamento do agente prestador do serviço
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!grupoAssaltoInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("O serviço AssaultParty foi terminado!");
    }
}

