#!perl @LYJ
#
use 5.010;
use warnings;
use utf8;
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

@ARGV = qw(temp.txt);
while (<>) {
	chomp $_;
	my $sth = $dbh->prepare("select url from ring where id = ?");
	$sth->bind_param( 1, '11');
	$sth->execute();
}

print "Opened database successfully\n";
