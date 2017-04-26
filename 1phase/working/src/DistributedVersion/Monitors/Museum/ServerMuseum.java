package DistributedVersion.Monitors.Museum;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.*;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class ServerMuseum {


    public static void main(String[] args)  {

        Museum museu;
        MuseumInterface museuInter;
        ServerCom scon, sconi;
        ClientProxy cliProxy;


        String generalRepository = machine_log;

        scon = new ServerCom(portMuseum);
        scon.start();
        museu = new Museum(generalRepository);
        museuInter = new MuseumInterface(museu);
        GenericIO.writelnString("O serviço Museum foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");


        /*
         * processamento de pedidos
         */

        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();                            // entrada em processo de escuta
                cliProxy = new ClientProxy(sconi, museuInter);    // lançamento do agente prestador do serviço
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!museuInter.isAlive()){
                    keepAlive = false;
                }
            }

        }

        GenericIO.writelnString("O serviço Museum foi terminado!");
    }
}
