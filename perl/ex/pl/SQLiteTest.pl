#!perl @LYJ
#
use 5.010;
use warnings;
use Tkx;
use utf8;
use DBD::SQLite;
use DBI qw(:sql_types);

my $driver   = "SQLite";
my $database = "test.db";
my $dsn      = "DBI:$driver:dbname=$database";
my $userid   = "";
my $password = "";
my $dbh =
DBI->connect( $dsn, $userid, $password)
  or die $DBI::errstr;
#my $sql = "create table mytable(id int null, name int null)";
#$dbh->do($sql);

my $blob = "bug.jpg";
my $sth  = $dbh->prepare("INSERT INTO mytable VALUES (1, ?)");
$sth->bind_param( 1, $blob, SQL_BLOB );
$sth->execute();

print "Opened database successfully\n";
