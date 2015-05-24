#!perl -w
use strict;
use LWP;

my $browser = LWP::UserAgent->new();
my $response= $browser->get("http://www.toolmao.com");

print $response->content; # 输出获得的网页内容