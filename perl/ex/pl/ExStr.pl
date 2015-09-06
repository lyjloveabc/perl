#!perl
#lyj
use 5.010;
use warnings;
use diagnostics;
use strict;

use DBI;

my $driver   = "SQLite";
my $database = "0702.db";
my $dsn      = "DBI:$driver:dbname=$database";
my $userid   = "";
my $password = "";
my $dbh      = DBI->connect( $dsn, $userid, $password )
  or die $DBI::errstr;
#my $sql = "create table tab0701(id varchar(50) null, url varchar(255) null)";
#$dbh->do($sql);

my $sqlStr = $dbh->prepare("insert into tab0701 values(?,?)");
$sqlStr->bind_param(1,"aaaa");
$sqlStr->bind_param(2,"hehe");
$sqlStr->execute();

print "Opened database successfully\n";