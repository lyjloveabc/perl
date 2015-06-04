#!perl by LYJ
#
use 5.010;
use warnings;
use Tkx;
use utf8;
$mw=Tkx::widget->new(".");
$mw->new_ttk__frame(-padding => "3 3 12 12");
#Tkx::tk___getOpenFile();
Tkx::tk___messageBox(
	-type    => "yesno",
	-message => "Are you sure you want to install SuperVirus?",
	-icon    => "error",
	-title   => "error"
);
Tkx::MainLoop();