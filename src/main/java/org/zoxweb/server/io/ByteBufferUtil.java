package org.zoxweb.server.io;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.HashMap;

import java.util.Map;
//import java.util.logging.Logger;


import org.zoxweb.shared.util.SharedUtil;
import org.zoxweb.shared.util.SimpleQueue;

public class ByteBufferUtil 
{
	
	private static final ByteBufferUtil SINGLETON = new ByteBufferUtil();
//	private static final String CLASS_NAME = "java.nio.CustomHeapByteBuffer";
//	private static final transient Logger log = Logger.getLogger(ByteBufferUtil.class.getName());
	
//	private Constructor<?> constructor = null;
//	private Method getInternalBuffer = null;
//	private Method limit = null;
//	private volatile long counter = 0;
	private Map<Integer, SimpleQueue<ByteBuffer>> cachedBuffers = new HashMap<Integer, SimpleQueue<ByteBuffer>>();
	
	
	public static final int DEFAULT_BUFFER_SIZE = 4096;
	public static final int CACHE_LIMIT = 512;
	
	private ByteBufferUtil()
	{	
//		try
//		{
//			//InternalBufferAccess iab = null;
//			CustomClassLoader cl = new CustomClassLoader(((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs());
//			Class<?> clazz = Class.forName(CLASS_NAME, true, cl);
//			constructor = clazz.getConstructor(byte[].class, int.class, int.class);
//			getInternalBuffer = clazz.getMethod("getInternalBuffer");
//			limit = clazz.getMethod("limit");
//			
//		}
//		catch(Throwable t)
//		{
//			System.err.println(CLASS_NAME + " NOT LOADED.");		
//		}
		
	}
	
	private void cache0(ByteBuffer bb)
	{
		synchronized(cachedBuffers)
		{
			if (bb != null)
			{
				
				SimpleQueue<ByteBuffer> sq = cachedBuffers.get(bb.capacity());
				if (sq == null)
				{
					sq = new SimpleQueue<ByteBuffer>(false);
					//System.out.println("new capacity:" + bb.capacity());
					cachedBuffers.put(bb.capacity(), sq);
				}
				if (sq.size() < CACHE_LIMIT)
				{	
					bb.clear();
					if(!sq.contains(bb))
					{
						sq.queue(bb);
					}
				}
			}
		}
	}
	
	
	private  ByteBuffer toByteBuffer0(byte[] buffer, int offset, int length, boolean copy)
	{
			
		ByteBuffer bb = null;
		SimpleQueue<ByteBuffer> sq = null;
		synchronized(cachedBuffers)
		{
			sq = cachedBuffers.get(length-offset);
			if (sq != null)
			{
				bb = sq.dequeue();
			}
		}
		
		
//		if (constructor != null)
//		{
//			try 
//			{
//			
//				if (bb == null)
//				{
//					if (buffer == null)
//					{
//						buffer = new byte[length-offset];
//						bb = (ByteBuffer) constructor.newInstance(buffer, offset, length);
//						bb.clear();
//					}
//					else
//						bb = (ByteBuffer) constructor.newInstance(buffer, offset, length);
//					log.info("["+ (counter++) + "]must create new buffer:" + bb.capacity() + " " + bb.getClass().getName());
//				}
//				else
//				{
//					bb.put(buffer, offset, length);
//				}
//				if (!copy)
//					bb.clear();				
//			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) 
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		else
		{
			if (bb == null)
			{
				bb = ByteBuffer.allocateDirect(length-offset);
				//log.info("["+ (counter++) + "]must create new buffer:" + bb.capacity() + " " + bb.getClass().getName());
			}
			if (copy)
			{
				bb.put(buffer, offset, length);
				bb.flip();
			}
			
		}
		
		return bb;
		
	}
	
//	private ByteBuffer toByteBuffer0(UByteArrayOutputStream ubaos)
//	{
//		//synchronized(ubaos)
//		{
//			return toByteBuffer0(ubaos.getInternalBuffer(), 0, ubaos.size(), true);
//		}
//	}
	
	
//	private ByteBuffer toByteBuffer1(byte[] buffer, int offset, int length)
//	{
//			
//		ByteBuffer bb = null;
//		
//		if (constructor != null)
//		{
//			try 
//			{
//				bb = (ByteBuffer) constructor.newInstance(buffer, offset, length);
//				return bb;
//				
//			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//					| InvocationTargetException e) 
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		
//		if (bb == null)
//		{
//			bb = ByteBuffer.allocate(length-offset);
//			bb.put(buffer, offset, length);
//			bb.flip();
//		}
//		
//		return bb;
//		
//	}
	
//	public static ByteBuffer toByteBuffer(UByteArrayOutputStream ubaos)
//	{
//		return SINGLETON.toByteBuffer0(ubaos);
//	}
//	
//	
//	
//	public static ByteBuffer toByteBuffer(byte[] buffer, int offset, int length)
//	{
//		return SINGLETON.toByteBuffer0(buffer, offset, length, true);
//	}
	
	public static void write(ByteChannel bc, UByteArrayOutputStream ubaos) throws IOException
	{
		
//		ByteBuffer bb = SINGLETON.toByteBuffer0(ubaos);
//		while(bb.hasRemaining())
//			bc.write(bb);
		
		write(bc, ubaos.getInternalBuffer(), 0, ubaos.size());
	}
	
	public static void write(ByteChannel bc, byte array[], int off, int len) throws IOException
	{
		SharedUtil.checkIfNulls("null byte channel", bc);
		if (off < 0)
		{
			throw new IllegalArgumentException("invalid offset " + off);
		}
		ByteBuffer bb = allocateByteBuffer(DEFAULT_BUFFER_SIZE);
		try
		{
			
			int end = off + len;
			if (end > array.length)
			{
				end = array.length;
			}
			
			for (int offset = off; offset < end;)
			{
				
				int length = offset+bb.capacity() > end ? end - offset : bb.capacity();
	
				bb.clear();
				bb.put(array, offset, length);
				offset+=length;
				write(bc, bb);
			}
		}
		finally
		{
			cache(bb);
		}
	}
	
	
	public static ByteBuffer allocateByteBuffer(int capacity)
	{
		ByteBuffer ret =  SINGLETON.toByteBuffer0(null, 0, capacity, false);
		//ret.clear();
		return ret;
	}
	
	
	public static ByteBuffer allocateByteBuffer(byte buffer[], int offset, int length, boolean copy)
	{
		return SINGLETON.toByteBuffer0(buffer, offset, length, copy);
	}
	
	
	
	public static void write(ByteChannel bc, ByteBuffer bb) throws IOException
	{
		bb.flip();
		while(bb.hasRemaining())
			bc.write(bb);
	}
	
//	public static void fastWrite(UByteArrayOutputStream ubaos, ByteBuffer bb) throws IOException
//	{
//		
////		if (bb.getClass().getName().equals(CLASS_NAME))
////		{
////			try
////			{
////				bb.flip();
////				byte[] buf = (byte[]) SINGLETON.getInternalBuffer.invoke(bb);
////				int size = (int) SINGLETON.limit.invoke(bb);
////				ubaos.write(buf, 0, size);
////			}
////			catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException  e)
////			{
////				e.printStackTrace();
////			}
////			
////		}
////		else 
//		{
//			write(ubaos, bb);
//		}
//		
//	}
	
	
	public static void write(UByteArrayOutputStream ubaos, ByteBuffer bb) throws IOException
	{
		bb.flip();
		for (int i = 0; i < bb.limit(); i++)
		{
			ubaos.write(bb.get());
		}
	}
	
	public static void cache(ByteBuffer bb)
	{
		SINGLETON.cache0(bb);
	}
	
}

