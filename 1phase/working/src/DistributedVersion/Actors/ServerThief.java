package DistributedVersion.Actors;

import DistributedVersion.ProblemInformation.Constants;
import genclass.GenericIO;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerThief {

    public static void main(String[] args) {
        Thief[] thief = new Thief[Constants.NUM_THIEVES];
        String concentrationSite = null;
        String collectionSite = null;
        String grupoAssalto = null;
        String generalRepo = null;


                /*
         * Obtenção dos parâmetros do problema
         */
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor ConcentrationSite? ");
        concentrationSite = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor escritorio? ");
        collectionSite = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor grupos de assalto? ");
        grupoAssalto = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor log? ");
        generalRepo = GenericIO.readlnString();


        for (int i = 0; i < Constants.NUM_THIEVES; i++) {
            thief[i] = new Thief(i, "Thief " + i, generalRepo, grupoAssalto, collectionSite, concentrationSite);
            thief[i].start();
        }

        for (int i = 0; i < Constants.NUM_THIEVES; i++) {
            try {
                thief[i].join();
            } catch (InterruptedException ex) {
                System.out.println("O Ladrão " + i + " terminou o seu trabalho!");
            }
        }
    }

}
