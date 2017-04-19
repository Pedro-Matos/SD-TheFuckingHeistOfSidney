package VersaoDistribuida.AtoresPrincipais;

import VersaoDistribuida.ComInfo.ClientCom;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.MuseuMessage;
import VersaoDistribuida.ParametrosDoProblema.GeneralRepository;
import VersaoDistribuida.Regioes.base.ConcentrationSite;
import VersaoDistribuida.Regioes.escritorioChefe.CollectionSite;
import VersaoDistribuida.Regioes.gruposAssalto.GrupoAssalto;


import static VersaoConcorrente.ParametrosDoProblema.Constantes.*;

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
    private ClientCom escritorio;
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


    private int portCollectionSite = 22464;
    private int portConcentrationSite = 22462;
    private int portGroup = 22463;
    private int portRepository =22460;



    /**
     * @param id Thief ID
     * @param name Thief name
     * @param generalRepository General Repository
     * @param grupo Assault Party manager
     * @param escritorio Concentration Site
     * @param concentrationSite ConcentrationSite
     */
    public Ladrao(int id, String name, String generalRepository, String grupo, String escritorio, String concentrationSite) {

        super(name);
        this.name = name;
        this.id = id;

        this.generalRepository = new ClientCom(generalRepository, portRepository);
        this.escritorio = new ClientCom(escritorio, portCollectionSite);
        this.grupo = new ClientCom(grupo, portGroup);
        this.concentrationSite = new ClientCom(concentrationSite, portConcentrationSite);

        this.agilidade = getAgility();

        this.meuGrupo = -1;
        this.generalRepository.setThiefDisplacement(this.id, this.agilidade);
        this.pos_grupo = -1;
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


    @Override
    public void run() {

        int posicao = 0;

        concentrationSite.estouPronto(this.id);

        boolean quadro = false;
        boolean heistOver = false;

        while (!heistOver) {
            int stat = concentrationSite.getStateLadrao(id);

            switch (stat) {

                case OUTSIDE:
                    generalRepository.setThiefState(this.id, stat);

                    boolean getBusyLadrao = concentrationSite.getBusyLadrao(id);

                    if (!getBusyLadrao && this.meuGrupo == -1) {
                        concentrationSite.amINeeded(this.id);
                    } else {


                        //obter o grupo
                        this.meuGrupo = concentrationSite.getGrupoLadrao(id);
                        //obter a posicao no grupo
                        this.pos_grupo = this.grupo.getPos(this.id, this.meuGrupo);

                        if(this.meuGrupo != -1 && this.pos_grupo != -1){
                            concentrationSite.prepareExcursion(this.id);
                        }




                    }
                    break;


                case CRAWLING_INWARDS:

                    grupo.waitMinhaVez(id, this.meuGrupo);
                    posicao = grupo.crawlIn(this.id, agilidade, meuGrupo, this.pos_grupo);
                    int getDistanciaSala = this.grupo.getDistanciaSala(meuGrupo);

                    if (posicao == getDistanciaSala) {
                        concentrationSite.naSala(id);
                    }
                    break;

                case AT_A_ROOM:

                    quadro = this.grupo.rollACanvas(meuGrupo);

                    int room = escritorio.getSalaAssalto(this.meuGrupo);

                    if (this.meuGrupo == 0) {
                        generalRepository.setAP1_canvas(this.pos_grupo, quadro, room);
                    } else if (this.meuGrupo == 1) {
                        generalRepository.setAP2_canvas(this.pos_grupo, quadro, room);
                    }


                    concentrationSite.reverseDirection(id);
                    break;

                case CRAWLING_OUTWARDS:

                    if (posicao != 0) {
                        grupo.waitMinhaVez(id, this.meuGrupo);
                        posicao = grupo.crawlOut(this.id, this.agilidade, this.meuGrupo, this.pos_grupo);
                    }

                    if (this.meuGrupo == 0) {
                        generalRepository.setAP1_pos(this.pos_grupo, posicao);
                    } else if (this.meuGrupo == 1) {
                        generalRepository.setAP2_pos(this.pos_grupo, posicao);
                    }

                    if (posicao == 0) {
                        if (quadro) {

                            int getSalaAssalto = escritorio.getSalaAssalto(meuGrupo);
                            int getPosGrupo = escritorio.getPosGrupo(id, meuGrupo);
                            escritorio.handACanvas(id, getSalaAssalto, this.meuGrupo, getPosGrupo);

                        } else {

                            int getSalaAssalto = escritorio.getSalaAssalto(meuGrupo);
                            int getPosGrupo = escritorio.getPosGrupo(id, meuGrupo);
                            escritorio.indicarSalaVazia(getSalaAssalto, this.meuGrupo, getPosGrupo);
                        }

                        if (this.meuGrupo == 0) generalRepository.setAP1_reset(this.pos_grupo, this.id);
                        else if (this.meuGrupo == 1) generalRepository.setAP2_reset(this.pos_grupo, this.id);

                        this.meuGrupo = -1;
                        concentrationSite.indicarChegada(id);
                    }

                    break;

                case HEIST_END:

                    heistOver = true;
                    break;
            }
        }
    }
}
