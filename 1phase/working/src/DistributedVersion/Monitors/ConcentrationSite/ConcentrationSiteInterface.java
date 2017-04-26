package DistributedVersion.Monitors.ConcentrationSite;

import DistributedVersion.Messages.ConcentrationSiteMessage;
import DistributedVersion.Messages.ConcentrationSiteMessageException;

import static DistributedVersion.ProblemInformation.Constants.MAX_AGIL;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class ConcentrationSiteInterface {
    private ConcentrationSite base;
    private boolean isAlive = true;
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

        switch (inMessage.getType()) {
            case ConcentrationSiteMessage.CALLTHIEF:
                if (inMessage.getArg1() > 1 || inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Grupo inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.WAITTHIVES:
                break;
            case ConcentrationSiteMessage.WAITTHIEVESEND:
                break;
            case ConcentrationSiteMessage.GETTHIEFNUMBER:
                break;
            case ConcentrationSiteMessage.AMINEEDED:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.IMREADY:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETBUSYTHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETGROUPTHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETSTATETHIEF:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.INDICARCHEGADA:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.PREPAREEXCURSION:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.NASALA:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.REVERSEDIRECTION:
                if (inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Id do ladrao inválido!", inMessage);
                }
                break;
            case ConcentrationSiteMessage.GETAGILITY:
                if (inMessage.getArg1() > MAX_AGIL || inMessage.getArg1() < 0) {
                    throw new ConcentrationSiteMessageException("Agilidade inválida!", inMessage);
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
            case ConcentrationSiteMessage.CALLTHIEF:
                resp = base.chamaLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPCHAMALADRAO, resp);
                break;
            case ConcentrationSiteMessage.WAITTHIVES:
                base.esperaLadroes();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.WAITTHIEVESEND:
                base.esperaLadroesFim();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.AMINEEDED:
                base.amINeeded(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.IMREADY:
                base.estouPronto(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ACK);
                break;
            case ConcentrationSiteMessage.GETBUSYTHIEF:
                check = base.getBusyLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETBUSYLADRAO, check);
                break;
            case ConcentrationSiteMessage.GETGROUPTHIEF:
                resp = base.getGrupoLadrao(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETGRUPOLADRAO, resp);
                break;
            case ConcentrationSiteMessage.GETTHIEFNUMBER:
                resp = base.getNrLadroes();
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETNRLADROES, resp);
                break;
            case ConcentrationSiteMessage.GETSTATETHIEF:
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
            case ConcentrationSiteMessage.GETAGILITY:
                resp = base.getAgilidade(inMessage.getArg1());
                outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.RESPGETAGILITY,resp);
                break;
            case ConcentrationSiteMessage.TERMINAR:
                isAlive = false;
                break;
            default:
                throw new ConcentrationSiteMessageException("Tipo inválido!", inMessage);

        }
        return outMessage;
    }

    public boolean isAlive(){
        return this.isAlive;
    }
}

