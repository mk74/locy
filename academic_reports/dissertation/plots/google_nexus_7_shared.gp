set key off
#set boxwidth 0.5
set boxwidth 0.4
set lmargin 10
set style fill solid
set xtics rotate
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approx. 1% battery life (secs)" font "Arial Bold,18" 
set xtics font "Arial, 14"
#set xrange [-0.5:8.5]
set xrange [-0.5:5.8]
#set yrange [200:1000]
#set yrange [0:100]
set yrange [0:10]
#set yrange [25:100]
set term post eps
plot "plots/google_nexus_7_shared.dat"  using ($1/1.5):5:xtic(2) with boxes lc rgb "blue" fs solid 0.5 border -1
