package DistributedVersion.Messages;

import java.io.Serializable;

/**
 * Created by pmatos9 on 17/04/17.
 */
public class MuseumMessage implements Serializable {

    /**
     * Chave de serialização
     *
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 1000L;
    public static final int STEALPAINTING = 0;
    /**
     *
     */
    public static final int SENDRESPROUBARQUADRO = 1;
    /**
     *
     */
    public static final int GETDISTANCE = 2;
    /**
     *
     */
    public static final int SENDDISTANCIA = 3;
    /**
     *
     */
    public static final int GETNUMBEROFSTOLENGPAINTINGS = 4;
    /**
     *
     */
    public static final int SENDNUMEROQUADROS = 5;

    public static final int END = 99;
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
    public MuseumMessage(int type) {
        this.msgType = type;
    }

    /**
     *
     * @param type
     * @param info
     */
    public MuseumMessage(int type, int info) {
        this.msgType = type;

        switch (type) {
            case MuseumMessage.GETDISTANCE:
                this.nrSala = info;
                break;
            case MuseumMessage.GETNUMBEROFSTOLENGPAINTINGS:
                this.nrSala = info;
                break;
            case MuseumMessage.STEALPAINTING:
                this.nrSala = info;
                break;
            case MuseumMessage.SENDDISTANCIA:
                this.distanciaSala = info;
                break;
            case MuseumMessage.SENDNUMEROQUADROS:
                this.nrQuadros = info;
                break;
        }

    }

    /**
     *
     * @param type
     * @param info
     */
    public MuseumMessage(int type, boolean info) {
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

