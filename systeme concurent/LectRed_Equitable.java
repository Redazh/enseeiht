// Time-stamp: <28 oct 2022 09:24 queinnec@enseeiht.fr>

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Synchro.Assert;

/** Lecteurs/rédacteurs
 * stratégie d'ordonnancement: priorité aux lecteurs,
 * implantation: avec un moniteur. */
public class LectRed_Equitable implements LectRed {
    private ReentrantLock mon;
    private Condition lectureOk;
    private Condition ecritueOk;
    private int nbL;
    private int nbLA;
    private int nbRA;
    private boolean red;

    public LectRed_Equitable() {
        this.mon = new ReentrantLock();
        this.lectureOk = mon.newCondition();
        this.ecritueOk = mon.newCondition();
        this.nbL = 0;
        this.nbLA = 0;
        this.nbRA = 0;
        this.red = false;
    }

    public void demanderLecture() throws InterruptedException {
        mon.lock();
        while (this.red || this.nbRA > 0){
            this.nbLA ++;
            lectureOk.await();
            this.nbLA --;
        }
        this.nbL ++;
        lectureOk.signal();
        mon.unlock();
    }

    public void terminerLecture() throws InterruptedException {
        mon.lock();
        this.nbL --;
        //hna
        //si n'y a plus de lecteurs actifs et il y a un redacteur qui attent
        if (this.nbL == 0 && this.nbRA > 0) {
            ecritueOk.signal();
        } 
        mon.unlock();
    }

    public void demanderEcriture() throws InterruptedException {
        mon.lock();
        //hna
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
        this.lectureOk.signal();
        if (this.nbL == 0) {
            this.ecritueOk.signal();
        }
        mon.unlock();
    }

    public String nomStrategie() {
        return "Stratégie: Equitable.";
    }
}

