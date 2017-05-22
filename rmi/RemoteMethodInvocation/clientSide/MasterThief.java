package RemoteMethodInvocation.clientSide;

import RemoteMethodInvocation.interfaces.CollectionSiteInterface;
import RemoteMethodInvocation.interfaces.ConcentrationSiteInterface;
import RemoteMethodInvocation.interfaces.GeneralRepositoryInterface;
import RemoteMethodInvocation.support.Tuple;
import RemoteMethodInvocation.support.VectorTimestamp;

import java.rmi.RemoteException;
import java.util.Vector;

import static RemoteMethodInvocation.support.Constantes.*;


/**
 * Represents the Master Thief
 * @author Pedro Matos and Tiago Bastos
 */
public class MasterThief extends Thread {


    /**
     * Log that contains the thief's values
     */
    private GeneralRepositoryInterface generalRepository;
    /**
     * Concentration Site
     */
    private CollectionSiteInterface collectionSiteInterface;
    /**
     * Thief's concentrationSite
     */
    private ConcentrationSiteInterface concentrationSite;
    /**
     * Thief ID
     */
    private int id;
    /**
     * Thief name
     */
    private String name;

    private VectorTimestamp vt;


    /**
     *
     * @param name Masther Thief's name
     * @param generalRepository General Repository/log
     * @param collectionSiteInterface Concentration Site
     * @param concentrationSite CollectionSite
     */
    public MasterThief(String name, GeneralRepositoryInterface generalRepository, CollectionSiteInterface collectionSiteInterface,
                       ConcentrationSiteInterface concentrationSite) {

        super(name);
        this.name = name;
        this.id = NUM_THIEVES;

        this.generalRepository = generalRepository;

        this.concentrationSite = concentrationSite;
        this.collectionSiteInterface = collectionSiteInterface;

        vt = new VectorTimestamp(VECTOR_TIMESTAMP_SIZE, 0);
    }

    @Override
    public void run() {

        int numero_grupos = -1;
        boolean heistOver = false;

            while (!heistOver) {

                int stat = getMasterThiefState(vt.clone());


                switch (stat) {
                    case PLANNING_THE_HEIST:

                        startLog(vt.clone());
                        setMasterThiefState(stat, vt.clone());

                        if (getNumberOfThieves(vt.clone()) == NUM_THIEVES) {
                            startOperations(vt.clone());
                        }

                        break;
                    case DECIDING_WHAT_TO_DO:
                        numero_grupos = checkGroups(vt.clone());

                        boolean emptyMuseu = checkEmptyMuseum(vt.clone());

                        boolean checkSalasLivres = checkEmptyRooms(vt.clone());
                        if (numero_grupos != -1 && !emptyMuseu && checkSalasLivres)
                            prepareAssaultParty(numero_grupos, vt.clone());
                        else {
                            emptyMuseu = checkEmptyMuseum(vt.clone());
                            if (emptyMuseu)
                                sumUpResults(vt.clone());
                            else
                                takeARest(vt.clone());

                        }
                        break;
                    case ASSEMBLING_A_GROUP:

                        waitForThieves(vt.clone());

                        int nrElemGrupo = getNumberElemGroup(numero_grupos, vt.clone());

                        if (nrElemGrupo == 0) {

                            for (int i = 0; i < NUM_GROUP; i++) {
                                int ladrao = callThief(numero_grupos, vt.clone());
                                joinAssaultParty(ladrao, numero_grupos, vt.clone());
                            }
                            takeARest(vt.clone());
                        }
                        break;
                    case WAITING_FOR_GROUP_ARRIVAL:
                        takeARest(vt.clone());
                        break;
                    case PRESENTING_THE_REPORT:
                        waitForThievesEnd(vt.clone());

                        int nrQuadrosRoubados = getNumberOfStolenPaints(vt.clone());
                        endReport(nrQuadrosRoubados, vt.clone());
                        heistOver = true;
                        break;
                }
            }


    }

    private void endReport(int nrQuadrosRoubados, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.finalizarRelatorio(nrQuadrosRoubados, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getNumberOfStolenPaints(VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getNumberofStolenPaints(vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;    }

    private void waitForThievesEnd(VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = concentrationSite.waitForThievesEnd(vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int joinAssaultParty(int ladrao, int numero_grupos, VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.joinAssaultParty(ladrao, numero_grupos, vectorTimestamp);
            ret = tuple.getSecond();
            vt.update(tuple.getClock());
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int callThief(int numero_grupos, VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.callThief(numero_grupos, vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;    }

    private int getNumberElemGroup(int numero_grupos, VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getNumberElemGroup(numero_grupos, vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void waitForThieves(VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = concentrationSite.waitForThieves(vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void takeARest(VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = collectionSiteInterface.takeARest(vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void sumUpResults(VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = collectionSiteInterface.sumUpResults(vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void prepareAssaultParty(int numero_grupos, VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = collectionSiteInterface.prepareAssaultParty(numero_grupos, vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean checkEmptyRooms(VectorTimestamp vectorTimestamp) {
        boolean ret = false;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Boolean> tuple = collectionSiteInterface.checkEmptyRooms(vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private boolean checkEmptyMuseum(VectorTimestamp vectorTimestamp) {
        boolean ret = false;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Boolean> tuple = collectionSiteInterface.checkEmptyMuseum(vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private int checkGroups(VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.checkGroups(vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void startOperations(VectorTimestamp vectorTimestamp) {
        try {
            vectorTimestamp.increment();
            VectorTimestamp clock = collectionSiteInterface.startOperations(vectorTimestamp);
            vt.update(clock);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getNumberOfThieves(VectorTimestamp vectorTimestamp) {
        int ret = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = concentrationSite.getNumberOfThieves(vectorTimestamp);
            vt.update(tuple.getClock());
            ret = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    private void setMasterThiefState(int stat, VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.setMasterThiefState(stat, vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void startLog(VectorTimestamp vectorTimestamp) {
        try {
            this.generalRepository.startLog(vectorTimestamp);
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private int getMasterThiefState(VectorTimestamp vectorTimestamp) {
        int state = -1;
        try {
            vectorTimestamp.increment();
            Tuple<VectorTimestamp, Integer> tuple = collectionSiteInterface.getMasterThiefState(id, vectorTimestamp);
            vt.update(tuple.getClock());
            state = tuple.getSecond();
        } catch (RemoteException e){
            System.err.println("Excepção na invocação remota de método" + getName() + ": " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        return state;
    }
}
