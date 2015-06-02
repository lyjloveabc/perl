#!perl by LYJ
#
use 5.010;
use warnings;
use LWP;

@ARGV = qw(ring.txt);
while (<>) {
	chomp $_;
	#my $url ="http://61.191.44.200:80/spservice/delpersonaltone.do?sid=ljjtest&sidpwd=ljjtest2011&ringtype=1&issendsms=0&randomsessionkey=13339106748&tonecode=";
    my $url ="http://61.191.44.200:80/spservice/downring.do?sid=ljjtest&sidpwd=ljjtest2011&opertype=1&issendsms=0&randomsessionkey=13339106748&tonecode=";
	my $agent = LWP::UserAgent->new();
	my $request = HTTP::Request->new( POST => $url.$_ );
	my $response = $agent->request($request);
	$response->is_success or die "$url: ", $response->message, "/n";
	print $response->content."\n";
}
