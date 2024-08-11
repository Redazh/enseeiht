# TP2 HPC-BigData : script d'evaluation des modeles

library(MASS)

data = read.table(file="DataTP4.txt", header=TRUE)
data$CouvTot = as.factor(data$CouvTot)

RMSE=function(obs,pr){
return(sqrt(mean((pr-obs)^2)))}

# Fonction pour calculer le biais
BIAIS = function(obs, pr) {
  return(mean(pr - obs))
}


# Choix automatique de predicteurs a partir du modele complet sans puis avec interactions
Im.out = lm(P60 ~ ., data)
Im.outBIC = stepAIC(Im.out, k = log(nrow(data)), trace=0)
Im.outINT_initial = lm(P60 ~ .*., data)
Im.outBICint = stepAIC(Im.outINT_initial, k = log(nrow(data)), trace=0)
Im.outINT = Im.outBICint


# Préparation pour l'évaluation des modèles
k = 100
tab = matrix(nrow = k, ncol = 8)

for (i in 1:k) {
  nappr = ceiling(0.6 * nrow(data))
  ii = sample(1:nrow(data), nappr)
  jj = setdiff(1:nrow(data), ii)
  datatest = data[jj,]
  datapp = data[ii,]
  
  # Estimation des modèles
  regout = lm(formula(Im.out), datapp)
  regbic = lm(formula(Im.outBIC), datapp)
  regbicint = lm(formula(Im.outBICint), datapp)
  regint = lm(formula(Im.outBICint), datapp) # Utilisation de Im.outBICint pour les interactions
  
  # Scores sur apprentissage
  tab[i,1] = RMSE(datapp$P60, predict(regout))
  tab[i,2] = RMSE(datapp$P60, predict(regbic))
  tab[i,3] = RMSE(datapp$P60, predict(regbicint))
  tab[i,4] = RMSE(datapp$P60, predict(regint))
  
  # Scores sur test
  tab[i,5] = RMSE(datatest$P60, predict(regout, datatest))
  tab[i,6] = RMSE(datatest$P60, predict(regbic, datatest))
  tab[i,7] = RMSE(datatest$P60, predict(regbicint, datatest))
  tab[i,8] = RMSE(datatest$P60, predict(regint, datatest))
}

# Tracer les résultats
x11()
boxplot(tab, col = c(rep("blue", 4), rep("red", 4)), xlab = "bleu=apprentissage - rouge=test",
        names = c("Im.out", "Im.outBIC", "Im.outBICint", "Im.outINT", "Im.out", "Im.outBIC", "Im.outBICint", "Im.outINT"),
        main = "Comparaison des Modèles - Score RMSE")
