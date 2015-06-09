#!perl by LYJ
#
use 5.010;
use warnings;
use strict;
use utf8;
use Spreadsheet::XLSX;
use Encode;
use Encode::Locale qw($ENCODING_LOCALE_FS);

use DBI qw(:sql_types);

my $driver   = "SQLite";
my $database = "ring.db";
my $dsn      = "DBI:$driver:dbname=$database";
my $userid   = "";
my $password = "";
my $dbh      = DBI->connect( $dsn, $userid, $password )
  or die $DBI::errstr;

#my $sql = "create table ring(id varchar(50) null, url varchar(255) null)";
#$dbh->do($sql);

my $file = encode( locale_fs => '最新歌单.xlsx' );
my $excel = Spreadsheet::XLSX->new($file);

my $id = 94;
for my $sheet ( @{ $excel->{Worksheet} } ) {
	$sheet->{MaxRow} ||= $sheet->{MinRow};
	foreach my $row ( $sheet->{MinRow} + 2 .. $sheet->{MaxRow} ) {
		my $cell1 = $sheet->{Cells}[$row][1];
		my $cell2 = $sheet->{Cells}[$row][2];
		my $cell3 = $sheet->{Cells}[$row][3];
		my $cell4 = substr( $sheet->{Cells}[$row][3]->{Val}, 0, 1 );
		my $cell6 = $sheet->{Cells}[$row][5];

		my $cell1str = substr( $cell1->{Val}, 0, 12 );

		my $sth = $dbh->prepare("select url from ring where id = ?;");
		$sth->bind_param( 1, '810023330134' );
		my $rv = $sth->execute() or die $DBI::errstr;
		if ( $rv < 0 ) {
			print $DBI::errstr;
		}
		while ( my @row = $sth->fetchrow_array() ) {
			say
qq(insert into CLHLG_RING(id,CREATE_TIME,MODIFY_TIME,RING_ID,NAME,SINGER,PRICE,URL,TYPE) 
			values($id,'2015-06-05 11:33:49','2015-06-05 11:33:49','$cell1str','$cell2->{Val}','$cell3->{Val}','$cell4','$row[0]','1'); )
			  ;
		}
		$id++;
	}
}
