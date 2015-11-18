#!perl @LYJ
#针对转盘，输入第几块，得到这一块的中间角度值
use 5.010;
use warnings;
use utf8;
my $which = 0;
while ( $which != 100 ) {
	$which = <STDIN>;
	chomp $which;
	my $firstAngle = 30 * ( $which - 1 );
	my $lastAngle  = 30 * $which;
	my $angle      = ( $lastAngle - $firstAngle ) / 2 + $firstAngle;
	say $angle;
}
