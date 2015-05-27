#!perl -w
# file get_url.pl
#--开启语法检查并使用LWP modules
use strict;
use LWP;

#--取得URL
my $url = "http://dotamax.com/zjwap/rings/indexS.action";

#--建立LWP ::UserAgent 与HTTP ::Request 物件，
#--其中Request对象将$url传进去
my $agent = LWP::UserAgent->new();
my $request = HTTP::Request->new( GET => $url );

#--透过UserAgent的request method将Request送出
my $response = $agent->request($request);

#--检查是否有error发生
$response->is_success or die "$url: ", $response->message, "/n";

#--显示responser的内容
print $response->content;
