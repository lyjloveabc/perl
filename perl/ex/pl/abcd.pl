#!perl -w
use strict;
use LWP;
use utf8;

#--取得URL
my $url = "http://localhost:8080/activity_ah_daqnrl/submitAnswer.action";

#--建立LWP ::UserAgent 与HTTP ::Request 物件，
#--其中Request对象将$url传进去
my $agent = LWP::UserAgent->new();

my @user =qw(opEF8jgIYGPyM8kmU4__n7yK--9o a1 a2 a3 a4 a5 a6 a7 a8 a9 b1 b2 b3 b4 b5 b6 b7 b8 b9 b10);
foreach (@user) {
	my $request = HTTP::Request->new( POST => $url."?phone=".$_ );
	#--透过UserAgent的request method将Request送出
	my $response = $agent->request($request);

	#--检查是否有error发生
	$response->is_success or die "$!$url: ", $response->message, "/n";

	#--显示responser的内容
	print $response->content;
}
