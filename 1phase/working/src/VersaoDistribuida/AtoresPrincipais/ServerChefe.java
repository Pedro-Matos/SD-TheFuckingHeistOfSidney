package VersaoDistribuida.AtoresPrincipais;

import VersaoDistribuida.AtoresPrincipais.Chefe;
import genclass.GenericIO;
import VersaoDistribuida.ComInfo.ComStrings.*;

/**
 * Created by tiagoalexbastos on 24-04-2017.
 */
public class ServerChefe {

    public static void main(String[] args) {

        Chefe chefe;
        String concentrationSite = null;
        String collectionSite = null;
        String generalRepo = null;

                /* Obtenção dos parâmetros do problema */
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor base? ");
        concentrationSite = GenericIO.readlnString ();
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor escritorio? ");
        collectionSite = GenericIO.readlnString ();
        GenericIO.writeString ("\nNome do sistema computacional onde está o servidor log? ");
        generalRepo = GenericIO.readlnString ();

        chefe = new Chefe("Chefao", generalRepo, collectionSite, concentrationSite);
        chefe.start();

        try {
            chefe.join();
        } catch (InterruptedException ex) {
            System.out.println("O chefe terminou!");
        }

    }

}
