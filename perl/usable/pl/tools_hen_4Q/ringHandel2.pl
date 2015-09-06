#!perl
####注意铃音价格的00
use warnings;
use strict;

open INFILE ,'<','ringInfo.txt';
while(<INFILE>){
	chomp;
	my @strs = split("	",$_);
	my $sql = "insert into FQ_RING (ID,CREATE_TIME,MODIFY_TIME,RING_CODE,NAME,SINGER,PRICE,URL,TYPE,THEME_TYPE) values (SEQ_FQ_RING.nextval,'2015-08-27 16:36:05','2015-08-27 16:36:05','$strs[0]','$strs[1]','$strs[2]','$strs[3]','$strs[4]','$strs[5]','1');";
	print $sql."\n";
}