package DistributedVersion.Messages;

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
     * @serialField JOINPARTY
     */
    public static final int JOINPARTY = 1;
    /**
     * Pedir distancia da sala do Museum
     *
     * @serialField GETROOMDISTANCE
     */
    public static final int GETROOMDISTANCE = 2;
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
     * Rastejar em direçao á sala do Museum
     *
     * @serialField CRAWLIN
     */
    public static final int CRAWLIN = 12;

    public static final int RESPCRAWLIN = 13;

    /**
     * Rastejar em direção ao terreiro
     *
     * @serialField CRAWLOUT
     */
    public static final int CRAWLOUT = 14;

    public static final int RESPCRAWLOUT = 15;

    /**
     * Roubar um quadro de uma sala do Museum
     *
     * @serialField STEALPAINTING
     */
    public static final int STEALPAINTING = 16;
    /**
     * Resposta a roubar quadro
     *
     * @serialField RESPROUBARQUADRO
     */
    public static final int RESPROUBARQUADRO = 17;
    /**
     * Formar um novo grupo de assalto
     *
     * @serialField PREPAREASSAULTPARTY
     */
    public static final int CREATEASSAULTPARTY = 18;
    /**
     * Confirmaçao de novo grupo formado
     *
     * @serialField RESPPREPAREASSAULTPARTY
     */
    public static final int RESPFORMARGRUPO = 19;
    /**
     * Desfazer um grupo de assalto
     *
     * @serialField DESTROYGRPOUP
     */
    public static final int DESTROYGRPOUP = 20;
    /**
     * Verificar se o grupo está formado
     *
     * @serialField CHECKGRUPONULL
     */
    public static final int CHECKGRUPONULL = 21;
    /**
     * Resposta a verificar se o grupo está formado
     *
     * @serialField RESPCHECKGRUPONULL
     */
    public static final int RESPCHECKGRUPONULL = 22;

    public static final int WAITMYTURN = 23;

    public static final int END = 99;
    /**
     * Resposta de confirmação
     *
     * @serialField ACK
     */
    public static final int ACK = 0;
/*    private int msgType = -1;
    private int idGrupo = -1;
    private int info = -1;
    private int agilidade = -1;
    private boolean quadro = false;
    private boolean grupoNull = false;
    private int ThiefId = -1;
    private int PosGrupo = -1;*/

    private int msgType = -1;
    private int arg1 = -1;
    private int arg2 = -1;
    private int arg3 = -1;
    private int arg4 = -1;
    private boolean arg_b1 = false;



    public AssaultPartyMessage(int msgType) {
        this.msgType = msgType;
    }

    /**
     *Instanciação de uma mensagem (tipo 2)
     * @param msgType tipo de mensagem
     * @param arg1
     */
    public AssaultPartyMessage(int msgType, int arg1) {
        this.msgType = msgType;
        this.arg1 = arg1;
    }

    /**
     *Instanciação de uma mensagem (tipo 3)
     * @param msgType tipo de mensagem
     * @param arg1 id do grupo
     * @param arg2 id do ladrao
     */
    public AssaultPartyMessage(int msgType, int arg1, int arg2) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    /**
     *Instanciação de uma mensagem (tipo 4)
     * @param msgType tipo de mensagem
     * @param arg1 id do grupo
     * @param arg2 id do ladrão
     * @param arg3 agilidade do ladrao
     */
    public AssaultPartyMessage(int msgType, int arg1, int arg2, int arg3) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }
    public AssaultPartyMessage(int msgType, int arg1, int arg2, int arg3, int arg4) {
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
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
                this.arg_b1 = info;
                break;
            case AssaultPartyMessage.CHECKGRUPONULL:
                this.arg_b1 = info;
                break;
            case AssaultPartyMessage.RESPCHECKGRUPONULL:
                this.arg_b1 = info;
                break;
        }
    }

    public int getMsgType() {
        return msgType;
    }

    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }

    public int getArg3() {
        return arg3;
    }

    public int getArg4() {
        return arg4;
    }

    public boolean getArg_b1() {
        return arg_b1;
    }

    @Override
    public String toString() {
        return "AssaultPartyMessage{" +
                "msgType=" + msgType +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", arg3=" + arg3 +
                ", arg_b1=" + arg_b1 +
                '}';
    }
}

