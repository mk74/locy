set key off
set boxwidth 0.5
set lmargin 10
#set style fill solid
set xtics rotate
#set title "Google Nexus 7: Energy consumption of different sensors" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approx. battery life (hrs)" font "Arial Bold,18" 
set xtics font "Arial, 14"
set xrange [-0.5:7.5]
set yrange [0:200]
set term post eps
plot "plots/senseless.dat"  using 1:3:xtic(2) with boxes lc rgb "blue" fs solid 0.5 border -1
