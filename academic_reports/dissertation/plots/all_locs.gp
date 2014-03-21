set auto x
set yrange [0:15]
#set yrange [5:10]
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set boxwidth 0.9
set xtic scale 0
set ylabel "Energy efficiency level" font "Arial Bold,18"
set term post eps
plot 'plots/all_locs.dat' using ($3/60):xtic(2) ti col fc rgb "blue" fs solid 0.5, '' u ($4/30) ti col fc rgb "red" fs solid 0.5, '' u ($5/50) ti col fc rgb "green" fs solid 0.5
#plot 'plots/all_locs.dat' using (($3/60)/1.5):xtic(2) ti col fc rgb "blue" fs solid 0.5, '' u (($4/30)/0.9) ti col fc rgb "red" fs solid 0.5, '' u (($5/50)/0.5) ti col fc rgb "green" fs solid 0.5
#plot 'plots/all_locs.dat' using ((($3/60)/15)*10):xtic(2) ti col fc rgb "blue" fs solid 0.5, '' u ((($4/30)/9)*10) ti col fc rgb "red" fs solid 0.5, '' u ((($5/50)/5)*10) ti col fc rgb "green" fs solid 0.5