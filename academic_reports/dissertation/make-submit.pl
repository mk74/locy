#!/usr/bin/perl -w

# turn multiple LaTeX and BibTeX files into a
# single file for ACM submission
# Assumes that BibTeX has already been run and
# created the .bbl files

use strict;
use File::Find;

@main::bblfiles = ();
find(\&find_bbl_files, '.');

while (my $line = <>) {
	if ($line !~ /^%/) {
		if ($line =~ /\\input\{(.*)\}/) {
			if (open(FILE,"<$1.tex")) {
				while (<FILE>) {
					if ($_ !~ /^%/) {print $_;}
				}
				close(FILE);
			}
			next;
		} elsif ($line =~ /\\bibliography\{(.*)\}/) {
			foreach my $bblfile (@main::bblfiles) {
				if (open(FILE,"$bblfile")) {
					while (<FILE>) {
						print $_;
					}
					close(FILE);
				}
			}
			next;
		}
		print $line;
	}
	#print $line;
}

sub find_bbl_files {
	push (@main::bblfiles, $File::Find::name) if (-f $_ && /\.bbl$/);
}
