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

            switch (inMessage.getType()) {
                case AssaultPartyMessage.FORMARGRUPO:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= NUM_ROOMS) {
                        throw new AssaultPartyMessageException("Sala invalida", inMessage);
                    }
                    break;
                case AssaultPartyMessage.ENTRAR:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= NUM_THIEVES) {
                        throw new AssaultPartyMessageException("id Ladrao invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETDISTANCIASALA:
                    break;
                case AssaultPartyMessage.GETINDICE:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= NUM_THIEVES) {
                        throw new AssaultPartyMessageException("id Ladrao invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETPOS:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido5", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= NUM_GROUP) {
                        throw new AssaultPartyMessageException("indice elemento do grupo invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.DESFAZERGRUPO:
                case AssaultPartyMessage.ROUBARQUADRO:
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.CHECKGRUPONULL:
                    break;
                case AssaultPartyMessage.CRAWLIN:
                case AssaultPartyMessage.CRAWLOUT:
                    if (inMessage.getInfo() >= NUM_THIEVES || inMessage.getInfo() < 0) {
                        throw new AssaultPartyMessageException("Ladrao invalido", inMessage);
                    }
                    if (inMessage.getAgilidade() < MIN_AGIL || inMessage.getAgilidade() > MAX_AGIL) {
                        throw new AssaultPartyMessageException("Agilidade invalida", inMessage);
                    }
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    break;
                default:
                    throw new AssaultPartyMessageException("Tipo inválido!", inMessage);
            }
            int resp;
        /*
         * processamento das mensagens recebidas
         */

            switch (inMessage.getType()) {
                case AssaultPartyMessage.ENTRAR:
                    grupo[inMessage.getIdGrupo()].entrar(inMessage.getThiefId(), inMessage.getPosGrupo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                    break;
                case AssaultPartyMessage.DESFAZERGRUPO:
                    desfazerGrupo(inMessage.getInfo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.ACK);
                    break;
                case AssaultPartyMessage.FORMARGRUPO:
                    resp = formarGrupo(inMessage.getIdGrupo(), inMessage.getInfo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPFORMARGRUPO, resp);
                    break;
                case AssaultPartyMessage.GETDISTANCIASALA:
                    resp = grupo[inMessage.getInfo()].getDistanciaSala();
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETDISTANCIASALA, resp);
                    break;
                case AssaultPartyMessage.GETINDICE:
                    resp = grupo[inMessage.getIdGrupo()].getPosicao(inMessage.getInfo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETINDICE, resp);
                    break;
                case AssaultPartyMessage.GETPOS:
                    if (grupo[inMessage.getIdGrupo()] != null) {
                        resp = grupo[inMessage.getIdGrupo()].getPos(inMessage.getInfo());
                    } else {
                        resp = -1;
                    }
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETPOS, resp);
                    break;
                case AssaultPartyMessage.CRAWLIN:
                    resp = grupo[inMessage.getIdGrupo()].rastejarIn(inMessage.getThiefId(), inMessage.getAgilidade(), inMessage.getIdGrupo(), inMessage.getPosGrupo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLIN, resp);
                    break;
                case AssaultPartyMessage.CRAWLOUT:
                    resp = grupo[inMessage.getIdGrupo()].rastejarOut(inMessage.getThiefId(), inMessage.getAgilidade(), inMessage.getIdGrupo(), inMessage.getPosGrupo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCRAWLOUT, resp);
                    break;
                case AssaultPartyMessage.ROUBARQUADRO:
                    boolean quadro = grupo[inMessage.getInfo()].roubarQuadro();
                    System.out.println(quadro);
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPROUBARQUADRO, quadro);
                    break;
                case AssaultPartyMessage.CHECKGRUPONULL:
                    if (grupo[inMessage.getInfo()] == null) {
                        outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCHECKGRUPONULL, true);
                    } else {
                        outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPCHECKGRUPONULL, false);
                    }
                    break;
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

