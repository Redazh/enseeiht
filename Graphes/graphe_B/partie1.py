import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
import numpy as np

def matrice_adh(df,d):
    num_nodes = len(df)  # Nombre de noeuds dans le dataframe
    M = np.zeros((num_nodes, num_nodes))  #la matrice d'adjacence
    for i in range(num_nodes):
        for j in range(i + 1, num_nodes):
            distance = ((df.iloc[i]['x'] - df.iloc[j]['x'])**2 +
                        (df.iloc[i]['y'] - df.iloc[j]['y'])**2 +
                        (df.iloc[i]['z'] - df.iloc[j]['z'])**2)**0.5
            if distance <= d:
                M[i, j] = 1
                M[j, i] = 1
    return M

# Liste de différentes distances pour la création des graphes
distances=[20000,40000,60000]
# Chemins vers les fichiers de données
chemins = ['topology_low.csv', 'topology_avg.csv', 'topology_high.csv']
for chemin in chemins:
    df = pd.read_csv(chemin)
    
    position = None   # Initialisation de la position des noeuds dans le graphe

    # Attribution de la densité en fonction du fichier pour l'affichage des figures
    if chemin == chemins [0]:
        densite = 'faible'
    elif chemin == chemins [1]:
        densite = 'moyenne'
    else:
        densite = 'forte'

    for d in distances:
        M = matrice_adh(df, d)
        G = nx.from_numpy_array(M)

        title = f'Figure pour une densité {densite} et pour une portée = {d}'
        plt.figure(title, figsize=(10, 6))

        if position is None:
            position = nx.spring_layout(G)  

        # Dessin des arêtes et des noeuds du graphe
        edges = nx.draw_networkx_edges(G, position, alpha=0.3)
        nodes = nx.draw_networkx_nodes(G, position, node_color='c', node_size=150)
        labels = {node: str(node) for node in G.nodes()}
        nx.draw_networkx_labels(G, position, labels=labels, font_size=8)
        plt.title(title)
        plt.show()