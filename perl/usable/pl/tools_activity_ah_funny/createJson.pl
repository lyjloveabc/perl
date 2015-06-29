#!perl @LYJ
use 5.010;
use warnings;
use strict;
my $str = "A";
my $i = 1;
open OUT,'>','json.txt';
while(ord($str) < ord("Z")){
	print OUT qq("id":$i,
		 "text":"$str"
		},{\n);
	$str = chr(ord($str)+1);
	$i++;
}
close OUT;