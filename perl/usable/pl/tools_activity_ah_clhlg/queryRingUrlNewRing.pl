#!perl by LYJ
#
use 5.010;
use warnings;
use strict;
use utf8;
use Spreadsheet::XLSX;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

my $file  = encode( locale_fs => '最新歌单.xlsx' );
my $excel = Spreadsheet::XLSX->new($file);
my $res   = "";
my %hash;
for my $sheet ( @{ $excel->{Worksheet} } ) {
	$sheet->{MaxRow} ||= $sheet->{MinRow};
	foreach my $row ( $sheet->{MinRow} + 1 .. $sheet->{MaxRow} ) {
		my $cell1 = $sheet->{Cells}[$row][1];
		chomp $cell1->{Val};
		my $str = substr( $cell1->{Val}, 0, 12 );
		if(exists $hash{$str}){
			say $str;
		}
		$hash{$str} = $str;
		$res = $res . "'" . $str . "',";
	}
}
say
qq(select t.ring_code,t.web_file_url from ahtel.ring t where ring_code in ($res););
my $length = keys %hash;
say $length;
