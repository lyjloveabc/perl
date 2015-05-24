#!perl -w
use 5.010;
use strict;
use LWP;
use Encode;

my $browser  = LWP::UserAgent->new();
my $response = $browser->get("http://xav7.info");
my $content  = $response->content;
my $output   = Encode::decode( "gbk", $content );

Encode::_utf8_on($output);
my $result = Encode::encode( 'utf8', $output );
print $result;