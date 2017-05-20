package RemoteMethodInvocation.interfaces;

import java.rmi.Remote;

/**
 * Created by tiagoalexbastos on 19-05-2017.
 */
public interface AssaultPartyManagerInterface extends Remote {

    public boolean formarGrupo(int idGrupo, int nrSala);

    public int getPos(int ladraoID, int idGrupo);

    public void desfazerGrupo(int idGrupo);

    public void entrar(int ladraoID, int idGrupo, int pos_grupo);

    public int crawlIn(int ladraoID, int agilidade, int idGrupo, int posgrupo);

    public int crawlOut(int ladraoID, int agilidade, int idGrupo, int posgrupo);

    public int getDistanciaSala(int idGrupo);

    public boolean rollACanvas(int idGrupo);

    public void waitMinhaVez(int id, int idGrupo);



}
