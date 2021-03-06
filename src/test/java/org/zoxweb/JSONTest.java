package org.zoxweb;




import java.util.List;
import java.util.logging.Logger;

import org.zoxweb.server.util.GSONUtil;
import org.zoxweb.server.util.ServerUtil;
import org.zoxweb.shared.data.AddressDAO;
import org.zoxweb.shared.data.SystemInfoDAO;
import org.zoxweb.shared.net.InetAddressDAO;
import org.zoxweb.shared.net.NetworkInterfaceDAO;
import org.zoxweb.shared.util.Const;
import org.zoxweb.shared.util.NVEntity;

import com.google.gson.Gson;

public class JSONTest 
{
	private static Logger log = Logger.getLogger(Const.LOGGER_NAME);
	public static void main ( String ...args)
	{
		try 
		{
			SystemInfoDAO sysDAO = ServerUtil.loadSystemInfoDAO();
			
			for (NVEntity nve : sysDAO.getNetworkInterfaces().values())
			{
				NetworkInterfaceDAO niDAO = (NetworkInterfaceDAO) nve;
				for ( InetAddressDAO iaDAO : niDAO.getInetAddresses())
				{
					String json = GSONUtil.toJSON( iaDAO, false);
					System.out.println( json);
					System.out.println( ""+GSONUtil.fromJSON(json, InetAddressDAO.class));
				}
				
				
				
				long ts1 = System.nanoTime();
				String json1 = new Gson().toJson(niDAO);
				ts1 = System.nanoTime() - ts1;
				
				String json = null;
				long ts = System.nanoTime();
				json = GSONUtil.toJSON( niDAO, false);
				ts = System.nanoTime() -ts;
				
				
				System.out.println( json1);
				System.out.println( json);
				System.out.println( "******* json conversion Gson:" + ts1 + " it took zoxweb json:" + ts + " delta " +((ts1-ts)) + " zoxweb is "  + ((float)ts1/(float)ts) + " faster");
				
				
				
				ts1 = System.nanoTime();
				NetworkInterfaceDAO niTemp = new Gson().fromJson(json1, NetworkInterfaceDAO.class);
				ts1 = System.nanoTime() - ts1;
				
				
				ts = System.nanoTime();
				NetworkInterfaceDAO zbNI = GSONUtil.fromJSON(json, NetworkInterfaceDAO.class);
				ts = System.nanoTime() -ts;
				System.out.println( "Gson obj:"+niTemp);
				System.out.println( "ZW   obj:"+zbNI);
				System.out.println( "NI   DAO:"+niDAO);
				System.out.println( "******* Object conversion Gson:" + ts1 + " it took zoxweb json:" + ts + " delta " +((ts1-ts)) + " zoxweb is "  + ((float)ts1/(float)ts) + " faster");
			}
			
			
			
			String temp =  GSONUtil.toJSON(sysDAO, true);
			System.out.println(temp);
			SystemInfoDAO newSysDAO =  GSONUtil.fromJSON(temp, SystemInfoDAO.class);
			System.out.println( "Json Equals : " + temp.equals(GSONUtil.toJSON( newSysDAO, true)));
			System.out.println( sysDAO);
			log.info("java logger"+ newSysDAO);
			
			AddressDAO address = new AddressDAO();
			address.setStreet("P.O Box 251906");
			address.setCity("Los Angeles");
			address.setStateOrProvince("CA");
			address.setZIPOrPostalCode("90025");
			address.setCountry("USA");
			
			System.out.println(GSONUtil.toJSON(address, true, false, false));
			System.out.println(GSONUtil.toJSONWrapper("address", address, true, false, false));
			
			String jsonValues = GSONUtil.toJSONValues(sysDAO.getNetworkInterfaces().values(), true, false);
			System.out.println(jsonValues);
			List<NVEntity> values = GSONUtil.fromJSONValues(jsonValues);
			System.out.println(values);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
