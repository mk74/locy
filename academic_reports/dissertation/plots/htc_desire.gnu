set key off
set boxwidth 0.5
set lmargin 10
#set style fill solid
set xtics rotate
set title "HTC desire: Energy consumption of different sensors" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approximate 1% battery life (in seconds)" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set xrange [-0.5:9.5]
set yrange [130:270]
set term png
set terminal png size 1000,1000
set output "htc_desire.png"
plot "htc_desire.dat"  using 1:3:4:xtic(2) with boxerror lc rgb "blue" fs solid 0.5 border -1
