package VersaoDistribuida.Mensagens;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ConcentrationSiteMessageException extends Exception {

        /**
         * Mensagem que originou a excepção
         *
         * @serialField msg
         */
        private ConcentrationSiteMessage msg;

        /**
         * Instanciação de uma mensagem.
         *
         * @param errorMessage texto sinalizando a condição de erro
         * @param msg mensagem que está na origem da excepção
         */
     public ConcentrationSiteMessageException(String errorMessage, ConcentrationSiteMessage msg) {
            super(errorMessage);
            this.msg = msg;
        }

        /**
         * Obtenção da mensagem que originou a excepção.
         *
         * @return mensagem
         */
    public ConcentrationSiteMessage getMessageVal() {
        return (msg);
    }
}

