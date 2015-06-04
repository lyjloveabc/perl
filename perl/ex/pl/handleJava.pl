#!perl by LYJ
#
use 5.010;
use warnings;

@ARGV=qw(java.txt);
while(<>){
	#chomp $_;
	s/\s|　/ /g;
	say $_;
}