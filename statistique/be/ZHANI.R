data=read.table(file="DataTP4.txt",header=TRUE)

names(data)
summary(data)
data$CouvTot=as.factor(data$CouvTot)
dim(data)


## Modele de base
Im.out=lm(P60~.,data)
summary(Im.out)

# Par defaut, R pose comme contrainte d'identification CouvTotA=0. La modalite A du facteur CouvTot est donc ici prise comme reference.
# R estime l'effet differentiel des modalite B et C par rapport a la modalite A (de p-value respectivement 0.01161 et 0.00104).
# Ce parametre est juge significatif par le test de Student car p_value < 0.05 et donc le predicteur CouvTot a ici un interet.

#Lien entre P60 et CouvTot 
# En utilisant un test statistique : 
anova1=lm(P60~CouvTot,data)
summary(anova1)
model.matrix(anova1)[1:10,]
# CouvTot a un effet significatif sur P60 car dans le test de Student p_value < 0.05.


# On conserve les predicteur qui ont une p_value < 0,05 dans notre modele Im.out,Et donc on conserve FFSOL,PMER,HautSoleil,HOUR,FLSOLAIRE_D,CAPE_INS,HU950 et CouvTot

#Les prédicteurs avec des p-values inférieures à 0.05 sont considérés comme ayant un effet significatif sur P60 et devraient être conservés dans le modèle.

# Verification des Hypothèses du Modèle Linéaire Gaussien : 

# Verification de l'homoscédasticité (variance constante)
plot(fitted(Im.out),residuals(Im.out),main="Hypothèse d'homoscédasticité",xlab="Valeurs ajustées",ylab="Résidus")
# heteroscedasticite observee : la variabilite de l'erreur n'est pas stable, les points ne sont pas répartis de manière homogène et une structure en cone apparait, donc l'hypothese d'homoscedasticite est non verifiee

# Verification de la normalité des Résidus
qqnorm(residuals(Im.out))
hist(residuals(Im.out))
# l'hypothese de la normalite est verifie dans ce modele

#Verification des indépendance des Résidus
acf(residuals(Im.out))
# La condition d'indépendance des residus est globalement vérifiée. À l'exception du lag 0 et 30, les barres sont à l'intérieur des limites de confiance, indiquant une absence d'autocorrélation significative.

# On conclue que certaines hypothèses du modèle linéaire gaussien ne sont pas respectées dans ce modele.

#Expliquation de la valeur elevee du R^2
#La valeur élevée du R^2 (84.34%) signifie que le modèle de régression explique une grande partie de la variance totale, mais la valeur du R^2 augumente avec le dimension du modele


# Algo de selection automatique : 

library(MASS)
Im.outBIC=stepAIC(Im.out,k=log(nrow(data)))
formula(Im.outBIC)
# La selection automatique en utilisant BIC nous donne : P60 ~ FFSOL + HautSoleil + HOUR + FLSOLAIRE_D + CAPE_INS + HU950 + CouvTot
# Et dans la selection manuelle faite dans la question precedente, on a choisit les predicteurs qui ont une p_value < 0,05 qui sont FFSOL,PMER,HautSoleil,HOUR,FLSOLAIRE_D,CAPE_INS,HU950 et CouvTot
# La sélection automatique avec le critère BIC et la sélection manuelle donnent des modèles similaires avec une petite différence (absence de PMER dans le modèle BIC).


#Modele testant toutes les interactions d'ordre 2
Im.outintInter=lm(P60~.*.,data)
summary(Im.outintInter)
Im.outBICint=stepAIC(Im.outintInter,k=log(nrow(data)))
formula(Im.outBICint)

#Dimension des 3 modeles precedents: 
length(coef(Im.out))
#14 
length(coef(Im.outBIC))
#9, plus robuste que le premier modele, utilisant uniquement les prédicteurs significatifs
length(coef(Im.outBICint))
#25, plus complexe, susceptible d'un sur-apprentissage.




# Forcer l'algo de sélection automatique à proposer un modèle plus simple,de meme dimension que Im.outBIC
?stepAIC
Im.outINT = Im.outBICint
#


#Visualisation des previsions :
# Tracer la série chronologique des 100 premières valeurs de P60
x11()
plot(data$P60[1:100], type = "l", lwd = 2, main = "Série Chronologique des 100 premières valeurs de P60", xlab = "Date", ylab = "P60")
points(predict(Im.outBIC, newdata = data[1:100, ]), col = "red", pch="+")
points(predict(Im.outBICint, newdata = data[1:100, ]), col = "blue",pch="+")
legend(0,2850,lty=1,col=c("black"),legend=c("observee"),bty="n")
legend(0,2700,pch="+",col=c("red","blue"),legend=c("       prevue par Im.outBIC","       prevue par Im.outBICint"),bty="n") 

#Les croix rouges et bleues montrent des valeurs ajustées proches des valeurs observées, indiquant que les deux modèles ajustent bien les données. 
#Cependant, le modèle BICint, incluant des interactions, semble légèrement mieux suivre les variations extrêmes,donc la variabilite du predictand est bien reproduite.


# Evaluation des modeles : 
source("CV.R") 

