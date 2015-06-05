#!perl by LYJ
#
use 5.010;
use warnings;
use strict;
use utf8;
use Spreadsheet::XLSX;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

my $file  = encode( locale_fs => '歌单.xlsx' );
my $excel = Spreadsheet::XLSX->new($file);
my $res   = "";
for my $sheet ( @{ $excel->{Worksheet} } ) {
	$sheet->{MaxRow} ||= $sheet->{MinRow};
	foreach my $row ( $sheet->{MinRow} + 1 .. $sheet->{MaxRow} ) {
		my $cell1 = $sheet->{Cells}[$row][0];
		chomp $cell1->{Val};
		my $str = substr( $cell1->{Val}, 0, 12 );
		$res = $res . "'" . $str . "',";
	}
}
say
qq(select t.ring_code,t.web_file_url from ahtel.ring t where ring_code in ($res););
