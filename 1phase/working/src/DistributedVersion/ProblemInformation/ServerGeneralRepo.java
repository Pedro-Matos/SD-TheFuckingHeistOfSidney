package DistributedVersion.ProblemInformation;

import DistributedVersion.ComInfo.ServerCom;
import genclass.GenericIO;

import java.net.SocketTimeoutException;

import static DistributedVersion.ComInfo.ComPorts.portGeneralRepo;

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
        boolean keepAlive = true;

        while (keepAlive) {
            try{
                sconi = scon.accept();                            // entrada em processo de escuta
                cliProxy = new ClientProxy(sconi, godInter);    // lançamento do agente prestador do serviço
                cliProxy.start();
            }catch(SocketTimeoutException e){
                if(!godInter.isAlive()){
                    keepAlive = false;
                }
                continue;
            }

        }

        GenericIO.writelnString("O serviço god foi terminado!");
    }

}
