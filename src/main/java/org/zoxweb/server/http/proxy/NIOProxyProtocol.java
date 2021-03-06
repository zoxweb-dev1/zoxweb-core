package org.zoxweb.server.http.proxy;


import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;


import org.zoxweb.server.http.HTTPUtil;
import org.zoxweb.server.io.ByteBufferUtil;
import org.zoxweb.server.io.IOUtil;
import org.zoxweb.server.io.UByteArrayOutputStream;
import org.zoxweb.server.net.ChannelRelayTunnel;
import org.zoxweb.server.net.InetFilterRulesManager;
import org.zoxweb.server.net.NIOChannelCleaner;
import org.zoxweb.server.net.NIOSocket;
import org.zoxweb.server.net.NetUtil;
import org.zoxweb.server.net.ProtocolSessionFactory;
import org.zoxweb.server.net.ProtocolSessionProcessor;

import org.zoxweb.server.task.TaskUtil;
import org.zoxweb.shared.http.HTTPMessageConfig;
import org.zoxweb.shared.http.HTTPMessageConfigInterface;
import org.zoxweb.shared.http.HTTPHeaderName;
import org.zoxweb.shared.http.HTTPMethod;

import org.zoxweb.shared.http.HTTPVersion;
import org.zoxweb.shared.http.HTTPStatusCode;
import org.zoxweb.shared.net.InetSocketAddressDAO;
import org.zoxweb.shared.protocol.MessageStatus;
import org.zoxweb.shared.protocol.ProtocolDelimiter;
import org.zoxweb.shared.security.SecurityStatus;
import org.zoxweb.shared.util.NVPair;
import org.zoxweb.shared.util.SharedStringUtil;




public class NIOProxyProtocol 
extends ProtocolSessionProcessor
{
	private static boolean debug = false;
	private static final transient Logger log = Logger.getLogger(NIOProxyProtocol.class.getName());
	public static final String NAME = "ZWNIOProxy";
	
	
	
	
	private static class RequestInfo
	{
		HTTPMessageConfigInterface hmci = null;
		int payloadIndex = -1;
		int payloadSent = 0;
		InetSocketAddressDAO remoteAddress = null;
		boolean headerNotSent = true;
		
		RequestInfo(HTTPMessageConfigInterface hmci, UByteArrayOutputStream ubaos)
		{
			this.hmci = hmci;
			remoteAddress = HTTPUtil.parseHost(hmci.getURI());
			//if (hmci.getContentLength() > 0)
			{
				payloadIndex = ubaos.indexOf(ProtocolDelimiter.CRLFCRLF.getBytes());
				if (payloadIndex > 0)
				{
					payloadIndex += ProtocolDelimiter.CRLFCRLF.getBytes().length;
				}
				else
				{
					throw new IllegalArgumentException("invalid message");
				}
				
			}
		}
		
		
		public void writeHeader(SocketChannel remoteChannel, UByteArrayOutputStream requestRawBuffer)
			throws IOException
		{
			if (headerNotSent)
			{
				ByteBufferUtil.write(remoteChannel, HTTPUtil.formatRequest(hmci, true, null, HTTPHeaderName.PROXY_CONNECTION.getName()));
				headerNotSent = false;
				writePayload(remoteChannel, requestRawBuffer, payloadIndex);
				if (hmci.getContentLength()  < 0)
    			{
    				
    				requestRawBuffer.reset();
    			}
			}
		}
		
		public void writePayload(SocketChannel remoteChannel, UByteArrayOutputStream requestRawBuffer, int fromIndex) throws IOException
		{
			if (hmci.getContentLength() > 1 && !isRequestComplete())
			{
				int dataLength = requestRawBuffer.size() - fromIndex;
				if ((dataLength + payloadSent) > hmci.getContentLength())
				{
					dataLength = hmci.getContentLength() - payloadSent - fromIndex;
				}
				
				ByteBufferUtil.write(remoteChannel, requestRawBuffer.getInternalBuffer(), fromIndex, dataLength);
				payloadSent += dataLength;
				requestRawBuffer.shiftLeft(fromIndex+dataLength, 0);
			}
		}
		
		
		public boolean isRequestComplete()
		{
			return payloadSent == hmci.getContentLength();
		}
		
		
	}
	
	
	public static final ProtocolSessionFactory<NIOProxyProtocol> FACTORY = new ProtocolSessionFactory<NIOProxyProtocol>()
	{

		@Override
		public NIOProxyProtocol newInstance() {
			// TODO Auto-generated method stub
			return new NIOProxyProtocol();
		}
		
		public boolean isBlocking()
		{
			return false;
		}

	};
	
	
	
	
	private UByteArrayOutputStream requestBuffer = new UByteArrayOutputStream();
	
	private HTTPMessageConfigInterface requestMCCI = null;
	private InetSocketAddressDAO lastRemoteAddress = null;
	private SocketChannel remoteChannel = null;
	private SelectionKey  remoteChannelSK = null;
	private SocketChannel clientChannel = null;
	private SelectionKey  clientChannelSK = null;
	private ChannelRelayTunnel channelRelay = null;
	private RequestInfo requestInfo = null;
	
	
	//private int changeConnection = 0;
	//protected Set<SelectionKey> remoteSet = null;//new HashSet<SelectionKey>();
	private boolean ssl = false;
	
	
	
	
	private NIOProxyProtocol()
	{
		bBuffer =  ByteBufferUtil.allocateByteBuffer(getReadBufferSize());
	}
	
	
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public String getDescription() 
	{
		// TODO Auto-generated method stub
		return "Experimental http proxy";
	}

	@Override
	public void close() throws IOException
	{
		
		// TODO Auto-generated method stub
		IOUtil.close(clientChannel);
		//if (ssl)
		{
			if (channelRelay != null)
			{
				IOUtil.close(channelRelay);
			}
			else
			{
				IOUtil.close(remoteChannel);
			}
		}
		postOp();
	}
	

	@Override
	protected void processRead(SelectionKey key) 
	{
		try
    	{
			if (clientChannel == null)
			{
				clientChannel = (SocketChannel)key.channel();
				clientChannelSK = key;
			}
			
			
			int read = 0;
    		do
    		{
    			bBuffer.clear();
    			
    			read = ((SocketChannel)key.channel()).read(bBuffer);
    			if (read > 0)
    			{
    				if (ssl)
    				{
    					ByteBufferUtil.write(remoteChannel, bBuffer);
    				}
    				else
    				{

    					ByteBufferUtil.write(requestBuffer, bBuffer);
    					//log.info(new String(requestBuffer.getInternalBuffer(), 0, requestBuffer.size()));
    					tryToConnectRemote2(requestBuffer, read);

    					
    				}	
    			}

    		}
    		while(read > 0);
					
				
    	
    		if (read == -1)
    		{
    			if(debug)
    				log.info("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+Read:" + read);
    			
    			
    			
    			close();
//    			if (remoteChannel == null || !remoteChannel.isOpen())
//    			{
//    				if(debug)
//    					log.info("Will close the whole connection since remote it is NOT OPEN:" + key + " remote:" +remoteChannel);
//    				
//    				//
//    				getSelectorController().cancelSelectionKey(key);
//    			}
//    			else
//    			{
//    				getSelectorController().cancelSelectionKey(key);
//    				
//
//    				if (debug)
//    					log.info(key + ":" + key.isValid()+ " " + Thread.currentThread() + " " + TaskUtil.getDefaultTaskProcessor().availableExecutorThreads());
//    					
//    			}
    		}
			
    	}
    	catch(Exception e)
    	{
    		if(debug)
    			e.printStackTrace();
    		IOUtil.close(this);
    		
    	}
    	
    	//setSeletectable(true);

	}

	

	
	
	public static  HTTPMessageConfigInterface createErrorMSG(int a, String errorMsg, String url)
	{
		HTTPMessageConfigInterface hcc = createHeader(null, a);
		
		hcc.getHeaderParameters().add(new NVPair("Content-Type","text/html; charset=iso-8859-1"));
		String msg = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r"
		+ "<HTML><HEAD>\r"
		+ "<TITLE>" + hcc.getHTTPStatusCode().CODE + "</TITLE>\r"
		+ "</HEAD>\r"  // use css style sheet in htdocs
		+ "<BODY BGCOLOR=\"#FFFFFF\" TEXT=\"#000000\" LINK=\"#000080\" VLINK=\"#000080\" ALINK=\"#000080\">\r"
		+ "<h2 class=\"headline\">HTTP " + hcc.getHTTPStatusCode().CODE + " </h2>\r"
		+ "<HR size=\"4\">\r"
		+ "<p class=\"i30\">Your request for the following URL failed:</p>"
		+ "<p class=\"tiagtext\"><a href=\"" + url + "\">" + url + "</A> </p>\r"
		+ "<P class=\"i25\">Reason: " + errorMsg + "</P>"
		+ "<HR size=\"4\">\r"
		+ "<p class=\"i25\"><A HREF=\"http://www.zoxweb.com/\">ProxyNIO</A> HTTP Proxy, Version " + NAME
		+"</p>\r"
		//+"<p class=\"i25\"><A HREF=\"http://" + localhost + "/\">jHTTPp2 local website</A> <A HREF=\"http://" + localhost + "/" + server.WEB_CONFIG_FILE + "\">Configuration</A></p>"
		+ "</BODY></HTML>";
		hcc.setContent(SharedStringUtil.getBytes(msg));
		
		return hcc;
		
		
	}
	
	
	public static HTTPMessageConfigInterface createHeader(HTTPMessageConfigInterface hcc, int httpStatus)
	{
		
		if (hcc == null)
			hcc = HTTPMessageConfig.createAndInit(null, null, (HTTPMethod)null);
		
		HTTPStatusCode statusCode = HTTPStatusCode.statusByCode(httpStatus);
		
		if (statusCode == null)
			statusCode = HTTPStatusCode.INTERNAL_SERVER_ERROR;
		
		hcc.setHTTPStatusCode(statusCode);
		hcc.getHeaderParameters().add(new NVPair("Server", NAME));
		if (httpStatus == 501)
		{
			hcc.getHeaderParameters().add(new NVPair("Allow", "GET, HEAD, POST, PUT, DELETE, CONNECT, PATCH"));
		}
		hcc.getHeaderParameters().add(new NVPair("Cache-Control", "no-cache, must-revalidate"));
		hcc.getHeaderParameters().add(new NVPair("Connection","close"));
		return hcc;
	}
	
	



	
	@SuppressWarnings("unused")
	private void tryToConnectRemote(UByteArrayOutputStream requestRawBuffer, int lastRead) throws IOException
	{
		requestMCCI = HTTPUtil.parseRawHTTPRequest(requestRawBuffer, requestMCCI, false);
		if (requestMCCI != null)
		{
			
			MessageStatus ms = HTTPUtil.checkRequestStatus(requestMCCI);
			if(debug)
				log.info("MS:" + ms + " len " + requestMCCI.getContentLength() + " " + requestMCCI.getMethod() + ":" + (requestMCCI.getContent() != null ? requestMCCI.getContent().length : 0) + ":" + requestMCCI.getURI());
			switch(ms)
			{
			case COMPLETE:
				if (debug)
					log.info(new String(requestRawBuffer.toByteArray()));
				
				InetSocketAddressDAO remoteAddress = HTTPUtil.parseHost(requestMCCI.getURI());
				
				
				
				if (requestMCCI.getMethod() == HTTPMethod.CONNECT)
				{
					// this is an HTTPS connection
					
					ssl = true;
						
					if (NetUtil.validateRemoteAccess(getInetFilterRulesManager(), remoteAddress.getInetAddress(), remoteChannel) !=  SecurityStatus.ALLOW)
					{
						HTTPMessageConfigInterface hccError = createErrorMSG(403, "Access Denied", requestMCCI.getURI());
						
						ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
						close();
						return;	
						// we must reply with an error
					}
					
					if(remoteChannel != null && !NetUtil.areInetSocketAddressDAOEquals(remoteAddress, lastRemoteAddress))
					{
						if(debug)
							log.info("NOT supposed to happen");
						//IOUtil.close(remoteChannel);
						if (channelRelay != null)
						{
							// try to read any pending data
							// very very nasty bug
							channelRelay.processRead(remoteChannelSK);
							channelRelay.waitThenStopReading();
						}
						else
							getSelectorController().cancelSelectionKey(remoteChannelSK);
					}
					
					
					try
					{
						remoteChannel = SocketChannel.open((new InetSocketAddress(remoteAddress.getInetAddress(), remoteAddress.getPort())));
					}
					catch(Exception e)
					{
						
						HTTPMessageConfigInterface hccError = createErrorMSG(404, "Host Not Found", requestMCCI.getURI());
						
						ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
						close();
						return;	
					}
					
					
					
	    			requestRawBuffer.reset();
	    			
	    			requestRawBuffer.write(HTTPVersion.HTTP_1_1.getValue() + " 200 Connection established" + ProtocolDelimiter.CRLF);
	    			requestRawBuffer.write(HTTPHeaderName.PROXY_AGENT + ": " +getName() + ProtocolDelimiter.CRLFCRLF);
	    			ByteBufferUtil.write(clientChannel, requestRawBuffer);
	    			requestRawBuffer.reset();
	    			requestMCCI = null;
	    			if(debug)
	    				log.info(new String(requestRawBuffer.toByteArray()));
	    			
	    			
	    			remoteChannelSK = getSelectorController().register(NIOChannelCleaner.DEFAULT, remoteChannel, SelectionKey.OP_READ, new ChannelRelayTunnel(getReadBufferSize(), remoteChannel, clientChannel, clientChannelSK, false, getSelectorController()), FACTORY.isBlocking());

				}
				else
				{
					if (debug)
						log.info(new String(requestRawBuffer.toByteArray()));
					
					if (NetUtil.validateRemoteAccess(getInetFilterRulesManager(), remoteAddress.getInetAddress(), remoteChannel) !=  SecurityStatus.ALLOW)
					{
						HTTPMessageConfigInterface hccError = createErrorMSG(403, "Access Denied", requestMCCI.getURI());
					
						ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
						close();
						return;	
						// we must reply with an error
					}
					
					if(!NetUtil.areInetSocketAddressDAOEquals(remoteAddress, lastRemoteAddress))
					{
						
						//IOUtil.close(remoteChannel);
						if (channelRelay != null)
						{
							channelRelay.processRead(remoteChannelSK);
							channelRelay.waitThenStopReading();
							if(debug)
								log.info("THIS IS  supposed to happen RELAY STOP:" +lastRemoteAddress + "," + remoteAddress);
						}
						else if (remoteChannelSK != null)
						{
							if (debug)
								log.info("THIS IS  supposed to happen CANCEL READ");
							getSelectorController().cancelSelectionKey(remoteChannelSK);
						}
					}
					
					
					
					
					if(remoteChannelSK == null || !remoteChannelSK.isValid())
					{
						//log.info("ChangeConnection:" + changeConnection);
						try
						{
							remoteChannel = SocketChannel.open((new InetSocketAddress(remoteAddress.getInetAddress(), remoteAddress.getPort())));
							//remoteChannelSK = getSelectorController().register(remoteSet, remoteChannel, SelectionKey.OP_READ, new ChannelRelay(remoteChannel, clientChannel), FACTORY.isBlocking());
						}
						catch(Exception e)
						{
							if (debug)
							{
								log.info(new String(requestRawBuffer.toByteArray()));
								log.info("" + remoteAddress);
								e.printStackTrace();
							}
							HTTPMessageConfigInterface hccError = createErrorMSG(404, "Host Not Found", remoteAddress.getInetAddress() + ":" + remoteAddress.getPort());
							ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
							close();
							return;	
						}
					}
					
					if (requestMCCI.isMultiPartEncoding())
					{
						log.info("We have multi Econding");
					}
					ByteBufferUtil.write(remoteChannel, HTTPUtil.formatRequest(requestMCCI, true, requestRawBuffer, HTTPHeaderName.PROXY_CONNECTION.getName()));
				
					if(remoteChannelSK == null || !remoteChannelSK.isValid())
					{
						channelRelay = new ChannelRelayTunnel(getReadBufferSize(), remoteChannel, clientChannel, clientChannelSK, false, getSelectorController());
						remoteChannelSK = getSelectorController().register(NIOChannelCleaner.DEFAULT, remoteChannel, SelectionKey.OP_READ, channelRelay, FACTORY.isBlocking());
					}
					
	    			
	    			requestMCCI = null;
	    			requestRawBuffer.reset();
	    			lastRemoteAddress = remoteAddress;
	    			
				}

				break;
			case PARTIAL:
				if(!HTTPUtil.isContentComplete(requestMCCI) && lastRead == -1)
				{
					if (debug)
						log.info("WE HAVE AN INVALID REQUEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					throw new IOException("Invalid request");
				}
				else
				{
					if (debug)
						log.info("We have partial");
				}
				break;
				
			case INVALID:
				log.info("INVALID:" +requestMCCI);
			default:
				break;
			
			}
			
		}
		
	}
	
	
	
	private void tryToConnectRemote2(UByteArrayOutputStream requestRawBuffer, int lastRead) throws IOException
	{
		
		
		// new code 
		if (requestMCCI == null)
		{
			requestMCCI = HTTPUtil.parseRawHTTPRequest(requestRawBuffer, requestMCCI, true);
			if (requestMCCI != null)
				requestInfo = new RequestInfo(requestMCCI, requestRawBuffer);
		}
		
		if (requestInfo != null)
		{
			//InetSocketAddressDAO remoteAddress = HTTPUtil.parseHost(requestMCCI.getURI());
			if (requestMCCI.getMethod() == HTTPMethod.CONNECT)
			{
				
				ssl = true;
				if (NetUtil.validateRemoteAccess(getInetFilterRulesManager(), requestInfo.remoteAddress.getInetAddress(), remoteChannel) !=  SecurityStatus.ALLOW)
				{
					HTTPMessageConfigInterface hccError = createErrorMSG(403, "Access Denied", requestMCCI.getURI());
					
					ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
					close();
					return;	
					// we must reply with an error
				}
				if(remoteChannel != null && !NetUtil.areInetSocketAddressDAOEquals(requestInfo.remoteAddress, lastRemoteAddress))
				{
					if(debug)
						log.info("NOT supposed to happen");
					//IOUtil.close(remoteChannel);
					if (channelRelay != null)
					{
						// try to read any pending data
						// very very nasty bug
						channelRelay.processRead(remoteChannelSK);
						channelRelay.waitThenStopReading();
					}
					else
						getSelectorController().cancelSelectionKey(remoteChannelSK);
				}
				
				
				try
				{
					remoteChannel = SocketChannel.open((new InetSocketAddress(requestInfo.remoteAddress.getInetAddress(), requestInfo.remoteAddress.getPort())));
				}
				catch(Exception e)
				{
					
					HTTPMessageConfigInterface hccError = createErrorMSG(404, "Host Not Found", requestMCCI.getURI());
					
					ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
					close();
					return;	
				}
				
				
				
    			requestRawBuffer.reset();
    			
    			requestRawBuffer.write(HTTPVersion.HTTP_1_1.getValue() + " 200 Connection established" + ProtocolDelimiter.CRLF);
    			requestRawBuffer.write(HTTPHeaderName.PROXY_AGENT + ": " +getName() + ProtocolDelimiter.CRLFCRLF);
    			ByteBufferUtil.write(clientChannel, requestRawBuffer);
    			requestRawBuffer.reset();
    			requestMCCI = null;
    			if(debug)
    				log.info(new String(requestRawBuffer.toByteArray()));
    			
    			
    			remoteChannelSK = getSelectorController().register(NIOChannelCleaner.DEFAULT, remoteChannel, SelectionKey.OP_READ, new ChannelRelayTunnel(getReadBufferSize(), remoteChannel, clientChannel, clientChannelSK, false, getSelectorController()), FACTORY.isBlocking());
    			requestInfo = null;
    			
			}
			else
			{
				if (debug)
					log.info(new String(requestRawBuffer.toByteArray()));
				
				
				if (NetUtil.validateRemoteAccess(getInetFilterRulesManager(), requestInfo.remoteAddress.getInetAddress(), remoteChannel) !=  SecurityStatus.ALLOW)
				{
					HTTPMessageConfigInterface hccError = createErrorMSG(403, "Access Denied", requestMCCI.getURI());
				
					ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
					close();
					return;	
					// we must reply with an error
				}
				
				if(!NetUtil.areInetSocketAddressDAOEquals(requestInfo.remoteAddress, lastRemoteAddress))
				{
					
					//IOUtil.close(remoteChannel);
					if (channelRelay != null)
					{
						channelRelay.processRead(remoteChannelSK);
						channelRelay.waitThenStopReading();
						if(debug)
							log.info("THIS IS  supposed to happen RELAY STOP:" +lastRemoteAddress + "," + requestInfo.remoteAddress);
					}
					else if (remoteChannelSK != null)
					{
						if (debug)
							log.info("THIS IS  supposed to happen CANCEL READ");
						getSelectorController().cancelSelectionKey(remoteChannelSK);
					}
				}
				
				
				
				
				if(remoteChannelSK == null || !remoteChannelSK.isValid())
				{
					//log.info("ChangeConnection:" + changeConnection);
					try
					{
						remoteChannel = SocketChannel.open((new InetSocketAddress(requestInfo.remoteAddress.getInetAddress(), requestInfo.remoteAddress.getPort())));
						//remoteChannelSK = getSelectorController().register(remoteSet, remoteChannel, SelectionKey.OP_READ, new ChannelRelay(remoteChannel, clientChannel), FACTORY.isBlocking());
					}
					catch(Exception e)
					{
						if (debug)
						{
							log.info(new String(requestRawBuffer.toByteArray()));
							log.info("" + requestInfo.remoteAddress);
							e.printStackTrace();
						}
						HTTPMessageConfigInterface hccError = createErrorMSG(404, "Host Not Found", requestInfo.remoteAddress.getInetAddress() + ":" + requestInfo.remoteAddress.getPort());
						ByteBufferUtil.write(clientChannel, HTTPUtil.formatResponse(hccError, requestRawBuffer));
						close();
						return;	
					}
				}
				
				if (requestMCCI.isMultiPartEncoding())
				{
					log.info("We have multi Econding");
				}
				
			
				requestInfo.writeHeader(remoteChannel, requestRawBuffer);
				requestInfo.writePayload(remoteChannel, requestRawBuffer, 0);
				
//				if (requestInfo == null)
//				{
//					requestInfo = new RequestInfo(requestMCCI, requestRawBuffer);
//					ByteBufferUtil.write(remoteChannel, HTTPUtil.formatRequest(requestMCCI, true, null, HTTPHeaderName.PROXY_CONNECTION.getName()));
//					if (requestInfo.hmci.getContentLength() > 1)
//	    			{
//	    				ByteBufferUtil.write(remoteChannel, requestRawBuffer.getInternalBuffer(), requestInfo.payloadPendingIndex, requestRawBuffer.size() - requestInfo.payloadPendingIndex);
//	    				requestInfo.bytesSent += requestRawBuffer.size() - requestInfo.payloadPendingIndex;
//	    				requestRawBuffer.reset();
//	    				if (requestInfo.bytesSent == requestInfo.hmci.getContentLength())
//	    				{
//	    					requestInfo = null;
//	    	    			requestMCCI = null;
//	    	    			lastRemoteAddress = requestInfo.remoteAddress;
//	    					
//	    				}
//	    			}
//				}
				
				
				if(remoteChannelSK == null || !remoteChannelSK.isValid())
				{
					channelRelay = new ChannelRelayTunnel(getReadBufferSize(), remoteChannel, clientChannel, clientChannelSK, false, getSelectorController());
					remoteChannelSK = getSelectorController().register(NIOChannelCleaner.DEFAULT, remoteChannel, SelectionKey.OP_READ, channelRelay, FACTORY.isBlocking());
				}
				
				
				
    			
//				if (requestInfo != null && requestInfo.hmci.getContentLength()  > 1 && requestInfo.payloadSent != requestInfo.hmci.getContentLength() && requestRawBuffer.size() > 0)
//				{
//					ByteBufferUtil.write(remoteChannel, requestRawBuffer.getInternalBuffer(), 0, requestRawBuffer.size());
//    				requestInfo.payloadSent += requestRawBuffer.size();	
//				}
				
				
				lastRemoteAddress = requestInfo.remoteAddress;
				
				if (requestInfo != null && requestInfo.payloadSent == requestInfo.hmci.getContentLength() || requestInfo.hmci.getContentLength() < 1)
				{
					requestInfo = null;
	    			requestMCCI = null;
				}
				
//				lastRemoteAddress = requestInfo.remoteAddress;
//				requestRawBuffer.reset();
    			
			}
		}
		
		
		// code to be removed 
	
		
	}
	
	
	
	
	
	

	

	
	
	@SuppressWarnings("resource")
	public static void main(String ...args)
	{
		try
		{
			
			int index = 0;
			int port = Integer.parseInt(args[index++]);
			InetFilterRulesManager clientIFRM = null;
			TaskUtil.setThreadMultiplier(4);
			
			for(; index < args.length; index++)
			{
				if(clientIFRM == null)
				{
					clientIFRM = new InetFilterRulesManager();
				}
				try
				{
					clientIFRM.addInetFilterProp(args[index]);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			NIOSocket nsio = new NIOSocket(FACTORY, new InetSocketAddress(port), clientIFRM, null, TaskUtil.getDefaultTaskProcessor());
			nsio.setStatLogCounter(0);
			
			//nios.addSeverSocket(2401, new NIOTunnelFactory(new InetSocketAddressDAO("10.0.0.1:2401")));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			TaskUtil.getDefaultTaskScheduler().close();
			TaskUtil.getDefaultTaskProcessor().close();
		}
	}

	
	
	@SuppressWarnings("unused")
	private static void logInfo(HTTPMessageConfigInterface hmci)
	{
		if (hmci != null)
		{
			if(hmci.getHeaderParameters().get(HTTPHeaderName.CONTENT_LENGTH) != null)
			log.info(""+hmci.getContentLength() + ", " +hmci.getContentType());
		}
	}
	
}