/*
	version	modify_date	modify_user	description
	1.00	20200221	DANIEL	���ӥ��d�ӤH���էO�~�ݪťղէO
	1.01	20200922	JAY		�W�[HRCONFIG�P�_
*/
package hr;
import jcx.jform.hproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import jcx.lib.*;
import hr.common5_v2;
/*
	dat:hrm7w
	function name:B5.�[�Z��(�w���ӽ�)
	note:
*/
public class B05phadd2_v2 extends hproc{
	boolean bEncryptGrade;
	talk t = null;
	String sql = "";
	String[][] ret = null;
	final String BACK = "history.back();";
	String ISNULL = "";
	//20181123 DANIEL COULSON begin:oracle��+�O||
	String PLUS = ""; 
	private common5_v2 common5;
	//20181123 DANIEL COULSON end:oracle��+�O|| 
	public String action(String value)throws Throwable{
		t = getTalk();
		ISNULL = pDB.getISNULL(t);
		//20181123 DANIEL COULSON begin:oracle��+�O||
		PLUS = pDB.getPLUS(t);
		//20181123 DANIEL COULSON end:oracle��+�O||
		String DATELIST = (String)get("SYSTEM.DATELIST");
		String FUNCTIONID = getFunctionID();

		//20140211:edward:begin:�W�[¾��¾�ť[�ѱK
		String encryptGrade = (String)get("HRCONFIG_GRADE.ENCRYPTION");
		bEncryptGrade = "Y".equals(encryptGrade);
		//20140211:edward:end:�W�[¾��¾�ť[�ѱK
		
		//20120816 tobey �N�w�p�[�Z�歶����X�ܭ���
		boolean bCheckEMPID = checkEMPID(t, DATELIST);
		if (!bCheckEMPID) {
			return value;	
		}
		String OLDOVER = getValue("OLDOVER").trim();
		//20130702:leo:begin:�����
		if("MOBILE_B5".equals(FUNCTIONID)){
			OLDOVER = (String)get("MOBILE_B5_OLDOVER");
			if(OLDOVER==null){
				OLDOVER = getValue("OLDOVER");
			}else{
				remove("MOBILE_B5_OLDOVER");
			}
		}
		//20130702:leo:end:�����
		if(OLDOVER.trim().length()==0){
			//message("�w�p�[�Z�渹���i�ť�",BACK);
			//message("%1%2",new String[]{translate("�w�p�[�Z�渹") , translate("���i�ť�")},BACK);
			//	20190329 MAX COULSON BEGIN:��½Ķ
			message(hr.Msg.hrm8w_B32.Msg007,BACK);
			//	20190329 MAX COULSON END:��½Ķ
			return value;
		}

		setValue("TYPE" , "A");//20170629 DRACO ELINE begin:�[�Z���O�T�w�w�]A

		sql = "select a.EMPID , b.HECNAME , b.POSSIE , b.CPNYID , b.DEPT_NO , a.SDATE , a.EDATE , a.STIME"//7
			+ " , a.ETIME , a.NOTE , (select SET14 from HRUSER_DEPT_BAS where DEP_NO=b.DEPT_NO)"//10
			+ " , a.SHHMM , a.EHHMM , b.CLASS , b.GRADE , a.OTYPE , a.HPRICE , a.REPRICE"//17
			+ " , a.AMT"//18
			//20181123 DANIEL COULSON begin:�����w�w�[�Z��NOTE��� �ɤu�渹�GXXXXXXX
			//+ " , (select WDEPT from SUPPORT where PNO=a.PNO)"//19 20180209 DRACO ELINE begin:�q�ɤu����Ӫ��w�w�[�Z�]�n��[�Z���a�W
			+ " , (select WDEPT from SUPPORT where '�ɤu�渹�G'"+PLUS+"PNO = a.NOTE)"//19
			//20181123 DANIEL COULSON end:�����w�w�[�Z��NOTE��� �ɤu�渹�GXXXXXXX
			//20200221 DANIEL PATRICK begin:�ҶԲէO
			+ " , b.SET107, (select GROUP_A from HRUSER_DEPT_BAS where DEP_NO=b.DEPT_NO)"//20-21
			//20200221 DANIEL PATRICK end:�ҶԲէO
			+ " from OVERPLAN a , HRUSER b"
			+ " where a.EMPID = b.EMPID"
			+ " and a.PNO = '"+convert.ToSql(OLDOVER.trim())+"'"
			+ " and isnull(a.USED,'"+get("SYSTEM.ISNULL")+"') != '1'";
		ret = t.queryFromPool(sql);
		if(ret.length==0){
			//message(translate("��Ƥw�ӽЩΦ��H���s�b����ɤ�"),BACK);
			//	20190329 MAX COULSON BEGIN:��½Ķ
			message(hr.Msg.hrm8w_B32.Msg008,BACK);
			//	20190329 MAX COULSON END:��½Ķ
			return value;
		}

		//���XDB �̪����
		String EMPID = ret[0][0].trim();
		String HECNAME = ret[0][1].trim();
		String POSSIE = ret[0][2].trim();
		String CPNYID = ret[0][3].trim();
		String DEPT_NO = ret[0][4].trim();
		String SDATE = ret[0][5].trim();
		String EDATE = ret[0][6].trim();
		String STIME = ret[0][7].trim();
		String ETIME = ret[0][8].trim();
		String NOTE = ret[0][9].trim();
		String SET14 = ret[0][10].trim();
		String SHHMM = ret[0][11].trim();
		String EHHMM = ret[0][12].trim();
		String CLASS = ret[0][13].trim();
		String GRADE = ret[0][14].trim();
		String OTYPE = ret[0][15].trim();
		String HPRICE = ret[0][16].trim();
		String REPRICE = ret[0][17].trim();
		String AMT = ret[0][18].trim();
		String USERCLASS = ret[0][13].trim();
		
		//20180209 DRACO ELINE begin:�q�ɤu����Ӫ��w�w�[�Z�]�n��[�Z���a�W
		String WDEPT = ret[0][19].trim();
		//20180209 DRACO ELINE end:�q�ɤu����Ӫ��w�w�[�Z�]�n��[�Z���a�W
		//20200221 DANIEL PATRICK begin:�ҶԲէO
		String SET107 = ret[0][20].trim();
		if(SET107.length() == 0){
			SET107 = ret[0][21].trim();
		}
		//20200221 DANIEL PATRICK end:�ҶԲէO

		String CLASSDA[][] = t.queryFromPool("select CLASS from CLASSDA where EMPID = '"+convert.ToSql(EMPID.trim())+"' and CLASSDA = '"+convert.ToSql(SDATE.trim())+"' ");
		if(CLASSDA.length!=0){
			if(CLASSDA[0][0]!=null && !CLASSDA[0][0].trim().toUpperCase().equals("NULL") && CLASSDA[0][0].trim().length()!=0){
				CLASS = CLASSDA[0][0].trim();
			}
		}
		else{
			int DAY = Integer.parseInt(SDATE.trim().substring(6,8));
			String PROCEED[][] = t.queryFromPool("select K"+DAY+" from PROCEED where YYMM = '"+convert.ToSql(SDATE.trim().substring(0,6))+"' and PRTYPE = '"+convert.ToSql(CLASS.trim())+"'");
			if(PROCEED.length!=0){
				if(PROCEED[0][0]!=null && !PROCEED[0][0].trim().toUpperCase().equals("NULL") && PROCEED[0][0].trim().length()!=0){
					CLASS = PROCEED[0][0].trim();
				}
			} else {
				String a[][]=hr.common5.getPROCEED_by_virtual(t,SDATE,SDATE,EMPID);
				if(a.length!=0) CLASS=a[0][1].trim();
			}
		}
		if(CLASS==null || CLASS.trim().toUpperCase().equals("NULL") || CLASS.trim().length()==0){
			//message(translate("���H�ƯZ�]�w���~,���ˬd"),BACK);
			//	20190329 MAX COULSON BEGIN:��½Ķ
			message(hr.Msg.hrm8w_B32.Msg009,BACK);
			//	20190329 MAX COULSON END:��½Ķ
			return value;
		}

		Hashtable htCLASSSET = new Hashtable(); 
		try{
			sql = " select CLASS , CLNAME , CSTIME , CETIME , RSTIME , "
				+ " RETIME , CLASSYN , DAYPAY , MONPAY , FMIN , WORKHOUR , "
				+ " RSTIME2 , RETIME2 , RSTIME3 , RETIME3 , RSTIME4 , RETIME4 "
				+ " , OTYPE"//17
				+ " from CLASSSET ";
			String[][] dtCLASSSET = t.queryFromPool(sql,20);
			for(int i = 0; i < dtCLASSSET.length ; i++){
				htCLASSSET.put(dtCLASSSET[i][0].trim(),dtCLASSSET[i]);
			}
		} catch(Exception e){
			System.err.println("�ҶԯZ�O classset ���~");
		}
		String CLASS_OTYPE = "";
		String CLASS_CSTIME = "";
		String[] arrCLASSSET = (String[])htCLASSSET.get(CLASS);
		if(arrCLASSSET != null)
		{
			CLASS_OTYPE = arrCLASSSET[17];
			CLASS_CSTIME = arrCLASSSET[2];
		}

		//20170503 DRACO ELINE begin:���e�[�Z�i��|��STIME ETIME���t�����p�A�n�٭즨��ڵo�ͪ�����ɶ�
		String WORKDATE = SDATE;
		EDATE = SDATE;
		if(STIME.startsWith("-"))
		{
			//20170608 DRACO ELINE begin:�ק�W�h�A��B4��METHOD�p������ܪ�����ɶ�
			String[] showtime = hr.B04phadd.DBToShowDateTime(WORKDATE , "0000" , STIME , ETIME);
			//SDATE = showtime[0].trim();//�n������ۤv��ʥӽФ@�ˡA�ҥH�������
			STIME = showtime[1].trim();
			//EDATE = showtime[2].trim();//�n������ۤv��ʥӽФ@�ˡA�ҥH�������
			ETIME = showtime[3].trim();
			//20170608 DRACO ELINE begin:�ק�W�h�A��B4��METHOD�p������ܪ�����ɶ�

			setValue("BEFORE_WORK" , "1");
		}
		//20170503 DRACO ELINE end:���e�[�Z�i��|��STIME ETIME���t�����p�A�n�٭즨��ڵo�ͪ�����ɶ�


		String CANYN="";
		//if(CPNYID.trim().equals("AIC")){
		if(true){
			String RRT[][] = t.queryFromPool("select isnull(CANYN,'N') from HOLIDAY where HDATE = '"+SDATE+"' and CPNYID='"+CPNYID+"'");
			if(RRT.length==0){
				RRT = t.queryFromPool("select isnull(CANYN,'N') from HOLIDAY where HDATE = '"+SDATE+"' and (CPNYID='' or CPNYID is null)");
			}
			if(RRT.length!=0){
				CANYN=RRT[0][0].trim();
				if(RRT[0][0].trim().equals("N")){
					//message(translate("��w����]�w�����i�ӽХ[�Z"),"history.back()");
					//	20190329 MAX COULSON BEGIN:��½Ķ
					message(hr.Msg.hrm8w_B32.Msg010,BACK);
					//	20190329 MAX COULSON END:��½Ķ
					return value;
				}
			}
		}

		Hashtable HOL  = (Hashtable)get("SYSTEM.HOLIDAY");
		if(HOL==null){
			HOL = new Hashtable();
		}
		String HOLYN = (String)HOL.get(CLASS.trim());
		setValue("WORKCLASS",CLASS);
		//20130925 BLUE �s�W�Z�O
		sql = "select CLNAME from CLASSSET where CLASS = '"+convert.ToSql(CLASS)+"' ";
		String[][] CLASSSET = t.queryFromPool(sql);
		if(CLASSSET.length!=0){
			setValue("WORKCLASS1",CLASS+" "+CLASSSET[0][0]);
		}else{
			setValue("WORKCLASS1",CLASS);
		}
		String SET110 = "";
		String[][] dtSALPARA = common5.getSALPARA_SET110(t,EMPID,"SET1,SET2");
		if(dtSALPARA.length > 0){
			SET110 = dtSALPARA[0][(dtSALPARA[0].length - 2)];
		}
		String B6_FLEHR = (String)get("HRCONFIG_B6_FLEHR_" + CPNYID + "." + SET110);
		if(B6_FLEHR == null){
			B6_FLEHR = (String)get("HRCONFIG_B6_FLEHR_" + CPNYID);
			if(B6_FLEHR == null){
				B6_FLEHR = (String)get("HRCONFIG_B6_FLEHR");
			}
		}
		//System.err.println("==============="+B6_FLEHR);
		
		//20160129 LUCAS ELINE begin:�[�Z�樫�u�ʳW�h
		if("Y".equals(B6_FLEHR)){
			String SET12 = "4";
			String SET13 = "8";
			String WITH_CARDIO = "";
			String[][] dtKSYSTEM = hr.common5.getKSYSTEM(t,EMPID,"SET11,SET12,SET13");
			try{
				WITH_CARDIO = dtKSYSTEM[0][0];
				SET12 = dtKSYSTEM[0][1];
				SET13 = dtKSYSTEM[0][2];
			} catch(Exception e){
				System.err.println("�Ҷ԰򥻳]�w����줹�\�����Ʀ��~�A�ϥιw�]�� 0 ����");
			}
			
			String SDATE_S1 = datetime.dateAdd(SDATE,"d",-1);
			String EDATE_S1 = datetime.dateAdd(SDATE,"d",1);
			Hashtable htPROCEED = new Hashtable();
			try{
				sql = " select YYMM , K1 , K2 , K3 , K4 , K5 , K6 , K7 , K8 , K9 , K10 , K11 , K12 , K13 , K14 , K15 , K16 , K17 , K18 , K19 , K20"
					+ " , K21 , K22 , K23 , K24 , K25 , K26 , K27 , K28 , K29 , K30 , K31 , "
					+ " PRTYPE"
					+ " from PROCEED where YYMM between '"+(Integer.parseInt(SDATE_S1)/100)+"' and '"+(Integer.parseInt(EDATE_S1)/100)+"'";
				String[][] dtPROCEED = t.queryFromPool(sql,10);
				if(dtPROCEED.length == 0){
					dtPROCEED = common5.getPROCEED_by_virtual(t,SDATE_S1.substring(0,6) + "01",EDATE_S1.substring(0,6) + "31",EMPID);
				}
					
				for (int x = 0; x < dtPROCEED.length; x++) {
					htPROCEED.put(dtPROCEED[x][0]+"@"+dtPROCEED[x][32],dtPROCEED[x]);
				}
			}catch(Exception e){

			}

			//	���o�d�ߴ������Z�O
			Hashtable htCLASS = new Hashtable();
			sql = " select CLASSDA ,CLASS "
				+ " from CLASSDA "
				+ " where EMPID = '" + convert.ToSql(EMPID) + "'";
			String[][] dtCLASSDA = t.queryFromPool(sql);
			for(int i = 0 ; i < dtCLASSDA.length ; i++){
				htCLASS.put(dtCLASSDA[i][0].trim(),dtCLASSDA[i][1].trim());
			}

			sql = " select EMPID , CLASSDA , OCLASS , NCLASS "
				+ " from WORKCARDCHG "
				+ " where EMPID = '" + EMPID + "' "
				+ " order by EMPID , CLASSDA";
			String[][] dtWORKCARDCHG = t.queryFromPool(sql);	//	�Z�O�մ��O��
			//20160223 JENNY CINDY:�ǤJ�Z�O�n�έӤH�Z�O,���M�|�X�{���ݯZ�O �L�_���ɶ�
			//Vector vCARD_FS = common5.CARD_DATA(t,EMPID,CLASS,SDATE_S1,EDATE_S1,htCLASSSET,htPROCEED,htCLASS,SET12,SET13,WITH_CARDIO,new Hashtable(),null,false,false,dtWORKCARDCHG);
			Vector vCARD_FS = common5.CARD_DATA(t,EMPID,USERCLASS,SDATE_S1,EDATE_S1,htCLASSSET,htPROCEED,htCLASS,SET12,SET13,WITH_CARDIO,new Hashtable(),null,false,false,dtWORKCARDCHG);
			
			String IN_CARD = ETIME;
			String OUT_CARD = STIME;
			if(vCARD_FS != null && vCARD_FS.size() > 1){
				String[] arrCARD_INFO = (String[])vCARD_FS.elementAt(1);
				IN_CARD = arrCARD_INFO[0];
				OUT_CARD = arrCARD_INFO[1];
			}
			int SHIFT_MINUTE = 0;	//	�u�ʤ��\������
			try{
				if(dtSALPARA[0][1].length() > 0){
					SHIFT_MINUTE = Integer.parseInt(dtSALPARA[0][1]);
				}
				if(SHIFT_MINUTE > 10000){
					SHIFT_MINUTE = SHIFT_MINUTE % 10000;
				}
			}catch(Exception e){
				System.err.println("���]�w���u�ʤ��\�����Ʀ��~�A�ϥιw�]�� 0 ����");
			}
			
			String WORKHOUR = "8";
			
			
			try{
				String CSTIME = arrCLASSSET[2].trim();
				String CETIME = arrCLASSSET[3].trim();
				String RSTIME1 = arrCLASSSET[4];
				String RETIME1 = arrCLASSSET[5];
				String RSTIME2 = arrCLASSSET[11];
				String RETIME2 = arrCLASSSET[12];
				String RSTIME3 = arrCLASSSET[13];
				String RETIME3 = arrCLASSSET[14];
				String RSTIME4 = arrCLASSSET[15];
				String RETIME4 = arrCLASSSET[16];
				WORKHOUR = arrCLASSSET[10];
				
				String workhour_min = operation.mul(WORKHOUR,"60");
				
				String rest_min = "0";
				int res_min1 = 0;
				int res_min2 = 0;
				//	��X�𮧮ɶ��b�o�ӯZ�O�e�h�֤�����
				if(CSTIME.length() == 4 && CETIME.length() == 4){
					if(RSTIME1.length() == 4 && RETIME1.length() == 4){
						if(operation.compareTo(CSTIME,RSTIME1) < 0 && operation.compareTo(RETIME1,CETIME) < 0){
							res_min1 = Integer.parseInt(RSTIME1.substring(0,2)) * 60 + Integer.parseInt(RSTIME1.substring(2,4));
							res_min2 = Integer.parseInt(RETIME1.substring(0,2)) * 60 + Integer.parseInt(RETIME1.substring(2,4));
							rest_min = operation.add(rest_min,String.valueOf(res_min2 - res_min1));
						}
					}else if(RSTIME2.length() == 4 && RETIME2.length() == 4){
						if(operation.compareTo(CSTIME,RSTIME2) < 0 && operation.compareTo(RETIME2,CETIME) < 0){
							res_min1 = Integer.parseInt(RSTIME2.substring(0,2)) * 60 + Integer.parseInt(RSTIME2.substring(2,4));
							res_min2 = Integer.parseInt(RETIME2.substring(0,2)) * 60 + Integer.parseInt(RETIME2.substring(2,4));
							rest_min = operation.add(rest_min,String.valueOf(res_min2 - res_min1));
						}
					}else if(RSTIME3.length() == 4 && RETIME3.length() == 4){
						if(operation.compareTo(CSTIME,RSTIME3) < 0 && operation.compareTo(RETIME3,CETIME) < 0){
							res_min1 = Integer.parseInt(RSTIME3.substring(0,2)) * 60 + Integer.parseInt(RSTIME3.substring(2,4));
							res_min2 = Integer.parseInt(RETIME3.substring(0,2)) * 60 + Integer.parseInt(RETIME3.substring(2,4));
							rest_min = operation.add(rest_min,String.valueOf(res_min2 - res_min1));
						}
					}else if(RSTIME4.length() == 4 && RETIME4.length() == 4){
						if(operation.compareTo(CSTIME,RSTIME4) < 0 && operation.compareTo(RETIME4,CETIME) < 0){
							res_min1 = Integer.parseInt(RSTIME4.substring(0,2)) * 60 + Integer.parseInt(RSTIME4.substring(2,4));
							res_min2 = Integer.parseInt(RETIME4.substring(0,2)) * 60 + Integer.parseInt(RETIME4.substring(2,4));
							rest_min = operation.add(rest_min,String.valueOf(res_min2 - res_min1));
						}
					}
				}
				
				if(CSTIME.length() == 4 && CETIME.length() == 4){
					//System.err.println("=====���`�u�ɥ𮧮ɶ�=>" + rest_min);

					String elasticity_cstime = common5.getCalTime(SDATE,CSTIME,SHIFT_MINUTE + "","m","HHmm");
					String elasticity_cstime2 = common5.getCalTime(SDATE,CSTIME,-SHIFT_MINUTE + "","m","HHmm");
					//System.err.println("=====���`�u�ɼu�ʤW�Z�ɶ�=>"+elasticity_cstime + "~" + elasticity_cstime2);
					String elasticity_cetime = common5.getCalTime(SDATE,CETIME,SHIFT_MINUTE + "","m","HHmm");
					String elasticity_cetime2 = common5.getCalTime(SDATE,CETIME,-SHIFT_MINUTE + "","m","HHmm");
					//System.err.println("=====���`�u�ɼu�ʤU�Z�ɶ�=>"+elasticity_cetime + "~" + elasticity_cetime2);

					String elasticity_in_crad = IN_CARD;
					if(elasticity_in_crad.length() != 4){
						//System.err.println("=====�Ĥ@�i�d��������|�X�A�̷ӤW�Z�ɶ��_�l��@�i�d�ɶ�=>"+elasticity_cstime);
						elasticity_in_crad = CSTIME;
					}
					

					//System.err.println("=====�Ĥ@�i�d���ɶ���=>"+elasticity_in_crad);
					if(operation.compareTo(elasticity_cstime,elasticity_in_crad) > 0){
						//System.err.println("=====�Ĥ@�i�d���ɶ�����u�ʤW�Z�ɶ�=>"+elasticity_in_crad + " < " + elasticity_cstime + " �G�N��u�ʤW�Z�C��" + elasticity_cstime);
						elasticity_in_crad = elasticity_cstime;
					}else if(operation.compareTo(elasticity_in_crad,elasticity_cstime2) > 0){
						//System.err.println("=====�Ĥ@�i�d���ɶ��ߩ�u�ʤW�Z�ɶ�=>"+elasticity_in_crad + " < " + elasticity_cstime2 + " �G�N��u�ʤW�Z�C��" + elasticity_cstime2);
						elasticity_in_crad = elasticity_cstime2;
					}
					STIME = common5.getCalTime(SDATE,elasticity_in_crad,operation.mul(workhour_min,"-1") + "","m","HHmm");
					STIME = common5.getCalTime(SDATE,STIME,operation.mul(rest_min,"-1") + "","m","HHmm");
					ETIME = OUT_CARD;
				}
			}catch(Exception e){
				e.printStackTrace(System.err);
			}
		}
		//20160129 LUCAS ELINE end:�[�Z�樫�u�ʳW�h

		//20160223 JENNY CINDY:�ǤJ�Z�O�n�έӤH�Z�O,���M�|�X�{���ݯZ�O �L�_���ɶ�
		

		if ( bEncryptGrade ){
			GRADE = decrypt("hr" , GRADE).trim();
		}
		//20190527 DANIEL PATRICK begin:���O�Ʀr�N��0
		try{
			GRADE = ""+Long.parseLong(GRADE);
		}catch(Exception e){
			GRADE = "0";
		}
		//20190527 DANIEL PATRICK end:���O�Ʀr�N��0
		if(CPNYID.length()==4 && CPNYID.startsWith("YT")){
			setValue("MEMOES","�y"+translate("�{¾��줣����[�Z���ɡA���I�覡�@�ߥH�w�B�䴩�z�K���I")+"�z");
		}
		else{
			setValue("MEMOES","");
		}

		setValue("EMPID",EMPID);
		setValue("HECNAME",HECNAME);
		setValue("POSSIE",POSSIE);
		//20151218 JILL ELINE : �p�G���]�w��ܮ榡,�|��ȧﱼ,�ҥH�h�[�������Ӧs��CPNYID
		setValue("CPNYID",CPNYID);//����ƥ�
		setValue("CPNYID1",CPNYID);//��ܥ�
		setValue("NDEPT",DEPT_NO);
		
		//20180209 DRACO ELINE begin:�q�ɤu����Ӫ��w�w�[�Z�]�n��[�Z���a�W
		//setValue("WDEPT",DEPT_NO);
		//common5.setNameByDept(this,t,"WDEPT",DEPT_NO);
		if(WDEPT.length()>0)
		{
			setValue("WDEPT" , WDEPT);
			common5.setNameByDept(this , t , "WDEPT" , WDEPT);
		}
		else
		{
			setValue("WDEPT",DEPT_NO);
			common5.setNameByDept(this,t,"WDEPT",DEPT_NO);
		}
		//20180209 DRACO ELINE end:�q�ɤu����Ӫ��w�w�[�Z�]�n��[�Z���a�W
		
		if(SHHMM.length()!=0){
			setValue("SHHMM",SHHMM.substring(0,2)+":"+SHHMM.substring(2,4));
		}
		else{
			setValue("SHHMM","");
		}
		if(EHHMM.length()!=0){
			setValue("EHHMM",EHHMM.substring(0,2)+":"+EHHMM.substring(2,4));
		}
		else{
			setValue("EHHMM","");
		}
		if(DATELIST.trim().equals("A")){
			setValue("SDATE",convert.ac2roc(SDATE));
			setValue("WORKDATE",convert.ac2roc(WORKDATE));
			setValue("EDATE",convert.ac2roc(EDATE));
			setValue("PSDATE",convert.ac2roc(SDATE));
			setValue("PEDATE",convert.ac2roc(EDATE));
		}
		else{
			setValue("SDATE",SDATE);
			setValue("WORKDATE",SDATE);
			setValue("EDATE",EDATE);
			setValue("PSDATE",SDATE);
			setValue("PEDATE",EDATE);
		}
		
		setValue("STIME",STIME);
		setValue("ETIME",ETIME);

		//20161020 LUCAS ELINE begin:�W�[��ܷ��w�ֿn�[�Z�ɼ�(���t����):N�p��
		//20200221 DANIEL PATRICK begin:���ӥ��d�ӤH���էO�~�ݪťղէO
		//{
		sql = " select YYMM , SDATE , EDATE "
			+ " from ATTEND_DATE "
			+ " where 1 = 1 "
			+ common5.getBetweenSEDATE("SDATE" , "EDATE" , SDATE , SDATE)
			+ " and isnull(UTYPE,'" + ISNULL + "') = '" + convert.ToSql(SET107) + "'"
			+ " and CPNYID='"+convert.ToSql(CPNYID)+"'"//20170727 DRACO add CPNYID
			+ "";
		ret = t.queryFromPool(sql,180);
		if(ret.length == 0){
		//20200221 DANIEL PATRICK end:���ӥ��d�ӤH���էO�~�ݪťղէO
			sql = " select YYMM , SDATE , EDATE "
				+ " from ATTEND_DATE "
				+ " where 1 = 1 "
				+ common5.getBetweenSEDATE("SDATE" , "EDATE" , SDATE , SDATE)
				+ " and isnull(UTYPE,'" + ISNULL + "') = '" + ISNULL + "'"
				+ " and CPNYID='"+convert.ToSql(CPNYID)+"'"//20170727 DRACO add CPNYID
				+ "";
			ret = t.queryFromPool(sql,180);
		}
		
		if(ret.length == 0){
			//message("�d�L�Ӥ���Ҷ԰϶����",BACK);
			//	20190329 MAX COULSON BEGIN:��½Ķ
			message(hr.Msg.hrm8w_B32.Msg011,BACK);
			//	20190329 MAX COULSON END:��½Ķ
			return value;
		}
		String ATTEND_SDATE = ret[0][1].trim();
		String ATTEND_EDATE = ret[0][2].trim();
		sql = " select sum(AMT) from OVERTIME "
			+ " where 1 = 1 "
			+ common5.getBetweenSEDATE("SDATE" , "EDATE" , ATTEND_SDATE , ATTEND_EDATE)
			+ " and EMPID = '" + convert.ToSql(EMPID) + "'";
		ret = t.queryFromPool(sql,30);
		String APPLY_HOURS = "0";
		if(ret.length > 0){
			APPLY_HOURS = ret[0][0].trim();
		}
		setValue("APPLY_HOURS",APPLY_HOURS);

		//20161020 LUCAS ELINE end:�W�[��ܷ��w�ֿn�[�Z�ɼ�(���t����):N�p��

		setValue("PLAN_STIME",STIME);//20160517 DRACO add �t�O�ӽйw�w�[�Z�ɬq
		setValue("PLAN_ETIME",ETIME);//20160517 DRACO add �t�O�ӽйw�w�[�Z�ɬq
		
		setValue("PSTIME",STIME);
		setValue("PETIME",ETIME);
		setValue("NOTE",NOTE);
		setValue("PLANNOTE",NOTE);
		setValue("EATYN","Y");
		setValue("OLDOVER",OLDOVER);
		String MIN_HOUR="1"; //����[�Z�̤p�ɼ�
		String ADD_HOUR="1"; //����[�Z���W�ɼ�
		String CARDIO_FLAG="N";
		String K_SET9=""; //�[�Z���ɦh�֤Ѥ��ӽ�(�ťթ�0���ˮ�)
		String K_SET10=""; //�[�Z���v�覡(����)
		String K_SET32=""; //�[�Z���v�覡(����)
		String K_SET27=""; //�[�Z�ɥ�i�w�ɪ��ɼ�
		String K_SET28=""; //�[�Z�ɥ�W���ɼ�
		String K_SET35=""; //�[�Z���v�覡(����)
		String K_SET36=""; //�[�Z���v�覡(����)
		String K_SET50=""; //�[�Z���\�覡(����)
		String K_SET51=""; //�[�Z���\�覡(����)
		String K_SET34=""; //�[�Z��i�_�ۦ���w�z�K���Z�O
		String VAOVER1 = "1"; //����[�Z�̤p�ɼ�
		String VAOVER2 = "1"; //����[�Z���W�ɼ�
		
		String K_HSET32 = ""; //�[�Z���v�覡(�갲) 20161221 DRACO add
		String K_HSET36 = ""; //N¾���H�W(�t)(�갲) 20161221 DRACO add
		String K_OSET32 = ""; //�[�Z���v�覡(�𮧤�) 20161221 DRACO add
		String K_OSET36 = ""; //N¾���H�W(�t)(�𮧤�) 20161221 DRACO add
		String K_OVERSET = "";//20170807 DRACO add
		
		String K_SET54 = "";//20180308 DRACO add �[�Z���v�覡(�K�X�Ԥ�)
		String K_SET55 = "";//20180308 DRACO add N¾���H�W�ɥ�覡�]�w(�K�X�Ԥ�)
		
		//String KSM[][] = common5.getKSYSTEM(t,EMPID,"SET5,SET4,SET11,SET10,SET27,SET28,SET32,SET35,SET36,SET9,SET50,SET51,SET34,VAOVER1,VAOVER2 , HSET32 , HSET36 , OSET32 , OSET36");
		//String KSM[][] = hr.common5.getKSYSTEM(t,EMPID,"SET5,SET4,SET11,SET10,SET27,SET28,SET32,SET35,SET36,SET9,SET50,SET51,SET34 , HSET32 , HSET36 , OSET32 , OSET36");
		String KSMFIELDS = "SET5"//0
			+ " , SET4 , SET11 , SET10 , SET27 , SET28"//1-5
			+ " , SET32 , SET35 , SET36 , SET9 , SET50"//6-10
			+ " , SET51 , SET34 , VAOVER1 , VAOVER2 , HSET32"//11-15
			+ " , HSET36 , OSET32 , OSET36"//16-18
			+ " , OVERSET"//19 20170807 DRACO add
			+ " , SET54 , SET55"//20-21 20180308 DRACO add
			+"";
		String KSM[][] = hr.common5.getKSYSTEM(t , EMPID , KSMFIELDS);
		if(KSM.length!=0){
			MIN_HOUR=KSM[0][0].trim();
			ADD_HOUR=KSM[0][1].trim();
			CARDIO_FLAG=KSM[0][2].trim();
			K_SET10=KSM[0][3].trim();
			K_SET27=KSM[0][4].trim();
			K_SET28=KSM[0][5].trim();
			K_SET32=KSM[0][6].trim();
			K_SET35=KSM[0][7].trim();
			K_SET36=KSM[0][8].trim();
			K_SET9=KSM[0][9].trim();
			K_SET50=KSM[0][10].trim();
			K_SET51=KSM[0][11].trim();
			K_SET34=KSM[0][12].trim();
			VAOVER1=KSM[0][13].trim();
			VAOVER2=KSM[0][14].trim();
			K_HSET32 = KSM[0][15].trim();//20161221 DRACO add
			K_HSET36 = KSM[0][16].trim();//20161221 DRACO add
			K_OSET32 = KSM[0][17].trim();//20161221 DRACO add
			K_OSET36 = KSM[0][18].trim();//20161221 DRACO add
			K_OVERSET = KSM[0][19].trim();//20170807 DRACO add
			
			K_SET54 = KSM[0][20].trim();//20180308 DRACO add �[�Z���v�覡(�K�X�Ԥ�)
			K_SET55 = KSM[0][21].trim();//20180308 DRACO add N¾���H�W�ɥ�覡�]�w(�K�X�Ԥ�)
		}

		//20161221 DRACO ELINE begin:CLASSSET.OTYPE��A�ɡASET32�n��OSET32�ASET36�n��OSET36�FCLASSSET.OTYPE��B�A�B�Z�O��H�}�Y�ɡASET32�n��HSET32�ASET36�n��HSET36
		setVisible("OVERSET" , false);//20170807 DRACO add
		if(operation.compareTo(SDATE , "20161223")>=0)
		{
			if("Y".equals(K_OVERSET))
			{
				setVisible("OVERSET" , true);//20170807 DRACO add
				setValue("OVERSET" , "Y");//20171026 DRACO ELINE begin:�Y�n��ܸ����A�w�]�Ŀ�
			}
			
			if("A".equals(CLASS_OTYPE))
			{
				if(K_OSET32.length()>0)
				{
					K_SET32 = K_OSET32;
				}
				if(K_OSET36.length()>0)
				{
					K_SET36 = K_OSET36;
				}
			}
			//20180308 DRACO CINDY begin:C�K�X�Ԥ�t�~�P�_
			else if("C".equals(CLASS_OTYPE))
			{
				if(K_SET54.length()>0)
				{
					K_SET32 = K_SET54;
				}
				if(K_SET55.length()>0)
				{
					K_SET36 = K_SET55;
				}
			}
			//20180308 DRACO CINDY end:C�K�X�Ԥ�t�~�P�_
			//else if("B".equals(CLASS_OTYPE) && CLASS.startsWith("H"))
			if(CLASS.startsWith("H"))//20170106 DRACO ELINE begin:�令�u�P�_�Z�O
			{	
				if(K_HSET32.length()>0)
				{
					K_SET32 = K_HSET32;
				}
				if(K_HSET36.length()>0)
				{
					K_SET36 = K_HSET36;
				}
			}
		}
		//20161221 DRACO ELINE end:CLASSSET.OTYPE��A�ɡASET32�n��OSET32�ASET36�n��OSET36�FCLASSSET.OTYPE��B�A�B�Z�O��H�}�Y�ɡASET32�n��HSET32�ASET36�n��HSET36
				
		if("MOBILE_B5".equals(FUNCTIONID)){
			setEditable("RCLASS",K_SET34.equals("Y"));
			setEditable("EATUNIT",false);
		}else{
			setVisible("RCLASS",K_SET34.equals("Y"));
			setVisible("EATUNIT",false);
		}

		{
			String DEFAULT_EATYN=K_SET51;
			if(HOLYN==null){  //����
				DEFAULT_EATYN=K_SET50;
			}else{ //20131226 BLUE �p�G�����  ��̤p�ɼ� �M ���W�ɼ� �אּ����
				MIN_HOUR = VAOVER1;  //����[�Z�̤p�ɼ�
				ADD_HOUR = VAOVER2; //����[�Z���W�ɼ�
			}
			if(DEFAULT_EATYN.equals("")) DEFAULT_EATYN="A";
			//20181107 DANIEL PATRICK begin:����O����O�� setEditable
			boolean bEATUNIT = false;
			if(DEFAULT_EATYN.equals("A")){
				setValue("EATYN","1");
				setEditable("EATYN",false);
				setVisible("EATUNIT",false);
			} else if(DEFAULT_EATYN.equals("B")){
				setValue("EATYN","1");
				setEditable("EATYN",true);
				setVisible("EATUNIT",true);
				bEATUNIT = true;
			} else if(DEFAULT_EATYN.equals("C")){
				setValue("EATYN","0");
				setEditable("EATYN",false);
				setVisible("EATUNIT",false);
			} else if(DEFAULT_EATYN.equals("D")){
				setValue("EATYN","0");
				setEditable("EATYN",true);
				setVisible("EATUNIT",true);
				bEATUNIT = true;
			}
			if("MOBILE_B5".equals(FUNCTIONID)){
				setEditable("EATUNIT",bEATUNIT);
			}
			//20181107 DANIEL PATRICK end:����O����O�� setEditable

		}

		//System.out.println("SET14==>"+SET14);
		/*
			if(SET14.trim().equals("A") || SET14.trim().equals("B")){
				setVisible("HPRICE",false);
				setVisible("REPRICE",false);
				setValue("OTYPE",SET14.trim());
				setEditable("OTYPE",false);
			}
			else{
				System.out.println("K_SET10==>"+K_SET10);
				System.out.println("K_SET32==>"+K_SET32);
				if(HOLYN==null){
					if(K_SET10.equals("A") || K_SET10.equals("B")){
						setValue("OTYPE",K_SET10);
						setEditable("OTYPE",false);
					}
					else{
						setEditable("OTYPE",true);
						setValue("OTYPE","B");
					}
					if(K_SET10.equals("D")){
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					} else {
						setVisible("HPRICE",false);
						setVisible("REPRICE",false);
					}
				}
				else{
					if(K_SET32.equals("A") || K_SET32.equals("B")){
						setValue("OTYPE",K_SET32);
						setEditable("OTYPE",false);
					}
					else{
						setEditable("OTYPE",true);
						setValue("OTYPE","B");
					}
					if(K_SET32.equals("D")){
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					} else {
						setVisible("HPRICE",false);
						setVisible("REPRICE",false);
					}
				}
			}
		*/
		//A.�u���ɥ�
		//B.�u�����~
		//C.�G�̿�@
		//D.�G�̬ҥi(�ӽФH�ۦ���t�ɼ�)
		//E.N¾���H�W�u���ɥ�,��l�u�����~
		//F.N¾���H�W�u���ɥ�,��l�G�̿�@
		//G.N¾���H�W�u���ɥ�,��l�G�̬ҥi
		//H.N¾���H�W�u�����~,��l�u���ɥ�
		//I.N¾���H�W�u�����~,��l�G�̿�@
		//J.N¾���H�W�u�����~,��l�G�̬ҥi
		//K.N¾���H�W�G�̿�@,��l�u���ɥ�
		//L.N¾���H�W�G�̿�@,��l�u�����~
		//M.N¾���H�W�G�̿�@,��l�G�̬ҥi
		//N.N¾���H�W�G�̬ҥi,��l�u���ɥ�
		//O.N¾���H�W�G�̬ҥi,��l�u�����~
		//P.N¾���H�W�G�̬ҥi,��l�G�̿�@
		//Q.�G���ҵ�(For�V�n) (20151130 JENNY new)
		//System.out.println("SET14==>"+SET14);

		boolean GRADE_BIGGER_THAN_K_SET35=false;
		try{
			Long.parseLong(GRADE);
			Long.parseLong(K_SET35);
			GRADE_BIGGER_THAN_K_SET35=(operation.compareTo(GRADE,K_SET35)>=0);
		} catch(Exception e){
			GRADE_BIGGER_THAN_K_SET35=(GRADE.compareTo(K_SET35)>=0);
		}
		boolean GRADE_BIGGER_THAN_K_SET36=false;
		try{
			Long.parseLong(GRADE);
			Long.parseLong(K_SET36);
			GRADE_BIGGER_THAN_K_SET36=(operation.compareTo(GRADE,K_SET36)>=0);
		} catch(Exception e){
			GRADE_BIGGER_THAN_K_SET36=(GRADE.compareTo(K_SET36)>=0);
		}

		// 20200922 JAY PATRICK begin �P�_OVERTIME_TYPE���w�]��
		boolean bOVERTIME_TYPE = "Y".equals((String)get("HRCONFIG_OVERTIME_TYPE"));
		// 20200922 JAY PATRICK end �P�_OVERTIME_TYPE���w�]��

		boolean bOTYPE_D = false;//20151228 JENNY:�O�_�� �G�̬ҥi�����
		boolean bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
		if(SET14.trim().equals("A") || SET14.trim().equals("B")){
			if("MOBILE_B5".equals(FUNCTIONID)){
				setEditable("HPRICE",false);
				setEditable("REPRICE",false);
			}else{
				setVisible("HPRICE",false);
				setVisible("REPRICE",false);
			}
			setValue("OTYPE",SET14.trim());
			setEditable("OTYPE",false);
			bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
		}
		else{
			//System.out.println("K_SET10==>"+K_SET10);
			//System.out.println("K_SET32==>"+K_SET32);
			//System.out.println("K_SET35==>"+K_SET35);
			//System.out.println("K_SET36==>"+K_SET36);
			if(HOLYN==null){
				//20181126 DANIEL PATRICK begin:Q ����
				if("Q".equals(K_SET10)){
					Vector v1=new Vector();
					Vector v2=new Vector();
					v1.addElement(translate("����"));
					v2.addElement("C");
					setReference("OTYPE",v1,v2);
				}
				//20181126 DANIEL PATRICK end:Q ����
				if(K_SET10.equals("A") || K_SET10.equals("B")){
					OTYPE = K_SET10;
					setEditable("OTYPE",false);
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
				}
				else if(K_SET10.equals("C") || K_SET10.equals("D")){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					//20180412 MAX PATRICK BEGIN:�w�]�令B4�ӽЮɿ�ܪ�OTYPE
					//OTYPE = "B";
					//20180412 MAX PATRICK END:�w�]�令B4�ӽЮɿ�ܪ�OTYPE

					// 20200922 JAY PATRICK begin �P�_OVERTIME_TYPE���w�]��
					if("C".equals(K_SET10) && bOVERTIME_TYPE){
						OTYPE = "A";
					}
					// 20200922 JAY PATRICK end �P�_OVERTIME_TYPE���w�]��
				}
				else if( (K_SET10.equals("E")||K_SET10.equals("F")||K_SET10.equals("G")) && GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "A";//�u���ɥ�
					setEditable("OTYPE",false);
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
				}
				else if( (K_SET10.equals("H")||K_SET10.equals("I")||K_SET10.equals("J")) && GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "B";//�u�����~
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("K")||K_SET10.equals("L")||K_SET10.equals("M")||K_SET10.equals("N")||K_SET10.equals("O")||K_SET10.equals("P")) && GRADE_BIGGER_THAN_K_SET35){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else if( (K_SET10.equals("H")||K_SET10.equals("K")||K_SET10.equals("N")) && !GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "A";//�@�Z�H�u���ɥ�
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("E")||K_SET10.equals("L")||K_SET10.equals("O")) && !GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "B";//�@�Z�H�u�����~
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("F")||K_SET10.equals("G")||K_SET10.equals("I")||K_SET10.equals("J")||K_SET10.equals("M")||K_SET10.equals("P")) && !GRADE_BIGGER_THAN_K_SET35){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else{
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				
				if(K_SET10.equals("D")){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
						//20191231 ERIN PATRICK begin�G�`�p�ɥ�ɼơB�`�p���~�ɼƤ��\�s��
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
						//20191231 ERIN PATRICK end�G�`�p�ɥ�ɼơB�`�p���~�ɼƤ��\�s��
					}
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				else if( (K_SET10.equals("N")||K_SET10.equals("O")||K_SET10.equals("P")) && GRADE_BIGGER_THAN_K_SET35){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				else if( (K_SET10.equals("G")||K_SET10.equals("J")||K_SET10.equals("M")) && !GRADE_BIGGER_THAN_K_SET35){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
				}
				//20151130 JENNY ELINE: Q.�G���ҵ� �]�����~�ɥ𳣵�,�ҥHOTYPE������ C �B
				else if("Q".equals(K_SET10)){
					OTYPE = "C";
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
					setEditable("HPRICE",false);
					setEditable("REPRICE",false);
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				else {
					if("MOBILE_B5".equals(FUNCTIONID)){
						setEditable("HPRICE",false);
						setEditable("REPRICE",false);
					}else{
						setVisible("HPRICE",false);
						setVisible("REPRICE",false);
					}
				}
			}
			else{
				if(CANYN.equals("A")||CANYN.equals("B")||CANYN.equals("C")||CANYN.equals("D")){
					//message("","window.status='12345';");
					K_SET32=CANYN;
				}

				//20181126 DANIEL PATRICK begin:Q ����
				if("Q".equals(K_SET32)){
					Vector v1=new Vector();
					Vector v2=new Vector();
					v1.addElement(translate("����"));
					v2.addElement("C");
					setReference("OTYPE",v1,v2);
				}
				//20181126 DANIEL PATRICK end:Q ����

				if(K_SET32.equals("A") || K_SET32.equals("B")){
					OTYPE = K_SET32;
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if(K_SET32.equals("C") || K_SET32.equals("D")){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					//20180412 MAX PATRICK BEGIN:�w�]�令B4�ӽЮɿ�ܪ�OTYPE
					//OTYPE = "B";
					//20180412 MAX PATRICK END:�w�]�令B4�ӽЮɿ�ܪ�OTYPE

					// 20200922 JAY PATRICK begin �P�_OVERTIME_TYPE���w�]��
					if("C".equals(K_SET32) && bOVERTIME_TYPE){
						OTYPE = "A";
					}
					// 20200922 JAY PATRICK end �P�_OVERTIME_TYPE���w�]��
				}
				else if( (K_SET32.equals("E")||K_SET32.equals("F")||K_SET32.equals("G")) && GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "A";//�u���ɥ�
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("H")||K_SET32.equals("I")||K_SET32.equals("J")) && GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "B";//�u�����~
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("K")||K_SET32.equals("L")||K_SET32.equals("M")||K_SET32.equals("N")||K_SET32.equals("O")||K_SET32.equals("P")) && GRADE_BIGGER_THAN_K_SET36){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else if( (K_SET32.equals("H")||K_SET32.equals("K")||K_SET32.equals("N")) && !GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "A";//�@�Z�H�u���ɥ�
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("E")||K_SET32.equals("L")||K_SET32.equals("O")) && !GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "B";//�@�Z�H�u�����~
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("F")||K_SET32.equals("G")||K_SET32.equals("I")||K_SET32.equals("J")||K_SET32.equals("M")||K_SET32.equals("P")) && !GRADE_BIGGER_THAN_K_SET36){
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else{
					bOTYPE_edit = true;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				
				if(K_SET32.equals("D")){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
						//20191231 ERIN PATRICK begin�G�`�p�ɥ�ɼơB�`�p���~�ɼƤ��\�s��
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
						//20191231 ERIN PATRICK end�G�`�p�ɥ�ɼơB�`�p���~�ɼƤ��\�s��
					}
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				else if( (K_SET32.equals("N")||K_SET32.equals("O")||K_SET32.equals("P")) && GRADE_BIGGER_THAN_K_SET36){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
				}
				else if( (K_SET32.equals("G")||K_SET32.equals("J")||K_SET32.equals("M")) && !GRADE_BIGGER_THAN_K_SET36){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				//20151130 JENNY ELINE: Q.�G���ҵ� �]�����~�ɥ𳣵�,�ҥHOTYPE������ C �B
				else if("Q".equals(K_SET32)){
					OTYPE = "C";
					bOTYPE_edit = false;//20160114 JENNY:���I�覡�i�_�s��
					setEditable("OTYPE",false);
					setEditable("HPRICE",false);
					setEditable("REPRICE",false);
					bOTYPE_D = true;//20151228 JENNY:�O�_�� �G�̬ҥi�����
				}
				else {
					if("MOBILE_B5".equals(FUNCTIONID)){
						setEditable("HPRICE",false);
						setEditable("REPRICE",false);
					}else{
						setVisible("HPRICE",false);
						setVisible("REPRICE",false);
					}
				}
			}
		}
		
		//20151228 JENNY ELINE begin:�p�G�OC �令�u�� �ɥ�ε��~
		Vector v1=new Vector();
		Vector v2=new Vector();
		v1.addElement(translate("�ɥ�"));
		v1.addElement(translate("���~"));
		v2.addElement("A");
		v2.addElement("B");
		//20160114 JENNY:���I�覡�i�_�s��
		//if(bOTYPE_D){
		if(bOTYPE_D || !bOTYPE_edit){
			v1.addElement(translate("����"));
			v2.addElement("C");
		}
		setReference("OTYPE",v1,v2);
		//20151228 JENNY ELINE end:�p�G�OC �令�u�� �ɥ�ε��~
		setValue("OTYPE" , OTYPE);
		setValue("HPRICE" , HPRICE);
		setValue("REPRICE" , REPRICE);

		//20160216 JENNY ELINE begin:�� HRCONFIG �P�_�O�_�n�b�e���W�q�X <font color=red>�Y���ɮפW�ǮɡA�Цb�U�ӵe���W�Ǳz���ɮסC</font> �r��
		//insert into HRCONFIG values ('OVERTIME.UPLOAD','1','Web�[�Z��}�����W��')
		String HRCONFIG_UPLOAD = (String) get("HRCONFIG_OVERTIME.UPLOAD");
		//�O�_�n��ܰT�� �p�G���o���ȬO 1 ���ܤ~�n�q
		boolean bShowFiledsMsg = HRCONFIG_UPLOAD != null && "1".equals(HRCONFIG_UPLOAD);
		if(!bShowFiledsMsg){//�p�G���q���ܩ�ť�
			setValue("showFiledsMsg" , "");
		}
		//20160216 JENNY ELINE end:�� HRCONFIG �P�_�O�_�n�b�e���W�q�X <font color=red>�Y���ɮפW�ǮɡA�Цb�U�ӵe���W�Ǳz���ɮסC</font> �r��

		return value;
	}
	public static String[][] getKSYSTEM(talk t,String EMPID,String FIELD) throws Exception{
		String KSYSTEM[][]=t.queryFromPool("select "+FIELD+",UTYPE from KSYSTEM where CPNYID in (select CPNYID from HRUSER where EMPID='"+EMPID+"') order by UTYPE",10);
		if(KSYSTEM.length==0) return KSYSTEM;
		if(KSYSTEM.length==1) return KSYSTEM;
		String A[][]=t.queryFromPool("select UTYPE from HRUSER where EMPID='"+EMPID+"'",10);
		String UTYPE=A[0][0];
		int M1=0;
		int index=-1;
		for(int i=0;i<KSYSTEM.length;i++){
			String G1=KSYSTEM[i][KSYSTEM[i].length-1];
			if(G1.equals("")){
				M1=i;
			} else {
				if(UTYPE.equals(G1)){
					index=i;
					break;
				}
			}

		}
		if(index==-1) index=M1;
		return new String[][]{KSYSTEM[index]};
	}
	public String getInformation(){
		return "---------------button1(\u65b0\u589e\u52a0\u73ed\u55ae).html_action()----------------";
	}

	/*
		�P�_EMPID�O�_����� (���ۭ�s�W���s����code) 20120816 added by tobey - �N�w�p�[�Z�渹����X�ܭ���
	*/
	public boolean checkEMPID(talk t, String DATELIST)throws Throwable{
		String sql = "";
		String[][] ret = null;

		String EMPID = getValue("EMPID").trim();

		String DAY1 = getToday("YYYYmmdd");
		DAY1 = datetime.dateAdd(DAY1,"d",-30);

		if (EMPID.length() > 0) {
			sql = "select CPNYID, EMPID "
				+ "from HRUSER "
				+ "where EMPID='"+convert.ToSql(EMPID.trim())+"' "
				+ get("CONDITION_B");
			ret = t.queryFromPool(sql);
			if (ret.length == 0) {
				//message(translate("�z�L�v�������H�ӳ�"),BACK);
				//	20190329 MAX COULSON BEGIN:��½Ķ
				message(hr.Msg.hrm8w_B32.Msg012,BACK);
				//	20190329 MAX COULSON END:��½Ķ
				return false;
			}
			if(!EMPID.equals(ret[0][1]) && EMPID.equalsIgnoreCase(ret[0][1])){
				//message("���u�s������"+ret[0][1]+"(�Ъ`�N�j�p�g���������ۦP)",BACK);
				//message("%1%2%3(�Ъ`�N�j�p�g���������ۦP)",new String[]{translate("���u�s��") , translate("����") , ret[0][1]},BACK);
				//	20190329 MAX COULSON BEGIN:��½Ķ
				message(hr.Msg.hrm8w_B32.Msg013,new String[]{ret[0][1]},BACK);
				//	20190329 MAX COULSON END:��½Ķ
				return false;
			}
		}
		//20180604 TIMO LUCAS �T�w�n��CONFIG�M�w�ݥͮĤ��٬O�ӽФ�
		boolean bB4 = false;
		String B4_DATE = "";
		sql = " select DATA from HRCONFIG where ID = 'B4'";
		ret = t.queryFromPool(sql);
		if(ret.length > 0){
			bB4 = true;
			B4_DATE = ret[0][0];
			if("SDATE".equals(B4_DATE)){
				bB4 = false;
			}
		}
		//sql = "select PNO, EMPID, SDATE "
			//+ "from OVERPLAN "
			//+ "where APPROVE is not null "
			//+ " and SDATE>'"+DAY1+"' "
			//+ " and PNO in (select PNO from OVERPLAN_FLOWC where F_INP_STAT="+(getTalk().f_db_type.equals("mssql")?"N":"")+"'�T�{') "
			//+ " and isnull(USED,'"+get("SYSTEM.ISNULL")+"') !='1' "
			//+ (EMPID.length() == 0? "": " and EMPID = '" + convert.ToSql(EMPID) + "' ")
			//+ get("CONDITION_B") + " "
			//+ "order by SDATE, EMPID";
		//20161123 TIMO FLASH B5��ڥ[�Z��L�kŪ����ڽs��
		sql = "select PNO, EMPID, SDATE "
			+ "from OVERPLAN "
			+ " where 1 = 1 ";
		//20180604 TIMO LUCAS �T�w�n��CONFIG�M�w�ݥͮĤ��٬O�ӽФ�
		if(bB4){
			sql += " and isnull(APPRDATE,'0') >'"+convert.ToSql(DAY1)+"' ";
		}else{
			sql += " and SDATE>'"+DAY1+"' ";
		}
		sql += " and PNO in (select PNO from OVERPLAN_FLOWC where F_INP_STAT="+(getTalk().f_db_type.equals("mssql")?"N":"")+"'�T�{') "
			+ " and isnull(USED,'"+get("SYSTEM.ISNULL")+"') !='1' "
			+ (EMPID.length() == 0? "": " and EMPID = '" + convert.ToSql(EMPID) + "' ")
			+ get("CONDITION_B")+" "
			+ "order by SDATE,EMPID";
		ret = t.queryFromPool(sql);
		if (ret.length == 0) {
			//message(translate("�|���ӽйw�w�[�Z�ӽг�"),BACK);
			//	20190329 MAX COULSON BEGIN:��½Ķ
			message(hr.Msg.hrm8w_B32.Msg014,BACK);
			//	20190329 MAX COULSON END:��½Ķ
			return false;
		}

		return true;
	}
}
