set key off
set boxwidth 0.5
set lmargin 10
set style fill solid
set xtics rotate
set title "Google Nexus 7: Energy consumption of different sensors" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Approximate 1% battery life (in seconds)" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set yrange [200:1000]
set term png
set terminal png size 1300,800
set output "google_nexus_7.png"
plot "google_nexus_7.dat"  using 1:3:xtic(2) with boxes
