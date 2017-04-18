package VersaoDistribuida.Mensagens;

import java.io.Serializable;

/**
 * Definição das mensagens trocadas com o servidor base
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
     * Chamar um ladrao da fila de espera da base
     *
     * @serialField CHAMALADRAO
     */
    public static final int CHAMALADRAO = 1;
    /**
     * Resposta a chamar um ladrao( resposta do servidor)
     *
     * @serialField RESPCHAMALADRAO
     */
    public static final int RESPCHAMALADRAO = 2;
    /**
     * Espera que estejam ladroes suficientes no terreiro
     *
     * @serialField ESPERALADROES
     */
    public static final int ESPERALADROES = 3;
    /**
     * Ladrao espera ordens
     *
     * @serialField ESPERAORDENS
     */
    public static final int AMINEEDED = 4;
    /**
     * Ladrao esta a aguardar no terreiro
     *
     * @serialField ESTOUPRONTO
     */
    public static final int ESTOUPRONTO = 5;
    /**
     * Verifica se o ladrao esta ocupado
     *
     * @serialField GETBUSYLADRAO
     *
     */
    public static final int GETBUSYLADRAO = 6;
    /**
     * Resposta ao estado do ladrao( resposta do servidor)
     *
     * @serialField RESPGETBUSYLADRAO
     */
    public static final int RESPGETBUSYLADRAO = 7;
    /**
     * Pedir o grupo de assalto em que o ladrao esta
     *
     * @serialField GETGRUPOLADRAO
     */
    public static final int GETGRUPOLADRAO = 8;
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
    public static final int GETLADROESBASE = 10;
    /**
     * Resposta do servidor
     *
     * @serialField RESPGETLADROESBASE
     */
    public static final int RESPGETLADROESBASE = 11;
    /**
     * Pedido do numero de ladroes no terreiro
     *
     * @serialField GETNRLADROES
     */
    public static final int GETNRLADROES = 12;
    /**
     * Numero de ladroes no terreiro (resposta do servidor)
     *
     * @serialField RESPGETNRLADROES
     */
    public static final int RESPGETNRLADROES = 13;
    /**
     * Pedido de estado do ladrao
     *
     * @serialField GETSTATELADRAO
     */
    public static final int GETSTATELADRAO = 14;
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
     * @serialField INICIARASSALTO
     */
    public static final int PREPAREEXCURSION = 17;
    /**
     * Indicar que se encontra na sala do museu
     *
     * @serialField NASALA
     */
    public static final int NASALA = 18;
    /**
     * Mudar estado do ladrao para regressar da incursao ao museu
     *
     * @serialField REGRESSARASSALTO
     */
    public static final int REVERSEDIRECTION = 19;
    /**
     * Espera que os ladroes regressem todos ao terreiro
     *
     * @serialField ESPERALADROESFIM
     */
    public static final int ESPERALADROESFIM = 20;


    public static final int GETAGILIDADE = 21;



    /**
     * Resposta de confirmação
     *
     * @serialField ACK
     */
    public static final int ACK = 0;
    private int msgType = -1;
    private int id = -1;
    private boolean check = false;
    private int state;

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
        this.check = check;
    }

    /**
     * Construtor mensagem em que é passado um estado do ladrao alem do tipo de mensagem
     * @param msgType tipo de mensagem
     * @param state estado do ladrão
     */
    public ConcentrationSiteMessage(int msgType, int state) {
        this.msgType = msgType;
        this.state = state;

    }

    /**
     * Getter do tipo de mensagem
     * @return tipo de mensagem
     */
    public int getType() {
        return this.msgType;
    }

    /**
     * Getter do valor inteiro
     * @return valor int
     */
    public int getId() {
        return id;
    }

    /**
     * Getter do valor Boolean
     * @return valor boolean
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Getter do estado do ladrao
     * @return estado do ladrão
     */
    public int getState() {
        return state;
    }


    /**
     *
     * @return Mensagem Base
     */
    @Override
    public String toString() {
        return ("Type " + msgType + "\n id " + id + "\n check  " + check + "\n estado " + state);
    }
}