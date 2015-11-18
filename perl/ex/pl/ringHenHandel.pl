#!perl by LYJ
#
use 5.010;
use warnings;
use LWP;

@ARGV = qw(ringHen.txt);
while (<>) {
	chomp $_;
	print $_.",";
}