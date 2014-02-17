# Saleem's LaTeX makefile, butchered by Tristan
#
# finalpdf  embedded PDF version of final document for publication
# pdf     PDF version of final document for web
# ps      PS version of final document
# dvi     DVI version of final document
# spell   spell check
# count   word & line count
# tar     makes a tar.bz2 of the whole dir

SHELL := 	${shell which bash}

########################################
# change stuff in this block as required
#
# NAME = name of this dir 
NAME := 	${shell basename `pwd`}
# DOC = name of the main TeX file (i.e., the one containing \begin{document} )
DOC = 		$(NAME)

# WORDS = a list of TeX files starting with the main file 
WORDS =		$(DOC).tex ${shell find . -mindepth 1 -maxdepth 1 -name \*.tex | sed -e "s/^.*\///" | grep -v $(DOC).tex}
BIB =		$(NAME).bib
# set a version number here, or else we use the subversion revision
VERSION = 	
# where are the plots/pictures/graphs located?
PLOTSDIR =  plots
# PLOTS = figures that are already in EPS format
PLOTS =		$(wildcard $(PLOTSDIR)/*.eps)
# GRAPHS AND PICTURES will be converted to EPS
GRAPHS = 	$(wildcard $(PLOTSDIR)/*.gnp $(PLOTSDIR)/*.gp $(PLOTSDIR)/*.gv)
PICTURES =	$(wildcard $(PLOTSDIR)/*.dia $(PLOTSDIR)/*.fig $(PLOTSDIR)/*.jpg $(PLOTSDIR)/*.png $(PLOTSDIR)/*.tif)
DATA =	
OTHER =		
PAPERSIZE =	letter# a4
# UTIL = other files that will go into the tarball
UTIL = 		$(wildcard *.bst *.cls *.sty)

# single(-ish) file for submission
SUBMIT = $(NAME)_submit

########################################

.SUFFIXES:	.tex .fig .dvi .gnp .gp .tif .png .jpg .ps .pdf .spell .dia .eps .gv
.PHONY:		spell count backup submit

# The combination of dvips and ps2pdf switches below generates
# .ps and .pdf files that keep happy both IEEE and ACM online
# submission tools. (All fonts are embedded.)

LATEX =		latex
PDFLATEX =	pdflatex
BIBTEX = 	bibtex
FIG2DEV =	fig2dev
EPS2PDF = 	epstopdf
DIA =		dia
GNUPLOT =	gnuplot
GRAPHVIZ =	fdp #change to dot, neato etc as required
DETEX =		detex
DVIPS = 	dvips -j0 -Ppdf -Pdownload35 -G0 -t $(PAPERSIZE)
SPELLCHECK =	aspell -c --lang=$(LANG) --mode=tex
PAGER =		less
TAR = 		tar 
COMPRESS =	bzip2 -9 -f -v
COUNT =		wc -w
PS2PDF =	ps2pdf -dEmbedAllFonts=true -dSubsetFonts=true -dEPSCrop=true \
			-dPDFSETTINGS=/prepress -dCompatibilityLevel=1.6

##############################################################
# should not normally need to change anything below this line

EPSPICS := 	$(sort $(filter %.eps,$(GRAPHS:%.gnp=%.eps) $(GRAPHS:%.gp=%.eps) \
			$(GRAPHS:%.gv=%.eps) \
			$(PICTURES:%.fig=%.eps) $(PICTURES:%.jpg=%.eps) \
			$(PICTURES:%.dia=%.eps) $(PICTURES:%.png=%.eps) \
			$(PICTURES:%.tif=%.eps)))
PDFPICS := 	$(PLOTS:.eps=.pdf) $(EPSPICS:.eps=.pdf)
CAPTIONS := $(PDFPICS:.pdf=.tex)
TAR-NAME =	$(NAME)-$(VERSION).tar
TAR-FILES = Makefile $(WORDS) $(BIB) $(PLOTS) $(GRAPHS) $(PICTURES) $(DATA) \
 			$(OTHER) $(ARCHIVE) $(UTIL) $(CAPTIONS)
CLEAN = 	$(DOC).dvi *.aux *.log $(DOC).ps $(DOC).pdf *.spell *.bbl *.blg \
			*.toc *.lof *.lot *.marks *.out *.xdv *~ *.tar *.spl core $(SUBMIT).{pdf,tex}
DISTCLEAN =	$(NAME)-*.tar* $(EPSPICS) $(PDFPICS) $(SUBMIT).zip

TEX =		$(DOC).tex
DVI =		$(DOC).dvi
PS =		$(DOC).ps
PDF =		$(DOC).pdf
BBL =		$(DOC).bbl
SPELL =		$(WORDS:.tex=.spell)

##############################################################
# regexes for rerunning latex or bibtex
RERUNTEX = "(There were undefined (references|citations)|Rerun to get (cross-references|the bars|/PageLabels))"
RERUNBIB = "Citation.*undefined"

##############################################################
# ooh! colours!
SETCOL_RED 	 = @echo -en "\\033[0;31m"
SETCOL_GREEN = @echo -en "\\033[0;32m"
SETCOL_BLUE	 = @echo -en "\\033[0;34m"
SETCOL_RESET = @tput sgr0

# targets
all:		pdf
tex:		$(TEX)
dvi:		$(DVI)
ps:			$(PS)
pdf:		$(PDF)
spell:		$(SPELL)

##############################################################
# build the document

# use LaTeX to create DVI
$(DVI):	$(EPSPICS) $(BBL)
		$(LATEX) $(DOC)
		@grep -E $(RERUNTEX) $(DOC).log && ($(LATEX) $(DOC)); true
		@grep -E $(RERUNTEX) $(DOC).log && ($(LATEX) $(DOC)); true
		@$(MAKE) -k -s check

# use pdflatex to create PDF with hyperlinks
$(PDF):	$(PDFPICS) $(BBL)
		$(PDFLATEX) $(DOC)
		@grep -E $(RERUNTEX) $(DOC).log && ($(PDFLATEX) $(DOC)); true
		@grep -E $(RERUNTEX) $(DOC).log && ($(PDFLATEX) $(DOC)); true
		@$(MAKE) -k -s check

# use dvips to create PS
$(PS):	$(DVI)
		$(DVIPS) -f $(DVI) > $@
# ps2pdf reads the last DOCINFO pdfmark in the .ps file.
# But, Gnuplot 4.2+ are adding DOCINFO pdfmark to generated eps
# files. Therefore, when ps2pdf is used to make a pdf, the pdf's
# metadata is wrongly that of the last plot embedded in the .ps
# file.
#
# The workaround is to clear the pdfmark for gnuplot's eps files
# in the intermediate .ps file.
#
# See:
# <http://groups.google.com/group/comp.graphics.apps.gnuplot/browse_thread/thread/5938f08fe95b0bdd/469c49b8f2bc57a8>
# <http://www.rhinocerus.net/forum/lang-postscript/132975-distilling-multiple-docinfo-pdfmarks.html>
# Credit:
# <http://www.kolej.mff.cuni.cz/~vesej3am/transfer/ispma/Makefile>
# fixed by Tristan to remove GNU sed-isms
		@sed -e "/Subject (gnuplot plot)/,/end/ s/DOCINFO pdfmark/DOCINFO cleartomark/" $@ > $@.psfixed && mv $@.psfixed $@



# use ps2pdf to create embedded PDF from PS
finalpdf: $(PS)
		$(PS2PDF) $(PS)

# concatenate .tex into single file
submit: $(PDF)
	@perl make-submit.pl < $(DOC).tex > $(SUBMIT).tex
	$(PDFLATEX) $(SUBMIT)
	$(PDFLATEX) $(SUBMIT)
	@rm -f $(SUBMIT).{aux,bbl,blg,log,out,dvi,spl}
	zip -9r $(SUBMIT).zip $(SUBMIT).{tex,pdf} $(PLOTSDIR) -x $(PLOTSDIR)/.svn\*

# LaTeX can be used for first pass even if we subsequently use pdflatex 
$(BBL):	$(WORDS) $(BIB) $(CAPTIONS)
		$(LATEX) $(DOC)
# only run bibtex if there are undefined citations
		@grep -E $(RERUNBIB) $(DOC).log && ($(BIBTEX) $(DOC)); true
# create bbl (even if no citations)
		@touch $(BBL)  

# check for TeX errors
check:	$(DOC).log
		$(SETCOL_GREEN)
		@echo ============== LaTeX errors =========================
		$(SETCOL_RED)
		@grep Warning:  $(DOC).log || true
		$(SETCOL_GREEN)
ifeq ($(shell test -f $(DOC).blg && echo 1),1)
		@echo ============== BibTeX errors ========================
		$(SETCOL_RED)
		@grep Warning-- $(DOC).blg || true
		$(SETCOL_GREEN)
endif
		@echo =====================================================
		$(SETCOL_RESET)

##############################################################
# determine what program to use to view PDF

OS :=		$(strip $(shell uname -s))
os:;		@echo $(OS)

MACOSX =	Darwin
LINUX =		Linux

# on MacOS use "open" to use the default PDF viewer
PREVIEW = 	open
# on Linux we see what we can find to open PDFs
XDGOPEN = xdg-open
GNOMEOPEN = gnome-open
ACROREAD =	acroread
EVINCE =	evince
XPDF =		xpdf
VIEWERS := $(GNOMEOPEN) $(XDGOPEN) $(EVINCE) $(XPDF) $(ACROREAD)
# set default PDF viewer to acroread
PDFVIEWER = $(ACROREAD)
ifeq ($(OS),$(MACOSX))
  PDFVIEWER = $(PREVIEW)
endif
ifeq ($(OS),$(LINUX))
  PDFVIEWER=$$(for viewer in $(VIEWERS); do \
	which $$viewer && break; \
	done || true 2>/dev/null)
endif

##############################################################
# create a version number
# if version is not defined, then use subversion or git revision or the date
ifeq ($(VERSION),)
# could use subversion properties for this but I always forget to set
# them :-(
  ifeq ($(shell svn info >&/dev/null && echo 1),1)
    VERSION=${shell svn info | grep ^Revision | sed -e "s/.*: /rev/"}
  else
    ifeq ($(shell git show >&/dev/null && echo 1),1)
      VERSION=${shell git show --abbrev-commit | grep ^commit | sed -e "s/^.*\s//"}
    else
      VERSION=${shell date +%Y%m%d}
    endif
  endif
endif

##############################################################
# help message
help:
	@echo " "
	@echo " Use one of the following:"
	@echo "  help      - this message"
	@echo "  all       - everything"
	@echo "  dvi       - main DVI file using '$(LATEX)' and '$(BIBTEX)'"
	@echo "  ps        - main PostScript file using '$(DVIPS)'"
	@echo "  finalpdf  - embedded PDF file for submission to ACM/IEEE using '$(PS2PDF)'"
	@echo "  pdf       - PDF file with hyperlinks (good for websites) using '$(PDFLATEX)'"
	@echo "  spell     - spell check using '$(SPELLCHECK)'"
	@echo "  clean     - removes all files in this dir matching '$(CLEAN)'"
	@echo "  distclean - a more exhaustive clean that also removes '$(DISTCLEAN)'"
	@echo "  count     - word count using '$(COUNT)'"
	@echo "  tar       - a '$(COMPRESS)' compressed tar of '$(TAR-NAME)'"
	@echo "  view      - view PDF using '$(PDFVIEWER)'"
	@echo " "

##############################################################
# convert graphics to something that TeX can use (EPS or PDF)
#
# {tif,png,jpg} -> eps assume you have installed the nettopbm tools.
# If you have not, on the mac, you can use macports:
#
#   sudo port install netpbm
#
# see the pnmtops man page at:
#
#   <http://netpbm.sourceforge.net/doc//pnmtops.html>
#
# for the DPI and SCALE settings. The settings below are to produce
# very sharp images but with a compact file size. I am not sure about
# jpeg photographs (have not tried), but tiff and png graphics
# come out very well and are fine for proceedings and journals

DPI =		300
SCALE =		0.25
PNMTOPS = 	pnmtops -rle -noturn -dpi $(DPI) -scale $(SCALE)

.tif.eps:;	tifftopnm $< | $(PNMTOPS) > $@

.png.eps:;	pngtopnm $< | $(PNMTOPS) > $@

.jpg.eps:;	jpegtopnm $< | $(PNMTOPS) > $@


# for dia, gnuplot and fig, I assume that eps generated directly is fine!

.dia.eps:;	$(DIA) -e $@ $<

.gnp.eps:;	GNUPLOT_LIB=$(PLOTSDIR) $(GNUPLOT) $< > $@

.gp.eps:;	GNUPLOT_LIB=$(PLOTSDIR) $(GNUPLOT) $< > $@

.fig.eps:;	$(FIG2DEV) -L ps $< | \
		sed 's/F2psDict\ 6400\ dict/F2psDict\ 50\ dict/' > $@ 

.gv.eps:;	$(GRAPHVIZ) -T eps $< > $@

.eps.pdf:;	$(EPS2PDF) $< 

##############################################################
# spell check

.tex.spell:
		$(SPELLCHECK) $<
		@touch $@

count: $(WORDS)
		@echo "number of words in $(WORDS):"
		@cat $(WORDS) | \
			sed '/\\begin{document}/,/\\end{document}/!d' | \
			$(DETEX) | $(COUNT)

##############################################################
# misc: clean up, tar up

clean:	
	rm -rf $(CLEAN)

distclean: clean
	rm -rf $(DISTCLEAN)

tar:;	@(cd ..; \
		$(TAR) -cvf $(NAME)/$(TAR-NAME) $(TAR-FILES:%=$(NAME)/%) && \
		$(COMPRESS) $(NAME)/$(TAR-NAME))

view:	$(PDF)
		@$(PDFVIEWER) $< &

