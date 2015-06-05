#!perl@lyj
#中文乱码解决-流编码
use 5.010;       #使用say
use warnings;    #打印警告模块
use utf8;

binmode( STDIN,  ':encoding(utf8)' );
binmode( STDOUT, ':encoding(utf8)' );

$abc = "中国";

print $abc;
