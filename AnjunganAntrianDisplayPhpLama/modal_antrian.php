<?php
include ('config.php');
?>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="fontawesome-free-5.6.3-web/css/all.css">
    <link href="css/gijgo.min.css" rel="stylesheet" type="text/css" />

    <style>
    .modal-full {
      min-width: 100%;
      margin: 0;
    }
    .modal-full .modal-content {
      min-height: 100vh;
    }
    .modal-fix {
      min-width: 1024px;
      margin: 0;
    }
    .modal-fix .modal-content {
      min-height: 100vh;
    }
    .modal .tab-content {
      min-height: 50vh;
    }
    .nav-pills.nav-wizard > li {
      position: relative;
      overflow: visible;
      border-right: 8px solid transparent;
      border-left: 8px solid transparent;
    }

    .nav-pills.nav-wizard > li + li {
      margin-left: 0;
    }

    .nav-pills.nav-wizard > li:first-child {
      border-left: 0;
    }

    .nav-pills.nav-wizard > li:first-child a {
      border-radius: 5px 0 0 5px;
    }

    .nav-pills.nav-wizard > li:last-child {
      border-right: 0;
    }

    .nav-pills.nav-wizard > li:last-child a {
      border-radius: 0 5px 5px 0;
    }

    .nav-pills.nav-wizard > li a {
      border-radius: 0;
      background-color: #eee;
    }

    .nav-pills.nav-wizard > li:not(:last-child) a:after {
      position: absolute;
      content: "";
      top: 0px;
      right: -20px;
      width: 0px;
      height: 0px;
      border-style: solid;
      border-width: 20px 0 20px 20px;
      border-color: transparent transparent transparent #eee;
      z-index: 150;
    }

    .nav-pills.nav-wizard > li:not(:first-child) a:before {
      position: absolute;
      content: "";
      top: 0px;
      left: -20px;
      width: 0px;
      height: 0px;
      border-style: solid;
      border-width: 20px 0 20px 20px;
      border-color: #eee #eee #eee transparent;
      z-index: 150;
    }

    .nav-pills.nav-wizard > li:hover:not(:last-child) a:after {
      border-color: transparent transparent transparent #aaa;
    }

    .nav-pills.nav-wizard > li:hover:not(:first-child) a:before {
      border-color: #aaa #aaa #aaa transparent;
    }

    .nav-pills.nav-wizard > li:hover a {
      background-color: #aaa;
      color: #fff;
    }

    .nav-pills.nav-wizard > li:not(:last-child) a.active:after {
      border-color: transparent transparent transparent #428bca;
    }

    .nav-pills.nav-wizard > li:not(:first-child) a.active:before {
      border-color: #428bca #428bca #428bca transparent;
    }

    .nav-pills.nav-wizard > li a.active {
      background-color: #428bca;
    }
<div class="modal fade" id="antrian" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalCenterTitle">Antrian Pendaftaran</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="row justify-content-around">
            <div class="col-5 pt-5 pb-5">
              <div id="printAntrianLoket" style="display: none;" class="cetak">
                <div style="width: 180px; font-family: Tahoma; margin-top: 10px; margin-right: 5px; margin-bottom: 100px; margin-left: 15px; font-size: 21px !important;">
                  <div id="print_nomor_loket"></div>
                  Loket Pendaftaran
                </div>
              </div>
              <div id="display_nomor_loket"></div>
              <form role="form" id="formloket" name="formloket">
                <button type="submit" class="btn btn-lg btn-danger btn-block" id="btnKRM" value="Submit" name="btnKRM" onclick="printDiv('printAntrianLoket');">ANTRIAN PENDAFTARAN</button>
              </form>
            </div>
            <div class="col-5 pt-5 pb-5">
              <div id="printAntrianCS" style="display: none;" class="cetak">
                <div style="width: 180px; font-family: Tahoma; margin-top: 10px; margin-right: 5px; margin-bottom: 100px; margin-left: 15px; font-size: 21px !important;">
                  <div id="print_nomor_cs"></div>
                 Kasir
                </div>
              </div>
              <div id="display_nomor_cs"></div>
              <form role="form" id="formcs" name="formcs">
                <button type="submit" class="btn btn-lg btn-danger btn-block" id="btnKRMCS" value="Submit" name="btnKRMCS" onclick="printDiv('printAntrianCS');">ANTRIAN KASIR</button>
              </form>
            </div>
            <div class="col-5 pt-5 pb-5">
              <div id="printAntrianApt" style="display: none;" class="cetak">
                <div style="width: 180px; font-family: Tahoma; margin-top: 10px; margin-right: 5px; margin-bottom: 100px; margin-left: 15px; font-size: 21px !important;">
                  <div id="print_nomor_apt"></div>
                 Apotek
                </div>
              </div>
              <div id="display_nomor_apt"></div>
              <form role="form" id="formcs" name="formcs">
                <button type="submit" class="btn btn-lg btn-danger btn-block" id="btnKRMCS" value="Submit" name="btnKRMCS" onclick="printDiv('printAntrianCS');">ANTRIAN APOTEK</button>
              </form>
            </div>
            </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Tutup</button>
          </div>
        </div>
      </div>
    </div>
