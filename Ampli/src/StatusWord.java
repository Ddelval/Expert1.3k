import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class StatusWord {
	private static int[] bands= new int[] {160, 80, 60, 40, 30, 20,
											17,	15, 12, 10,  6,  4};
	private static Map<Character,String> warnings= new HashMap<Character,String>();
	private static Map<Character,String> alarms= new HashMap<Character,String>();
	public String operate;
	public String transmit;
	public String memoryBank;
	public int input;
	public String bandm;
	public int txAntenna;
	public String ATU_status;
	public int powerLevel;
	public int outputPower;
	public double SWR_ATU;
	public double SWR_ANT;
	public double V_PA;
	public double I_PA;
	public int temp;
	public String warning=null;
	public String alarm=null;
	


	public static void setup() {
		warnings.put('M', "Alarm amplifier");
		warnings.put('A', "No selected antenna");
		warnings.put('S', "SWR atenna");
		warnings.put('B', "No valid band");
		warnings.put('P', "Power limit exceeded");
		warnings.put('O', "Overheating");
		warnings.put('Y', "ATU not available");
		warnings.put('W', "Tuning with no power");
		warnings.put('K', "ATU bypassed");
		warnings.put('R', "Power switch held by remote");
		warnings.put('T', "Combiner overheating");
		warnings.put('C', "Combiner fault");
		
		alarms.put('S', "SWR exceeding limits");
		alarms.put('A', "Amplifier protection");
		alarms.put('D', "Input overdriving");
		alarms.put('H', "Excess overheating");
		alarms.put('C', "Combiner fault");
	}
	public StatusWord() {
		
	}
	public StatusWord(StatusWord sw) {
		this.operate 		= sw.operate;
		this.transmit		= sw.transmit;
		this.memoryBank 	= sw.memoryBank;
		this.input 			= sw.input;
		this.bandm 			= sw.bandm;
		this.txAntenna 		= sw.txAntenna;
		this.ATU_status 	= sw.ATU_status;
		this.powerLevel 	= sw.powerLevel;
		this.outputPower 	= sw.outputPower;
		this.SWR_ATU 		= sw.SWR_ATU;
		this.SWR_ANT 		= sw.SWR_ANT;
		this.V_PA 			= sw.V_PA;
		this.I_PA 			= sw.I_PA;
		this.temp 			= sw.temp;
		this.warning 		= sw.warning;
		this.alarm 			= sw.alarm;
	}
	
	public StatusWord(String in) throws Exception {
		int tmp;
		
		in=in.replace("0.0a0", "0.00").replace("0N", "N");
		in=in.replaceAll("\n", "").replaceAll(",,", ",");
		in=in.replaceAll(",,", ",").replace(" .", " ");
		String base=new String(in.getBytes("ASCII"));
		base=base.replace(" ", "");
		String[] splitted=base.split(",");
		//System.out.println(splitted.length);
		if(splitted.length<20)throw new Exception("e");
		
		if(splitted[1].contains("O")) 	operate="Operative";
		else							operate="Standby";
		
		if(splitted[2].contains("T")) 	transmit="TX";
		else							transmit="RX";
		
		if(splitted[3].contains("A")) 	memoryBank="A";
		else							memoryBank="B";
		
		if(splitted[4].contains("1")) 	input=1;
		else							input=2;
		
		bandm=((tmp=Integer.parseInt(splitted[5]))<12)? Integer.toString(bands[tmp])+" m":"";
		
		if(splitted[6].length()>0)txAntenna=(Integer.parseInt(splitted[6].substring(0, 1)));
		
		
		char ch=' ';
		if(splitted[6].length()>1)ch=splitted[6].substring(1, 2).charAt(0);
		switch(ch) {
			case 't':
				ATU_status="Tunable antenna";
				break;
			case 'b':
				ATU_status="ATU bypassed";
				break;
			case 'a':
				ATU_status="ATU enabled";
				break;
			default:
				ATU_status="";
		}
		powerLevel=0;
		if(splitted[8].contains("L")) powerLevel=1;
		if(splitted[8].contains("M")) powerLevel=2;
		if(splitted[8].contains("H")) powerLevel=3;
		
		outputPower=Integer.parseInt(splitted[9]);
		
		SWR_ATU=Double.parseDouble(splitted[10]);
		SWR_ANT=Double.parseDouble(splitted[11]);
		
		V_PA=Double.parseDouble(splitted[12]);
		I_PA=Double.parseDouble(splitted[13]);
		
		temp=-1;
		temp=Integer.parseInt(splitted[14]);
		
		for(char c: splitted[17].toCharArray()) {
			if(warnings.containsKey(c)) warning=warnings.get(c);
		}
		
		for(char c: splitted[18].toCharArray()) {
			if(alarms.containsKey(c)) alarm=alarms.get(c);
		}
		
		//System.out.println(base);
		
	}
	public void updateData(StatusWord sw) {
		this.operate 		= sw.operate;
		this.transmit		= sw.transmit;
		this.memoryBank 	= sw.memoryBank;
		this.input 			= sw.input;
		this.bandm 			= sw.bandm;
		this.txAntenna 		= sw.txAntenna;
		this.ATU_status 	= sw.ATU_status;
		this.powerLevel 	= sw.powerLevel;
		this.outputPower 	= sw.outputPower;
		this.SWR_ATU 		= sw.SWR_ATU;
		this.SWR_ANT 		= sw.SWR_ANT;
		this.V_PA 			= sw.V_PA;
		this.I_PA 			= sw.I_PA;
		this.temp 			= sw.temp;
		this.warning 		= sw.warning;
		this.alarm 			= sw.alarm;
	}
	@Override
	public String toString() {
		return "StatusWord [operate=" + operate + ", transmit=" + transmit + ", memoryBank=" + memoryBank + ", input="
				+ input + ", bandm=" + bandm + ", txAntenna=" + txAntenna + ", ATU_status=" + ATU_status
				+ ", powerLevel=" + powerLevel + ", outputPower=" + outputPower + ", SWR_ATU=" + SWR_ATU + ", SWR_ANT="
				+ SWR_ANT + ", V_PA=" + V_PA + ", I_PA=" + I_PA + ", temp=" + temp + ", warning=" + warning + ", alarm="
				+ alarm + "]";
	}
	
	
	
	
	
}
