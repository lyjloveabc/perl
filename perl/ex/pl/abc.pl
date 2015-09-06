#!perl
#lyj
use warnings;
use strict;

my @arr = (1,2,4,2);
print &max(@arr);

sub max{
	my $max = shift;
	foreach(@_){
		if($_>$max){
			$max = $_;
		}
	}
	$max;
}