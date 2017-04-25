package VersaoDistribuida.Regioes.base;

import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessageException;

import static VersaoDistribuida.ParametrosDoProblema.Constantes.NUM_THIEVES;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ConcentrationSiteInterface {
    private ConcentrationSite base;

    ConcentrationSiteInterface(ConcentrationSite base) {
        this.base = base;
    }

    /**
     * Metodo que processa as mensagens e gera a mensagem de resposta
     *
     * @param inMessage mensagens recebida
     * @return mensagens de resposta
     * @throws ConcentrationSiteMessageException
     */
    public ConcentrationSiteMessage processAndReply(ConcentrationSiteMessage inMessage) throws ConcentrationSiteMessageException {

        ConcentrationSiteMessage outMessage = null;



        /*
         * processamento das mensagens recebidas
         */
        boolean check;
        int resp;
        int state;

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CHAMALADRAO:
                resp = base.chamaLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPCHAMALADRAO, resp);
                break;
            case ConcentrationSiteMessage.ESPERALADROES:
                base.esperaLadroes();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.ESPERALADROESFIM:
                base.esperaLadroesFim();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.AMINEEDED:
                base.amINeeded(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.ESTOUPRONTO:
                base.estouPronto(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETBUSYLADRAO:
                check = base.getBusyLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETBUSYLADRAO, check);
                break;
            case ConcentrationSiteMessage.GETGRUPOLADRAO:
                resp = base.getGrupoLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETGRUPOLADRAO, resp);
                break;
            case ConcentrationSiteMessage.GETNRLADROES:
                resp = base.getNrLadroes();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETNRLADROES, resp);
                break;
            case ConcentrationSiteMessage.GETSTATELADRAO:
                state = base.getStateLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETSTATELADRAO, state);
                break;
            case ConcentrationSiteMessage.INDICARCHEGADA:
                base.indicarChegada(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.PREPAREEXCURSION:
                base.prepareExcursion(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.NASALA:
                base.naSala(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.REVERSEDIRECTION:
                base.reverseDirection(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETAGILIDADE:
                resp = base.getAgilidade(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETAGILITY,resp);
                break;
            default:
                throw new ConcentrationSiteMessageException("Tipo inválido!", inMessage);

        }
        return outMessage;
    }
}

