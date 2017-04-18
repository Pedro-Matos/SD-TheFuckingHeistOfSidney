package VersaoDistribuida.Mensagens;

import java.io.Serializable;

/**
 * Created by pmatos9 on 18/04/17.
 */
public class AssaultPartyMessage implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1003L;
    /**
     * Entrar no grupo de assalto
     *
     * @serialField ENTRAR
     */
    public static final int ENTRAR = 1;
    /**
     * Pedir distancia da sala do museu
     *
     * @serialField GETDISTANCIASALA
     */
    public static final int GETDISTANCIASALA = 2;
    /**
     * Resposta ao pedido de distancia da sala
     *
     * @serialField RESPGETDISTANCIASALA
     */
    public static final int RESPGETDISTANCIASALA = 3;
    /**
     * Pedido de indice do ladrao no grupo
     *
     * @serialField GETINDICE
     */
    public static final int GETINDICE = 4;
    /**
     * Resposta ao pedido de indice do ladrao no grupo
     *
     * @serialField RESPGETINDICE
     */
    public static final int RESPGETINDICE = 5;
    /**
     * Pedido do id do ladrao
     *
     * @serialField  GETMEMBRO
     */
    public static final int GETMEMBRO = 6;
    /**
     * Resposta ao pedido de id do ladrao
     *
     * @serialField RESPGETMEMBRO
     */
    public static final int RESPGETMEMBRO = 7;
    /**
     * Pedido de nr de elementos no grupo
     *
     * @serialField GETNRELEMENTOS
     */
    public static final int GETNRELEMENTOS = 8;
    /**
     * Resposta do pedido de numero de elementos no grupo
     *
     * @serialField RESPGETNRELEMENTOS
     */
    public static final int RESPGETNRELEMENTOS = 9;
    /**
     * Pedido da posiçao do ladrao
     *
     * @serialField GETPOS
     */
    public static final int GETPOS = 10;
    /**
     * Resposta ao pedido de posiçao do ladrao
     *
     * @serialField RESPGETPOS
     */
    public static final int RESPGETPOS = 11;
    /**
     * Rastejar em direçao á sala do museu
     *
     * @serialField RASTEJARIN
     */
    public static final int RASTEJARIN = 12;
    /**
     * Rastejar em direção ao terreiro
     *
     * @serialField RASTEJAROUT
     */
    public static final int RASTEJAROUT = 13;
    /**
     * Roubar um quadro de uma sala do museu
     *
     * @serialField ROUBARQUADRO
     */
    public static final int ROUBARQUADRO = 14;
    /**
     * Resposta a roubar quadro
     *
     * @serialField RESPROUBARQUADRO
     */
    public static final int RESPROUBARQUADRO = 15;
    /**
     * Formar um novo grupo de assalto
     *
     * @serialField FORMARGRUPO
     */
    public static final int FORMARGRUPO = 16;
    /**
     * Confirmaçao de novo grupo formado
     *
     * @serialField RESPFORMARGRUPO
     */
    public static final int RESPFORMARGRUPO = 17;
    /**
     * Desfazer um grupo de assalto
     *
     * @serialField DESFAZERGRUPO
     */
    public static final int DESFAZERGRUPO = 18;
    /**
     * Resposta a rastejar
     *
     * @serialField RESPRASTEJAR
     */
    public static final int RESPRASTEJAR = 19;
    /**
     * Verificar se o grupo está formado
     *
     * @serialField CHECKGRUPONULL
     */
    public static final int CHECKGRUPONULL = 20;
    /**
     * Resposta a verificar se o grupo está formado
     *
     * @serialField RESPCHECKGRUPONULL
     */
    public static final int RESPCHECKGRUPONULL = 21;
    /**
     * Resposta de confirmação
     *
     * @serialField ACK
     */
    public static final int ACK = 0;
    private int msgType = -1;
    private int idGrupo = -1;
    private int info = -1;
    private int agilidade = -1;
    private boolean quadro = false;
    private boolean grupoNull = false;

    /**
     * Getter grupoNull
     * @return true se o grupo estiver a null, false se o grupo existir
     */
    public boolean grupoNull() {
        return this.grupoNull;
    }

    /**
     * Getter do idGrupo
     * @return id do grupo de assalto
     */
    public int getIdGrupo() {
        return idGrupo;
    }

    /**
     * Getter do quadro
     * @return true se tiver um quadro, false se vazio
     */
    public boolean isQuadro() {
        return quadro;
    }

    /**
     * Getter da agilidade do ladrao
     * @return agilidade do ladrão
     */
    public int getAgilidade() {
        return agilidade;
    }

    /**
     * Getter do tipo de mensagem
     * @return tipo de mensagem
     */
    public int getType() {
        return msgType;
    }

    /**
     * Getter do valor inteiro
     * @return valor int
     */
    public int getInfo() {
        return info;
    }

    /**
     * Instanciação de uma mensagem (tipo 1)
     * @param msgType tipo de mensagem
     */
    public AssaultPartyMessage(int msgType) {
        this.msgType = msgType;
    }

    /**
     *Instanciação de uma mensagem (tipo 2)
     * @param msgType tipo de mensagem
     * @param info
     */
    public AssaultPartyMessage(int msgType, int info) {
        this.msgType = msgType;
        this.info = info;
    }

    /**
     *Instanciação de uma mensagem (tipo 3)
     * @param msgType tipo de mensagem
     * @param idGrupo id do grupo
     * @param id id do ladrao
     */
    public AssaultPartyMessage(int msgType, int idGrupo, int id) {
        this.msgType = msgType;
        this.info = id;
        this.idGrupo = idGrupo;
    }

    /**
     *Instanciação de uma mensagem (tipo 4)
     * @param msgType tipo de mensagem
     * @param idGrupo id do grupo
     * @param id id do ladrão
     * @param agilidade agilidade do ladrao
     */
    public AssaultPartyMessage(int msgType, int idGrupo, int id, int agilidade) {
        this.msgType = msgType;
        this.agilidade = agilidade;
        this.info = id;
        this.idGrupo = idGrupo;
    }

    /**
     *Instanciação de uma mensagem (tipo 5)
     * @param msgType tipo de mensagem
     * @param info
     */
    public AssaultPartyMessage(int msgType, boolean info) {
        this.msgType = msgType;


        switch (this.msgType) {
            case AssaultPartyMessage.RESPROUBARQUADRO:
                this.quadro = info;
                break;
            case AssaultPartyMessage.CHECKGRUPONULL:
                this.grupoNull = info;
                break;
            case AssaultPartyMessage.RESPCHECKGRUPONULL:
                this.grupoNull = info;
                break;
        }
    }

    /**
     *
     * @return Mensagem Grupo de Assalto
     */
    @Override
    public String toString() {
        return ("Type " + msgType + "\n idGrupo " + idGrupo + "\n info " + info + "\n agilidade " + agilidade + "\n quadro " + quadro);
    }
}

