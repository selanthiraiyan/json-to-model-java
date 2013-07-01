package pojoGenerator;

import java.util.ArrayList;

public class ServiceProvider {

	public static class Service {
		String base;
		String servlet;
		public Service(String base, String servlet) {
			this.base = base;
			this.servlet = servlet;
		}
	}
	public ServiceProvider() {
		// TODO Auto-generated constructor stub
	}
	
	public static ArrayList<Service> getServiceList() {
		ArrayList<Service> list = new ArrayList<Service>();
		
		Service init = new Service("Base", "Init");
		list.add(init);
		
		Service addChitFund = new Service("ChitFund", "AddChitFund");
		list.add(addChitFund);
		
		Service chitOverview = new Service("ChitFund", "GetChitFundOverview");
		list.add(chitOverview);
		
		Service chitDetails = new Service("ChitFund", "GetChitFundDetails");
		list.add(chitDetails);		
		
		return list;
	}

}
