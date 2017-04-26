package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseuMessage implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1000L;
    public static final int ROUBARQUADRO = 0;
    /**
     *
     */
    public static final int SENDRESPROUBARQUADRO = 1;
    /**
     *
     */
    public static final int GETDISTANCIA = 2;
    /**
     *
     */
    public static final int SENDDISTANCIA = 3;
    /**
     *
     */
    public static final int GETNUMEROQUADROS = 4;
    /**
     *
     */
    public static final int SENDNUMEROQUADROS = 5;

    public static final int TERMINAR = 99;
    /**
     *
     */
    private int msgType = -1;
    private int nrSala = -1;
    private int distanciaSala = -1;
    private int nrQuadros = -1;
    private boolean quadroRoubado = false;

    @Override
    public String toString() {
        return ("Type "+msgType+"\n nrSala "+nrSala+"\n distancia Sala "+distanciaSala+"\n nrQuadros "+ nrQuadros+"\n quadro "+quadroRoubado);
    }



    /**
     *
     * @param type
     */
    public MuseuMessage(int type) {
        this.msgType = type;
    }

    /**
     *
     * @param type
     * @param info
     */
    public MuseuMessage(int type, int info) {
        this.msgType = type;

        switch (type) {
            case MuseuMessage.GETDISTANCIA:
                this.nrSala = info;
                break;
            case MuseuMessage.GETNUMEROQUADROS:
                this.nrSala = info;
                break;
            case MuseuMessage.ROUBARQUADRO:
                this.nrSala = info;
                break;
            case MuseuMessage.SENDDISTANCIA:
                this.distanciaSala = info;
                break;
            case MuseuMessage.SENDNUMEROQUADROS:
                this.nrQuadros = info;
                break;
        }

    }

    /**
     *
     * @param type
     * @param info
     */
    public MuseuMessage(int type, boolean info) {
        this.msgType = type;
        this.quadroRoubado = info;
    }

    /**
     *
     * @return
     */
    public boolean getQuadroRoubado() {
        return this.quadroRoubado;
    }

    /**
     *
     * @return
     */
    public int getNrQuadros() {
        return this.nrQuadros;
    }

    /**
     *
     * @return
     */
    public int getDistanciaSala() {
        return this.distanciaSala;
    }

    /**
     *
     * @return
     */
    public int getNrSala() {
        return this.nrSala;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return this.msgType;
    }
}

