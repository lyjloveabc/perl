use strict; 
use Spreadsheet::WriteExcel;

my $xl = Spreadsheet::WriteExcel->new("WriteExcelOutput2.xlsx");
my $xlsheet = $xl->add_worksheet("TestSheet");  #引号中为excel工作簿中表的名称
$xlsheet->freeze_panes(1, 0); #冻结首行