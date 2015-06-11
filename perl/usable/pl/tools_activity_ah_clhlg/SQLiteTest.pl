#!perl @LYJ
#
use 5.010;
use warnings;
use Tkx;
use utf8;
use DBD::SQLite;
use DBI qw(:sql_types);

my $driver   = "SQLite";
my $database = "ringNew.db";
my $dsn      = "DBI:$driver:dbname=$database";
my $userid   = "";
my $password = "";
my $dbh      = DBI->connect( $dsn, $userid, $password )
  or die $DBI::errstr;
my $sql = "create table ringNew(id varchar(50) null, url varchar(255) null)";
$dbh->do($sql);

@ARGV = qw(temp.txt);
while (<>) {
	chomp $_;
	@res=split(" ");
	my $sth = $dbh->prepare("INSERT INTO ringNew VALUES (?,?)");
	$sth->bind_param( 1, $res[0]);
	$sth->bind_param( 2, $res[1]);
	$sth->execute();
}

print "Opened database successfully\n";
