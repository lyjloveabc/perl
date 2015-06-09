#!perl@lyj
use 5.010;
use strict;
use warnings;
use LWP;
use utf8;
use Excel::Writer::XLSX;
use Encode;

#读取文件，根据其中的ringCode生成ringCodeStr
my $ringCodeStr = '';
my $inputFile   = <STDIN>;
@ARGV = $inputFile;
while (<>) {
	chomp;
	$ringCodeStr = $ringCodeStr . ',' . $_;
}

#excel表头
my $workbook  = Excel::Writer::XLSX->new('output.xlsx');
my $worksheet = $workbook->add_worksheet('单曲');
my $format    = $workbook->add_format();
&setXlsxHeader($worksheet);

#根据url请求获得数据
my $result = &getDataByUrl(
'http://localhost:8080/activity_ah_clhlg/sys/getZjcorpRingTest.action?ringCodeStr='
	  . substr( $ringCodeStr, 1 ) );

my @rows = split( /\n/, $result );
my $j = 1;
foreach my $row (@rows) {
	my @data = split( /#-#/, $row );
	for ( my $i = 0 ; $i < @data ; $i++ ) {
		$worksheet->write( $j, $i, decode( "utf8", $data[$i] ) );
	}
	$j++;
}

sub getDataByUrl($) {
	my $url      = shift;
	my $us       = LWP::UserAgent->new();
	my $request  = HTTP::Request->new( POST => $url );
	my $response = $us->request($request);
	$response->is_success or die "$!$url: ", $response->message, "/n";
	$response->content;
}

sub setXlsxHeader ($) {
	my $worksheet = shift;
	$worksheet->write( 0, 0, '铃音编号' );
	$worksheet->write( 0, 1, '铃音名称' );
	$worksheet->write( 0, 2, '歌手' );
	$worksheet->write( 0, 3, '价格' );
	$worksheet->write( 0, 4, '试听地址' );
	$worksheet->write( 0, 5, '类型' );
	$worksheet->set_column( 0, 0, 10 );
	$worksheet->set_column( 0, 1, 10 );
	$worksheet->set_column( 0, 2, 10 );
	$worksheet->set_column( 0, 3, 10 );
	$worksheet->set_column( 0, 4, 10 );
	$worksheet->set_column( 0, 5, 10 );
}
