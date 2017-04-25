package VersaoDistribuida.ParametrosDoProblema;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

import static VersaoDistribuida.ComInfo.ComPorts.portGeneralRepo;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerGeneralRepo {

    public static void main(String[] args) {

        GeneralRepositoryInterface godInter;
        ServerCom scon, sconi;

        ClientProxy cliProxy;

        scon = new ServerCom(portGeneralRepo);
        scon.start();
        GeneralRepository god = new GeneralRepository();
        godInter = new GeneralRepositoryInterface(god);
        GenericIO.writelnString("O serviço god foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");

        god.iniciarLog();


        /*
         * processamento de pedidos
         */

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, godInter);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }

}
