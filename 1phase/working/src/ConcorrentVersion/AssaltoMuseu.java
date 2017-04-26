package ConcorrentVersion;

import static ConcorrentVersion.ParametrosDoProblema.Constantes.*;

import ConcorrentVersion.ParametrosDoProblema.GeneralRepository;
import ConcorrentVersion.Regioes.base.ConcentrationSite;
import ConcorrentVersion.Regioes.escritorioChefe.CollectionSite;
import ConcorrentVersion.Regioes.gruposAssalto.GestorGruposAssalto;
import ConcorrentVersion.Regioes.museu.Museum;
import ConcorrentVersion.AtoresPrincipais.Chefe;
import ConcorrentVersion.AtoresPrincipais.Ladrao;

/**
 * Main Program
 *
 * @author Pedro Matos and Tiago Bastos
 */
public class AssaltoMuseu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        for(int z = 0; z <10; z++){
            System.out.println("Tentativa: "+z);



            GeneralRepository generalRepository = new GeneralRepository();

            Museum museum = new Museum(generalRepository);
            ConcentrationSite concentrationSite = new ConcentrationSite(generalRepository);
            GestorGruposAssalto gestorGrupos = new GestorGruposAssalto(museum, generalRepository);
            CollectionSite escritorio = new CollectionSite(museum, concentrationSite, gestorGrupos, generalRepository);


            Chefe chefe;

            Ladrao[] ladrao = new Ladrao[NUM_THIEVES];
            for (int i = 0; i < NUM_THIEVES; i++) {
                String nomeLadrao = "Thief-" + i;
                ladrao[i] = new Ladrao(i, nomeLadrao, generalRepository, gestorGrupos, escritorio, concentrationSite);
                ladrao[i].start();
            }


            chefe = new Chefe("LadraoChefe", generalRepository, escritorio, concentrationSite);
            chefe.start();

            try {
                chefe.join();
            } catch (InterruptedException ex) {
                System.out.println("Master Thief is over!");
            }


            for (int i = 0; i < NUM_THIEVES; i++) {
                try {
                    ladrao[i].join();
                } catch (InterruptedException ex) {
                    System.out.println("Thief " + i + " is over!");
                }
            }
        }
    }
}
