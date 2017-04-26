package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumMessageException extends Exception {

    /**
     * Mensagem que originou a excepção
     *
     * @serialField msg
     */
    private MuseumMessage msg;

    /**
     * Instanciação de uma mensagem.
     *
     * @param errorMessage texto sinalizando a condição de erro
     * @param msg mensagem que está na origem da excepção
     */
    public MuseumMessageException(String errorMessage, MuseumMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtenção da mensagem que originou a excepção.
     *
     * @return mensagem
     */
    public MuseumMessage getMessageVal() {
        return (msg);
    }
}

