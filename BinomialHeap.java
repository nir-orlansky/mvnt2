/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;
	
	public BinomialHeap() {
		this.size = 0;
		this.last= null;
		this.min = null;
	}
	
//	public BinomialHeap(HeapNode node) {
//		this.size = 1;
//		this.last = node;
//		this.min = node;
//	}
	
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{    
		HeapItem item = new HeapItem(key, info);
		this.InnerMeld(this.last, item.getNode());
		this.size ++;
		return item; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
//		//the min pointer points to the node before the minimal node so we could delete the next one more easily.
		return this.min.item; // should be replaced by student code
	} 

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		item.key = item.key - diff;
		HeapNode node = item.getNode();
		HeapNode parent = node.parent;
		while (parent != null && parent.item.key > node.item.key) {
			HeapItem tmp = node.item;
			node.item = parent.item;
			parent.item.node = node;
			parent.item = tmp;
			tmp.node = parent;
			node = parent;
			parent = node.parent;
		}
		if(parent == null && node.item.key < this.min.item.key) {
			this.min = node;
		}
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		this.InnerMeld(this.last, heap2.last);
		this.size += heap2.size;
		this.min = this.min.item.key < heap2.min.item.key ? this.min : heap2.min; //this.min = this.min if this.min < heap2.min else heap2.min
		heap2.min = null;
		heap2.last = null;
		heap2.size = 0;
		return; // should be replaced by student code   		
	}
	
	public void InnerMeld(HeapNode last1, HeapNode last2) 
	{
		HeapNode prev1 = last1;
		HeapNode curr1 = prev1.next;
		HeapNode curr2 = last2.next;
		last2.next=null;
		while(curr2 != null){
			curr2.parent = null;
			if(curr2.rank < curr1.rank) {
				HeapNode tmp = curr2.next;
				prev1.next = curr2;
				curr2.next = curr1;
				prev1 = curr2;
				curr2 = tmp;
			}
			else{
				if(curr2.rank == curr1.rank) {
					HeapNode tmp1 = curr1.next;
					HeapNode tmp2 = curr2.next;
					curr1 = this.link(curr1, curr2);
					prev1.next = curr1;
					curr1.next = tmp1;
					while(curr1.rank == curr1.next.rank && curr1 != curr1.next) {
						tmp1 = curr1.next.next;
						curr1 = this.link(curr1, curr1.next);
						prev1.next = curr1;
						curr1.next = tmp1;
					}
					curr2 = tmp2;
				}
				else {
					if(curr1.next.rank <= curr1.rank) {
						HeapNode tmp2 = curr2.next;
						curr2.next = curr1.next;
						curr1.next = curr2;
						prev1 = curr1;
						curr1 = curr2;
						curr2 = tmp2;
					}
					else {
						prev1 = curr1;
						curr1 = curr1.next;
					}
				}
				
			}
			
		}
		return;
	}
	
	public HeapNode link(HeapNode root, HeapNode node) {
		if(root.item.key > node.item.key) {
			HeapNode tmp = node;
			node = root;
			root = tmp;
		}
		if(root.child == null) {
			node.next = null;
		}
		else {
			node.next = root.child.next;
			root.child.next = node;
		}
		root.child = node;
		root.rank ++;
		node.parent = root;
		return root;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return 42; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return false; // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		public HeapNode(HeapItem item) {
			this.item = item;
			this.child = null;
			this.parent = null;
			this.next = this;
			this.rank = 0;
		}
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		public HeapItem(int key, String info) {
			this.key = key;
			this.info = info;
			this.node = new HeapNode(this);
		}
		
		public HeapNode getNode() {
			return this.node;
		}
	}

}
