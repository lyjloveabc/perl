#!perl by LYJ
#
use 5.010;
use warnings;

$i=1;
@ARGV=qw(sql2.txt);
while(<>){
	chomp $_;
	say "update CLHLG_RING set url = '".$_."' where id =".$i.";";
	$i++;
}