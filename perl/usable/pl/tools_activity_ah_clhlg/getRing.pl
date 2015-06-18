#!perl@lyj
#访问远程action，从ahtel.ring表中获取ring的信息
use 5.010;
use strict;
use LWP;
use Excel::Writer::XLSX;
use utf8;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

my $responseContent = &getDataByUrl(
	'http://61.191.44.201:8082/activity_ah_clhlg/sys/getAhtelRing.action',
	'ringCodeStr=810034570233,810028400008' );

my $file = encode( locale_fs => '彩铃.xlsx' );

my $workbook = Excel::Writer::XLSX->new($file);
die "failed to new xlsx:$!" unless defined $workbook;
my $format = &SetBookFmt($workbook);

my $worksheet = $workbook->add_worksheet("彩铃信息");
$worksheet->set_column( 0, 0, 18 );
$worksheet->set_column( 1, 2, 25 );
$worksheet->set_column( 3, 3, 5 );
$worksheet->set_column( 4, 4, 60 );
$worksheet->set_column( 5, 5, 5 );

my $headings =
  [ '铃音编号', '铃音名称', '歌手', "价格 ", "地址", "类型" ];
&setTableHeadings( $worksheet, $headings );
open READ, '<:encoding(utf8)', \$responseContent;

my $i = 2;
while (<READ>) {
	my @ringItem = split( '#-#', $_ );
	$worksheet->write( 'A' . $i, \@ringItem, $format - {"numFormat0x01"} );
	$i++;
}

sub getDataByUrl($$) {
	my ( $url, $parameter ) = @_;
	my $agent = LWP::UserAgent->new();
	my $request = HTTP::Request->new( POST => $url );
	$request->content_type('application/x-www-form-urlencoded');
	$request->content($parameter);
	my $response = $agent->request($request);
	$response->is_success or die "$!$url: ", $response->message, "\n";
	$response->content;
}

sub setTableHeadings($$) {
	my ( $worksheet, $headings ) = @_;
	$worksheet->write( 'A1', $headings );
}

sub SetBookFmt($) {
	my $workbook = shift;
	my %fmt;

	$fmt{"numFormat0x01"} = $workbook->add_format();
	$fmt{"numFormat0x01"}->set_num_format(0x01);

	$fmt{"data"} = $workbook->add_format();
	return ( \%fmt );
}
