package VersaoDistribuida.Mensagens;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class CollectionSiteMessage implements Serializable {

    private static final long serialVersionUID = 1001L;

    /**
     * Mensagem que verifica os grupos
     */
    public static final int CHECKGRUPOS = 0;
    /**
     * Resposta
     */
    public static final int RESPCHECKGRUPOS = 1;

    /**
     * Mensagem que faz o ladrao entrar no grupo
     */
    public static final int ENTRARGRUPO = 2;
    /**
     * Resposta
     */
    public static final int RESPENTRARGRUPO = 3;

    /**
     * Mensagem que indica grupo cheio
     */
    public static final int GRUPOCHEIO = 4;
    /**
     * Resposta
     */
    public static final int RESPGRUPOCHEIO = 5;

    /**
     * Mensagem que indica o estado do chefe
     */
    public static final int GETESTADOCHEFE = 6;
    /**
     * Resposta
     */
    public static final int RESPGETESTADOCHEFE = 7;

    /**
     * Mensagem que inicia o Assalto
     */
    public static final int INICIARASSALTO = 8;
    /**
     * Resposta
     */
    public static final int RESPINICIARASSALTO = 9;

    /**
     * Mensagem que forma o grupo
     */
    public static final int FORMARGRUPO = 10;
    /**
     * Resposta
     */
    public static final int RESPFORMARGRUPO = 11;

    /**
     * Mensagem que planeia o assalto
     */
    public static final int PLANEARASSALTO = 12;
    /**
     * Resposta
     */
    public static final int RESPPLANEARASSALTO = 13;

    /**
     * mensagem que descansa o chefe
     */
    public static final int DESCANSAR = 14;
    /**
     * Resposta
     */
    public static final int RESPDESCANSAR = 15;

    /**
     * Mensagem que retorna o nr de elementos por grupo
     */
    public static final int GETNRELEMENTOSGRUPO = 16;
    /**
     * Resposta
     */
    public static final int RESPGETNRELEMENTOSGRUPO = 17;

    /**
     * Mensagem que retorna o grupo do ladrao
     */
    public static final int GETMEUGRUPO = 18;
    /**
     * Resposta
     */
    public static final int RESPGETMEUGRUPO = 19;

    /**
     * Mensagem que entrega o quadro
     */
    public static final int ENTREGARQUADRO = 20;
    /**
     * Resposta
     */
    public static final int RESPENTREGARQUADRO = 21;

    /**
     * Mensagem que indica a sala vazia
     */
    public static final int INDICARSALAVAZIA = 22;
    /**
     * Resposta
     */
    public static final int RESPINDICARSALAVAZIA = 23;

    /**
     * Mensagem que retorna a posição do grupo
     */
    public static final int GETPOSGRUPO = 24;
    /**
     * Resposta
     */
    public static final int RESPGETPOSGRUPO = 25;

    /**
     * Mensagem que verifica se o museu está vazio
     */
    public static final int CHECKEMPTYMUSEU = 26;
    /**
     * Resposta
     */
    public static final int RESPCHECKEMPTYMUSEU = 27;

    /**
     * Mensagem que termina o assalto
     */
    public static final int TERMINARASSALTO = 28;
    /**
     * Resposta
     */
    public static final int RESPTERMINARASSALTO = 29;

    /**
     * Mensagem que retorna o nr de quadros roubados
     */
    public static final int GETQUADROSROUBADOS = 30;
    /**
     * Resposta
     */
    public static final int RESPGETQUADROSROUBADOS = 31;

    /**
     * Mensagem que verifica se existe salas vazias
     */
    public static final int CHECKSALASVAZIAS = 32;
    /**
     * Resposta
     */
    public static final int RESPCHECKSALASVAZIAS = 33;

    /**
     * Mensagem que conta o numero de groupos
     */
    public static final int GETCOUNTGRUPOS = 34;
    /**
     * Resposta
     */
    public static final int RESPGETCOUNTGRUPOS = 35;

    /**
     * Mensagem que retorna a sala de assalto de um grupo
     */
    public static final int GETSALAASSALTO = 36;
    /**
     * Resposta
     */
    public static final int RESPGETSALAASSALTO = 37;

    /**
     * Mensagem que retorna a sala vazia
     */
    public static final int GETSALAVAZIA = 38;
    /**
     * Resposta
     */
    public static final int RESPGETSALAVAZIA = 39;


    private int info = -1;
    private boolean bInfo = false;
    private int msgType = -1;
    private int ladraoID = -1;
    private int grupoID = -1;
    private int stat;

    @Override
    public String toString() {
        return ("Type "+msgType+"\n idGrupo "+grupoID+"\n info "+info+"\n ladraoId "+ ladraoID+"\n binfo "+bInfo+"\n estado chefe "+stat);
    }



    /**
     * Retorna estado do chefe
     * @return
     */
    public int getStat(){
        return this.stat;
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
     * Retorna o id do Ladrao
     * @return
     */
    public int getLadraoID(){
        return this.ladraoID;
    }

    /**
     * Retorna o ID do grupo
     * @return
     */
    public int getGrupoID(){
        return this.grupoID;
    }

    /**
     * Mensagem tipo 2
     * @param type
     * @param stat
     */
    public CollectionSiteMessage(int type, int stat){
        this.msgType = type;
        this.stat = stat;

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
     * @param ladraoID
     * @param grupoID
     */
    public CollectionSiteMessage(int type, int ladraoID, int grupoID){
        this.msgType = type;
        this.ladraoID = ladraoID;
        this.grupoID = grupoID;
    }

    /**
     * Retorna o Tipo de Mensagem
     * @return
     */
    public int getMsgType(){
        return this.msgType;
    }



}

