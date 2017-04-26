package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class CollectionSiteMessage implements Serializable {

    private static final long serialVersionUID = 1001L;

    /**
     * Mensagem que verifica os grupos
     */
    public static final int CHECKGROUPS = 0;
    /**
     * Resposta
     */
    public static final int RESPCHECKGRUPOS = 1;

    /**
     * Mensagem que faz o ladrao entrar no grupo
     */
    public static final int JOINGROUP = 2;
    /**
     * Resposta
     */
    public static final int RESPENTRARGRUPO = 3;

    /**
     * Mensagem que indica grupo cheio
     */
    public static final int ISGROUPDFULL = 4;
    /**
     * Resposta
     */
    public static final int RESPGRUPOCHEIO = 5;

    /**
     * Mensagem que indica o estado do chefe
     */
    public static final int GETMASTERTHIEFSTATE = 6;
    /**
     * Resposta
     */
    public static final int RESPGETESTADOCHEFE = 7;

    /**
     * Mensagem que inicia o Assalto
     */
    public static final int STARTOPERATIONS = 8;
    /**
     * Resposta
     */
    public static final int RESPSTARTOPERATIONS = 9;

    /**
     * Mensagem que forma o grupo
     */
    public static final int PREPAREASSAULTPARTY = 10;
    /**
     * Resposta
     */
    public static final int RESPPREPAREASSAULTPARTY = 11;


    /**
     * mensagem que descansa o chefe
     */
    public static final int TAKEAREST = 14;
    /**
     * Resposta
     */
    public static final int RESPTAKEAREST = 15;

    /**
     * Mensagem que retorna o nr de elementos por grupo
     */
    public static final int GETNRELEMENTSGROUP = 16;
    /**
     * Resposta
     */
    public static final int RESPGETNRELEMENTOSGRUPO = 17;


    /**
     * Mensagem que entrega o quadro
     */
    public static final int HANDACANVAS = 20;
    /**
     * Resposta
     */
    public static final int RESPHANDACANVAS = 21;

    /**
     * Mensagem que indica a sala vazia
     */
    public static final int FLAGEMPTYROOM = 22;
    /**
     * Resposta
     */
    public static final int RESPINDICARSALAVAZIA = 23;

    /**
     * Mensagem que retorna a posição do grupo
     */
    public static final int GETGROUPPOSITION = 24;
    /**
     * Resposta
     */
    public static final int RESPGETPOSGRUPO = 25;

    /**
     * Mensagem que verifica se o Museum está vazio
     */
    public static final int CHECKEMPTYMUSEUM = 26;
    /**
     * Resposta
     */
    public static final int RESPCHECKEMPTYMUSEU = 27;

    /**
     * Mensagem que termina o assalto
     */
    public static final int SUMUPRESULTS = 28;
    /**
     * Resposta
     */
    public static final int RESPSUMUPRESULTS = 29;

    /**
     * Mensagem que retorna o nr de quadros roubados
     */
    public static final int GETSTOLENPAINTINGS = 30;
    /**
     * Resposta
     */
    public static final int RESPGETQUADROSROUBADOS = 31;

    /**
     * Mensagem que verifica se existe salas vazias
     */
    public static final int CHECKEMPTYROOMS = 32;
    /**
     * Resposta
     */
    public static final int RESPCHECKSALASVAZIAS = 33;

    /**
     * Mensagem que retorna a sala de assalto de um grupo
     */
    public static final int GETASSAULTINGROOM = 36;
    /**
     * Resposta
     */
    public static final int RESPGETSALAASSALTO = 37;

    public static final int END = 99;



    private int info = -1;
    private boolean bInfo = false;
    private int msgType = -1;


    private int arg1 = -1;
    private int arg2 = -1;
    private int arg3 = -1;
    private int arg4 = -1;

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

    /**
     * Retorna informação da mensagem
     * @return
     */
    public int getInfo(){
        return this.info;
    }

    /**
     * Retorna informação da mensagem booleana
     * @return
     */
    public boolean bInfo(){
        return this.bInfo;
    }


    /**
     * Mensagem tipo 2
     * @param type
     * @param arg1
     */
    public CollectionSiteMessage(int type, int arg1){
        this.msgType = type;
        this.arg1 = arg1;

    }

    /**
     * Mensagem Tipo 1
     * @param type
     */
    public CollectionSiteMessage(int type){
        this.msgType = type;
    }

    /**
     * Mensagem tipo 4
     * @param type
     * @param info
     */
    public CollectionSiteMessage(int type, boolean info){
        this.msgType = type;
        this.bInfo = info;
    }

    /**
     * Mensagem tipo 5
     * @param type
     * @param arg1
     * @param arg2
     */
    public CollectionSiteMessage(int type, int arg1, int arg2){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    public CollectionSiteMessage(int type, int arg1, int arg2, int arg3){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
    }

    public CollectionSiteMessage(int type, int arg1, int arg2, int arg3, int arg4){
        this.msgType = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
    }

    public boolean getBool(){
        return bInfo;
    }


    /**
     * Retorna o Tipo de Mensagem
     * @return
     */
    public int getMsgType(){
        return this.msgType;
    }


    @Override
    public String toString() {
        return "CollectionSiteMessage{" +
                "info=" + info +
                ", bInfo=" + bInfo +
                ", msgType=" + msgType +
                ", arg1=" + arg1 +
                ", arg2=" + arg2 +
                ", arg3=" + arg3 +
                ", arg4=" + arg4 +
                '}';
    }
}

