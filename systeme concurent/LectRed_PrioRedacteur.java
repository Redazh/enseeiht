// Time-stamp: <28 oct 2022 09:24 queinnec@enseeiht.fr>

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Synchro.Assert;

/** Lecteurs/rédacteurs
 * stratégie d'ordonnancement: priorité aux rédacteurs,
 * implantation: avec un moniteur. */
public class LectRed_PrioRedacteur implements LectRed {
    private ReentrantLock mon;
    private Condition lectureOk;
    private Condition ecritueOk;
    // nb de lecteur actuellement en lecture
    private int nbL;
    // nb de rédacteurs en attente
    private int nbRA;
    // true si quelqu'un ecrit
    private boolean red;

    public LectRed_PrioRedacteur() {
        this.mon = new ReentrantLock();
        this.lectureOk = mon.newCondition();
        this.ecritueOk = mon.newCondition();
        this.nbL = 0;
        this.nbRA = 0;
        this.red = false;
    }

    public void demanderLecture() throws InterruptedException {
        mon.lock();
        // tq il y qlq'un qui ecrit ou il y a un redacteur qui attent d'ecrire (Prio Redacteur) on doit attendre
        while (this.red || this.nbRA != 0){
              lectureOk.await();
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
        //tq il y a des lecteurs ou il y a qui ecrit , on doit attendre
        while (this.nbL > 0 || this.red) {
            this.nbRA ++;
            ecritueOk.await();
            this.nbRA --;
        }
        this.red = true;
        mon.unlock();
    }

    public void terminerEcriture() throws InterruptedException {
        mon.lock();
        this.red = false;
        //on libere d'abord l'ecriture (Priorité Rédacteurs)
        this.ecritueOk.signal();
        if (this.red == false) {
            this.lectureOk.signal();
        }
        mon.unlock();
    }

    public String nomStrategie() {
        return "Stratégie: Priorité Rédacteurs.";
    }
}
