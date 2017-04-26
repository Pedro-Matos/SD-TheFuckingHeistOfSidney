package DistributedVersion.Actors;

import genclass.GenericIO;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerMasterThief {

    public static void main(String[] args) {

        MasterThief masterThief;
        String concentrationSite = null;
        String collectionSite = null;
        String generalRepo = null;

                /* Obtenção dos parâmetros do problema */
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor ConcentrationSite? ");
        concentrationSite = GenericIO.readlnString ();
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor escritorio? ");
        collectionSite = GenericIO.readlnString ();
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor log? ");
        generalRepo = GenericIO.readlnString ();

        masterThief = new MasterThief("Chefao", generalRepo, collectionSite, concentrationSite);
        masterThief.start();

        try {
            masterThief.join();
        } catch (InterruptedException ex) {
            System.out.println("O masterThief terminou!");
        }

    }

}
