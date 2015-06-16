open my $fh, '<', 'temp.txt';
while(<$fh>){
	chomp;
	print $_;
}
