#!perl@lyj
#访问远程action，从ahtel.ring表中获取ring的信息
use strict;
use LWP;
use Excel::Writer::XLSX;
use utf8;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

#my $responseContent = &getDataByUrl(
#	'http://61.191.44.201:8082/activity_ah_clhlg/sys/getAhtelRing.action?',
#	'ringCodeStr=810034570233,810028400008'
#);

my $file = encode( locale_fs => '彩铃.xlsx' );

my $workbook = Excel::Writer::XLSX->new($file);
die "failed to new xlsx:$!" unless defined $workbook;
my $worksheet = $workbook->add_worksheet("彩铃信息");

my $headings = [ '铃音编号', '铃音名称', '歌手' ,"价格 " ,"地址"];


sub getDataByUrl($$) {
	my ( $url, $parameter ) = @_;
	my $agent = LWP::UserAgent->new('activity_ah_clhlg_App/0.1');
	my $request = HTTP::Request->new( POST => $url );
	$request->content($parameter);
	my $response = $agent->request($request);
	$response->is_success or die "$!$url: ", $response->message, "/n";
	$response->content;
}

sub setTableHeadings(){
	my $worksheet = shift;
	$worksheet->write();
}
