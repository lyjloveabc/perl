#!perl
`cd D:/develop/Workspaces-MyEclipse Professional 2014/MyEclipse Professional 2014/activity_jl_diyGame/src/com/sinontech/activity/entity`;
foreach my $file(glob "*.java"){
	$old=$file;
	$file=~s/Match/Game/;
	rename $old,$file;
}