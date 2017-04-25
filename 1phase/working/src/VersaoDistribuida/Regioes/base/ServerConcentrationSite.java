package VersaoDistribuida.Regioes.base;

import VersaoDistribuida.ComInfo.ServerCom;
import genclass.GenericIO;

import static VersaoDistribuida.ComInfo.ComPorts.portConcentrationSite;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerConcentrationSite {

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

        GenericIO.writeString("\nNome do sistema computacional onde está o servidor General Repository? ");
        String generalRepository = GenericIO.readlnString();

        scon = new ServerCom(portConcentrationSite);
        scon.start();
        base = new ConcentrationSite(generalRepository);
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

