package VersaoDistribuida.Mensagens;

/**
 * Created by pmatos9 on 24/04/17.
 */
public class GeneralRepositoryMessageException extends Exception {

    /**
     * Mensagem que originou a excepção
     *
     * @serialField msg
     */
    private GeneralRepositoryMessage msg;

    /**
     * Instanciação de uma mensagem.
     *
     * @param errorMessage texto sinalizando a condição de erro
     * @param msg          mensagem que está na origem da excepção
     */
    public GeneralRepositoryMessageException(String errorMessage, GeneralRepositoryMessage msg) {
        super(errorMessage);
        this.msg = msg;
    }

    /**
     * Obtenção da mensagem que originou a excepção.
     *
     * @return mensagem
     */
    public GeneralRepositoryMessage getMessageVal() {

        return (msg);
    }
}