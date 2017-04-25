package VersaoDistribuida.AtoresPrincipais;

import VersaoDistribuida.ComInfo.ClientCom;
import VersaoDistribuida.Mensagens.AssaultPartyMessage;
import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.GeneralRepositoryMessage;


import static VersaoDistribuida.ParametrosDoProblema.Constantes.*;
import static VersaoDistribuida.ComInfo.ComPorts.*;

/**
 * Ordinary Thief
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class Ladrao extends Thread {

    /**
     * General Repository
     */
    private ClientCom generalRepository;
    /**
     * Assault Party's manager
     */
    private ClientCom grupo;
    /**
     * Concentration Site
     */
    private ClientCom collectionSite;
    /**
     * ConcentrationSite
     */
    private ClientCom concentrationSite;
    /**
     * Thief ID
     */
    private int id;
    /**
     * Thief name
     */
    private String name;
    /**
     * Thief Group
     */
    private int meuGrupo;
    /**
     * Thief Agility
     */
    private int agilidade;
    /**
     * Thief position in the group
     */
    private int pos_grupo;






    /**
     * @param id Thief ID
     * @param name Thief name
     * @param generalRepository General Repository
     * @param grupo Assault Party manager
     * @param collectionSite Concentration Site
     * @param concentrationSite ConcentrationSite
     */
    public Ladrao(int id, String name, String generalRepository, String grupo, String collectionSite, String concentrationSite) {

        super(name);
        this.name = name;
        this.id = id;

        this.generalRepository = new ClientCom(generalRepository, portGeneralRepo);
        this.collectionSite = new ClientCom(collectionSite, portCollectionSite);
        this.grupo = new ClientCom(grupo, portAssaultGroup);
        this.concentrationSite = new ClientCom(concentrationSite, portConcentrationSite);

        this.agilidade = getAgility();

        this.meuGrupo = -1;
        setThiefDisplacement(this.id, this.agilidade);
        this.pos_grupo = -1;
    }

    private void setThiefDisplacement(int id, int agilidade) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFDISPLACEMENT, id, agilidade);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    public int getAgility(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETAGILIDADE, this.id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }


    public void estouPronto(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ESTOUPRONTO, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public int getStateLadrao(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETSTATELADRAO, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    public boolean getBusyLadrao(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETBUSYLADRAO, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg_b1();
    }

    public void amINeeded(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.AMINEEDED, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public int getGrupoLadrao(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETGRUPOLADRAO, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    public int getPos(int id, int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.GETPOS, id,group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();
        return inMessage.getArg1();
    }

    public void prepareExcursion(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.PREPAREEXCURSION, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public void waitMinhaVez(int id, int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.WAITMYTURN, id,group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();
    }

    public int crawlIn(int id, int agilidade, int meuGrupo, int pos_grupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.CRAWLIN, id,agilidade,meuGrupo,pos_grupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public int getDistanciaSala(int group_id){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.GETDISTANCIASALA, group_id);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public void naSala(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.NASALA, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public boolean rollACanvas(int meuGrupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.ROUBARQUADRO, meuGrupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg_b1();
    }

    public int getSalaAssalto(int meuGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETSALAASSALTO, meuGrupo);
        grupo.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();

    }

    public void reverseDirection(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.REVERSEDIRECTION, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    public int crawlOut(int id, int agilidade, int meuGrupo, int pos_grupo){
        AssaultPartyMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new AssaultPartyMessage(AssaultPartyMessage.CRAWLOUT, id,agilidade,meuGrupo,pos_grupo);
        grupo.writeObject(outMessage);
        inMessage = (AssaultPartyMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public int getPosGrupo(int id, int meuGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETPOSGRUPO,id, meuGrupo);
        grupo.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) grupo.readObject();
        grupo.close();

        return inMessage.getArg1();
    }

    public void handACanvas(int id, int tmp_salaassalto, int meuGrupo, int tmp_getPosGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.HANDACANVAS, id,tmp_salaassalto,meuGrupo,tmp_getPosGrupo);
        grupo.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) grupo.readObject();
        grupo.close();

    }

    public void indicarSalaVazia(int tmp_salaassalto, int meuGrupo, int tmp_getPosGrupo){
        CollectionSiteMessage inMessage, outMessage;

        while(!this.grupo.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.INDICARSALAVAZIA,tmp_salaassalto,meuGrupo,tmp_getPosGrupo);
        grupo.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) grupo.readObject();
        grupo.close();

    }

    public void indicarChegada(int id){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.INDICARCHEGADA, id);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    private void setThiefState(int id, int stat) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETTHIEFSTATE, id, stat);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP1_canvas(int pos_grupo, boolean quadro, int room) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1CANVAS, pos_grupo, quadro, room);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP2_canvas(int pos_grupo, boolean quadro, int room) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2CANVAS, pos_grupo, quadro, room);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP1_pos(int pos_grupo, int posicao) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1POS, pos_grupo, posicao);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP2_pos(int pos_grupo, int posicao) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2POS, pos_grupo, posicao);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP1_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP1RESET, pos_grupo, id);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    private void setAP2_reset(int pos_grupo, int id) {
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETAP2RESET, pos_grupo, id);
        generalRepository.writeObject(outMessage);
        generalRepository.close();
    }

    @Override
    public void run() {

        int posicao = 0;

        estouPronto(this.id);

        boolean quadro = false;
        boolean heistOver = false;

        while (!heistOver) {
            int stat = getStateLadrao(this.id);

            switch (stat) {

                case OUTSIDE:
                    setThiefState(this.id, stat);

                    boolean getBusyLadrao = getBusyLadrao(id);

                    if (!getBusyLadrao && this.meuGrupo == -1) {
                        amINeeded(this.id);
                    } else {
                        //obter o grupo
                        this.meuGrupo = getGrupoLadrao(id);
                        //obter a posicao no grupo
                        this.pos_grupo = getPos(this.id, this.meuGrupo);

                        if(this.meuGrupo != -1 && this.pos_grupo != -1){
                            prepareExcursion(this.id);
                        }
                    }
                    break;


                case CRAWLING_INWARDS:

                    waitMinhaVez(id, this.meuGrupo);
                    posicao = crawlIn(this.id, agilidade, meuGrupo, this.pos_grupo);
                    int getDistanciaSala = getDistanciaSala(meuGrupo);

                    if (posicao == getDistanciaSala) {
                        naSala(id);
                    }
                    break;

                case AT_A_ROOM:

                    quadro = rollACanvas(meuGrupo);

                    int room = getSalaAssalto(this.meuGrupo);

                    if (this.meuGrupo == 0) {
                        setAP1_canvas(this.pos_grupo, quadro, room);
                    } else if (this.meuGrupo == 1) {
                        setAP2_canvas(this.pos_grupo, quadro, room);
                    }
                    reverseDirection(id);
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        waitMinhaVez(id, this.meuGrupo);
                        posicao = crawlOut(this.id, this.agilidade, this.meuGrupo, this.pos_grupo);
                    }

                    if (this.meuGrupo == 0) {
                        setAP1_pos(this.pos_grupo, posicao);
                    } else if (this.meuGrupo == 1) {
                        setAP2_pos(this.pos_grupo, posicao);
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            int tmp_salaassalto = getSalaAssalto(meuGrupo);
                            int tmp_getPosGrupo = getPosGrupo(id, meuGrupo);
                            handACanvas(id, tmp_salaassalto, this.meuGrupo, tmp_getPosGrupo);

                        } else {

                            int tmp_salaassalto = getSalaAssalto(meuGrupo);
                            int tmp_getPosGrupo = getPosGrupo(id, meuGrupo);
                            indicarSalaVazia(tmp_salaassalto, this.meuGrupo, tmp_getPosGrupo);
                        }

                        if (this.meuGrupo == 0) setAP1_reset(this.pos_grupo, this.id);
                        else if (this.meuGrupo == 1) setAP2_reset(this.pos_grupo, this.id);

                        this.meuGrupo = -1;
                        indicarChegada(id);
                    }

                    break;

                case HEIST_END:

                    heistOver = true;
                    break;
            }
        }
    }
}
