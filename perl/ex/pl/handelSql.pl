#!perl by LYJ
#new a pl file with some rule
use 5.010;
use warnings;

@ARGV=qw(sql.txt);
while(<>){
	s/DIYMATCH_COLORRING/CLHLG_RING/;
	s/COLORRING_ID/RING_ID/;
	s/COLORRING_//g;
	say $_;
}