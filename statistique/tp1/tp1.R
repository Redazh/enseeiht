
##ex1
an=1880:2024
an[an%%4==0 & an%%100!=0 | an%%400==0]


##2##
A=matrix(runif(120000,0,1),nrow=12,ncol=1000)
hist(A) 
#L'histogramme des données AA montre une distribution 
#uniforme. Cela signifie que chaque valeur entre 0 et 1 
#a une probabilité à peu près égale d'être choisie.
MoyA=apply(A,2,mean) 
hist(MoyA) 
#L'histogramme des moyennes MoyAMoyA montre
# une distribution qui est approximativement normale.
#Ce qui prouve le theoreme de central limite :  la distribution 
#des moyennes d'échantillons tend vers une distribution normale

##EX3
data(iris)
summary(iris)
iris2 = iris[iris$Species=="versicolor",]
iris2[order(iris2$Sepal.Length,decreasing=TRUE),]

##EX4
data=read.table(file="ozone.txt",header=T)

#getwd()
#list.files()
#setwd("C:/Users/marou/OneDrive/Bureau/statistique/tp1")
summary(data)
data[data$T15>30,]$maxO3
data2 = data[data$pluie=="Sec",]
data2[order(data2$T12,decreasing=TRUE),]


par(mfrow=c(1,2))
hist(data$Ne9) ; boxplot(data$Ne9) 

#Médiane :
#La ligne épaisse dans la boîte représente la médiane de la variable Ne9.
#Cela indique que 50% des données se trouvent au-dessus de cette valeur et 50% en dessous.

#Quartiles :

 #   Les bords de la boîte représentent le premier quartile (Q1) et le troisième quartile (Q3).
  #  Q1 (25ème percentile) est la valeur en dessous de laquelle 25% des données se trouvent.
   # Q3 (75ème percentile) est la valeur en dessous de laquelle 75% des données se trouvent.

#Étendue Interquartile (IQR) :

 #   L'IQR est la distance entre Q1 et Q3. C'est une mesure de la dispersion des données.
  #  Dans ce cas, l'IQR est relativement large, ce qui indique une grande variabilité dans les données.

#Moustaches et Outliers :

 #   Les moustaches s'étendent jusqu'aux valeurs minimum et maximum qui ne sont pas considérées comme des outliers.
  #  Les valeurs en dehors des moustaches sont des outliers, mais il n'y en a pas dans ce cas spécifique.

#Symétrie et Distribution :

 #   La boîte à moustaches montre que les données de Ne9 sont quelque peu asymétriques, avec une légère tendance à se regrouper vers les valeurs supérieures (6 et 8).
  #  La médiane est plus proche du quartile supérieur, suggérant une légère asymétrie.


#Les Différents Déciles

 #   1er décile (D1) : 10% des données sont en dessous de cette valeur.
  #  2ème décile (D2) : 20% des données sont en dessous de cette valeur.
  #  3ème décile (D3) : 30% des données sont en dessous de cette valeur.
 ##   4ème décile (D4) : 40% des données sont en dessous de cette valeur.
   # 5ème décile (D5) : 50% des données sont en dessous de cette valeur (aussi appelé la médiane).
    #6ème décile (D6) : 60% des données sont en dessous de cette valeur.
   # 7ème décile (D7) : 70% des données sont en dessous de cette valeur.
   # 8ème décile (D8) : 80% des données sont en dessous de cette valeur.
   # 9ème décile (D9) : 90% des données sont en dessous de cette valeur.

# Calcul des déciles

quantile(data$maxO3,probs=seq(0.1,0.9,0.1))

##EX5

data=read.table("CLIM.txt",sep=";",dec=",",header=T)

dim(data)

names(data)
head(data) ; tail(data)
summary(data)

#2
data$AN=as.numeric(substr(data$DATE,1,4))
data$MOIS=as.numeric(substr(data$DATE,5,6))

#3)
agen=subset(data,POSTE==47091001)
toul=data[data$POSTE==31069001,]  

#4)
agen[agen$TX==max(agen$TX),c("DATE","TX")]
#19820708  
# 38.8

toul[toul$TX==max(toul$TX),c("DATE","TX")]
#19820708
#40.2

#5)
par(mfrow=c(1,2))
hist(agen$TX) ; hist(toul$TX)

xx=seq(-9,41,2)
hist(agen$TX,main="Agen 1971-2000",xlab="TX en �C",ylab="Effectif", breaks=xx, col="blue")
hist(toul$TX,main="Toulouse 1971-2000",xlab="TX en �C",ylab="Effectif", breaks=xx, col="red") 

#6)
matrice=function(nom,num) {
    n=max(nom$AN)-min(nom$AN)+1
    tab=matrix(nrow=n,ncol=13)
    for(i in 1:n) {
        yy=min(nom$AN)+i-1
            for(j in 1:12) {
                tab[i,j]=mean(nom[(nom$AN==yy)&(nom$MOIS==j),num])
            }
        tab[i,13]=mean(nom[nom$AN==yy,num])
    }
    return(tab)
}

#7)
toulTX=matrice(toul,5)
max(toulTX[,-13]) #31.09677
max(toulTX[,13]) #19.69178

agenTX=matrice(agen,5)
max(agenTX[,-13]) #30.66452
max(agenTX[,13]) #19.66082

#8)
boxplot(toulTX[,13],agenTX[,13],names=c("Toulouse","Agen"),main="Moyenne annuelle TX")



