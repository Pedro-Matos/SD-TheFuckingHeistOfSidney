package DistributedVersion.Monitors.ConcentrationSite;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.portConcentrationSite;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ServerConcentrationSite {

    /**
     * Programa Principal servidor ConcentrationSite(que representa o terreiro)
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
        GenericIO.writelnString("O serviço ConcentrationSite foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();                            // entrada em processo de escuta
                cliProxy = new ClientProxy(sconi, baseInter);   // lançamento do agente prestador do serviço
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!baseInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("O serviço Concentration Site foi terminado!");
    }
}

