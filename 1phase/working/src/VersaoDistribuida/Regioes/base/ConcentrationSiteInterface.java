package VersaoDistribuida.Regioes.base;

import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessageException;

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
         * validação da mensagem recebida
         */

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CHAMALADRAO:
                if (inMessage.getId() > 1 || inMessage.getId() < 0) {
                    throw new ConcentrationSiteMessageException("Grupo inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.ESPERALADROES:
            case ConcentrationSiteMessage.ESPERALADROESFIM:
            case ConcentrationSiteMessage.GETNRLADROES:
                break;
            case ConcentrationSiteMessage.ESPERAORDENS:
            case ConcentrationSiteMessage.ESTOUPRONTO:
            case ConcentrationSiteMessage.GETBUSYLADRAO:
            case ConcentrationSiteMessage.GETGRUPOLADRAO:
            case ConcentrationSiteMessage.GETLADROESBASE:
            case ConcentrationSiteMessage.GETSTATELADRAO:
            case ConcentrationSiteMessage.INDICARREGRESSO:
            case ConcentrationSiteMessage.INICIARASSALTO:
            case ConcentrationSiteMessage.NASALA:
            case ConcentrationSiteMessage.REGRESSARASSALTO:
                if (inMessage.getId() >= K || inMessage.getId() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            default:
                throw new ConcentrationSiteMessageException("Tipo inválido!", inMessage);
        }

        /*
         * processamento das mensagens recebidas
         */
        boolean check;
        int resp;
        int state;

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CHAMALADRAO:
                resp = base.chamaLadrao(inMessage.getId());
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
            case ConcentrationSiteMessage.ESPERAORDENS:
                base.esperaOrdens(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.ESTOUPRONTO:
                base.estouPronto(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETBUSYLADRAO:
                check = base.getBusyLadrao(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETBUSYLADRAO, check);
                break;
            case ConcentrationSiteMessage.GETGRUPOLADRAO:
                resp = base.getGrupoLadrao(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETGRUPOLADRAO, resp);
                break;
            case ConcentrationSiteMessage.GETLADROESBASE:
                check = base.getLadroesBase();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETLADROESBASE, check);
                break;
            case ConcentrationSiteMessage.GETNRLADROES:
                resp = base.getNrLadroes();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETNRLADROES, resp);
                break;
            case ConcentrationSiteMessage.GETSTATELADRAO:
                state = base.getStateLadrao(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETSTATELADRAO, state);
                break;
            case ConcentrationSiteMessage.INDICARREGRESSO:
                base.indicarRegresso(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.INICIARASSALTO:
                base.iniciarAssalto(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.NASALA:
                base.naSala(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(BaseMessage.ACK);
                break;
            case ConcentrationSiteMessage.REGRESSARASSALTO:
                base.regressarAssalto(inMessage.getId());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;


        }
        return outMessage;
    }
}

