package VersaoDistribuida.Regioes.escritorioChefe;

import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.CollectionSiteMessageException;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessageException;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class CollectionSiteInterface {
        private CollectionSite escritorio;

        public CollectionSiteInterface(CollectionSite escritorio) {
            this.escritorio = escritorio;
        }

        public CollectionSiteMessage processAndReply(CollectionSiteMessage inMessage) throws CollectionSiteMessageException {

            CollectionSiteMessage outMessage = null;



            switch (inMessage.getMsgType()) {
                case CollectionSiteMessage.CHECKEMPTYMUSEU:
                    break;
                case CollectionSiteMessage.CHECKGRUPOS:
                    break;
                case CollectionSiteMessage.CHECKSALASVAZIAS:
                    break;
                case CollectionSiteMessage.TAKEAREST:
                    break;
                case CollectionSiteMessage.ENTRARGRUPO:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" Id Ladrao inválido!", inMessage);
                    }
                    else if (inMessage.getArg2() < 0) {
                        throw new CollectionSiteMessageException(" Id grupo inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.PREPAREASSAULTPARTY:
                    break;
                case CollectionSiteMessage.HANDACANVAS:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" Id Ladrao inválido!", inMessage);
                    }
                    else if (inMessage.getArg2() < 0) {
                        throw new CollectionSiteMessageException(" Sala assalto inválida!", inMessage);
                    }
                    else if (inMessage.getArg3() < 0) {
                        throw new CollectionSiteMessageException(" Id grupo inválido!", inMessage);
                    }
                    else if (inMessage.getArg4() < 0) {
                        throw new CollectionSiteMessageException(" Id Pos Grupo inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.GETESTADOCHEFE:
                    break;
                case CollectionSiteMessage.GETNRELEMENTOSGRUPO:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.GETPOSGRUPO:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" inválido!", inMessage);
                    }
                    else if (inMessage.getArg2() < 0) {
                        throw new CollectionSiteMessageException(" inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.GETQUADROSROUBADOS:
                    break;
                case CollectionSiteMessage.GETSALAASSALTO:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.GRUPOCHEIO:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.INDICARSALAVAZIA:
                    if (inMessage.getArg1() < 0) {
                        throw new CollectionSiteMessageException(" Sala inválido!", inMessage);
                    }
                    else if (inMessage.getArg3() < 0) {
                        throw new CollectionSiteMessageException(" Posicao inválido!", inMessage);
                    }
                    break;
                case CollectionSiteMessage.STARTOPERATIONS:
                    break;
                case CollectionSiteMessage.SUMUPRESULTS:
                    break;
                default:
                    break;
            }


        /*
         * processamento das mensagens
         */

            boolean bResp;
            int resp;
            int stat;




            switch (inMessage.getMsgType()) {
                case CollectionSiteMessage.CHECKEMPTYMUSEU:
                    bResp = escritorio.checkEmptyMuseu();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPCHECKEMPTYMUSEU,bResp);
                    break;
                case CollectionSiteMessage.CHECKGRUPOS:
                    resp = escritorio.checkGrupos();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPCHECKGRUPOS,resp);
                    break;
                case CollectionSiteMessage.CHECKSALASVAZIAS:
                    bResp = escritorio.checkSalasLivres();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPCHECKSALASVAZIAS,bResp);
                    break;
                case CollectionSiteMessage.TAKEAREST:
                    escritorio.takeARest();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPTAKEAREST);
                    break;
                case CollectionSiteMessage.ENTRARGRUPO:
                    escritorio.entrarGrupo(inMessage.getArg1(),inMessage.getArg2());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPENTRARGRUPO);
                    break;
                case CollectionSiteMessage.PREPAREASSAULTPARTY:
                    bResp = escritorio.prepareAssaultParty(inMessage.getArg1());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPPREPAREASSAULTPARTY,bResp);
                    break;
                case CollectionSiteMessage.HANDACANVAS:
                    escritorio.handACanvas(inMessage.getArg1(),inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPHANDACANVAS);
                    break;
                case CollectionSiteMessage.GETESTADOCHEFE:
                    stat = escritorio.getEstadoChefe();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETESTADOCHEFE,stat);
                    break;
                case CollectionSiteMessage.GETNRELEMENTOSGRUPO:
                    resp = escritorio.getNrElemGrupo(inMessage.getArg1());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETNRELEMENTOSGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETPOSGRUPO:
                    resp = escritorio.getPosGrupo(inMessage.getArg1(),inMessage.getArg2());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETPOSGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETQUADROSROUBADOS:
                    resp = escritorio.getQuadrosRoubados();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETQUADROSROUBADOS,resp);
                    break;
                case CollectionSiteMessage.GETSALAASSALTO:
                    resp = escritorio.getSalaAssalto(inMessage.getArg1());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETSALAASSALTO,resp);
                    break;
                case CollectionSiteMessage.GRUPOCHEIO:
                    bResp = escritorio.grupoCheio(inMessage.getArg1());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGRUPOCHEIO,bResp);
                    break;
                case CollectionSiteMessage.INDICARSALAVAZIA:
                    escritorio.indicarSalaVazia(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPINDICARSALAVAZIA);
                    break;
                case CollectionSiteMessage.STARTOPERATIONS:
                    escritorio.startOperations();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPSTARTOPERATIONS);
                    break;
                case CollectionSiteMessage.SUMUPRESULTS:
                    escritorio.sumUpResults();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPSUMUPRESULTS);
                    break;
                default:
                    throw new CollectionSiteMessageException("Tipo inválido!", inMessage);
            }

            return outMessage;
        }
}

