use Spreadsheet::WriteExcel;
use Encode;
my $dstFile    = "WriteExcelOutput111.xlsx";
my $dstBook    = Spreadsheet::WriteExcel->new($dstFile);
#my $bookFormat = &SetBookFmt($dstBook);
my $flowSheet  = $dstBook->add_worksheet( decode( "utf8", "中奖名单Abc123" ) );
$flowSheet->set_column( 0, 5, 16 );
$flowSheet->write( 0, 0, "", $bookFormat->{"coltitle"} );
$flowSheet->write(
	0, 1,
	decode( "utf8", "回国" ),
	$bookFormat->{"coltitle"}
);
$dstBook->close();

sub SetBookFmt($) {
	my $book = shift;

	my %fmt;

	$fmt{"coltitle"} = $book->add_format();
	$fmt{"coltitle"}->set_border(2);
	$fmt{"coltitle"}->set_align('center');
	$fmt{"coltitle"}->set_align('top');
	$fmt{"coltitle"}->set_properties( bold => 1 );
	$fmt{"coltitle"}->set_font( decode( "utf8", '楷体' ) );
	$fmt{"coltitle"}->set_size(12);

	#$fmt{"coltitle"}->set_color(1);
	$fmt{"coltitle"}->set_pattern();
	$fmt{"coltitle"}
	  ->set_fg_color( $book->set_custom_color( 20, 97, 202, 255 ) );
	$fmt{"coltitle"}->set_merge(0);
	$fmt{"coltitle"}->set_text_wrap();

	$fmt{"data"} = $book->add_format();
	$fmt{"data"}->set_border(1);
	$fmt{"data"}->set_align('center');
	$fmt{"data"}->set_align('vcenter');
	$fmt{"data"}->set_text_wrap();
	$fmt{"data"}->set_font( decode( "utf8", '微软雅黑' ) );
	$fmt{"data"}->set_size(9);

	return ( \%fmt );
}
