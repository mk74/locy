set auto x
#set yrange [0:15]
set yrange [0:10]
set xtics rotate
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set xtics font "Arial, 19"
set xrange [-0.5:7.5]
set boxwidth 0.9
set ylabel "Energy efficiency order" font "Arial Bold,24"
set term post eps

plot 'plots/shared.dat' using 3:xtic(2) ti col fc rgb "blue" fs solid 0.5 border -1, '' u 4 ti col fc rgb "red" fs solid 0.5 border -1, '' u 5 ti col fc rgb "green" fs solid 0.5 border -1

#plot 'plots/shared.dat' using ($6/60):xtic(2) ti col fc rgb "blue" fs solid 0.5 border -1, '' u ($7/30) ti col fc rgb "red" fs solid 0.5 border -1, '' u ($8/50) ti col fc rgb "green" fs solid 0.5 border -1