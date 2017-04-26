package DistributedVersion.ProblemInformation;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.GeneralRepositoryMessage;
import DistributedVersion.Messages.GeneralRepositoryMessageException;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ClientProxy extends Thread {

    /**
     * Contador de threads lançados
     *
     * @serialField nProxy
     */
    private static int nProxy;
    /**
     * Canal de comunicação
     *
     * @serialField sconi
     */
    private ServerCom sconi;
    /**
     * Interface à ConcentrationSite
     *
     * @serialField generalRepositoryInterface
     */
    private GeneralRepositoryInterface generalRepositoryInterface;

    /**
     * Instanciação do interface à ConcentrationSite.
     *
     * @param sconi canal de comunicação
     * @param generalRepositoryInterface interface à ConcentrationSite
     */
    public ClientProxy(ServerCom sconi, GeneralRepositoryInterface generalRepositoryInterface) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.generalRepositoryInterface = generalRepositoryInterface;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        GeneralRepositoryMessage inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (GeneralRepositoryMessage) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = generalRepositoryInterface.processAndReply(inMessage);         // processá-lo
        } catch (GeneralRepositoryMessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }


        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
    }

    /**
     * Geração do identificador da instanciação.
     *
     * @return identificador da instanciação
     */
    private static int getProxyId() {
        Class<DistributedVersion.ProblemInformation.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<DistributedVersion.ProblemInformation.ClientProxy>) Class.forName("DistributedVersion.ProblemInformation.ClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("O tipo de dados ClientProxy não foi encontrado!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
}

