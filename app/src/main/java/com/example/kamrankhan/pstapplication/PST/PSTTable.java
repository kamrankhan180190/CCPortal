
package com.example.kamrankhan.pstapplication.PST;

import com.example.kamrankhan.pstapplication.PST.*;
import com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem;
import com.example.kamrankhan.pstapplication.PST.PSTException;
import com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream;

import java.io.IOException;
import java.util.HashMap;



class PSTTable {

	protected String tableType;
	protected byte tableTypeByte;
	protected int hidUserRoot;

	protected Long[] arrayBlocks = null;
	
	// info from the b5 header
	protected int sizeOfItemKey;
	protected int sizeOfItemValue;
	protected int hidRoot;
	protected int numberOfKeys = 0;
	protected int numberOfIndexLevels = 0;

	private com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream in;
	
	//private int[][]	rgbiAlloc = null;
	//private byte[]	data = null;
	private HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> subNodeDescriptorItems = null;
	
	protected String description = "";
	
	protected PSTTable(com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream in, HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> subNodeDescriptorItems)
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		this.subNodeDescriptorItems = subNodeDescriptorItems;
		this.in = in;

		arrayBlocks = in.getBlockOffsets();

		// the next two bytes should be the table type (bSig)
		// 0xEC is HN (Heap-on-Node)
		in.seek(0);
		byte[] headdata = new byte[4];
		in.read(headdata);
		if ((int)headdata[2] != 0xffffffec) {
			//System.out.println(in.isEncrypted());
			com.example.kamrankhan.pstapplication.PST.PSTObject.decode(headdata);
			com.example.kamrankhan.pstapplication.PST.PSTObject.printHexFormatted(headdata, true);
			throw new com.example.kamrankhan.pstapplication.PST.PSTException("Unable to parse table, bad table type...");
		}

		tableTypeByte = headdata[3];
		switch ((int)tableTypeByte) {	// bClientSig
			case 0x7c:					// Table Context (TC/HN)
				tableType = "7c";
				break;
//			case 0x9c:
//				tableType = "9c";
//				valid = true;
//				break;
//			case 0xa5:
//				tableType = "a5";
//				valid = true;
//				break;
//			case 0xac:
//				tableType = "ac";
//				valid = true;
//				break;
//			case 0xFFFFFFb5:		// BTree-on-Heap (BTH)
//				tableType = "b5";
//				valid = true;
//				break;
			case 0xffffffbc:
				tableType = "bc";	// Property Context (PC/BTH)
				break;
			default:
				throw new com.example.kamrankhan.pstapplication.PST.PSTException("Unable to parse table, bad table type.  Unknown identifier: 0x"+Long.toHexString(headdata[3]));
		}
		

		hidUserRoot = (int)in.seekAndReadLong(4, 4);		// hidUserRoot
/*
		System.out.printf("Table %s: hidUserRoot 0x%08X\n", tableType, hidUserRoot);
/**/


		// all tables should have a BTHHEADER at hnid == 0x20
		NodeInfo headerNodeInfo = getNodeInfo(0x20);
		headerNodeInfo.in.seek(headerNodeInfo.startOffset);
		int headerByte = headerNodeInfo.in.read() &0xFF;
		if ( headerByte != 0xb5 ) {
			headerNodeInfo.in.seek(headerNodeInfo.startOffset);
			headerByte = headerNodeInfo.in.read() &0xFF;
			headerNodeInfo.in.seek(headerNodeInfo.startOffset);
			byte[] tmp = new byte[1024];
			headerNodeInfo.in.read(tmp);
			com.example.kamrankhan.pstapplication.PST.PSTObject.printHexFormatted(tmp, true);
			//System.out.println(PSTObject.compEnc[headerByte]);
			throw new com.example.kamrankhan.pstapplication.PST.PSTException("Unable to parse table, can't find BTHHEADER header information: "+headerByte);
		}
		
		sizeOfItemKey = (int)headerNodeInfo.in.read() & 0xFF;		// Size of key in key table
		sizeOfItemValue = (int)headerNodeInfo.in.read() & 0xFF;	// Size of value in key table

		numberOfIndexLevels = (int)headerNodeInfo.in.read() & 0xFF;
		if ( numberOfIndexLevels != 0 ) {
			// System.out.println(this.tableType);
			// System.out.printf("Table with %d index levels\n", numberOfIndexLevels);
		}
		//hidRoot = (int)PSTObject.convertLittleEndianBytesToLong(nodeInfo, 4, 8);	// hidRoot
		hidRoot = (int)headerNodeInfo.seekAndReadLong(4, 4);
		//System.out.println(hidRoot);
		//System.exit(0);
/*
		System.out.printf("Table %s: hidRoot 0x%08X\n", tableType, hidRoot);
/**/		
		description += "Table ("+tableType+")\n"+
			"hidUserRoot: "+hidUserRoot+" - 0x"+Long.toHexString(hidUserRoot)+"\n"+
			"Size Of Keys: "+sizeOfItemKey+" - 0x"+Long.toHexString(sizeOfItemKey)+"\n"+
			"Size Of Values: "+sizeOfItemValue+" - 0x"+Long.toHexString(sizeOfItemValue)+"\n"+
			"hidRoot: "+hidRoot+" - 0x"+Long.toHexString(hidRoot)+"\n";
	}


	protected void releaseRawData() {
		subNodeDescriptorItems = null;
	}

	
	/**
	 * get the number of items stored in this table.
	 * @return
	 */
	public int getRowCount() {
		return this.numberOfKeys;
	}

	class NodeInfo
	{
		int		startOffset;
		int		endOffset;
		//byte[]	data;
		com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream in;
		
		NodeInfo(int start, int end, com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream in)
           throws com.example.kamrankhan.pstapplication.PST.PSTException
       {
           if (start > end)
               throw new com.example.kamrankhan.pstapplication.PST.PSTException(String.format("Invalid NodeInfo parameters: start %1$d is greater than end %2$d", start, end));
			startOffset = start;
			endOffset = end;
			this.in = in;
			//this.data = data;
		}
		
		int length() {
			return endOffset - startOffset;
		}

		long seekAndReadLong(long offset, int length)
				throws IOException, com.example.kamrankhan.pstapplication.PST.PSTException
		{
			return this.in.seekAndReadLong(startOffset+offset, length);
		}
	}
	
	protected NodeInfo getNodeInfo(int hnid)
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{

		// Zero-length node?
		if ( hnid == 0 ) {
			return new NodeInfo(0, 0, this.in);
		}

		// Is it a subnode ID?
		if ( subNodeDescriptorItems != null &&
			 subNodeDescriptorItems.containsKey(hnid) )
		{
			com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem item = subNodeDescriptorItems.get(hnid);
			//byte[] data;
			NodeInfo subNodeInfo = null;

			try {
				//data = item.getData();
				com.example.kamrankhan.pstapplication.PST.PSTNodeInputStream subNodeIn = new PSTNodeInputStream(in.getPSTFile(), item);
				subNodeInfo = new NodeInfo(0, (int)subNodeIn.length(), subNodeIn);
			} catch (IOException e) {
				throw new com.example.kamrankhan.pstapplication.PST.PSTException(String.format("IOException reading subNode: 0x%08X", hnid));
			}

			//return new NodeInfo(0, data.length, data);
			return subNodeInfo;
		}

		if ( (hnid & 0x1F) != 0 ) {
			// Some kind of external node
			return null;
		}

		int whichBlock = (hnid >>> 16);
		if ( whichBlock > this.arrayBlocks.length ) {
			// Block doesn't exist!
			String err = String.format("getNodeInfo: block doesn't exist! hnid = 0x%08X\n", hnid);
			err += String.format("getNodeInfo: block doesn't exist! whichBlock = 0x%08X\n", whichBlock);
			err += "\n"+(this.arrayBlocks.length);
			throw new com.example.kamrankhan.pstapplication.PST.PSTException(err);
			//return null;
		}

		// A normal node in a local heap
		int index = (hnid & 0xFFFF) >> 5;
		int blockOffset = 0;
		if (whichBlock > 0) {
			blockOffset = arrayBlocks[whichBlock-1].intValue();
		}
		// Get offset of HN page map
		int iHeapNodePageMap = (int)in.seekAndReadLong(blockOffset, 2) + blockOffset;
		int cAlloc = (int)in.seekAndReadLong(iHeapNodePageMap, 2);
		if ( index >= cAlloc+1 ) {
			throw new PSTException(String.format("getNodeInfo: node index doesn't exist! nid = 0x%08X\n", hnid));
			//return null;
		}
		iHeapNodePageMap += (2 * index)+2;
		int start = (int)in.seekAndReadLong(iHeapNodePageMap, 2) + blockOffset;
		int end = (int)in.seekAndReadLong(iHeapNodePageMap + 2, 2) + blockOffset;

		NodeInfo out = new NodeInfo(start, end, in);
		return out;
	}

}
