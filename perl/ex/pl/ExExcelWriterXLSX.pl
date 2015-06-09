#!perl@lyj
use 5.010;
use strict;
use warnings;
use Excel::Writer::XLSX;

my $workbook = Excel::Writer::XLSX->new("temp1.xlsx");
die "failed to new xlsx:$!" unless defined $workbook;
my $worksheet = $workbook->add_worksheet("first");
my $format    = $workbook->add_format();
$format->set_format_properties( bold => 1, color => 'blue' );
$worksheet->write( 0, 0, 'One', $format );
$format->set_format_properties( bold => 1, color => 'green' );

