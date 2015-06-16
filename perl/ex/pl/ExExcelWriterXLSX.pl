#!perl@lyj
use 5.010;
use strict;
use warnings;
use Excel::Writer::XLSX;

my $workbook = Excel::Writer::XLSX->new("temp1.xlsx");
die "failed to new xlsx:$!" unless defined $workbook;

my $worksheet = $workbook->add_worksheet();
 my $chart = $workbook->add_chart( type => 'column' );

 

