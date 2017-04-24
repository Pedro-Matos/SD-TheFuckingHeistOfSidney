package VersaoDistribuida.ParametrosDoProblema;

import VersaoDistribuida.ComInfo.ServerCom;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessageException;
import VersaoDistribuida.Mensagens.GeneralRepositoryMessage;
import VersaoDistribuida.Mensagens.GeneralRepositoryMessageException;
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
     * Interface à base
     *
     * @serialField generalRepositoryInterface
     */
    private GeneralRepositoryInterface generalRepositoryInterface;

    /**
     * Instanciação do interface à base.
     *
     * @param sconi canal de comunicação
     * @param generalRepositoryInterface interface à base
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
        Class<VersaoDistribuida.ParametrosDoProblema.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<VersaoDistribuida.ParametrosDoProblema.ClientProxy>) Class.forName("VersaoDistribuida.ParametrosDoProblema.ClientProxy");
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

