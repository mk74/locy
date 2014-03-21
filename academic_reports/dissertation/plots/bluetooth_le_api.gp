set key off
set boxwidth 0.5
set lmargin 10
set style fill solid
set xtics rotate
set xlabel "Bluetooth Sensor" font "Arial Bold, 18"
set ylabel "Approx. 1% battery life (secs)" font "Arial Bold,18"
set xtics font "Arial Bold, 14"
set yrange [650:850]
set term post eps
plot "plots/bluetooth_le_api.dat"  using 1:3:xtic(2) with boxes lc rgb "blue" fs solid 0.5