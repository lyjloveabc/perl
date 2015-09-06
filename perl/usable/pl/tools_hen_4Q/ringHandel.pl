#!perl
use warnings;
use strict;

open INFILE ,'<','ringCode.txt';
while(<INFILE>){
	chomp;
	$_ = $_.",";
	print;
}