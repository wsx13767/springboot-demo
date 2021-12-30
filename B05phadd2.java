/*
	version	modify_date	modify_user	description
	1.00	20200221	DANIEL	應該先查個人的組別才看空白組別
	1.01	20200922	JAY		增加HRCONFIG判斷
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
	function name:B5.加班單(預先申請)
	note:
*/
public class B05phadd2_v2 extends hproc{
	boolean bEncryptGrade;
	talk t = null;
	String sql = "";
	String[][] ret = null;
	final String BACK = "history.back();";
	String ISNULL = "";
	//20181123 DANIEL COULSON begin:oracle的+是||
	String PLUS = ""; 
	private common5_v2 common5;
	//20181123 DANIEL COULSON end:oracle的+是|| 
	public String action(String value)throws Throwable{
		t = getTalk();
		ISNULL = pDB.getISNULL(t);
		//20181123 DANIEL COULSON begin:oracle的+是||
		PLUS = pDB.getPLUS(t);
		//20181123 DANIEL COULSON end:oracle的+是||
		String DATELIST = (String)get("SYSTEM.DATELIST");
		String FUNCTIONID = getFunctionID();

		//20140211:edward:begin:增加職等職級加解密
		String encryptGrade = (String)get("HRCONFIG_GRADE.ENCRYPTION");
		bEncryptGrade = "Y".equals(encryptGrade);
		//20140211:edward:end:增加職等職級加解密
		
		//20120816 tobey 將預計加班單頁面整合至首頁
		boolean bCheckEMPID = checkEMPID(t, DATELIST);
		if (!bCheckEMPID) {
			return value;	
		}
		String OLDOVER = getValue("OLDOVER").trim();
		//20130702:leo:begin:手機版
		if("MOBILE_B5".equals(FUNCTIONID)){
			OLDOVER = (String)get("MOBILE_B5_OLDOVER");
			if(OLDOVER==null){
				OLDOVER = getValue("OLDOVER");
			}else{
				remove("MOBILE_B5_OLDOVER");
			}
		}
		//20130702:leo:end:手機版
		if(OLDOVER.trim().length()==0){
			//message("預計加班單號不可空白",BACK);
			//message("%1%2",new String[]{translate("預計加班單號") , translate("不可空白")},BACK);
			//	20190329 MAX COULSON BEGIN:改翻譯
			message(hr.Msg.hrm8w_B32.Msg007,BACK);
			//	20190329 MAX COULSON END:改翻譯
			return value;
		}

		setValue("TYPE" , "A");//20170629 DRACO ELINE begin:加班類別固定預設A

		sql = "select a.EMPID , b.HECNAME , b.POSSIE , b.CPNYID , b.DEPT_NO , a.SDATE , a.EDATE , a.STIME"//7
			+ " , a.ETIME , a.NOTE , (select SET14 from HRUSER_DEPT_BAS where DEP_NO=b.DEPT_NO)"//10
			+ " , a.SHHMM , a.EHHMM , b.CLASS , b.GRADE , a.OTYPE , a.HPRICE , a.REPRICE"//17
			+ " , a.AMT"//18
			//20181123 DANIEL COULSON begin:對應預定加班的NOTE欄位 借工單號：XXXXXXX
			//+ " , (select WDEPT from SUPPORT where PNO=a.PNO)"//19 20180209 DRACO ELINE begin:從借工單轉來的預定加班也要把加班單位帶上
			+ " , (select WDEPT from SUPPORT where '借工單號：'"+PLUS+"PNO = a.NOTE)"//19
			//20181123 DANIEL COULSON end:對應預定加班的NOTE欄位 借工單號：XXXXXXX
			//20200221 DANIEL PATRICK begin:考勤組別
			+ " , b.SET107, (select GROUP_A from HRUSER_DEPT_BAS where DEP_NO=b.DEPT_NO)"//20-21
			//20200221 DANIEL PATRICK end:考勤組別
			+ " from OVERPLAN a , HRUSER b"
			+ " where a.EMPID = b.EMPID"
			+ " and a.PNO = '"+convert.ToSql(OLDOVER.trim())+"'"
			+ " and isnull(a.USED,'"+get("SYSTEM.ISNULL")+"') != '1'";
		ret = t.queryFromPool(sql);
		if(ret.length==0){
			//message(translate("資料已申請或此人不存在於基本檔中"),BACK);
			//	20190329 MAX COULSON BEGIN:改翻譯
			message(hr.Msg.hrm8w_B32.Msg008,BACK);
			//	20190329 MAX COULSON END:改翻譯
			return value;
		}

		//取出DB 裡的資料
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
		
		//20180209 DRACO ELINE begin:從借工單轉來的預定加班也要把加班單位帶上
		String WDEPT = ret[0][19].trim();
		//20180209 DRACO ELINE end:從借工單轉來的預定加班也要把加班單位帶上
		//20200221 DANIEL PATRICK begin:考勤組別
		String SET107 = ret[0][20].trim();
		if(SET107.length() == 0){
			SET107 = ret[0][21].trim();
		}
		//20200221 DANIEL PATRICK end:考勤組別

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
			//message(translate("此人排班設定有誤,請檢查"),BACK);
			//	20190329 MAX COULSON BEGIN:改翻譯
			message(hr.Msg.hrm8w_B32.Msg009,BACK);
			//	20190329 MAX COULSON END:改翻譯
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
			System.err.println("考勤班別 classset 有誤");
		}
		String CLASS_OTYPE = "";
		String CLASS_CSTIME = "";
		String[] arrCLASSSET = (String[])htCLASSSET.get(CLASS);
		if(arrCLASSSET != null)
		{
			CLASS_OTYPE = arrCLASSSET[17];
			CLASS_CSTIME = arrCLASSSET[2];
		}

		//20170503 DRACO ELINE begin:往前加班可能會有STIME ETIME為負的狀況，要還原成實際發生的日期時間
		String WORKDATE = SDATE;
		EDATE = SDATE;
		if(STIME.startsWith("-"))
		{
			//20170608 DRACO ELINE begin:修改規則，用B4的METHOD計算應顯示的日期時間
			String[] showtime = hr.B04phadd.DBToShowDateTime(WORKDATE , "0000" , STIME , ETIME);
			//SDATE = showtime[0].trim();//要做成跟自己手動申請一樣，所以日期不動
			STIME = showtime[1].trim();
			//EDATE = showtime[2].trim();//要做成跟自己手動申請一樣，所以日期不動
			ETIME = showtime[3].trim();
			//20170608 DRACO ELINE begin:修改規則，用B4的METHOD計算應顯示的日期時間

			setValue("BEFORE_WORK" , "1");
		}
		//20170503 DRACO ELINE end:往前加班可能會有STIME ETIME為負的狀況，要還原成實際發生的日期時間


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
					//message(translate("國定假日設定為不可申請加班"),"history.back()");
					//	20190329 MAX COULSON BEGIN:改翻譯
					message(hr.Msg.hrm8w_B32.Msg010,BACK);
					//	20190329 MAX COULSON END:改翻譯
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
		//20130925 BLUE 新增班別
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
		
		//20160129 LUCAS ELINE begin:加班單走彈性規則
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
				System.err.println("考勤基本設定的遲到允許分鐘數有誤，使用預設值 0 分鐘");
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

			//	取得查詢期間的班別
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
			String[][] dtWORKCARDCHG = t.queryFromPool(sql);	//	班別調換記錄
			//20160223 JENNY CINDY:傳入班別要用個人班別,不然會出現所屬班別 無起迄時間
			//Vector vCARD_FS = common5.CARD_DATA(t,EMPID,CLASS,SDATE_S1,EDATE_S1,htCLASSSET,htPROCEED,htCLASS,SET12,SET13,WITH_CARDIO,new Hashtable(),null,false,false,dtWORKCARDCHG);
			Vector vCARD_FS = common5.CARD_DATA(t,EMPID,USERCLASS,SDATE_S1,EDATE_S1,htCLASSSET,htPROCEED,htCLASS,SET12,SET13,WITH_CARDIO,new Hashtable(),null,false,false,dtWORKCARDCHG);
			
			String IN_CARD = ETIME;
			String OUT_CARD = STIME;
			if(vCARD_FS != null && vCARD_FS.size() > 1){
				String[] arrCARD_INFO = (String[])vCARD_FS.elementAt(1);
				IN_CARD = arrCARD_INFO[0];
				OUT_CARD = arrCARD_INFO[1];
			}
			int SHIFT_MINUTE = 0;	//	彈性允許分鐘數
			try{
				if(dtSALPARA[0][1].length() > 0){
					SHIFT_MINUTE = Integer.parseInt(dtSALPARA[0][1]);
				}
				if(SHIFT_MINUTE > 10000){
					SHIFT_MINUTE = SHIFT_MINUTE % 10000;
				}
			}catch(Exception e){
				System.err.println("遲到設定的彈性允許分鐘數有誤，使用預設值 0 分鐘");
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
				//	算出休息時間在這個班別占多少分鐘數
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
					//System.err.println("=====正常工時休息時間=>" + rest_min);

					String elasticity_cstime = common5.getCalTime(SDATE,CSTIME,SHIFT_MINUTE + "","m","HHmm");
					String elasticity_cstime2 = common5.getCalTime(SDATE,CSTIME,-SHIFT_MINUTE + "","m","HHmm");
					//System.err.println("=====正常工時彈性上班時間=>"+elasticity_cstime + "~" + elasticity_cstime2);
					String elasticity_cetime = common5.getCalTime(SDATE,CETIME,SHIFT_MINUTE + "","m","HHmm");
					String elasticity_cetime2 = common5.getCalTime(SDATE,CETIME,-SHIFT_MINUTE + "","m","HHmm");
					//System.err.println("=====正常工時彈性下班時間=>"+elasticity_cetime + "~" + elasticity_cetime2);

					String elasticity_in_crad = IN_CARD;
					if(elasticity_in_crad.length() != 4){
						//System.err.println("=====第一張卡片不等於四碼，依照上班時間起始當作進卡時間=>"+elasticity_cstime);
						elasticity_in_crad = CSTIME;
					}
					

					//System.err.println("=====第一張卡片時間為=>"+elasticity_in_crad);
					if(operation.compareTo(elasticity_cstime,elasticity_in_crad) > 0){
						//System.err.println("=====第一張卡片時間早於彈性上班時間=>"+elasticity_in_crad + " < " + elasticity_cstime + " 故將其彈性上班列為" + elasticity_cstime);
						elasticity_in_crad = elasticity_cstime;
					}else if(operation.compareTo(elasticity_in_crad,elasticity_cstime2) > 0){
						//System.err.println("=====第一張卡片時間晚於彈性上班時間=>"+elasticity_in_crad + " < " + elasticity_cstime2 + " 故將其彈性上班列為" + elasticity_cstime2);
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
		//20160129 LUCAS ELINE end:加班單走彈性規則

		//20160223 JENNY CINDY:傳入班別要用個人班別,不然會出現所屬班別 無起迄時間
		

		if ( bEncryptGrade ){
			GRADE = decrypt("hr" , GRADE).trim();
		}
		//20190527 DANIEL PATRICK begin:不是數字就給0
		try{
			GRADE = ""+Long.parseLong(GRADE);
		}catch(Exception e){
			GRADE = "0";
		}
		//20190527 DANIEL PATRICK end:不是數字就給0
		if(CPNYID.length()==4 && CPNYID.startsWith("YT")){
			setValue("MEMOES","『"+translate("現職單位不等於加班單位時，給付方式一律以定額支援津貼給付")+"』");
		}
		else{
			setValue("MEMOES","");
		}

		setValue("EMPID",EMPID);
		setValue("HECNAME",HECNAME);
		setValue("POSSIE",POSSIE);
		//20151218 JILL ELINE : 如果有設定顯示格式,會把值改掉,所以多加隱藏欄位來存放CPNYID
		setValue("CPNYID",CPNYID);//捉資料用
		setValue("CPNYID1",CPNYID);//顯示用
		setValue("NDEPT",DEPT_NO);
		
		//20180209 DRACO ELINE begin:從借工單轉來的預定加班也要把加班單位帶上
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
		//20180209 DRACO ELINE end:從借工單轉來的預定加班也要把加班單位帶上
		
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

		//20161020 LUCAS ELINE begin:增加顯示當月已累積加班時數(不含本次):N小時
		//20200221 DANIEL PATRICK begin:應該先查個人的組別才看空白組別
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
		//20200221 DANIEL PATRICK end:應該先查個人的組別才看空白組別
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
			//message("查無該日期考勤區間資料",BACK);
			//	20190329 MAX COULSON BEGIN:改翻譯
			message(hr.Msg.hrm8w_B32.Msg011,BACK);
			//	20190329 MAX COULSON END:改翻譯
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

		//20161020 LUCAS ELINE end:增加顯示當月已累積加班時數(不含本次):N小時

		setValue("PLAN_STIME",STIME);//20160517 DRACO add 暗記申請預定加班時段
		setValue("PLAN_ETIME",ETIME);//20160517 DRACO add 暗記申請預定加班時段
		
		setValue("PSTIME",STIME);
		setValue("PETIME",ETIME);
		setValue("NOTE",NOTE);
		setValue("PLANNOTE",NOTE);
		setValue("EATYN","Y");
		setValue("OLDOVER",OLDOVER);
		String MIN_HOUR="1"; //平日加班最小時數
		String ADD_HOUR="1"; //平日加班遞增時數
		String CARDIO_FLAG="N";
		String K_SET9=""; //加班迄時多少天內申請(空白或0表不檢核)
		String K_SET10=""; //加班補償方式(平日)
		String K_SET32=""; //加班補償方式(假日)
		String K_SET27=""; //加班補休可預借的時數
		String K_SET28=""; //加班補休上限時數
		String K_SET35=""; //加班補償方式(平日)
		String K_SET36=""; //加班補償方式(假日)
		String K_SET50=""; //加班用餐方式(平日)
		String K_SET51=""; //加班用餐方式(假日)
		String K_SET34=""; //加班單可否自行指定津貼的班別
		String VAOVER1 = "1"; //假日加班最小時數
		String VAOVER2 = "1"; //假日加班遞增時數
		
		String K_HSET32 = ""; //加班補償方式(國假) 20161221 DRACO add
		String K_HSET36 = ""; //N職等以上(含)(國假) 20161221 DRACO add
		String K_OSET32 = ""; //加班補償方式(休息日) 20161221 DRACO add
		String K_OSET36 = ""; //N職等以上(含)(休息日) 20161221 DRACO add
		String K_OVERSET = "";//20170807 DRACO add
		
		String K_SET54 = "";//20180308 DRACO add 加班補償方式(免出勤日)
		String K_SET55 = "";//20180308 DRACO add N職等以上補休方式設定(免出勤日)
		
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
			
			K_SET54 = KSM[0][20].trim();//20180308 DRACO add 加班補償方式(免出勤日)
			K_SET55 = KSM[0][21].trim();//20180308 DRACO add N職等以上補休方式設定(免出勤日)
		}

		//20161221 DRACO ELINE begin:CLASSSET.OTYPE為A時，SET32要看OSET32，SET36要看OSET36；CLASSSET.OTYPE為B，且班別為H開頭時，SET32要看HSET32，SET36要看HSET36
		setVisible("OVERSET" , false);//20170807 DRACO add
		if(operation.compareTo(SDATE , "20161223")>=0)
		{
			if("Y".equals(K_OVERSET))
			{
				setVisible("OVERSET" , true);//20170807 DRACO add
				setValue("OVERSET" , "Y");//20171026 DRACO ELINE begin:若要顯示該欄位，預設勾選
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
			//20180308 DRACO CINDY begin:C免出勤日另外判斷
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
			//20180308 DRACO CINDY end:C免出勤日另外判斷
			//else if("B".equals(CLASS_OTYPE) && CLASS.startsWith("H"))
			if(CLASS.startsWith("H"))//20170106 DRACO ELINE begin:改成只判斷班別
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
		//20161221 DRACO ELINE end:CLASSSET.OTYPE為A時，SET32要看OSET32，SET36要看OSET36；CLASSSET.OTYPE為B，且班別為H開頭時，SET32要看HSET32，SET36要看HSET36
				
		if("MOBILE_B5".equals(FUNCTIONID)){
			setEditable("RCLASS",K_SET34.equals("Y"));
			setEditable("EATUNIT",false);
		}else{
			setVisible("RCLASS",K_SET34.equals("Y"));
			setVisible("EATUNIT",false);
		}

		{
			String DEFAULT_EATYN=K_SET51;
			if(HOLYN==null){  //平日
				DEFAULT_EATYN=K_SET50;
			}else{ //20131226 BLUE 如果假日時  把最小時數 和 遞增時數 改為假日
				MIN_HOUR = VAOVER1;  //假日加班最小時數
				ADD_HOUR = VAOVER2; //假日加班遞增時數
			}
			if(DEFAULT_EATYN.equals("")) DEFAULT_EATYN="A";
			//20181107 DANIEL PATRICK begin:手機板控制是用 setEditable
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
			//20181107 DANIEL PATRICK end:手機板控制是用 setEditable

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
		//A.只有補休
		//B.只有給薪
		//C.二者選一
		//D.二者皆可(申請人自行分配時數)
		//E.N職等以上只有補休,其餘只有給薪
		//F.N職等以上只有補休,其餘二者選一
		//G.N職等以上只有補休,其餘二者皆可
		//H.N職等以上只有給薪,其餘只有補休
		//I.N職等以上只有給薪,其餘二者選一
		//J.N職等以上只有給薪,其餘二者皆可
		//K.N職等以上二者選一,其餘只有補休
		//L.N職等以上二者選一,其餘只有給薪
		//M.N職等以上二者選一,其餘二者皆可
		//N.N職等以上二者皆可,其餘只有補休
		//O.N職等以上二者皆可,其餘只有給薪
		//P.N職等以上二者皆可,其餘二者選一
		//Q.二項皆給(For越南) (20151130 JENNY new)
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

		// 20200922 JAY PATRICK begin 判斷OVERTIME_TYPE給預設值
		boolean bOVERTIME_TYPE = "Y".equals((String)get("HRCONFIG_OVERTIME_TYPE"));
		// 20200922 JAY PATRICK end 判斷OVERTIME_TYPE給預設值

		boolean bOTYPE_D = false;//20151228 JENNY:是否有 二者皆可的資料
		boolean bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
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
			bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
		}
		else{
			//System.out.println("K_SET10==>"+K_SET10);
			//System.out.println("K_SET32==>"+K_SET32);
			//System.out.println("K_SET35==>"+K_SET35);
			//System.out.println("K_SET36==>"+K_SET36);
			if(HOLYN==null){
				//20181126 DANIEL PATRICK begin:Q 都有
				if("Q".equals(K_SET10)){
					Vector v1=new Vector();
					Vector v2=new Vector();
					v1.addElement(translate("都有"));
					v2.addElement("C");
					setReference("OTYPE",v1,v2);
				}
				//20181126 DANIEL PATRICK end:Q 都有
				if(K_SET10.equals("A") || K_SET10.equals("B")){
					OTYPE = K_SET10;
					setEditable("OTYPE",false);
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
				}
				else if(K_SET10.equals("C") || K_SET10.equals("D")){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					//20180412 MAX PATRICK BEGIN:預設改成B4申請時選擇的OTYPE
					//OTYPE = "B";
					//20180412 MAX PATRICK END:預設改成B4申請時選擇的OTYPE

					// 20200922 JAY PATRICK begin 判斷OVERTIME_TYPE給預設值
					if("C".equals(K_SET10) && bOVERTIME_TYPE){
						OTYPE = "A";
					}
					// 20200922 JAY PATRICK end 判斷OVERTIME_TYPE給預設值
				}
				else if( (K_SET10.equals("E")||K_SET10.equals("F")||K_SET10.equals("G")) && GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "A";//只有補休
					setEditable("OTYPE",false);
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
				}
				else if( (K_SET10.equals("H")||K_SET10.equals("I")||K_SET10.equals("J")) && GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "B";//只有給薪
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("K")||K_SET10.equals("L")||K_SET10.equals("M")||K_SET10.equals("N")||K_SET10.equals("O")||K_SET10.equals("P")) && GRADE_BIGGER_THAN_K_SET35){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else if( (K_SET10.equals("H")||K_SET10.equals("K")||K_SET10.equals("N")) && !GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "A";//一班人只有補休
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("E")||K_SET10.equals("L")||K_SET10.equals("O")) && !GRADE_BIGGER_THAN_K_SET35){
					OTYPE = "B";//一班人只有給薪
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET10.equals("F")||K_SET10.equals("G")||K_SET10.equals("I")||K_SET10.equals("J")||K_SET10.equals("M")||K_SET10.equals("P")) && !GRADE_BIGGER_THAN_K_SET35){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else{
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				
				if(K_SET10.equals("D")){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
						//20191231 ERIN PATRICK begin：總計補休時數、總計給薪時數允許編輯
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
						//20191231 ERIN PATRICK end：總計補休時數、總計給薪時數允許編輯
					}
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
				}
				else if( (K_SET10.equals("N")||K_SET10.equals("O")||K_SET10.equals("P")) && GRADE_BIGGER_THAN_K_SET35){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
				}
				else if( (K_SET10.equals("G")||K_SET10.equals("J")||K_SET10.equals("M")) && !GRADE_BIGGER_THAN_K_SET35){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
				}
				//20151130 JENNY ELINE: Q.二項皆給 因為給薪補休都給,所以OTYPE直接給 C 且
				else if("Q".equals(K_SET10)){
					OTYPE = "C";
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
					setEditable("HPRICE",false);
					setEditable("REPRICE",false);
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
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

				//20181126 DANIEL PATRICK begin:Q 都有
				if("Q".equals(K_SET32)){
					Vector v1=new Vector();
					Vector v2=new Vector();
					v1.addElement(translate("都有"));
					v2.addElement("C");
					setReference("OTYPE",v1,v2);
				}
				//20181126 DANIEL PATRICK end:Q 都有

				if(K_SET32.equals("A") || K_SET32.equals("B")){
					OTYPE = K_SET32;
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if(K_SET32.equals("C") || K_SET32.equals("D")){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					//20180412 MAX PATRICK BEGIN:預設改成B4申請時選擇的OTYPE
					//OTYPE = "B";
					//20180412 MAX PATRICK END:預設改成B4申請時選擇的OTYPE

					// 20200922 JAY PATRICK begin 判斷OVERTIME_TYPE給預設值
					if("C".equals(K_SET32) && bOVERTIME_TYPE){
						OTYPE = "A";
					}
					// 20200922 JAY PATRICK end 判斷OVERTIME_TYPE給預設值
				}
				else if( (K_SET32.equals("E")||K_SET32.equals("F")||K_SET32.equals("G")) && GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "A";//只有補休
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("H")||K_SET32.equals("I")||K_SET32.equals("J")) && GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "B";//只有給薪
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("K")||K_SET32.equals("L")||K_SET32.equals("M")||K_SET32.equals("N")||K_SET32.equals("O")||K_SET32.equals("P")) && GRADE_BIGGER_THAN_K_SET36){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else if( (K_SET32.equals("H")||K_SET32.equals("K")||K_SET32.equals("N")) && !GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "A";//一班人只有補休
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("E")||K_SET32.equals("L")||K_SET32.equals("O")) && !GRADE_BIGGER_THAN_K_SET36){
					OTYPE = "B";//一班人只有給薪
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
				}
				else if( (K_SET32.equals("F")||K_SET32.equals("G")||K_SET32.equals("I")||K_SET32.equals("J")||K_SET32.equals("M")||K_SET32.equals("P")) && !GRADE_BIGGER_THAN_K_SET36){
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				else{
					bOTYPE_edit = true;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",true);
					OTYPE = "B";
				}
				
				if(K_SET32.equals("D")){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
						//20191231 ERIN PATRICK begin：總計補休時數、總計給薪時數允許編輯
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
						//20191231 ERIN PATRICK end：總計補休時數、總計給薪時數允許編輯
					}
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
				}
				else if( (K_SET32.equals("N")||K_SET32.equals("O")||K_SET32.equals("P")) && GRADE_BIGGER_THAN_K_SET36){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
				}
				else if( (K_SET32.equals("G")||K_SET32.equals("J")||K_SET32.equals("M")) && !GRADE_BIGGER_THAN_K_SET36){
					if("MOBILE_B5".equals(FUNCTIONID)){
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setEditable("OTYPE",false);
						setEditable("HPRICE",true);
						setEditable("REPRICE",true);
					}else{
						bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
						setVisible("OTYPE",false);
						setVisible("HPRICE",true);
						setVisible("REPRICE",true);
					}
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
				}
				//20151130 JENNY ELINE: Q.二項皆給 因為給薪補休都給,所以OTYPE直接給 C 且
				else if("Q".equals(K_SET32)){
					OTYPE = "C";
					bOTYPE_edit = false;//20160114 JENNY:給付方式可否編輯
					setEditable("OTYPE",false);
					setEditable("HPRICE",false);
					setEditable("REPRICE",false);
					bOTYPE_D = true;//20151228 JENNY:是否有 二者皆可的資料
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
		
		//20151228 JENNY ELINE begin:如果是C 改成只有 補休或給薪
		Vector v1=new Vector();
		Vector v2=new Vector();
		v1.addElement(translate("補休"));
		v1.addElement(translate("給薪"));
		v2.addElement("A");
		v2.addElement("B");
		//20160114 JENNY:給付方式可否編輯
		//if(bOTYPE_D){
		if(bOTYPE_D || !bOTYPE_edit){
			v1.addElement(translate("都有"));
			v2.addElement("C");
		}
		setReference("OTYPE",v1,v2);
		//20151228 JENNY ELINE end:如果是C 改成只有 補休或給薪
		setValue("OTYPE" , OTYPE);
		setValue("HPRICE" , HPRICE);
		setValue("REPRICE" , REPRICE);

		//20160216 JENNY ELINE begin:依 HRCONFIG 判斷是否要在畫面上秀出 <font color=red>若需檔案上傳時，請在下個畫面上傳您的檔案。</font> 字樣
		//insert into HRCONFIG values ('OVERTIME.UPLOAD','1','Web加班單開放附件上傳')
		String HRCONFIG_UPLOAD = (String) get("HRCONFIG_OVERTIME.UPLOAD");
		//是否要顯示訊息 如果取得的值是 1 的話才要秀
		boolean bShowFiledsMsg = HRCONFIG_UPLOAD != null && "1".equals(HRCONFIG_UPLOAD);
		if(!bShowFiledsMsg){//如果不秀的話放空白
			setValue("showFiledsMsg" , "");
		}
		//20160216 JENNY ELINE end:依 HRCONFIG 判斷是否要在畫面上秀出 <font color=red>若需檔案上傳時，請在下個畫面上傳您的檔案。</font> 字樣

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
		判斷EMPID是否有資料 (取自原新增按鈕的內code) 20120816 added by tobey - 將預計加班單號頁整合至首頁
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
				//message(translate("您無權限替此人申報"),BACK);
				//	20190329 MAX COULSON BEGIN:改翻譯
				message(hr.Msg.hrm8w_B32.Msg012,BACK);
				//	20190329 MAX COULSON END:改翻譯
				return false;
			}
			if(!EMPID.equals(ret[0][1]) && EMPID.equalsIgnoreCase(ret[0][1])){
				//message("員工編號應為"+ret[0][1]+"(請注意大小寫必須完全相同)",BACK);
				//message("%1%2%3(請注意大小寫必須完全相同)",new String[]{translate("員工編號") , translate("應為") , ret[0][1]},BACK);
				//	20190329 MAX COULSON BEGIN:改翻譯
				message(hr.Msg.hrm8w_B32.Msg013,new String[]{ret[0][1]},BACK);
				//	20190329 MAX COULSON END:改翻譯
				return false;
			}
		}
		//20180604 TIMO LUCAS 確定要看CONFIG決定看生效日還是申請日
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
			//+ " and PNO in (select PNO from OVERPLAN_FLOWC where F_INP_STAT="+(getTalk().f_db_type.equals("mssql")?"N":"")+"'確認') "
			//+ " and isnull(USED,'"+get("SYSTEM.ISNULL")+"') !='1' "
			//+ (EMPID.length() == 0? "": " and EMPID = '" + convert.ToSql(EMPID) + "' ")
			//+ get("CONDITION_B") + " "
			//+ "order by SDATE, EMPID";
		//20161123 TIMO FLASH B5實際加班單無法讀取單據編號
		sql = "select PNO, EMPID, SDATE "
			+ "from OVERPLAN "
			+ " where 1 = 1 ";
		//20180604 TIMO LUCAS 確定要看CONFIG決定看生效日還是申請日
		if(bB4){
			sql += " and isnull(APPRDATE,'0') >'"+convert.ToSql(DAY1)+"' ";
		}else{
			sql += " and SDATE>'"+DAY1+"' ";
		}
		sql += " and PNO in (select PNO from OVERPLAN_FLOWC where F_INP_STAT="+(getTalk().f_db_type.equals("mssql")?"N":"")+"'確認') "
			+ " and isnull(USED,'"+get("SYSTEM.ISNULL")+"') !='1' "
			+ (EMPID.length() == 0? "": " and EMPID = '" + convert.ToSql(EMPID) + "' ")
			+ get("CONDITION_B")+" "
			+ "order by SDATE,EMPID";
		ret = t.queryFromPool(sql);
		if (ret.length == 0) {
			//message(translate("尚未申請預定加班申請單"),BACK);
			//	20190329 MAX COULSON BEGIN:改翻譯
			message(hr.Msg.hrm8w_B32.Msg014,BACK);
			//	20190329 MAX COULSON END:改翻譯
			return false;
		}

		return true;
	}
}
