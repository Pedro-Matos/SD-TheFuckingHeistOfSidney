package DistributedVersion.Messages;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class AssaultPartyMessageException extends Exception {

    /**
     * Mensagem que originou a excepção
     *
     * @serialField msg
     */
    private AssaultPartyMessage msg;

    /**
     * Instanciação de uma mensagem.
     *
     * @param errorMessage texto sinalizando a condição de erro
     * @param msg mensagem que está na origem da excepção
     */
    public AssaultPartyMessageException(String errorMessage, AssaultPartyMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtenção da mensagem que originou a excepção.
     *
     * @return mensagem
     */
    public AssaultPartyMessage getMessageVal() {
        return (msg);
    }
}

