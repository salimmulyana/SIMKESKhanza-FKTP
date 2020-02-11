# APM
Anjungan Pasien Mandiri

## Jika Belum Ada Tabel antrian_loket

~~~~sql
CREATE TABLE `antrian_loket` (
  `kd` int(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `noantrian` varchar(50) NOT NULL,
  `postdate` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL DEFAULT '00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

ALTER TABLE `antrian_loket` ADD PRIMARY KEY (`kd`);
ALTER TABLE `antrian_loket` MODIFY `kd` int(50) NOT NULL AUTO_INCREMENT;
~~~~

## Jika sudah ada sebelumnya dan belum ada kolom type, start_time dan end_time

~~~~sql
ALTER TABLE `antrian_loket` ADD `type` VARCHAR(50) NOT NULL AFTER `kd`;
ALTER TABLE `antrian_loket` CHANGE `postdate` `postdate` DATE NOT NULL;
ALTER TABLE `antrian_loket` ADD `start_time` TIME NOT NULL AFTER `postdate`, ADD `end_time` TIME NOT NULL DEFAULT '00:00:00' AFTER `start_time`;
~~~~

## Tambahkan antrics

~~~~sql
CREATE TABLE `antrics` (
  `loket` int(11) NOT NULL,
  `antrian` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

ALTER TABLE `antrics`
  ADD KEY `loket` (`loket`) USING BTREE,
  ADD KEY `antrian` (`antrian`) USING BTREE;
~~~~
