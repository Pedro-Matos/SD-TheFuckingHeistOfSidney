package VersaoDistribuida.Regioes.museu;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

import static VersaoDistribuida.ComInfo.ComPorts.portMuseum;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class ServerMuseum {


    public static void main(String[] args) {

        Museum museu;
        MuseumInterface museuInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;

        GenericIO.writeString("\nNome do sistema computacional onde está o servidor General Repository? ");
        String generalRepository = GenericIO.readlnString();

        scon = new ServerCom(portMuseum);
        scon.start();
        museu = new Museum(generalRepository);
        museuInter = new MuseumInterface(museu);
        GenericIO.writelnString("O serviço museu foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, museuInter);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
