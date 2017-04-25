package VersaoDistribuida.Regioes.gruposAssalto;

import VersaoDistribuida.ComInfo.ServerCom;
import VersaoDistribuida.Mensagens.AssaultPartyMessage;
import VersaoDistribuida.Mensagens.AssaultPartyMessageException;
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
     * Interface ao Grupo de assalto
     *
     * @serialField grupoInter
     */
    private GrupoAssaltoInterface grupoInter;

    /**
     * Instanciação do interface ao Grupo Assalto.
     *
     * @param sconi canal de comunicação
     * @param grupoInter interface ao grupo Assalto
     */
    public ClientProxy(ServerCom sconi, GrupoAssaltoInterface grupoInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.grupoInter = grupoInter;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        AssaultPartyMessage inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

//        sconi.readObject().toString();
        inMessage = (AssaultPartyMessage) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = grupoInter.processAndReply(inMessage);         // processá-lo
        } catch (AssaultPartyMessageException e) {
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
        Class<VersaoDistribuida.Regioes.gruposAssalto.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<VersaoDistribuida.Regioes.gruposAssalto.ClientProxy>) Class.forName("VersaoDistribuida.Regioes.gruposAssalto.ClientProxy");
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

