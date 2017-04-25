package VersaoDistribuida.Regioes.gruposAssalto;

import VersaoDistribuida.Mensagens.AssaultPartyMessage;
import VersaoDistribuida.Mensagens.AssaultPartyMessageException;
import VersaoDistribuida.Regioes.museu.Museum;
import static VersaoDistribuida.ParametrosDoProblema.Constantes.*;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class GrupoAssaltoInterface {

        private GrupoAssalto[] grupo;
        private String museu, repo;

        GrupoAssaltoInterface(GrupoAssalto[] grupo, String museu, String repo) {
            this.grupo = grupo;
            this.museu = museu;
            this.repo = repo;
        }

        AssaultPartyMessage processAndReply(AssaultPartyMessage inMessage) throws AssaultPartyMessageException {
            AssaultPartyMessage outMessage = null;


        /*
         * validação da mensagem recebida
         */




            int resp;
        /*
         * processamento das mensagens recebidas
         */

            switch (inMessage.getMsgType()) {
                case AssaultPartyMessage.ENTRAR:
                    grupo[inMessage.getArg2()].entrar(inMessage.getArg1(), inMessage.getArg3());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                    break;
                case AssaultPartyMessage.DESFAZERGRUPO:
                    desfazerGrupo(inMessage.getArg1());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                    break;
                case AssaultPartyMessage.FORMARGRUPO:
                    resp = formarGrupo(inMessage.getArg1(), inMessage.getArg2());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPFORMARGRUPO, resp);
                    break;
                case AssaultPartyMessage.GETDISTANCIASALA:
                    resp = grupo[inMessage.getArg1()].getDistanciaSala();
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETDISTANCIASALA, resp);
                    break;
                /*case AssaultPartyMessage.GETINDICE:
                    resp = grupo[inMessage.getIdGrupo()].getPosicao(inMessage.getInfo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETINDICE, resp);
                    break; */
                case AssaultPartyMessage.GETPOS:
                    if (grupo[inMessage.getArg2()] != null) {
                        resp = grupo[inMessage.getArg2()].getPos(inMessage.getArg1());
                    } else {
                        resp = -1;
                    }
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETPOS, resp);
                    break;
                case AssaultPartyMessage.CRAWLIN:
                    resp = grupo[inMessage.getArg3()].rastejarIn(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLIN, resp);
                    break;
                case AssaultPartyMessage.CRAWLOUT:
                    resp = grupo[inMessage.getArg3()].rastejarOut(inMessage.getArg1(), inMessage.getArg2(), inMessage.getArg3(), inMessage.getArg4());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLOUT, resp);
                    break;
                case AssaultPartyMessage.ROUBARQUADRO:
                    boolean quadro = grupo[inMessage.getArg1()].roubarQuadro();
                    System.out.println(quadro);
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPROUBARQUADRO, quadro);
                    break;
                /*case AssaultPartyMessage.CHECKGRUPONULL:
                    if (grupo[inMessage.getInfo()] == null) {
                        outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCHECKGRUPONULL, true);
                    } else {
                        outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCHECKGRUPONULL, false);
                    }
                    break;*/
                case AssaultPartyMessage.WAITMYTURN:
                    grupo[inMessage.getArg2()].waitMinhaVez(inMessage.getArg1());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                    break;
                default:
                    throw new AssaultPartyMessageException("Tipo inválido!", inMessage);
            }

            return outMessage;
        }

        public int formarGrupo(int id, int nrSala) {
            if (grupo[id] == null) {
                grupo[id] = new GrupoAssalto(museu, nrSala, id,repo);
                return id;
            } else {
                return -1;
            }
        }

        public void desfazerGrupo(int id) {
            grupo[id] = null;
        }
}

