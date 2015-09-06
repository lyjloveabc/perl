#!perl
use 5.010;
use warnings;
use strict;

my $temp = "  sd	
";
chomp $temp;
say length $temp;
$" = "-";
my @arr = qw(a b c d e a b);
my $count = @arr;
my @soArr = sort @arr;
say "@soArr";
say @soArr;
print "@arr";
say $count;
