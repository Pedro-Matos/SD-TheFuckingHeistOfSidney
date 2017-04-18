package VersaoDistribuida.Regioes.gruposAssalto;

import VersaoDistribuida.Mensagens.AssaultPartyMessage;
import VersaoDistribuida.Mensagens.AssaultPartyMessageException;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class GrupoAssaltoInterface {

        private GrupoAssalto[] grupo;
        private String museu;

        GrupoAssaltoInterface(GrupoAssalto[] grupo, String museu) {
            this.grupo = grupo;
            this.museu = museu;
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
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= Constantes.N) {
                        throw new AssaultPartyMessageException("Sala invalida", inMessage);
                    }
                    break;
                case AssaultPartyMessage.ENTRAR:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= Constantes.K) {
                        throw new AssaultPartyMessageException("id Ladrao invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETDISTANCIASALA:
                    break;
                case AssaultPartyMessage.GETINDICE:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= Constantes.K) {
                        throw new AssaultPartyMessageException("id Ladrao invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETMEMBRO:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= Constantes.G) {
                        throw new AssaultPartyMessageException("indice elemento do grupo invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETPOS:
                    if (inMessage.getIdGrupo() < 0 || inMessage.getIdGrupo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido5", inMessage);
                    }
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() >= Constantes.G) {
                        throw new AssaultPartyMessageException("indice elemento do grupo invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.GETNRELEMENTOS:
                case AssaultPartyMessage.DESFAZERGRUPO:
                case AssaultPartyMessage.ROUBARQUADRO:
                    if (inMessage.getInfo() < 0 || inMessage.getInfo() > 1) {
                        throw new AssaultPartyMessageException("Grupo invalido", inMessage);
                    }
                    break;
                case AssaultPartyMessage.CHECKGRUPONULL:
                    break;
                case AssaultPartyMessage.RASTEJARIN:
                case AssaultPartyMessage.RASTEJAROUT:
                    if (inMessage.getInfo() >= Constantes.K || inMessage.getInfo() < 0) {
                        throw new AssaultPartyMessageException("Ladrao invalido", inMessage);
                    }
                    if (inMessage.getAgilidade() < 0 || inMessage.getAgilidade() > Constantes.A) {
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


                    grupo[inMessage.getIdGrupo()].entrar(inMessage.getInfo());
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
                    resp = grupo[inMessage.getIdGrupo()].getIndice(inMessage.getInfo());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETINDICE, resp);
                    break;
                case AssaultPartyMessage.GETMEMBRO:
                    if (grupo[inMessage.getIdGrupo()] != null) {
                        resp = grupo[inMessage.getIdGrupo()].getMembro(inMessage.getInfo());
                    } else {
                        resp = -1;
                    }
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETMEMBRO, resp);
                    break;
                case AssaultPartyMessage.GETNRELEMENTOS:
                    resp = grupo[inMessage.getInfo()].getNrElementos();
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETNRELEMENTOS, resp);
                    break;
                case AssaultPartyMessage.GETPOS:
                    if (grupo[inMessage.getIdGrupo()] != null) {
                        resp = grupo[inMessage.getIdGrupo()].getPos(inMessage.getInfo());
                    } else {
                        resp = -1;
                    }
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPGETPOS, resp);
                    break;
                case AssaultPartyMessage.RASTEJARIN:
                    resp = grupo[inMessage.getIdGrupo()].rastejarIn(inMessage.getInfo(), inMessage.getAgilidade());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPRASTEJAR, resp);
                    break;
                case AssaultPartyMessage.RASTEJAROUT:
                    resp = grupo[inMessage.getIdGrupo()].rastejarOut(inMessage.getInfo(), inMessage.getAgilidade());
                    outMessage = new AssaultPartyMessage(AssaultPartyMessage.RESPRASTEJAR, resp);
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
                grupo[id] = new GrupoAssalto(museu, nrSala);
                return id;
            } else {
                return -1;
            }
        }

        public void desfazerGrupo(int id) {
            grupo[id] = null;
        }
}

