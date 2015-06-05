#!perl@lyj
#中文乱码解决-读取文件本身就是utf8
use 5.010;       #使用say
use warnings;    #打印警告模块
use Encode;      #编码模块

@ARGV = qw(toCh2.txt);
while (<>) {

	#Encode::_utf8_on($_);
	#$str = Encode::encode('utf8', $_);#以什么编码格式编码字符串
	chomp;
	say "$_";
}
<STDIN>
