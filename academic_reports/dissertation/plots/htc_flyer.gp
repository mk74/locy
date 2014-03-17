set key off
set boxwidth 0.5
set lmargin 10
#set style fill solid
set xtics rotate
#set title "HTC Flyer: Energy consumption of different sensors" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approximate 1% battery life (in seconds)" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set xrange [-0.5:8.5]
set yrange [200:300]
set term post eps
#set term png
#set terminal png size 1000,1000
#set output "htc_flyer.png"
plot "plots/htc_flyer.dat"  using 1:3:4:xtic(2) with boxerror lc rgb "blue" fs solid 0.5 border -1