set boxwidth 0.5
set style fill solid
set xtics rotate
set title "HTC desire: Energy consumption of different sensors." font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approximate 1% battery life" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set term png
set output "htc_desire.png"
plot "htc_desire.dat"  using 1:3:xtic(2) with boxes
