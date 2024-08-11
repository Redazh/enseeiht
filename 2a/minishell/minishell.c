#define _XOPEN_SOURCE 700

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <string.h>
#include <signal.h>
#include "readcmd.h"
#include <fcntl.h>


/* Structure Process pour stocker les informations des processus */
struct Process {
      int ident;
      pid_t pid;
      char *etat; 
      char **cmd;
      int taille_cmd;
      struct Process *suiv;
};

typedef struct Process Process;

Process *pr = NULL; // Liste des processus en arrière-plan
int ident_suiv = 1;  // Identifiant suivant à attribuer à un nouveau processus
pid_t pid_avant_plan = -1; // Contient le pid en avant-plan et est egal a -1 sinon

/* Trouver un processus dans la liste pr avec son pid */
Process *trouverAvecPid(pid_t pid_tr) {
       Process *pr_trouv = pr;
       while (pr_trouv != NULL && pr_trouv->pid != pid_tr) {
            pr_trouv = pr_trouv->suiv;
       }     
       return pr_trouv;
}


/* supprimer un processus de la liste  pr*/
void supprimer_pr (pid_t pid_supp) {
        Process *pr_avant = NULL;
        Process *pr_courant = pr;
        while (pr_courant != NULL && pr_courant->pid != pid_supp) {
            pr_avant = pr_courant;
            pr_courant = pr_courant->suiv;
        } 
        if (pr_courant->pid == pid_supp) {
             if (pr_avant != NULL) {
                 pr_avant->suiv = pr_courant->suiv;
             } else {
                pr = pr_courant->suiv;
             }
             free(pr_courant);    
        }          
}

/* handler pour SIGCHLD*/
void handler_sigchld(int signal_num) {
     int wstatus;
     pid_t pid_enf;
     while ((pid_enf = waitpid(-1, &wstatus, WNOHANG|WUNTRACED|WCONTINUED))>0) {
           Process *pr_enf = trouverAvecPid(pid_enf);
           if (pr_enf != NULL) {
               if (WIFSTOPPED(wstatus)) {
                    pr_enf->etat = "suspendu";
               } else if (WIFCONTINUED(wstatus)) {
                    pr_enf->etat = "actif";
               } else if (WIFEXITED(wstatus)) {
                    supprimer_pr(pid_enf);
               } 
           } 
           
     }
}

/* handler pour SIGTSTP*/
void handler_sigtstp(int signal_num) {
       if(pid_avant_plan != -1){
          kill(pid_avant_plan,SIGSTOP);
          pid_avant_plan = -1; 
       }
}

/* handler pour SIGINT*/
void handler_sigint(int signal_num) {
      if(pid_avant_plan != -1){
          supprimer_pr(pid_avant_plan);
          kill(pid_avant_plan,SIGKILL);
          pid_avant_plan = -1;
          
       }
}


/* trouver un processus dans la liste avec son identifiant */
Process *trouverAvecIdent(int ident_tr) {
       Process *pr_trouv = pr;
       while (pr_trouv != NULL && pr_trouv->ident != ident_tr) {
            pr_trouv = pr_trouv->suiv;
       }     
       return pr_trouv;
}

/* lister les processus lances */
void listjobs () {
     Process *pr_lj = pr;
     while (pr_lj != NULL) {
           printf("Identifiant : %d, pid : %d, etat : %s, commande :  ", pr_lj->ident, pr_lj->pid, pr_lj->etat);
           //Afficher commande 
           for (int i = 0; i < pr_lj->taille_cmd; i++) {
                printf("%s ", pr_lj->cmd[i]);
           }
           printf("\n");
           pr_lj = pr_lj->suiv;
     }
     
}

/* suspendre un processus avec un identifiant donné */
void stopjob (int ident_sj) {
     Process *pr_sj = trouverAvecIdent(ident_sj);
     if ( pr_sj != NULL) {
          pid_t pid_sj = pr_sj->pid;
          kill(pid_sj, SIGSTOP);          
     }
}


/* reprendre un processus en arrière-plan avec un identifiant donné */
void background (int ident_bg) {
     Process *pr_bg = trouverAvecIdent(ident_bg);
     if ( pr_bg != NULL) {
          pid_t pid_bg = pr_bg->pid;
          kill(pid_bg, SIGCONT);         
     }
}


/* poursuivre un processus en avant-plan avec un identifiant donné */
void foreground (int ident_fg) {
     Process *pr_fg = trouverAvecIdent(ident_fg);
     if ( pr_fg != NULL) {
          pid_t pid_fg = pr_fg->pid;
          pid_avant_plan = pid_fg;
          kill(pid_fg, SIGCONT); 
          sleep(1);
          int wstatus;
          waitpid(pid_fg, &wstatus, 0);        
     }    
}


/* ajouter un nouveau processus à la liste */
void ajouter_pr (pid_t pid_ajout, char *etat_aj, char **cmd_aj) {
     //creer le pr a ajouter 
     Process *pr_aj = (Process *)malloc(sizeof(Process));
     pr_aj->ident = ident_suiv;
     ident_suiv++;
     pr_aj->pid = pid_ajout;
     pr_aj->etat = etat_aj;
     
     //Calculer la taille de la commande
     int taille_cmdaj = 0;
     while (cmd_aj[taille_cmdaj] != NULL) {
           taille_cmdaj++;
     }
     pr_aj->taille_cmd = taille_cmdaj;
     
     pr_aj->cmd = malloc(sizeof(char *) * (taille_cmdaj + 1));
     
     
     for (int i = 0; i<taille_cmdaj; i++) {
        pr_aj->cmd[i] = malloc(strlen(cmd_aj[i]) + 1);
        strcpy(pr_aj->cmd[i], cmd_aj[i]);
     }
     
          
     pr_aj->suiv = NULL;
     
     //ajouter pr_aj a pr
     if (pr == NULL) {
         pr = pr_aj;
     } else {
         Process *pr_dernier = pr;
         while (pr_dernier->suiv != NULL) {
               pr_dernier = pr_dernier->suiv;
         }
         pr_dernier->suiv = pr_aj;
     }
     
     
}



int main () {



     while(1) {
         printf("sh-3.2$ ");
         //pid_avant_plan = -1;
         signal(SIGCHLD, handler_sigchld);
         signal(SIGTSTP, handler_sigtstp);
         signal(SIGINT, handler_sigint);
        

       
         struct cmdline *cmd = readcmd();
       
         
         if (cmd != NULL && cmd->seq[0] != NULL && cmd->seq[0][0] != NULL) {
            
            //Si la commande est exit 
            if (strcmp(cmd->seq[0][0], "exit") == 0) {
                exit(EXIT_SUCCESS);
            
            
            //Si la commande est cd
            } else if (strcmp(cmd->seq[0][0], "cd") == 0) {
                if (cmd->seq[0][1] != NULL) {
                     if (chdir(cmd->seq[0][1]) == -1) {
                        perror("cd");
                    }
                }
                
            //Si la commande est lj
            } else if (strcmp(cmd->seq[0][0], "lj") == 0) {
                listjobs ();
            
            
            //Si la commande est sj
            } else if (strcmp(cmd->seq[0][0], "sj") == 0) {
                  if (cmd->seq[0][1] != NULL) {
                        int ident_sj = atoi(cmd->seq[0][1]);
                        stopjob(ident_sj);
                        
                  }
            
            
            //Si la commande est bg
            } else if (strcmp(cmd->seq[0][0], "bg") == 0) {
                    if (cmd->seq[0][1] != NULL) {
                        int ident_bg = atoi(cmd->seq[0][1]);
                        background(ident_bg);
                        
                    }
            
            //Si la commande est fg
            } else if (strcmp(cmd->seq[0][0], "fg") == 0) {
                    if (cmd->seq[0][1] != NULL) {
                        int ident_fg = atoi(cmd->seq[0][1]);
                        foreground(ident_fg);
                        
                    }  

            //Si la commande est susp        
            } else if (strcmp(cmd->seq[0][0], "susp") == 0){
                    signal(SIGTSTP,SIG_DFL);
                    kill(getpid(),SIGSTOP);
            } else {
                
                
                pid_t pidFils = fork();
            
                /* bonne pratique : tester systematiquement le retour des appels systeme */
                if (pidFils == -1) {
                    printf ("Erreur fork \n");
                    exit (EXIT_FAILURE);
                    /* par convention , renvoyer une valeur > 0 en cas d ’ erreur ,
                     * differente pour chaque cause d ’ erreur
                     */
                }

                if (pidFils == 0) { /* fils */
                    signal(SIGTSTP,SIG_IGN);
                    signal(SIGINT,SIG_IGN);
                    int conditionIN = (cmd->in != NULL);
                    int conditionOUT = (cmd->out != NULL);
                     
                    int desc_ent;
                    int desc_sort;
                    int dupdesc;

                    if (conditionIN){
                        desc_ent = open(cmd->in,O_RDONLY);
                        /* traiter systématiquement les retours d'erreur des appels */
                        if (desc_ent < 0) {
                        printf("Erreur ouverture %s\n", cmd->in) ;
                        exit(1) ;
                        }

                        /* rediriger sdtin vers desc_ent */
                        dupdesc = dup2(desc_ent, 0) ;
                        if (dupdesc == -1) {   /* échec du dup2 */
                            printf("Erreur dup2\n") ;
                            exit(1) ;
                        }
                    }

                    if (conditionOUT){
                        /* ouverture du fichier en ecriture, avec autorisations rw- -r- ---*/
                        /* avec création si le fichier n'existe pas : O_CREAT */
                        /* avec vidange (raz du contenu) si le fichier existe: O_TRUNC */
                        desc_sort = open(cmd->out, O_WRONLY | O_CREAT | O_TRUNC, 0640) ;
                        /* traiter systématiquement les retours d'erreur des appels */
                        if (desc_sort < 0) {
                        printf("Erreur ouverture %s\n", cmd->out) ;
                        exit(1) ;
                        }

                        /* rediriger sdout vers desc_sort*/
                        dupdesc = dup2(desc_sort, 1) ;
                        if (dupdesc == -1) {   /* échec du dup2 */
                            printf("Erreur dup2\n") ;
                            exit(1) ;
                        }
                    }


                    /*Si il y a une deuxieme commande on les relie par des tubes*/
                    if (cmd->seq[1]!=NULL) {
                        /*nombre de commande*/
                        int n_cmd = 2; //Si on est dans le boucle alors il y a au moins 2 commande
                        while (cmd->seq[n_cmd]!=NULL){
                            n_cmd++;
                        }

                        /* Créet NB_PIPE pipes */
                        int NB_PIPE = n_cmd-1;
                        int pipe_f2p[NB_PIPE][2];
                        int retour;  
                        
                        for (int i = 0 ; i < NB_PIPE ; i++) {
                                retour = pipe(pipe_f2p[i]) ;
                                if (retour == -1) {   /* échec du pipe */
                                    printf("Erreur pipe\n") ;
                                    exit(1) ;
                                }
                        }

                        /*Parcourir tout les commandes*/
                        for (int j = 0 ; j < n_cmd ; j++) {
                            pid_t pidFils2 = fork();

                            /* bonne pratique : tester systematiquement le retour des appels systeme */
                            if (pidFils2 == -1) {
                                printf ("Erreur fork \n");
                                exit (EXIT_FAILURE);
                                /* par convention , renvoyer une valeur > 0 en cas d ’ erreur ,
                                 * differente pour chaque cause d ’ erreur
                                */
                            }

                            if (pidFils2 == 0) { /* fils */


                                for (int k =0;k < NB_PIPE ; k++){
                                        if(k != j){
                                            close(pipe_f2p[k][1]);
                                        }
                                        if(k != j-1){
                                            close(pipe_f2p[k][0]);
                                        }
                                }
                                /*Si c'est pas la 1ere commande*/
                                if (j != 0){
                                    int dup0 =dup2(pipe_f2p[j-1][0],0);
                                    if (dup0 == -1) {   /* échec du dup2 */
                                            printf("Erreur dup2\n") ;
                                            exit(1);
                                    }
                                }

                                /*Si c'est pas la derniere commande*/
                                if (j != n_cmd-1){

                                    int dup1 =dup2(pipe_f2p[j][1],1);
                                    if (dup1 == -1) {   /* échec du dup2 */
                                            printf("Erreur dup2\n") ;
                                            exit(1);
                                    }
                                }

                                execvp(cmd->seq[j][0],cmd->seq[j]);
                                printf ("Erreur execvp \n");
                                exit (EXIT_FAILURE);

                            }

                        }
                        for(int i =0 ; i < NB_PIPE ;i++){
                            close(pipe_f2p[i][1]);
                            close(pipe_f2p[i][0]);
                        }
                        for (int i = 0; i < n_cmd; i++) {
                            wait(NULL);
                        }
                        exit(EXIT_SUCCESS);

                    }

                    execvp(cmd->seq[0][0], cmd->seq[0]);
                    printf ("Erreur execvp \n");
                    exit(EXIT_FAILURE); 
                }
            
                else { /* pere */
                    // Vérifier si le '&' est présent à la fin de la ligne de commande
                    int condition = (cmd->backgrounded != NULL);
                    ajouter_pr(pidFils, "actif", cmd->seq[0]);
                    // attendre que le processus fils se termine
                    if (!condition) {
                        pid_avant_plan = pidFils;
                        int status;
                        waitpid(pidFils, &status, 0);
                        pid_avant_plan = -1;
                    } else {
                          
                    }
                    
                }
            
            }
        } 
                
    }
            
            
}   
