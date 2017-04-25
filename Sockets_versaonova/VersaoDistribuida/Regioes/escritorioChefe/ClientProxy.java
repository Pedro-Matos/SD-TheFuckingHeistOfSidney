package VersaoDistribuida.Regioes.escritorioChefe;

import VersaoDistribuida.ComInfo.ServerCom;
import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.CollectionSiteMessageException;
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
     * Interface à barbearia
     *
     * @serialField bShopInter
     */
    private CollectionSiteInterface escriInter;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param sconi canal de comunicação
     * @param escriInter interface ao escritorio
     */
    public ClientProxy(ServerCom sconi, CollectionSiteInterface escriInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.escriInter = escriInter;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        CollectionSiteMessage inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (CollectionSiteMessage) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = escriInter.processAndReply(inMessage);         // processá-lo
        } catch (CollectionSiteMessageException e) {
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
        Class<VersaoDistribuida.Regioes.escritorioChefe.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<VersaoDistribuida.Regioes.escritorioChefe.ClientProxy>) Class.forName("VersaoDistribuida.Regioes.escritorioChefe.ClientProxy");
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


