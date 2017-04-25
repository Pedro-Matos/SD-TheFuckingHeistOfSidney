package VersaoDistribuida.Regioes.museu;

import VersaoDistribuida.Mensagens.MuseuMessage;
import VersaoDistribuida.Mensagens.MuseumMessageException;
import static VersaoDistribuida.ParametrosDoProblema.Constantes.*;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumInterface  {

    private Museum museu;

    public MuseumInterface(Museum museu) {
        this.museu = museu;

    }

    public MuseuMessage processAndReply(MuseuMessage inMessage) throws MuseumMessageException {

        MuseuMessage outMessage = null;

        /*
         * processamento das mensagens recebidas
         */

        switch (inMessage.getType()) {
            case MuseuMessage.ROUBARQUADRO:
                boolean resp = museu.roubarQuadro(inMessage.getNrSala());
                outMessage = new MuseuMessage(MuseuMessage.SENDRESPROUBARQUADRO, resp);
                break;
            case MuseuMessage.GETNUMEROQUADROS:
                int nrQuadros = museu.getNumeroQuadros(inMessage.getNrSala());
                outMessage = new MuseuMessage(MuseuMessage.SENDNUMEROQUADROS, nrQuadros);
                break;
            case MuseuMessage.GETDISTANCIA:
                int dist = museu.getDistancia(inMessage.getNrSala());
                outMessage = new MuseuMessage(MuseuMessage.SENDDISTANCIA, dist);
                break;
            default:
                throw new MuseumMessageException("Tipo inválido!", inMessage);
        }


        return outMessage;
    }
}

