package org.zoxweb.server.net.security;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.zoxweb.server.io.IOStreamInfo;
import org.zoxweb.server.io.IOUtil;
import org.zoxweb.server.io.StreamStats;
import org.zoxweb.shared.net.InetSocketAddressDAO;

/**
 * Simple client for ClamAV's clamd scanner. Provides straightforward instream scanning.
 */
public class ClamAVClient
{
	
	public static class ClamAVScanResult
		extends FilterInputStream
		implements StreamStats
	{
		long size = 0;
		String response;
		IOStreamInfo ci;
		long duration = 0;
		boolean isClean;
		String filename;
		ClamAVScanResult(IOStreamInfo ci, InputStream is, String filename)
		{
			super(is);
			this.filename = filename;
			this.ci = ci;
		}
		
		
		
		public String getScanResponse()
		{
			return response;
		}
		
		public IOStreamInfo getConnectionInfo()
		{
			return ci;
		}
		
		public long getDuration()
		{
			return duration;
		}
		
		public String getFilename()
		{
			return filename;
		}
		
		public boolean isClean()
		{
			return isClean;
		}
		
		
		
		public int read(byte[] buf)
				throws IOException
		{
			return read(buf, 0, buf.length);
		}
		
		public int read(byte[] buf, int offset, int len)
				throws IOException
		{
			int read = in.read(buf, offset, len);
			
			try
			{
				if (read > 0)
					partialScan(this, buf, offset, read);
			}
			catch(Exception e)
			{
				
			}
			return read;
		}
		
		public void close() throws IOException
		{
			try
			{
				finishScan(this);
			}
			catch(Exception e)
			{
				
			}
			IOUtil.close(in);
			IOUtil.close(ci);
		}
		public String toString()
		{
			return (filename != null ? filename +"," : "") + "Data size:" + size + ",Scan duration:" + duration + " millis" +",Is Clean:" + isClean + ".\n" + response;
		}

		@Override
		public long totalBytes() 
		{
			// TODO Auto-generated method stub
			return size;
		}
	}
	

  private String hostName;
  private int port;
  private int timeout;

  // "do not exceed StreamMaxLength as defined in clamd.conf, otherwise clamd will reply with INSTREAM size limit exceeded and close the connection."
  public static final int CHUNK_SIZE = 2048;
  public static final int DEFAULT_TIMEOUT = 500;

  /**
   * @param hostName The hostname of the server running clamav-daemon
   * @param port The port that clamav-daemon listens to(By default it might not listen to a port. Check your clamav configuration).
   * @param timeout zero means infinite timeout. Not a good idea, but will be accepted.
   */
  public ClamAVClient(String hostName, int port, int timeout)  {
    if (timeout < 0) {
      throw new IllegalArgumentException("Negative timeout value does not make sense.");
    }
    this.hostName = hostName;
    this.port = port;
    this.timeout = timeout;
  }

  public ClamAVClient(String hostName, int port) {
    this(hostName, port, DEFAULT_TIMEOUT);
  }

  /**
   * Run PING command to clamd to test it is responding.
   * 
   * @return true if the server responded with proper ping reply.
   */
  public boolean ping() throws IOException {
    try (Socket s = new Socket(hostName,port); OutputStream outs = s.getOutputStream()) {
      s.setSoTimeout(timeout);
      outs.write(asBytes("zPING\0"));
      outs.flush();
      byte[] b = new byte[4];
      s.getInputStream().read(b);
      return Arrays.equals(b, asBytes("PONG"));
    }
  }
  
  public ClamAVScanResult initScan(String fileName, InputStream is)
  	throws IOException
  {
	  long delta = System.currentTimeMillis();
	  Socket s =  new Socket(hostName,port);
	  s.setSoTimeout(timeout);
	  IOStreamInfo ci = new IOStreamInfo(s);
	  ci.os.write(asBytes("zINSTREAM\0"));
	  ci.os.flush();
	  ClamAVScanResult ret = new ClamAVScanResult(ci, is, fileName);
	  delta = System.currentTimeMillis() - delta;
	  ret.duration += delta;
	  return ret;
  }
  public static ClamAVScanResult partialScan(ClamAVScanResult cavsr, byte[] buffer, int offset, int len)
  	throws IOException
  {
	  long delta = System.currentTimeMillis(); 
	  
	  for(int i=0; i < len; i+=CHUNK_SIZE)
	  {
		  int chunkSize = CHUNK_SIZE;
		  if (i+CHUNK_SIZE > len)
		  {
			  chunkSize = len - i;
		  }
		  
		  
		  byte[] chunkSizeBuffer = ByteBuffer.allocate(4).putInt(chunkSize).array();
		  cavsr.ci.os.write(chunkSizeBuffer);
		  cavsr.ci.os.write(buffer, offset+i, chunkSize);
		  cavsr.size += chunkSize;
		  
	  }
	  delta = System.currentTimeMillis() - delta;
	  cavsr.duration += delta;
	  return cavsr;
  }
  
  public static ClamAVScanResult finishScan(ClamAVScanResult cavsr)
		  throws IOException
  {
	  long delta = System.currentTimeMillis();
	  cavsr.ci.os.write(new byte[]{0,0,0,0});
	  cavsr.ci.os.flush();

      // read reply
     
	  byte[] result = readAll(cavsr.ci.is);
	  cavsr.response = new String(result);
	  cavsr.ci.close();
	  delta = System.currentTimeMillis() - delta;
	  cavsr.duration += delta;
	  cavsr.isClean = isCleanReply(result);
	  return cavsr;
  }

  /**
   * Streams the given data to the server in chunks. The whole data is not kept in memory.
   * This method is preferred if you don't want to keep the data in memory, for instance by scanning a file on disk.
   * Since the parameter InputStream is not reset, you can not use the stream afterwards, as it will be left in a EOF-state.
   * If your goal is to scan some data, and then pass that data further, consider using {@link #scan(byte[]) scan(byte[] in)}.
   * <p>
   * Opens a socket and reads the reply. Parameter input stream is NOT closed. 
   * 
   * @param is data to scan. Not closed by this method!
   * @return server reply
   */
  public ClamAVScanResult scan(InputStream is)
		  throws IOException
  {
	  ClamAVScanResult  ret = new   ClamAVScanResult(null, is, null);
    try (Socket s = new Socket(hostName,port); OutputStream outs = new BufferedOutputStream(s.getOutputStream())) {
      s.setSoTimeout(timeout); 
      long delta = System.currentTimeMillis();
      // handshake
      outs.write(asBytes("zINSTREAM\0"));
      outs.flush();
      byte[] chunk = new byte[CHUNK_SIZE];

      // send data
      int read = ret.read(chunk);
      while (read >= 0) 
      {
    	ret.size += read;
        // The format of the chunk is: '<length><data>' where <length> is the size of the following data in bytes expressed as a 4 byte unsigned
        // integer in network byte order and <data> is the actual chunk. Streaming is terminated by sending a zero-length chunk.
        byte[] chunkSize = ByteBuffer.allocate(4).putInt(read).array();
        outs.write(chunkSize);
        outs.write(chunk, 0, read);
        read = is.read(chunk);
        
        
      }

      // terminate scan
      outs.write(new byte[]{0,0,0,0});
      outs.flush();

      // read reply
      try (InputStream clamIs = s.getInputStream()) 
      {
    	byte result[] = readAll(clamIs);
        ret.response = new String(result);
        delta = System.currentTimeMillis() - delta;
        ret.duration = delta;
        ret.isClean = isCleanReply(result);
        return ret;
      }
    } 
  }

  /**
   * Scans bytes for virus by passing the bytes to clamav
   * 
   * @param in data to scan
   * @return server reply
   **/
  public ClamAVScanResult scan(byte[] in) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(in);
    return scan(bis);
  }

  /**
   * Interpret the result from a  ClamAV scan, and determine if the result means the data is clean
   *
   * @param reply The reply from the server after scanning
   * @return true if no virus was found according to the clamd reply message
   */
  public static boolean isCleanReply(byte[] reply) {
    String r = new String(reply, StandardCharsets.US_ASCII);
    return (r.contains("OK") && !r.contains("FOUND"));
  }

  // byte conversion based on ASCII character set regardless of the current system locale
  private static byte[] asBytes(String s) {
    return s.getBytes(StandardCharsets.US_ASCII);
  }

  // reads all available bytes from the stream
  private static byte[] readAll(InputStream is) throws IOException {
    ByteArrayOutputStream tmp = new ByteArrayOutputStream();

    byte[] buf = new byte[2000];
    int read = is.read(buf);
    while (read > 0) {
      tmp.write(buf, 0, read);
      read = is.read(buf);
    }
    return tmp.toByteArray();
  }
  
  public static void main(String ...args)
  {
	  
	  try
	  {
		  int index = 0;
		  InetSocketAddressDAO hostInfo = new InetSocketAddressDAO(args[index++]);
		  ClamAVClient cavc = new ClamAVClient(hostInfo.getInetAddress(), hostInfo.getPort(), 0);
		  InputStream is = null;
		  byte buffer[] = new byte[CHUNK_SIZE];
		  for(; index < args.length; index++)
		  {
			  String filename = args[index];
			  is = null;
			  System.out.println("[" + index + "]Scanning:" + filename);
			  try
			  {
				  try
				  {
					  is = new URL(filename).openConnection().getInputStream();
				  }
				  catch(Exception e)
				  {
					  //e.printStackTrace();
					  is = new FileInputStream(filename);
				  }
				  
				  System.out.println(cavc.ping());
				  
				  ClamAVScanResult cavsr = cavc.initScan(filename, is);
				  while((cavsr.read(buffer)) >= 0);
				  cavsr.close();
				  System.out.println(cavsr);
			  }
			  catch(Exception e)
			  {
				  e.printStackTrace();
				  System.out.println("Processing error:" + e);
			  }
			  finally
			  {
				  IOUtil.close(is);
			  }

		  }
		  
	  }
	  catch(Exception e)
	  {
		  System.out.println("ClamAVClient host:port <list of file or urls>");
	  }
  }
}