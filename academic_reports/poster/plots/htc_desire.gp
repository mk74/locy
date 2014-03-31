set key off
#set boxwidth 0.5
set boxwidth 0.4
set lmargin 10
set bmargin 15
#set style fill solid
set xtics rotate
#set title "HTC desire: Energy consumption of different sensors" font "Arial Bold, 24" 
#set xlabel "Sensors" font "Arial Bold, 18" 
set xlabel "Sensors" font "Arial Bold, 18" offset 0, -7
set ylabel "Approx. 1% battery life (secs)" font "Arial Bold,26"  offset 0, -3
set xtics font "Arial, 26"
#set xrange [-0.5:9.5]
set xrange [-0.4:6.5]
set yrange [130:270]
set term post eps
#set term png
#set terminal png size 1000,1000
#set output "htc_desire.png"
#plot "plots/htc_desire.dat"  using 1:3:4:xtic(2) with boxerror lc rgb "red" fs solid 0.5 border -1
plot "plots/htc_desire.dat"  using ($1/1.5):3:4:xtic(2) with boxerror lc rgb "red" fs solid 0.5 border -1
