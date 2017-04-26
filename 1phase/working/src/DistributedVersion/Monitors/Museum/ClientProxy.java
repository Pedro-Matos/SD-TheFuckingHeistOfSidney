package DistributedVersion.Monitors.Museum;

import DistributedVersion.ComInfo.ServerCom;
import DistributedVersion.Messages.MuseumMessage;
import DistributedVersion.Messages.MuseumMessageException;
import genclass.GenericIO;

/**
 * Created by pmatos9 on 17/04/17.
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
    private MuseumInterface museuInter;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param sconi canal de comunicação
     * @param museumInter interface à barbearia
     */
    public ClientProxy(ServerCom sconi, MuseumInterface museumInter) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.museuInter = museumInter;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        MuseumMessage inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (MuseumMessage) sconi.readObject();                     // ler pedido do cliente
        try {
            outMessage = museuInter.processAndReply(inMessage);         // processá-lo
        } catch (MuseumMessageException e) {
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
        Class<DistributedVersion.Monitors.Museum.ClientProxy> cl = null;             // representação do tipo de dados ClientProxy na máquina
        //   virtual de Java
        int proxyId;                                         // identificador da instanciação

        try {
            cl = (Class<DistributedVersion.Monitors.Museum.ClientProxy>) Class.forName("DistributedVersion.Monitors.Museum.ClientProxy");
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

