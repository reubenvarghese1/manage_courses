/*Just added the generic function for file upload for now*/

package main.util.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Upload {
	
	static final String path="/home/stud/btech/cse/2015/kshitij.cs15/cms" ;
	String ip;
	String username;
	String password;
	int port=22;
	
	public Upload()
	{
		Properties prop=new Properties();
		try {
			prop.load(new FileInputStream("Files//SSHinfo.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ip=prop.getProperty("ip");
		username=prop.getProperty("username");
		password=prop.getProperty("password");
	}
	
	
	/*This is a generic function which will upload the file
	 *  specified in "from" in the local system to "to" on ssh.
	 *  Take care that both the paths must be complete upto the file name itself.
	 *  MAKE SURE THAT THE SEPARATOR USED IS '/'
	 */
	public void uploadFile(String from, String to)
	{
		Session     session     = null;
		Channel     channel     = null;
		ChannelSftp channelSftp = null;
		
		String directory=to.substring(0, to.lastIndexOf('/'));
        String filename=to.substring(to.lastIndexOf('/'+1));
		
		try{
            JSch jsch = new JSch();
            session = jsch.getSession(username,ip,port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp)channel;
            channelSftp.cd(directory);
            File f = new File(from);
            channelSftp.put(new FileInputStream(f), filename);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/*
	 * from - path on local system including file name
	 * courseid - specific course id including prof name +cours name
	 * assignment - assignment no.
	 * roll - roll no. of the student
	 */
	public void studentUploadAssignment(String from, String courseid, String assignment, String roll)
	{
		String extension = from.substring(from.lastIndexOf('.'));
		String to = path+"/"+courseid+"/uploaded assignments/"+assignment+"/"+roll+extension;
		uploadFile(from,to);
	}
	
	/*
	 * from - path on local system including file name
	 * courseid - specific course id including prof name +cours name
	 * folder - either "assignments" or "material"
	 */
	public void professorUploadFile(String from, String courseid, String folder )
	{
		String filename = from.substring(from.lastIndexOf('/')+1);
		String to = path+"/"+courseid+"/"+folder+"/"+filename;
		uploadFile(from, to);
	}

}