set key off
set boxwidth 0.5
set lmargin 10
set style fill solid
set xtics rotate
set title "HTC desire: Failed samples per sensor" font "Arial Bold, 24" 
set xlabel "Sensors" font "Arial Bold, 18" 
set ylabel "Percentage of failed samples [%]" font "Arial Bold,18" 
set xtics font "Arial Bold, 14"
set yrange [0:100]
set term post eps
#set term png
#set terminal png size 1000,1000
#set output "htc_desire_failed_samples.png"
plot "plots/htc_desire_failed_samples.dat"  using 1:3:xtic(2) with boxes lc rgb "red"

