set key off
set boxwidth 0.5
set lmargin 10
#set style fill solid
set xtics rotate
#set title "Google Nexus 7: Energy consumption of different sensors" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approx. 1% battery life (secs)" font "Arial Bold,18" 
set xtics font "Arial, 14"
set xrange [-0.5:11.5]
set yrange [200:1000]
set term post eps
plot "plots/google_nexus_7.dat"  using 1:3:4:xtic(2) with boxerror lc rgb "blue" fs solid 0.5 border -1
