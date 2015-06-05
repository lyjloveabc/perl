#!perl@lyj
#中文乱码解决-读取不是utf8的文件
use 5.010;       #使用say
use warnings;    #打印警告模块
use Encode;      #编码模块
@ARGV = qw(toCh.txt);
while (<>) {
	chomp;
	$enStr = decode( "gb2312", $_ );
	$str = Encode::encode('utf8', $enStr);
	say $str;
}