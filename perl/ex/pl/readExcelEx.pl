#!perl by LYJ
#
use 5.010;
use warnings;
use strict;
use utf8;
use Spreadsheet::XLSX;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

my $file = encode( locale_fs => '歌单.xlsx' );
my $excel = Spreadsheet::XLSX->new($file);

for my $sheet ( @{ $excel->{Worksheet} } ) {
	printf( "Sheet: %s\n", $sheet->{Name} );
	$sheet->{MaxRow} ||= $sheet->{MinRow};
	foreach my $row ( $sheet->{MinRow} .. $sheet->{MaxRow} ) {
		$sheet->{MaxCol} ||= $sheet->{MinCol};
		foreach my $col ( $sheet->{MinCol} .. $sheet->{MaxCol} ) {
			my $cell = $sheet->{Cells}[$row][$col];
			if ($cell) {
				printf( "( %s , %s ) => %s\n", $row, $col, $cell->{Val} );
			}
		}
	}
}