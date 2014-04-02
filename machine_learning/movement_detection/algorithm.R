setwd("/Users/mk74/src/locy/machine_learning/movement_detection/")
trainDataInPlace <- read.csv("inPlace.csv", header = TRUE, stringsAsFactors = FALSE, sep="\t")
trainDataMoving <- read.csv("moving.csv", header = TRUE, stringsAsFactors = FALSE, sep="\t")


#plotting graphs for the report:
#compare walking and not walking
#10 seconds since 20th second the program is running
trainDataMoving$Time <- trainDataMoving$Time - trainDataMoving$Time[1] 
trainDataInPlace$Time <- trainDataInPlace$Time - trainDataInPlace$Time[1]
window_size <- 2500
threshold <- 15
offset <- 10000 

#total magnitude
plot(trainDataInPlace$Time -offset, trainDataInPlace$Value, type='l',
     xlim=c(0, 20000), ylim = c (0, 300),
     xlab="Time [miliseconds]",ylab="Total magnitude")
plot(trainDataMoving$Time - offset, trainDataMoving$Value, type='l',
     xlim=c(0, 20000), ylim = c(0,300),
     xlab="Time [miliseconds]",ylab="Total magnitude")

#helper function to calculate standard deviation over data
calc_stddevs <-function(localdata){
  #cat("window_nr", "standard deviation", "amplitude", '\n')
  stddevs <- data.frame(Time = numeric(0), Value = numeric(0))
  for(i in 1:(max(localdata$Time)/window_size)) {
    tmpaccdata <- subset(localdata, Time>i*window_size & Time< (i+1)*window_size)
    if(nrow(tmpaccdata)>0){
      stddevs[i,] <- c(i*window_size, sd(tmpaccdata$Value))
    }
  }
  stddevs
}

#standard deviation of total magnitude over 2.5 seconds sampling windows
stddevsInPlace = calc_stddevs(trainDataInPlace)
plot(stddevsInPlace$Time - offset, stddevsInPlace$Value,
     xlim=c(0, 20000), ylim = c (0, 75),
     xlab="Time [miliseconds]",ylab="Standard deviation")
abline(h = threshold, col = 2)

stddevsMoving = calc_stddevs(trainDataMoving)
plot(stddevsMoving$Time - offset, stddevsMoving$Value,
     xlim=c(0, 20000), ylim = c (0, 75),
     xlab="Time [miliseconds]",ylab="Standard deviation")
abline(h = threshold, col = 2)
