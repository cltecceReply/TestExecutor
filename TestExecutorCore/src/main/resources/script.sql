CREATE TABLE "TEST_CASES"  (
		  "TEST_ID" INTEGER NOT NULL ,
		  "TEST_DATA" VARCHAR(1000 ) NOT NULL ,
		  "SERVICE_NAME" VARCHAR(100 ) NOT NULL ,
		  "TEST_WRITE" CHAR(1 ) NOT NULL WITH DEFAULT 0 );
ALTER TABLE "TEST_CASES"
	ADD PRIMARY KEY
		("TEST_ID")
	ENFORCED ;
-- Contains the endpoint of the services to be tested
CREATE TABLE "SERVICE_DESCRIPTION"  (
		  "SERVICE_NAME" VARCHAR(255) NOT NULL ,
		  "ENDPOINT_NEW" VARCHAR(255) ,
		  "ENDPOINT_OLD" VARCHAR(255) );
ALTER TABLE "SERVICE_DESCRIPTION"
	ADD PRIMARY KEY
		("SERVICE_NAME")
	ENFORCED ;


INSERT INTO SERVICE_DESCRIPTION (SERVICE_NAME,ENDPOINT_NEW,ENDPOINT_OLD) VALUES
	 ('GKDOCPRA','http://10.2.252.1:9083/gkdocpra/inquire','http://10.2.252.1:1800/gkdocpra/inquire'),
	 ('GESCONT','http://localhost:9080/gescont/D07700A0ImplService','http://10.2.252.1:1800/gescont/inquire'),
	 ('CRIFDATI','http://10.2.252.1:9083/crifdati/inquire','http://10.2.252.1:1800/crifdati/inquire'),
	 ('DALMWEB','http://10.2.252.1:9083/dalmweb/inquire','http://10.2.252.1:1800/dalmweb/inquire'),
	 ('PROGCARTE','http://10.2.252.1:9083/progcarte/inquire','http://10.2.252.1:1800/progcarte/inquire');



INSERT INTO TEST_CASES (TEST_ID,TEST_DATA,SERVICE_NAME,TEST_WRITE) VALUES
	 (0,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">   <soapenv:Header>    <!-- The Security element should be removed if WS-Security is not enabled on the SOAP target-url -->   </soapenv:Header>   <soapenv:Body>    <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory -->     <ns1:rec_in_d07701a0>      <ns1:w_in_numpra>03303140000752</ns1:w_in_numpra>      <ns1:w_in_documento>01</ns1:w_in_documento>     </ns1:rec_in_d07701a0>    </ns1:D07701A0Operation>   </soapenv:Body>  </soapenv:Envelope>','GKDOCPRA','N'),
	 (3,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory --> <ns1:rec_in_d07701a0><!-- mandatory --> <ns1:w_in_numpra><!-- mandatory --></ns1:w_in_numpra> <ns1:w_in_documento></ns1:w_in_documento> </ns1:rec_in_d07701a0> </ns1:D07701A0Operation> </soapenv:Body> </soapenv:Envelope>','GKDOCPRA','N'),
	 (6,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory --> <ns1:rec_in_d07701a0><!-- mandatory --> <ns1:w_in_numpra>12345678912345<!-- mandatory --></ns1:w_in_numpra> </ns1:rec_in_d07701a0> </ns1:D07701A0Operation> </soapenv:Body> </soapenv:Envelope>','GKDOCPRA','N'),
	 (7,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> <!-- The Security element should be removed if WS-Security is not enabled on the SOAP target-url --> </soapenv:Header> <soapenv:Body> <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory --> <ns1:rec_in_d07701a0> <ns1:w_in_numpra>03303117001612</ns1:w_in_numpra> <ns1:w_in_documento>01</ns1:w_in_documento> </ns1:rec_in_d07701a0> </ns1:D07701A0Operation> </soapenv:Body> </soapenv:Envelope>','GKDOCPRA','N'),
	 (8,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> <!-- The Security element should be removed if WS-Security is not enabled on the SOAP target-url --> </soapenv:Header> <soapenv:Body> <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory --> <ns1:rec_in_d07701a0> <ns1:w_in_numpra>03303110000752</ns1:w_in_numpra> <ns1:w_in_documento></ns1:w_in_documento> </ns1:rec_in_d07701a0> </ns1:D07701A0Operation> </soapenv:Body> </soapenv:Envelope>','GKDOCPRA','N'),
	 (9,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> <!-- The Security element should be removed if WS-Security is not enabled on the SOAP target-url --> </soapenv:Header> <soapenv:Body> <ns1:D07701A0Operation xmlns:ns1="http://www.D07701A0.D07701I.Request.com"><!-- mandatory --> <ns1:rec_in_d07701a0> <ns1:w_in_numpra>03303110000752</ns1:w_in_numpra> <ns1:w_in_documento>01</ns1:w_in_documento> </ns1:rec_in_d07701a0> </ns1:D07701A0Operation> </soapenv:Body> </soapenv:Envelope>','GKDOCPRA','N'),
	 (10,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">  	<soapenv:Header>  		<!-- The Security element should be removed if WS-Security is not enabled on the SOAP target-url --></soapenv:Header>  	<soapenv:Body>  		<ns1:D07700A0Operation xmlns:ns1="http://www.D07700A0.D07700I.Request.com">  			<!-- mandatory -->  			<ns1:rec_in_d07700a0>  				<!-- mandatory -->  				<ns1:w_in_cod_userid>00000000</ns1:w_in_cod_userid>  			</ns1:rec_in_d07700a0>  		</ns1:D07700A0Operation>  	</soapenv:Body>  </soapenv:Envelope>  ','GESCONT','Y'),
	 (11,' <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07704A0Operation xmlns:ns1="http://www.D07704A0.D07704I.Request.com"><!-- mandatory --> <ns1:rec_in_d07704a0><!-- mandatory --> <ns1:w_in_cod_fisc>22<!-- mandatory --></ns1:w_in_cod_fisc> </ns1:rec_in_d07704a0> </ns1:D07704A0Operation> </soapenv:Body> </soapenv:Envelope>','CRIFDATI','N'),
	 (12,' <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07704A0Operation xmlns:ns1="http://www.D07704A0.D07704I.Request.com"><!-- mandatory --> <ns1:rec_in_d07704a0><!-- mandatory --> </ns1:rec_in_d07704a0> </ns1:D07704A0Operation> </soapenv:Body> </soapenv:Envelope>','CRIFDATI','N'),
	 (13,' <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07704A0Operation xmlns:ns1="http://www.D07704A0.D07704I.Request.com"><!-- mandatory --> <ns1:rec_in_d07704a0><!-- mandatory --> <ns1:w_in_cod_fisc>1234567 90123456</ns1:w_in_cod_fisc> </ns1:rec_in_d07704a0> </ns1:D07704A0Operation> </soapenv:Body> </soapenv:Envelope>','CRIFDATI','N');
INSERT INTO TEST_CASES (TEST_ID,TEST_DATA,SERVICE_NAME,TEST_WRITE) VALUES
	 (14,' <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Header> </soapenv:Header> <soapenv:Body> <ns1:D07704A0Operation xmlns:ns1="http://www.D07704A0.D07704I.Request.com"><!-- mandatory --> <ns1:rec_in_d07704a0><!-- mandatory --> <ns1:w_in_cod_fisc>1 3<!-- mandatory --></ns1:w_in_cod_fisc> </ns1:rec_in_d07704a0> </ns1:D07704A0Operation> </soapenv:Body> </soapenv:Envelope>','CRIFDATI','N'),
	 (15,'<soapenv:Envelope	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">	<soapenv:Header/>	<soapenv:Body>		<ns1:D39050A0Operation			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">			<ns1:xprxarea_input>				<ns1:xprxcognome_selez></ns1:xprxcognome_selez>				<ns1:xprxnome_selez></ns1:xprxnome_selez>				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>			</ns1:xprxarea_input>				</ns1:D39050A0Operation>	</soapenv:Body></soapenv:Envelope>','DALMWEB','0'),
	 (16,'<soapenv:Envelope	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">	<soapenv:Header/>	<soapenv:Body>		<ns1:D39050A0Operation			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">			<ns1:xprxarea_input>				<ns1:xprxcognome_selez></ns1:xprxcognome_selez>				<ns1:xprxnome_selez></ns1:xprxnome_selez>				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>			</ns1:xprxarea_input>				</ns1:D39050A0Operation>	</soapenv:Body></soapenv:Envelope>','DALMWEB','0'),
	 (17,'<soapenv:Envelope
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Header/>
	<soapenv:Body>
		<ns1:D39050A0Operation
			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">
			<ns1:xprxarea_input>
				<ns1:xprxcognome_selez>kkk</ns1:xprxcognome_selez>
				<ns1:xprxnome_selez></ns1:xprxnome_selez>
				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>
				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>
				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>
			</ns1:xprxarea_input>
			</ns1:D39050A0Operation>
	</soapenv:Body>
</soapenv:Envelope>
','DALMWEB','0'),
	 (18,'<soapenv:Envelope
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Header/>
	<soapenv:Body>
		<ns1:D39050A0Operation
			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">
			<ns1:xprxarea_input>
				<ns1:xprxcognome_selez></ns1:xprxcognome_selez>
				<ns1:xprxnome_selez></ns1:xprxnome_selez>
				<ns1:xprxcap2_selez>aa</ns1:xprxcap2_selez>
				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>
				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>
			</ns1:xprxarea_input>
		</ns1:D39050A0Operation>
	</soapenv:Body>
</soapenv:Envelope>
','DALMWEB','0'),
	 (19,'<soapenv:Envelope
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Header/>
	<soapenv:Body>
		<ns1:D39050A0Operation
			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">
			<ns1:xprxarea_input>
				<ns1:xprxcognome_selez></ns1:xprxcognome_selez>
				<ns1:xprxnome_selez></ns1:xprxnome_selez>
				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>
				<ns1:xprxcap3_selez>bb</ns1:xprxcap3_selez>
				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>
			</ns1:xprxarea_input>
			</ns1:D39050A0Operation>
	</soapenv:Body>
</soapenv:Envelope>
','DALMWEB','0'),
	 (20,'<soapenv:Envelope
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<soapenv:Header/>
	<soapenv:Body>
		<ns1:D39050A0Operation
			xmlns:ns1="http://www.D39050A0.COM39050.Request.com">
			<ns1:xprxarea_input>
				<ns1:xprxcognome_selez></ns1:xprxcognome_selez>
				<ns1:xprxnome_selez>kkkk</ns1:xprxnome_selez>
				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>
				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>
				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>
			</ns1:xprxarea_input>
		</ns1:D39050A0Operation>
	</soapenv:Body>
</soapenv:Envelope>																																																																																																																																		
','DALMWEB','0'),
	 (21,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">	<soapenv:Header/>	<soapenv:Body>		<ns1:D39050A0Operation xmlns:ns1="http://www.D39050A0.COM39050.Request.com">			<ns1:xprxarea_input>				<ns1:xprxcognome_selez> </ns1:xprxcognome_selez>				<ns1:xprxnome_selez>  </ns1:xprxnome_selez>				<ns1:xprxcap2_selez> </ns1:xprxcap2_selez>				<ns1:xprxcap3_selez>  </ns1:xprxcap3_selez>				<ns1:xprxdat_nasc_selez>35140002</ns1:xprxdat_nasc_selez>			</ns1:xprxarea_input>		</ns1:D39050A0Operation>	</soapenv:Body></soapenv:Envelope>																																																																																																																																		
','DALMWEB','0'),
	 (22,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">	<soapenv:Header/>	<soapenv:Body>		<ns1:D39050A0Operation xmlns:ns1="http://www.D39050A0.COM39050.Request.com">			<ns1:xprxarea_input>				<ns1:xprxcognome_selez>ABFAFE</ns1:xprxcognome_selez>				<ns1:xprxnome_selez>ARGAHOADHI</ns1:xprxnome_selez>				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>				<ns1:xprxdat_nasc_selez></ns1:xprxdat_nasc_selez>			</ns1:xprxarea_input>		</ns1:D39050A0Operation>	</soapenv:Body></soapenv:Envelope>																																																																																																																																		
','DALMWEB','0'),
	 (23,'<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">	<soapenv:Header/>	<soapenv:Body>		<ns1:D39050A0Operation xmlns:ns1="http://www.D39050A0.COM39050.Request.com">			<ns1:xprxarea_input>				<ns1:xprxcognome_selez>AVAAFE</ns1:xprxcognome_selez>				<ns1:xprxnome_selez>ARGAHOI</ns1:xprxnome_selez>				<ns1:xprxcap2_selez></ns1:xprxcap2_selez>				<ns1:xprxcap3_selez></ns1:xprxcap3_selez>				<ns1:xprxdat_nasc_selez>150101</ns1:xprxdat_nasc_selez>			</ns1:xprxarea_input>		</ns1:D39050A0Operation>	</soapenv:Body></soapenv:Envelope>																																																																																																																																		
','DALMWEB','0');
INSERT INTO TEST_CASES (TEST_ID,TEST_DATA,SERVICE_NAME,TEST_WRITE) VALUES
	 (24,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10027504595589</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (25,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10027504599469</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (26,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10035043795847</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (27,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10035043796235</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (28,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10035043797011</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (29,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10042010480474</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (30,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10042010481735</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0'),
	 (31,'
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
 <soapenv:Header>
 </soapenv:Header>
 <soapenv:Body>
  <ns1:D50150A0Operation xmlns:ns1="http://www.D50150A0.D50150I.Request.com">
   <ns1:rec_in_d50150a0>
    <ns1:w_in_num_pra>10042010483093</ns1:w_in_num_pra>
   </ns1:rec_in_d50150a0>
  </ns1:D50150A0Operation>
 </soapenv:Body>
</soapenv:Envelope>','PROGCARTE','0');

