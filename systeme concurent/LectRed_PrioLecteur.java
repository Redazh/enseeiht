// Time-stamp: <28 oct 2022 09:24 queinnec@enseeiht.fr>

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Synchro.Assert;

/** Lecteurs/rédacteurs
 * stratégie d'ordonnancement: priorité aux lecteurs,
 * implantation: avec un moniteur. */
public class LectRed_PrioLecteur implements LectRed {
    private ReentrantLock mon;
    private Condition lectureOk;
    private Condition ecritueOk;
    // nb de lecteur actuellement en lecture
    private int nbL;
    // nb de Lecteurs en attente
    private int nbLA;
    // true si quelqu'un ecrit
    private boolean red;

    public LectRed_PrioLecteur() {
        this.mon = new ReentrantLock();
        this.lectureOk = mon.newCondition();
        this.ecritueOk = mon.newCondition();
        this.nbL = 0;
        this.nbLA = 0;
        this.red = false;
    }

    public void demanderLecture() throws InterruptedException {
        mon.lock();
        // tq il y qlq'un qui ecrit on doit attendre
        while (this.red){
            this.nbLA ++;
            lectureOk.await();
            this.nbLA --;
        }
        this.nbL ++;
        // permet à plusieurs lecteurs de lire simultanément
        lectureOk.signal();
        mon.unlock();
    }

    public void terminerLecture() throws InterruptedException {
        mon.lock();
        this.nbL --;
        //si n'y a plus de lecteurs on libere l'ecriture
        if (this.nbL == 0) {
            ecritueOk.signal();
        }
        mon.unlock();
    }

    public void demanderEcriture() throws InterruptedException {
        mon.lock();
        //tq il y a des lecteurs ou il y a qui ecrit ou il y a qlq qui attent de lire (Prio Lecteur), on doit attendre 
        while (this.nbL > 0 || this.red || this.nbLA > 0) {
            ecritueOk.await();
        }
        this.red = true;
        mon.unlock();
    }

    public void terminerEcriture() throws InterruptedException {
        mon.lock();
        this.red = false;
        //on libere d'abord la lecteure (Priorité Lecteur)
        this.lectureOk.signal();
        if (this.nbL == 0) {
            this.ecritueOk.signal();
        }
        mon.unlock();
    }

    public String nomStrategie() {
        return "Stratégie: Priorité Lecteurs.";
    }
}

