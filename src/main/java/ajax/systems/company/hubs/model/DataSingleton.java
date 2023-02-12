package ajax.systems.company.hubs.model;

public class DataSingleton {
	
	private static final DataSingleton INSTANCE = new DataSingleton();
	private Object data;
	
	public static DataSingleton getInstance() {return INSTANCE;}
	
	private DataSingleton(){}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	

}
