import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;



public class StartRemotePeers {

	public static void main(String[] args) throws IOException {
		
		StartRemotePeers st = new StartRemotePeers();
		TreeMap<String, String> peers = st.getPeers();
		String workingDir = System.getProperty("user.dir");
		String peerProcessName = new String("java peerProcess");

		for (String peerId : peers.keySet()) {
			String hostname = peers.get(peerId);
			
			//Process serverProcess=Runtime.getRuntime().exec(
			//		"ssh -i ~/.ssh/private.pem " + hostname + " cd " + workingDir + " ; "
			//				+ peerProcessName + " " + peerId);
			Process serverProcess=Runtime.getRuntime().exec(
					"ssh " + hostname + " cd " + workingDir + " ; "
							+ peerProcessName + " " + peerId);
			
			outputDisplayer outputDisplayer = new outputDisplayer(peerId, new BufferedReader(new InputStreamReader(serverProcess.getInputStream()))  );
			new Thread(outputDisplayer).start();
			System.out.println("ssh -i ~/.ssh/private.pem " + hostname + " cd " + workingDir + " ; "
					+ peerProcessName + " " + peerId);
			outputDisplayer errorDisplayer = new outputDisplayer(peerId, new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()))  );
			new Thread(errorDisplayer ).start();
		}
		

	}

	public TreeMap<String, String> getPeers() {

		TreeMap<String, String> ret = new TreeMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/peerInfo.cfg")));

			String line = null;

			while ((line = reader.readLine()) != null) {
				
				
				
				String[] info = line.split(" ");
				String peerId = info[0];
				ret.put(peerId, info[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;

	}
	
	

}
