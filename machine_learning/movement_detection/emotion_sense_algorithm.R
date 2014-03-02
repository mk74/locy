setwd("/Users/mk74/src/locy/machine_learning/movement_detection/")
trainDataInPlace <- read.csv("inPlace.csv", header = TRUE, stringsAsFactors = FALSE, sep="\t")
trainDataMoving <- read.csv("moving.csv", header = TRUE, stringsAsFactors = FALSE, sep="\t")


plot(trainDataInPlace$Value)
plot(trainDataMoving$Value)

plot(trainDataInPlace$Value, type="l")
plot(trainDataMoving$Value, type="l")

sd(trainDataInPlace$Value)
sd(trainDataMoving$Value)

funcTrainData <- trainDataInPlace
window_size <- 400 #400 - around 8 secss #133 - around 2.66 secs on my data
windows_n <- round(nrow(funcTrainData) / window_size)
for(i in 0: (windows_n-1) ){
  start <- i * window_size
  end <- (i+1) * window_size
  odchylenie = sd(funcTrainData$Value[start : end])
  print(odchylenie)
}