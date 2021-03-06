#!perl -w
# file get_url.pl
#--开启语法检查并使用LWP modules
use strict;
use LWP;
use utf8;

#--取得URL
my $url = "http://localhost:8080/activity_ah_clhlg/activity/clhlgRinglist.action?limit=10&start=0&resultType=json";

#--建立LWP ::UserAgent 与HTTP ::Request 物件，
#--其中Request对象将$url传进去
my $agent = LWP::UserAgent->new();
my $request = HTTP::Request->new( POST => $url );
#--透过UserAgent的request method将Request送出
my $response = $agent->request($request);

#--检查是否有error发生
$response->is_success or die "$!$url: ", $response->message, "/n";
#--显示responser的内容
print $response->content;

#{"clhlgOpenServer":"","listLDResult":[{"createTime":"2015-06-12 16:06:29","id":"2","modifyTime":"2015-06-12 16:06:29","phone":"13339106748","type":"1","typeName":"免费彩铃"},{"createTime":"2015-06-12 15:47:38","id":"1","modifyTime":"2015-06-12 15:47:38","phone":"13339106748","type":"0","typeName":"谢谢参与"}]}
