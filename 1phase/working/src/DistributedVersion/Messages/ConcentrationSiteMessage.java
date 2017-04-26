package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Definição das mensagens trocadas com o servidor ConcentrationSite
 *
 * @author pedro e tiago
 */
public class ConcentrationSiteMessage implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1002L;
    /**
     * Chamar um ladrao da fila de espera da ConcentrationSite
     *
     * @serialField CALLTHIEF
     */
    public static final int CALLTHIEF = 1;
    /**
     * Resposta a chamar um ladrao( resposta do servidor)
     *
     * @serialField RESPCHAMALADRAO
     */
    public static final int RESPCHAMALADRAO = 2;
    /**
     * Espera que estejam ladroes suficientes no terreiro
     *
     * @serialField WAITTHIVES
     */
    public static final int WAITTHIVES = 3;
    /**
     * Thief espera ordens
     *
     * @serialField ESPERAORDENS
     */
    public static final int AMINEEDED = 4;
    /**
     * Thief esta a aguardar no terreiro
     *
     * @serialField IMREADY
     */
    public static final int IMREADY = 5;
    /**
     * Verifica se o ladrao esta ocupado
     *
     * @serialField GETBUSYTHIEF
     *
     */
    public static final int GETBUSYTHIEF = 6;
    /**
     * Resposta ao estado do ladrao( resposta do servidor)
     *
     * @serialField RESPGETBUSYLADRAO
     */
    public static final int RESPGETBUSYLADRAO = 7;
    /**
     * Pedir o grupo de assalto em que o ladrao esta
     *
     * @serialField GETGROUPTHIEF
     */
    public static final int GETGROUPTHIEF = 8;
    /**
     * Resposta com o id do grupo em que o ladrao está
     *
     * @serialField RESPGETGRUPOLADRAO
     */
    public static final int RESPGETGRUPOLADRAO = 9;
    /**
     * Verifica se existem ladroes na fila de espera no terreiro
     *
     * @serial GETLADROESBASE
     */
    /**
     * Resposta do servidor
     *
     * @serialField RESPGETLADROESBASE
     */
    public static final int RESPGETLADROESBASE = 11;
    /**
     * Pedido do numero de ladroes no terreiro
     *
     * @serialField GETTHIEFNUMBER
     */
    public static final int GETTHIEFNUMBER = 12;
    /**
     * Numero de ladroes no terreiro (resposta do servidor)
     *
     * @serialField RESPGETNRLADROES
     */
    public static final int RESPGETNRLADROES = 13;
    /**
     * Pedido de estado do ladrao
     *
     * @serialField GETSTATETHIEF
     */
    public static final int GETSTATETHIEF = 14;
    /**
     * Estado do ladrao (resposta do servidor)
     *
     * @serialField RESPGETSTATELADRAO
     */
    public static final int RESPGETSTATELADRAO = 15;
    /**
     * Indicar que esta regressou do assalto
     *
     * @serialField INDICARREGRESSO
     */
    public static final int INDICARCHEGADA = 16;
    /**
     * Mudar estado do ladrao para iniciar assalto
     *
     * @serialField STARTOPERATIONS
     */
    public static final int PREPAREEXCURSION = 17;
    /**
     * Indicar que se encontra na sala do Museum
     *
     * @serialField NASALA
     */
    public static final int NASALA = 18;
    /**
     * Mudar estado do ladrao para regressar da incursao ao Museum
     *
     * @serialField REGRESSARASSALTO
     */
    public static final int REVERSEDIRECTION = 19;
    /**
     * Espera que os ladroes regressem todos ao terreiro
     *
     * @serialField WAITTHIEVESEND
     */
    public static final int WAITTHIEVESEND = 20;

    public static final int GETAGILITY = 21;

    public static final int RESPGETAGILITY = 22;

    public static final int END = 99;


    /**
     * Resposta de confirmação
     *
     * @serialField ACK
     */
    public static final int ACK = 0;
    private int msgType = -1;
    /*private int id = -1;
    private boolean check = false;
    private int state;*/

    private int arg1 = -1;
    private boolean arg_b1 = false;

    /**
     * Construtor mensagem
     * @param msgType tipo mensagem
     */
    public ConcentrationSiteMessage(int msgType) {
        this.msgType = msgType;
    }

//    /**
//     * Construtor mensagem em que é passado um valor inteiro alem do tipo de mensagem
//     * @param msgType tipo mensagem
//     * @param id
//     */
//    public BaseMessage(int msgType, int id) {
//        this.msgType = msgType;
//        this.id = id;
//    }

    /**
     *Construtor mensagem em que é passado um valor boolean alem do tipo de mensagem
     * @param msgType tipo de mensagem
     * @param check
     */
    public ConcentrationSiteMessage(int msgType, boolean check) {
        this.msgType = msgType;
        this.arg_b1 = check;
    }

    /**
     * Construtor mensagem em que é passado um estado do ladrao alem do tipo de mensagem
     * @param msgType tipo de mensagem
     * @param state estado do ladrão
     */
    public ConcentrationSiteMessage(int msgType, int state) {
        this.msgType = msgType;
        this.arg1 = state;

    }

    /**
     * Getter do tipo de mensagem
     * @return tipo de mensagem
     */
    public int getType() {
        return this.msgType;
    }

    public int getArg1() {
        return arg1;
    }

    public boolean getArg_b1() {
        return arg_b1;
    }
}