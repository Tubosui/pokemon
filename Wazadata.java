package tubosi;

public class Wazadata {
	String name;
	int type;
	int power_price;
	int physical;
	int additional_effect;
	
	Wazadata(String name, int[] data){
		this.name = name;
		this.type = data[0] - 1;
		this.power_price = data[1];
		this.physical = data[2];
		this.additional_effect = data[3];
	}

}

