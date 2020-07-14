
package com.example.kamrankhan.pstapplication.PST;
import com.example.kamrankhan.pstapplication.PST.*;
import com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode;
import com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem;
import com.example.kamrankhan.pstapplication.PST.PSTException;

import java.io.*;
import java.util.*;



public class PSTFolder extends PSTObject {
	
	/**
	 * a constructor for the rest of us...
	 * @param theFile
	 * @param descriptorIndexNode
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	PSTFolder(com.example.kamrankhan.pstapplication.PST.PSTFile theFile, com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode descriptorIndexNode)
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		super(theFile, descriptorIndexNode);
	}
	
	/**
	 * For pre-populating a folder object with values.
	 * Not recommended for use outside this library
	 * @param theFile
	 * @param folderIndexNode
	 * @param table
	 */
	PSTFolder(com.example.kamrankhan.pstapplication.PST.PSTFile theFile, com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode folderIndexNode, PSTTableBC table, HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> localDescriptorItems) {
		super(theFile, folderIndexNode, table, localDescriptorItems);
	}
	
	/**
	 * get all of the sub folders...
	 * there are not usually thousands, so we just do it in one big operation.
	 * @return all of the subfolders
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	public Vector<PSTFolder> getSubFolders()
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		initSubfoldersTable();
		Vector<PSTFolder> output = new Vector<PSTFolder>();
		if (this.hasSubfolders()) {
			try {
				List<HashMap<Integer, PSTTable7CItem>> itemMapSet = subfoldersTable.getItems();
				for (HashMap<Integer, PSTTable7CItem> itemMap : itemMapSet) {
					PSTTable7CItem item = itemMap.get(26610);
					PSTFolder folder = (PSTFolder)PSTObject.detectAndLoadPSTObject(pstFile, item.entryValueReference);
					output.add(folder);
				}
			} catch (com.example.kamrankhan.pstapplication.PST.PSTException err) {
				// hierachy node doesn't exist
				throw new com.example.kamrankhan.pstapplication.PST.PSTException("Can't get child folders for folder "+this.getDisplayName()+"("+this.getDescriptorNodeId()+") child count: "+this.getContentCount()+ " - "+err.toString());
			}
		}
		// try and get subfolders?
		return output;
	}

	private void initSubfoldersTable()
			throws IOException, com.example.kamrankhan.pstapplication.PST.PSTException
	{
		if (subfoldersTable != null) {
			return;
		}

		if (this.hasSubfolders()) {
			long folderDescriptorIndex = this.descriptorIndexNode.descriptorIdentifier + 11;
			try {
				com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode folderDescriptor = this.pstFile.getDescriptorIndexNode(folderDescriptorIndex);
				HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> tmp = null;
				if (folderDescriptor.localDescriptorsOffsetIndexIdentifier > 0) {
					//tmp = new PSTDescriptor(pstFile, folderDescriptor.localDescriptorsOffsetIndexIdentifier).getChildren();
					tmp = pstFile.getPSTDescriptorItems(folderDescriptor.localDescriptorsOffsetIndexIdentifier);
				}
				subfoldersTable = new PSTTable7C(new PSTNodeInputStream(pstFile, pstFile.getOffsetIndexNode(folderDescriptor.dataOffsetIndexIdentifier)), tmp);
			} catch (com.example.kamrankhan.pstapplication.PST.PSTException err) {
				// hierachy node doesn't exist
				throw new com.example.kamrankhan.pstapplication.PST.PSTException("Can't get child folders for folder "+this.getDisplayName()+"("+this.getDescriptorNodeId()+") child count: "+this.getContentCount()+ " - "+err.toString());
			}
		}

	}

	/**
	 * internal vars for the tracking of things..
	 */
	private int currentEmailIndex = 0;
	private LinkedHashSet<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode> otherItems = null;

	private PSTTable7C emailsTable = null;
	private LinkedList<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode> fallbackEmailsTable = null;
	private PSTTable7C subfoldersTable = null;
	
	/**
	 * this method goes through all of the children and sorts them into one of the three hash sets.
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	private void initEmailsTable()
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		if (this.emailsTable != null || this.fallbackEmailsTable != null) {
			return;
		}

		// some folder types don't have children:
		if (this.getNodeType() == PSTObject.NID_TYPE_SEARCH_FOLDER) {
			return;
		}

		try {
			long folderDescriptorIndex = this.descriptorIndexNode.descriptorIdentifier + 12; // +12 lists emails! :D
			com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode folderDescriptor = this.pstFile.getDescriptorIndexNode(folderDescriptorIndex);
			HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> tmp = null;
			if (folderDescriptor.localDescriptorsOffsetIndexIdentifier > 0) {
				//tmp = new PSTDescriptor(pstFile, folderDescriptor.localDescriptorsOffsetIndexIdentifier).getChildren();
				tmp = pstFile.getPSTDescriptorItems(folderDescriptor.localDescriptorsOffsetIndexIdentifier);
			}
			//PSTTable7CForFolder folderDescriptorTable = new PSTTable7CForFolder(folderDescriptor.dataBlock.data, folderDescriptor.dataBlock.blockOffsets,tmp, 0x67F2);
			emailsTable = new PSTTable7C(
					new PSTNodeInputStream(pstFile, pstFile.getOffsetIndexNode(folderDescriptor.dataOffsetIndexIdentifier)),
					tmp,
					0x67F2
			);
		} catch (Exception err) {

			// here we have to attempt to fallback onto the children as listed by the descriptor b-tree
			LinkedHashMap<Integer, LinkedList<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode>> tree = this.pstFile.getChildDescriptorTree();

			fallbackEmailsTable = new LinkedList<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode>();
			LinkedList<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode> allChildren = tree.get(this.getDescriptorNode().descriptorIdentifier);

			if (allChildren != null) {
               // quickly go through and remove those entries that are not messages!
               for (com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode node : allChildren) {
                   if (node != null && PSTObject.getNodeType(node.descriptorIdentifier) == PSTObject.NID_TYPE_NORMAL_MESSAGE) {
                       fallbackEmailsTable.add(node);
                   }
               }
			}

			System.err.println(
					"Can't get children for folder "+
					this.getDisplayName()+
					"("+this.getDescriptorNodeId()+") child count: "+
					this.getContentCount()+ " - "+
					err.toString()+ ", using alternate child tree with " + fallbackEmailsTable.size()+" items");
		}
	}
	
	/**
	 * get some children from the folder
	 * This is implemented as a cursor of sorts, as there could be thousands
	 * and that is just too many to process at once.
	 * @param numberToReturn
	 * @return bunch of children in this folder
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	public Vector<PSTObject> getChildren(int numberToReturn)
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		initEmailsTable();

		Vector<PSTObject> output = new Vector<PSTObject>();
		if (emailsTable != null) {
			List<HashMap<Integer, PSTTable7CItem>> rows = this.emailsTable.getItems(currentEmailIndex, numberToReturn);

			for (int x = 0; x < rows.size(); x++) {
				if (this.currentEmailIndex >= this.getContentCount())
				{
					// no more!
					break;
				}
				// get the emails from the rows
				PSTTable7CItem emailRow = rows.get(x).get(0x67F2);
				com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode childDescriptor = pstFile.getDescriptorIndexNode(emailRow.entryValueReference);
				PSTObject child = PSTObject.detectAndLoadPSTObject(pstFile, childDescriptor);
				output.add(child);
				currentEmailIndex++;
			}
		} else if (fallbackEmailsTable != null) {
			// we use the fallback
			ListIterator<com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode> iterator = this.fallbackEmailsTable.listIterator(currentEmailIndex);
			for (int x = 0; x < numberToReturn; x++) {
				if (this.currentEmailIndex >= this.getContentCount())
				{
					// no more!
					break;
				}
				com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode childDescriptor = iterator.next();
				PSTObject child = PSTObject.detectAndLoadPSTObject(pstFile, childDescriptor);
				output.add(child);
				currentEmailIndex++;
			}
		}


		return output;
	}

	public LinkedList<Integer> getChildDescriptorNodes() throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException {
		initEmailsTable();
		if (this.emailsTable == null) {
			return new LinkedList<Integer>();
		}
		LinkedList<Integer> output = new LinkedList<Integer>();
		List<HashMap<Integer, PSTTable7CItem>> rows = this.emailsTable.getItems();
		for (HashMap<Integer, PSTTable7CItem> row : rows) {
			// get the emails from the rows
			if (this.currentEmailIndex == this.getContentCount())
			{
				// no more!
				break;
			}
			PSTTable7CItem emailRow = row.get(0x67F2);
			if (emailRow.entryValueReference == 0) {
				break;
			}
			output.add(emailRow.entryValueReference);
		}
		return output;
	}


	
	/**
	 * Get the next child of this folder
	 * As there could be thousands of emails, we have these kind of cursor operations
	 * @return the next email in the folder or null if at the end of the folder
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	public PSTObject getNextChild()
		throws com.example.kamrankhan.pstapplication.PST.PSTException, IOException
	{
		initEmailsTable();

		if (this.emailsTable != null) {
			List<HashMap<Integer, PSTTable7CItem>> rows = this.emailsTable.getItems(currentEmailIndex, 1);

			if (this.currentEmailIndex == this.getContentCount())
			{
				// no more!
				return null;
			}
			// get the emails from the rows
			PSTTable7CItem emailRow = rows.get(0).get(0x67F2);
			com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode childDescriptor = pstFile.getDescriptorIndexNode(emailRow.entryValueReference);
			PSTObject child = PSTObject.detectAndLoadPSTObject(pstFile, childDescriptor);
			currentEmailIndex++;

			return child;
		} else if (this.fallbackEmailsTable != null) {
			if (this.currentEmailIndex >= this.getContentCount() || this.currentEmailIndex >= this.fallbackEmailsTable.size())
			{
				// no more!
				return null;
			}
			// get the emails from the rows
			DescriptorIndexNode childDescriptor = fallbackEmailsTable.get(currentEmailIndex);
			PSTObject child = PSTObject.detectAndLoadPSTObject(pstFile, childDescriptor);
			currentEmailIndex++;
			return child;
		}
		return null;
	}
	
	/**
	 * move the internal folder cursor to the desired position
	 * position 0 is before the first record.
	 * @param newIndex
	 */
	public void moveChildCursorTo(int newIndex)
			throws IOException, com.example.kamrankhan.pstapplication.PST.PSTException
	{
		initEmailsTable();

		if (newIndex < 1) {
			currentEmailIndex = 0;
			return;
		}
		if (newIndex > this.getContentCount()) {
			newIndex = this.getContentCount();
		}
		currentEmailIndex = newIndex;
	}
	
	/**
	 * the number of child folders in this folder
	 * @return number of subfolders as counted
	 * @throws IOException
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 */
	public int getSubFolderCount()
		throws IOException, com.example.kamrankhan.pstapplication.PST.PSTException
	{
		this.initSubfoldersTable();
		if (this.subfoldersTable != null)
		    return this.subfoldersTable.getRowCount();
		else
		    return 0;
	}
	
	/**
	 * the number of emails in this folder
	 * this is the count of emails made by the library and will therefore should be more accurate than getContentCount
	 * @return number of emails in this folder (as counted)
	 * @throws IOException
	 * @throws PSTException
	public int getEmailCount()
		throws IOException, PSTException
	{
		this.initEmailsTable();
		return this.emailsTable.getRowCount();
	}
	 */

	
	public int getFolderType() {
		return this.getIntItem(0x3601);
	}
	
	/**
	 * the number of emails in this folder
	 * this is as reported by the PST file, for a number calculated by the library use getEmailCount
	 * @return number of items as reported by PST File
	 */
	public int getContentCount() {
		return this.getIntItem(0x3602);
	}

	/**
	 * Amount of unread content items Integer 32-bit signed
	 */
	public int getUnreadCount() {
		return this.getIntItem(0x3603);
	}
	
	/**
	 * does this folder have subfolders
	 * once again, read from the PST, use getSubFolderCount if you want to know what the library makes of it all
	 * @return has subfolders as reported by the PST File
	 */
	public boolean hasSubfolders() {
		return (this.getIntItem(0x360a) != 0);
	}
	
	public String getContainerClass() {
		return this.getStringItem(0x3613);
	}
	
	public int getAssociateContentCount() {
		return this.getIntItem(0x3617);
	}
	
	/**
	 * Container flags Integer 32-bit signed
	 */
	public int getContainerFlags() {
		return this.getIntItem(0x3600);
	}
	
}
