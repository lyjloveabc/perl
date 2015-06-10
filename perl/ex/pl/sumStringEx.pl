use 5.010;
use warnings;
use strict;
use utf8;

@ARGV = qw(sumStringEx.txt);
while (<>) {
	chomp $_;
	my $count = 0;
	while (m/createTime/g) { $count++; }
	print $count, "\n";
}
