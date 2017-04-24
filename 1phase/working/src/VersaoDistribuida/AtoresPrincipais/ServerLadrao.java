package VersaoDistribuida.AtoresPrincipais;

import VersaoDistribuida.ParametrosDoProblema.Constantes;
import genclass.GenericIO;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerLadrao {

    public static void main(String[] args) {
        Ladrao[] ladrao = new Ladrao[Constantes.NUM_THIEVES];
        String concentrationSite = null;
        String collectionSite = null;
        String grupoAssalto = null;
        String generalRepo = null;


                /*
         * Obtenção dos parâmetros do problema
         */
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor base? ");
        concentrationSite = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor escritorio? ");
        collectionSite = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor grupos de assalto? ");
        grupoAssalto = GenericIO.readlnString();
        GenericIO.writeString("\nNome do sistema computacional onde está o servidor log? ");
        generalRepo = GenericIO.readlnString();


        for (int i = 0; i < Constantes.NUM_THIEVES; i++) {
            ladrao[i] = new Ladrao(i, "Ladrao " + i, generalRepo, grupoAssalto, collectionSite, concentrationSite);
            ladrao[i].start();
        }

        for (int i = 0; i < Constantes.NUM_THIEVES; i++) {
            try {
                ladrao[i].join();
            } catch (InterruptedException ex) {
                System.out.println("O Ladrão " + i + " terminou o seu trabalho!");
            }
        }
    }

}
