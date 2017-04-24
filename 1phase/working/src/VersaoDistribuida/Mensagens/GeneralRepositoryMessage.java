package VersaoDistribuida.Mensagens;

import java.io.Serializable;

/**
 * Created by pmatos9 on 24/04/17.
 */
public class GeneralRepositoryMessage implements Serializable {

    private static final long serialVersionUID = 1001L;

    public static final int INICIARLOG = 0;

    public static final int ADDLOG = 1;

    public static final int FINALIZARELATORIO = 2;

    public static final int SETDISTANCIASALA = 3;

    public static final int SETNRQUADROSALA = 4;

    public static final int SETMASTERTHIEFSTATE = 5;

    public static final int SETTHIEFSTATE = 6;

    public static final int SETTHIEFSITUATION = 7;

    public static final int SETTHIEFDISPLACEMENT = 8;

    public static final int SETASSAULTPARTY1ROOM = 9;

    public static final int SETASSAULTPARTY2ROOM = 10;

    public static final int SETAP1POS = 11;

    public static final int SETAP1CANVAS = 12;

    public static final int SETAP1POSIDCANVAS = 13;

    public static final int SETAP1RESET = 14;

    public static final int SETAP2POS = 15;

    public static final int SETAP2CANVAS = 16;

    public static final int SETAP2POSIDCANVAS = 17;

    public static final int SETAP2RESET = 18;


    public static final int ACK = 0;
    private int msgType = -1;
    private int arg1 = -1;
    private int arg2 = -1;
    private int arg3 = -1;
    private boolean b1 = false;

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

    public boolean getB1() {
        return b1;
    }

    public GeneralRepositoryMessage(int msgType){
        this.msgType = msgType;
    }

    public GeneralRepositoryMessage(int msgType, int arg1){
        this.msgType = msgType;
        this.arg1 = arg1;
    }

    public GeneralRepositoryMessage(int msgType, int arg1, int arg2){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public GeneralRepositoryMessage(int msgType, int arg1, boolean b1, int arg2){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.b1 = b1;
        this.arg2 = arg2;
    }

    public GeneralRepositoryMessage(int msgType, int arg1, int arg2, int arg3, boolean b1){
        this.msgType = msgType;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.b1 = b1;
    }






}
