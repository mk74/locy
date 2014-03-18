set key off
set boxwidth 0.5
set lmargin 10
#set style fill solid
set xtics rotate
set xlabel "Sample nr." font "Arial Bold, 18"
set ylabel "Error or not?" font "Arial Bold,18"
set xtics font "Arial Bold, 14"
set yrange [-0.5:1.5]
set ytics ("no error" 0, "error" 1)
#set ytics add ("no error" 0)
#set ytics add ("error" 1)
set term post eps
plot "htc_flyer_bursty_error.dat"  using 1:3:xtic(2) 