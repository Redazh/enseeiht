##Partie 1

data=read.table("DataTP.txt", header=TRUE)

names(data)
summary(data)
header(data)
dim(data)
#Assurez-vous que R considere bien par defaut les variables JJ et STATION comme etant de type factor 
#class(data$JJ)
#class(data$STATION)
#(effectifs par modalite affiches dans le summary), sinon forcez le type comme ceci : 
data$JJ=as.factor(data$JJ)
data$STATION=as.factor(data$STATION)
summary(data)


## 2
aix=subset(data,STATION=="Aix")
summary(aix)
