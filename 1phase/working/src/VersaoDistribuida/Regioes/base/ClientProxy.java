package VersaoDistribuida.Regioes.base;

import VersaoDistribuida.ComInfo.ServerCom;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessageException;
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
     * @serialField baseInter
     */
    private ConcentrationSiteInterface baseInter;

    /**
     * Instanciação do interface à base.
     *
     * @param sconi canal de comunicação
     * @param baseInter interface à base
     */
    public ClientProxy(ServerCom sconi, ConcentrationSiteInterface baseInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.baseInter = baseInter;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        ConcentrationSiteMessage inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (ConcentrationSiteMessage) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = baseInter.processAndReply(inMessage);         // processá-lo
        } catch (ConcentrationSiteMessageException e) {
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
        Class<VersaoDistribuida.Regioes.base.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<VersaoDistribuida.Regioes.base.ClientProxy>) Class.forName("VersaoDistribuida.Regioes.base.ClientProxy");
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

