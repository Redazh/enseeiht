import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
import numpy as np

csv_names = ["low", "avg", "high"]
def matrice_adh(df,d):
    num_nodes = len(df)
    M = np.zeros((num_nodes, num_nodes))
    for i in range(num_nodes):
        for j in range(i + 1, num_nodes):
            distance = ((df.iloc[i]['x'] - df.iloc[j]['x'])**2 +
                        (df.iloc[i]['y'] - df.iloc[j]['y'])**2 +
                        (df.iloc[i]['z'] - df.iloc[j]['z'])**2)**0.5
            if distance <= d:
                M[i, j] = 1
                M[j, i] = 1
    return M
def calculer_carateristiques(df, d, afficher):
    #Calculer la matrice d'adjacence
    M = matrice_adh(df, d)
    # Construire le graphe 
    G = nx.from_numpy_array(M)
    # Degré moyen
    degree_noeud = np.sum(M, axis=1)
    avg_degree = np.mean(degree_noeud)
    print(f"Le degré moyen vaut : {avg_degree}")
    # Calcul de la distribution du degré
    degree_distribution = np.bincount(degree_noeud.astype(int))/len(M)
    # Plotting la distribution du degré
    if afficher.lower() == "o":
        plt.bar(range(len(degree_distribution)), degree_distribution, width=0.8, align='center')
        plt.xlabel('Degré')
        plt.ylabel('Nombre de noeuds')
        plt.title('Distribution du degré du graphe', fontsize=10)
        plt.show()
    # Moyenne du clustering
    avg_clustering = nx.average_clustering(G)
    print(f"la moyenne du degré de clustring: {avg_clustering}")

    # Distribution du clustering  
    clustering_distribution = list(nx.clustering(G).values())
    #Plotting de la distribution du clustering
    if afficher.lower() == "o":
        plt.hist(clustering_distribution, bins = 20, edgecolor='black', alpha=0.7)
        plt.xlabel('Degré')
        plt.ylabel('Nombre de noeuds')
        plt.title('Distribution du degré clustering du graphe', fontsize=10)
        plt.show()
    # Nombre de cliques et l'ordre des cliques 
    cliques = list(nx.find_cliques(G))
    print(f"Nombre de cliques vaut : {len(cliques)}")
    afficher_ordre = input("Voulez-vous afficher l'ordre de chaque clique ? (o/n) : ")
    if (afficher_ordre.lower()=="o"):
        for s, clique in enumerate(cliques):
            print(f"L'ordre de la clique {s}:", len(clique))
    # Nombre de composantes connexes et leurs ordres 
    comp_connex = list(nx.connected_components(G))
    print(f"Nombre de composantes connexes vaut : {len(comp_connex)}")
    for l, comp in enumerate(comp_connex):
        print(f"L'ordre de la composante connexe {l}:", len(comp))
        
    # Longueur des chemins les plus courts 
    courts_chemins = dict(nx.all_pairs_shortest_path_length(G))
    print(f"Les chemins les plus court du graphe sont: {courts_chemins}")
    longueurs = [length for lengths in courts_chemins.values() for length in lengths.values()]
    # Plot the distribution of shortest path lengths
    if afficher.lower() == "o":
        plt.hist(longueurs, bins=np.arange(0, max(longueurs) + 1.5) - 0.5, edgecolor='black', alpha=0.7)
        plt.title('Distribution des plus courts chemins')
        plt.xlabel('La longueur des plus courts chemins')
        plt.ylabel('Frequence')
        plt.show()

    #Nombre des plus courts chemins entre source et entrée
    def nb_pluscourtschemins(graph, source, target):
        sp = {}
        nb_sp = 0
        if nx.has_path(graph, source=source, target=target):
            # Si un chemin existe, trouver tous les chemins les plus courts entre l'origine et la destination
            sp[(source, target)] = []
            for path in nx.all_shortest_paths(graph, source=source, target=target):
                sp[(source, target)].append(path)
                nb_sp +=1
            
        return nb_sp, sp
    # Obtenir l'entrée de l'utilisateur pour les nœuds source et cible
    continuer = 'o'
    while (continuer == 'o'):

        noeud_source = int(input("Entrez le noeud source : "))
        noeud_cible = int(input("Entrez le noeud cible : "))
        afficher_chemins = input("Voulez-vous afficher les chemins les plus courts ? (o/n) : ")
        # Calculer le nombre de plus courts chemins et leurs longueurs
        nb_sp, sp = nb_pluscourtschemins(G, noeud_source, noeud_cible)
        if (sp == {}):
            print(f"Nombre de plus courts chemins entre les noeuds {noeud_source} et {noeud_cible} dans le graphe vaut: {nb_sp}")
        else :
            print(f"Nombre de plus courts chemins entre les noeuds {noeud_source} et {noeud_cible} dans le graphe vaut: {nb_sp} et de longueur : {len(sp[(noeud_source, noeud_cible)][0])-1}")
        # Imprimer chaque chemin individuellement avec un label
        if (sp != {}) and (afficher_chemins.lower() == "o"):
            for l, path in enumerate(sp[(noeud_source, noeud_cible)], 1):
                print(f"Chemin {l}:", path)
        continuer = input("Voulez-vous continuer ? (o/n) : ")


topology_df = input("Choisis une topologie (low/avg/high): ").strip().lower()
distance = int(input("Choisis une distance (20/40/60): "))
afficher = input("afficher figures? (o/n): ").strip().lower()

# Vérifier les entrées 
if topology_df not in csv_names:
    print("Choix de topologie non valide.")
    exit()

if distance not in [20, 40, 60]:
    print("Choix de distance non valide.")
    exit()

# Topology choisie
if topology_df == "low":
    df = pd.read_csv('topology_low.csv')
    calculer_carateristiques(df, distance*10**3, afficher )
elif topology_df == "avg":
    df = pd.read_csv('topology_avg.csv')
    calculer_carateristiques(df, distance*10**3, afficher )
elif topology_df == "high":
    df = pd.read_csv('topology_high.csv')
    calculer_carateristiques(df, distance*10**3, afficher )