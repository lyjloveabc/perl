#!perl@lyj
#new a pl file with some rule
use 5.010;
use warnings;

say "START-P";
<STDIN>;
$nowTime = time;
$content = "#!perl by LYJ\n"
  . "#description\n"
  . "use 5.010;\n"
  . "use warnings;\n" . "\n"
  . "say \"START-P\";\n\n"
  . "<STDIN>;\n\n"
  . "#Content\n\n"
  . "say \"END-P\";\n"
  . "<STDIN>;";
open NEWFILE, ">", $nowTime . ".txt";
print NEWFILE $content;
close NEWFILE;
say "END-P";
<STDIN>;
