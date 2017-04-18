package VersaoDistribuida.Regioes.base;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerConcentrationSite {

    private static final int portNumb = ;

    /**
     * Programa Principal servidor base(que representa o terreiro)
     *
     * @param args
     */
    public static void main(String[] args) {

        ConcentrationSite base;
        ConcentrationSiteInterface baseInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;

        scon = new ServerCom(portNumb);
        scon.start();
        base = new ConcentrationSite();
        baseInter = new ConcentrationSiteInterface(base);
        GenericIO.writelnString("O serviço base foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        while (true) {
            sconi = scon.accept();                            // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, baseInter);    // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}

