package VersaoDistribuida.AtoresPrincipais;

import static VersaoDistribuida.ParametrosDoProblema.Constantes.*;

import VersaoDistribuida.ComInfo.ClientCom;
import VersaoDistribuida.Mensagens.CollectionSiteMessage;
import VersaoDistribuida.Mensagens.ConcentrationSiteMessage;
import VersaoDistribuida.ParametrosDoProblema.GeneralRepository;
import VersaoDistribuida.Regioes.base.ConcentrationSite;
import VersaoDistribuida.Regioes.escritorioChefe.CollectionSite;
import VersaoDistribuida.Regioes.escritorioChefe.CollectionSiteInterface;
import com.sun.tools.javac.util.ClientCodeException;


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

    private int portCollectionSite = 22464;
    private int portConcentrationSite = 22462;
    private int portRepository =22460;

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

        this.generalRepository = new ClientCom(generalRepository, portRepository);
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
        outMessage = new ConcentrationSiteMessage(CollectionSiteMessage.GETNRLADROES);
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
        collectionSite.close();
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


    @Override
    public void run() {

        int numero_grupos = -1;
        boolean heistOver = false;

            while (!heistOver) {

                int stat = getEstadoChefe();

                switch (stat) {
                    case PLANNING_THE_HEIST:

                        generalRepository.iniciarLog();
                        generalRepository.setMasterThiefState(stat);

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
                        generalRepository.finalizarRelatorio(nrQuadrosRoubados);
                        heistOver = true;
                        break;
                }
            }


    }
}
