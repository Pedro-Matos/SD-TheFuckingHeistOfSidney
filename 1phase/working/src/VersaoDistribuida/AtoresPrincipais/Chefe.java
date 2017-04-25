package VersaoDistribuida.AtoresPrincipais;

import static VersaoDistribuida.ComInfo.ComPorts.*;
import static VersaoDistribuida.ParametrosDoProblema.Constantes.*;

import VersaoDistribuida.ComInfo.ClientCom;
import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.Mensagens.GeneralRepositoryMessage;


/**
 * Represents the Master Thief
 * @author Pedro Matos and Tiago Bastos
 */
public class Chefe extends Thread {


    /**
     * Log that contains the thief's values
     */
    private ClientCom generalRepository;
    /**
     * Concentration Site
     */
    private ClientCom collectionSite;
    /**
     * Thief's concentrationSite
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
     *
     * @param name Masther Thief's name
     * @param generalRepository General Repository/log
     * @param collectionSite Concentration Site
     * @param concentrationSite ConcentrationSite
     */
    public Chefe(String name, String generalRepository, String collectionSite, String concentrationSite) {

        super(name);
        this.name = name;
        this.id = NUM_THIEVES;

        this.generalRepository = new ClientCom(generalRepository, portGeneralRepo);
        this.concentrationSite = new ClientCom(concentrationSite, portConcentrationSite);
        this.collectionSite = new ClientCom(collectionSite, portCollectionSite);
    }

    private int getEstadoChefe(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETESTADOCHEFE);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

        return inMessage.getArg1();
    }


    private int getNrLadroes(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.GETNRLADROES);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();

        return inMessage.getArg1();
    }

    private void startOperations(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.STARTOPERATIONS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    private int checkGrupos(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKGRUPOS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    private boolean checkEmptyMuseu(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKEMPTYMUSEU);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getBool();
    }

    private boolean checkSalasLivres(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.CHECKSALASVAZIAS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getBool();
    }

    private void prepareAssaultParty(int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.PREPAREASSAULTPARTY, numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        generalRepository.close();

    }

    private void sumUpResults(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.SUMUPRESULTS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    private void takeARest(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.TAKEAREST);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();

    }

    private void esperaLadroes(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ESPERALADROES);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    private int getNrElemGrupo(int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETNRELEMENTOSGRUPO,numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    private int chamaLadrao(int numero_grupos){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.CHAMALADRAO,numero_grupos);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
        return inMessage.getArg1();
    }

    private void entrarGrupo(int ladrao, int numero_grupos){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.ENTRARGRUPO,ladrao,numero_grupos);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
    }

    private void esperaLadroesFim(){
        ConcentrationSiteMessage inMessage, outMessage;

        while(!concentrationSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new ConcentrationSiteMessage(ConcentrationSiteMessage.ESPERALADROESFIM);
        concentrationSite.writeObject(outMessage);
        inMessage = (ConcentrationSiteMessage) concentrationSite.readObject();
        concentrationSite.close();
    }

    private int getQuadrosRoubados(){
        CollectionSiteMessage inMessage, outMessage;

        while(!collectionSite.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }
        outMessage = new CollectionSiteMessage(CollectionSiteMessage.GETQUADROSROUBADOS);
        collectionSite.writeObject(outMessage);
        inMessage = (CollectionSiteMessage) collectionSite.readObject();
        collectionSite.close();
        return inMessage.getArg1();
    }

    private void iniciarLog(){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.INICIARLOG);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void setMasterThiefState(int stat){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.SETMASTERTHIEFSTATE, stat);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }

    private void finalizarRelatorio(int quadros){
        GeneralRepositoryMessage inMessage, outMessage;

        while(!generalRepository.open()){
            try{
                Thread.sleep((long)(1000));
            }
            catch (InterruptedException e){
            }
        }

        outMessage = new GeneralRepositoryMessage(GeneralRepositoryMessage.FINALIZARELATORIO, quadros);
        generalRepository.writeObject(outMessage);
        inMessage = (GeneralRepositoryMessage) generalRepository.readObject();
        generalRepository.close();
    }


    @Override
    public void run() {

        int numero_grupos = -1;
        boolean heistOver = false;

            while (!heistOver) {

                int stat = getEstadoChefe();

                switch (stat) {
                    case PLANNING_THE_HEIST:

                        iniciarLog();
                        setMasterThiefState(stat);

                        if (getNrLadroes() == NUM_THIEVES) {
                            startOperations();
                        }

                        break;


                    case DECIDING_WHAT_TO_DO:


                        numero_grupos = checkGrupos();


                        boolean emptyMuseu = checkEmptyMuseu();

                        boolean checkSalasLivres = checkSalasLivres();
                        if (numero_grupos != -1 && !emptyMuseu && checkSalasLivres) {
                            prepareAssaultParty(numero_grupos);
                        }
                        else {
                            emptyMuseu = checkEmptyMuseu();
                            if (emptyMuseu) {
                                sumUpResults();
                            } else {
                                takeARest();
                            }
                        }

                        break;


                    case ASSEMBLING_A_GROUP:


                        esperaLadroes();


                        int nrElemGrupo = getNrElemGrupo(numero_grupos);

                        if (nrElemGrupo == 0) {

                            for (int i = 0; i < NUM_GROUP; i++) {

                                int ladrao = chamaLadrao(numero_grupos);
                                entrarGrupo(ladrao,numero_grupos);
                            }
                            takeARest();
                        }
                        break;


                    case WAITING_FOR_GROUP_ARRIVAL:
                        takeARest();
                        break;

                    case PRESENTING_THE_REPORT:
                        esperaLadroesFim();

                        int nrQuadrosRoubados = getQuadrosRoubados();
                        finalizarRelatorio(nrQuadrosRoubados);
                        heistOver = true;
                        break;
                }
            }


    }
}
