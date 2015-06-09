#!perl by LYJ
#练习new
use 5.010;
use warnings;
use strict;
use utf8;
use Spreadsheet::XLSX;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);



my $file = encode( locale_fs => '歌单.xlsx' );
my $excel = new Spreadsheet::XLSX($file);
say $excel->{Worksheet}[0]->{Cells}[0][0]->{Val};

