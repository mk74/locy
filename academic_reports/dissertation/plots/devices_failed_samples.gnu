set key off
set boxwidth 0.5
set lmargin 10
set style fill solid
set xtics rotate
set title "Percentage of failed samples among devices" font "Arial Bold, 24" 
set xlabel "Device" font "Arial Bold, 18" 
set ylabel "Percentage of failed samples [%]" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set yrange [0:100]
set term png
set terminal png size 1300,800
set output "devices_failed_samples.png"
plot "devices_failed_samples.dat"  using 1:3:xtic(2) with boxes
