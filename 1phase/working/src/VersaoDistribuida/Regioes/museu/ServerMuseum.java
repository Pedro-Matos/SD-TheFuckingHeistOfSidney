package VersaoDistribuida.Regioes.museu;

import VersaoConcorrente.ParametrosDoProblema.GeneralRepository;
import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class ServerMuseum {

    private static final int portNumb = ;

    public static void main(String[] args) {

        Museum museu;
        MuseumInterface museuInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;

        scon = new ServerCom(portNumb);
        scon.start();
        museu = new Museum();
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
