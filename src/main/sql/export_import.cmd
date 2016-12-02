rem export
mysqldbexport --server=root:password@localhost --export=DATA --bulk-insert --output-file=C:\Temp\data\text-analyzer.sql text-analyzer

rem csv data export
mysqldbexport --server=root:password@localhost --export=DATA --format=csv --file-per-table text-analyzer
