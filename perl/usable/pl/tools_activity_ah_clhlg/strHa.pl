#!perl

@ARGV = qw(b.txt);
while (<>) {
	chomp;
	my @str=split(",");
	$str[0]=lc($str[0]);
	print "browserList.add($str[0]);";
	print "\n";
}