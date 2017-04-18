package VersaoDistribuida.Regioes.escritorioChefe;

import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.CollectionSiteMessageException;

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
                case CollectionSiteMessage.DESCANSAR:
                    escritorio.takeARest();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPDESCANSAR);
                    break;
                case CollectionSiteMessage.ENTRARGRUPO:
                    escritorio.entrarGrupo(inMessage.getLadraoID(),inMessage.getGrupoID());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPENTRARGRUPO);
                    break;
                case CollectionSiteMessage.FORMARGRUPO:
                    resp = escritorio.formarGrupo(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPFORMARGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETCOUNTGRUPOS:
                    resp = escritorio.getCountGrupos();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETCOUNTGRUPOS,resp);
                    break;
                case CollectionSiteMessage.ENTREGARQUADRO:
                    escritorio.handACanvas(inMessage.getGrupoID(), inMessage.getLadraoID());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPENTREGARQUADRO);
                    break;
                case CollectionSiteMessage.GETESTADOCHEFE:
                    stat = escritorio.getEstadoChefe();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETESTADOCHEFE,stat);
                    break;
                case CollectionSiteMessage.GETMEUGRUPO:
                    resp = escritorio.getMeuGrupo(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETMEUGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETNRELEMENTOSGRUPO:
                    resp = escritorio.getNrElemGrupo(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETNRELEMENTOSGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETPOSGRUPO:
                    resp = escritorio.getPosGrupo(inMessage.getLadraoID(),inMessage.getGrupoID());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETPOSGRUPO,resp);
                    break;
                case CollectionSiteMessage.GETQUADROSROUBADOS:
                    resp = escritorio.getQuadrosRoubados();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETQUADROSROUBADOS,resp);
                    break;
                case CollectionSiteMessage.GETSALAASSALTO:
                    resp = escritorio.getSalaAssalto(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETSALAASSALTO,resp);
                    break;
                case CollectionSiteMessage.GETSALAVAZIA:
                    bResp = escritorio.getSalaVazia(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGETSALAVAZIA,bResp);
                    break;
                case CollectionSiteMessage.GRUPOCHEIO:
                    bResp = escritorio.grupoCheio(inMessage.getInfo());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPGRUPOCHEIO,bResp);
                    break;
                case CollectionSiteMessage.INDICARSALAVAZIA:
                    escritorio.indicarSalaVazia(inMessage.getGrupoID(), inMessage.getLadraoID());
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPINDICARSALAVAZIA);
                    break;
                case CollectionSiteMessage.INICIARASSALTO:
                    escritorio.iniciarAssalto();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPINICIARASSALTO);
                    break;
                case CollectionSiteMessage.PLANEARASSALTO:
                    escritorio.planearAssalto();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPPLANEARASSALTO);
                    break;
                case CollectionSiteMessage.TERMINARASSALTO:
                    escritorio.sumUpResults();
                    outMessage = new CollectionSiteMessage(CollectionSiteMessage.RESPTERMINARASSALTO);
                    break;
                default:
                    break;
            }

            return outMessage;
        }
}

