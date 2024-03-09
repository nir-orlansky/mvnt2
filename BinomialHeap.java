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
		/*
		Heap builder. Initialize a heap with the fields size, last, min.
		 */
		this.size = 0;
		this.last= null;
		this.min = null;
	}
	
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *
	 */
	public HeapItem insert(int key, String info) 
	{
		/*
		inserting new node to the heap using innerMeld. O(logn).
		 */
		HeapItem item = new HeapItem(key, info);
		if(this.size == 0){
			// if heap is empty, sets the item's node as first tree of the heap. O(1)
			this.min = item.getNode();
			this.last = item.getNode();
		}
		else {
			// melds the new item's node with the existing heap. updates the heap fields. O(logn) for inner meld.
			this.last = this.InnerMeld(this.last, item.getNode());
			this.min = key < this.min.item.key ? item.getNode() : this.min;
		}
		this.size ++;
		return item;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		/*
		finds prev of min in order to remove it. O(logn).
		Deletes the min value of the heap, which is a root of one of the trees. O(1)
		After removing the root of a tree, a new heap creates from it's children.
		Using innerMeld to meld the new heap to the original heap. O(logn).
		loops through the roots of the heap to update parents to null and find new min O(logn).
		time complexity O(logn).
		 */
		if(this.last == this.last.next){
			this.last = this.last.child;
		}
		else{
			HeapNode prev = this.last;
			while (prev.next != this.min) {
				prev = prev.next;
			}
			prev.next = this.min.next;
			this.min.next = null;
			if(this.last == this.min){
				this.last=prev;
			}
			if(this.min.child != null) {
				this.last = this.InnerMeld(this.last, this.min.child);
			}
		}
		this.size --;
		this.min = this.last;
		this.last.parent = null;
		HeapNode node = this.last.next;
		while (node != this.last){
			//loops through the heap roots, makes sure the roots parent is null and looks for the new min key.
			node.parent = null;
			if(node.item.key < this.min.item.key){
				this.min = node;
			}
			node = node.next;
		}
	}

	/**
	 * 
	 * Return the minimal HeapItem
	 *
	 */
	public HeapItem findMin()
	{
		return this.min.item;
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
		/*
		changes a node's key and bubbles it up the tree till heap is valid.
		The maximum changes can be as the tree's height which is smaller or equal to logn.
		Therefor the time complexity is O(logn).
		 */
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
		/*
		delets a node from heap by decreasing it's key to minimum and deleting minimum.
		O(logn) for decrease and for delete-min. Hence in total the time complexity is O(logn).
		 */
		int mini = this.findMin().key;
		this.decreaseKey(item, item.key - mini + 1);
		this.deleteMin();
	}

	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		/*
		gets a second heap. melding the two heaps using innerMeld (O(logn)).
		Makes the second heap unusable and updates the fields of the first heap to the new melded heap fields.O(1).
		In total time complexity O(logn).
		 */
		this.last = this.InnerMeld(this.last, heap2.last);
		this.size += heap2.size;
		this.min = this.min.item.key < heap2.min.item.key ? this.min : heap2.min; //this.min = this.min if this.min < heap2.min else heap2.min
		heap2.min = null;
		heap2.last = null;
		heap2.size = 0;

	}
	
	public HeapNode InnerMeld(HeapNode last1, HeapNode last2)
	{
		/*
		gets the last node of two heaps. Melding the second heap into the first.
		return the last tree of the new melded heap.
		the algorithm adds each tree in the second heap to the first one using link(O(1)) when needed. based on binary adding.
		The algorithm loops through the two heaps, and makes a total of 2logn iterations at most. when n is the size of the bigger heap.
		The inner loop updates the pointers of the outer loop.
		Time complexity O(logn).
		 */

		// keeping track of pointers in each heap.
		HeapNode prev1 = last1;
		HeapNode curr1 = prev1.next;
		HeapNode curr2 = last2.next;
		last2.next=null; //making the second heap not circular.
		while(curr2 != null){
			//loop through the second heap from smallest tree to biggest.
			curr2.parent = null;
			if(curr2.rank < curr1.rank) {
				// if the current tree's rank not in first heap, adding the tree between prev1 and curr1.
				HeapNode tmp = curr2.next;
				prev1.next = curr2;
				curr2.next = curr1;
				prev1 = curr2;
				curr2 = tmp;
			}
			else{
				if(curr2.rank == curr1.rank) {
					// ranks are the same, linking and inserting the linked tree to first heap.
					boolean onlytree = curr1 == prev1;
					HeapNode tmp1 = curr1.next;
					HeapNode tmp2 = curr2.next;
					curr1 = this.link(curr1, curr2);
					if(!onlytree){
						// prevents infinite loop in case the first heap contains only one tree.
						prev1.next = curr1;
						curr1.next = tmp1;
					}
					else{
						curr1.next = curr1;
					}
					while(curr1.rank == curr1.next.rank && curr1 != curr1.next) {
						// loop through the first heap until there are no two trees with the same rank. (no reminder in binary adding).
						tmp1 = curr1.next.next;
						boolean last2trees = curr1 == tmp1;
						curr1 = this.link(curr1, curr1.next);
						if (!last2trees){
							//prevnts infinite loop in case only two trees remain.
							prev1.next = curr1;
							curr1.next = tmp1;}
						else{
							curr1.next = curr1;
						}
					}
					if(curr1.rank > last1.rank){
						//update pointer of last node in first heap.
						last1 = curr1;
					}
					curr2 = tmp2;
				}
				else {
					// if rank of second tree is bigger than rank of first tree.
					if(curr1.next.rank <= curr1.rank) { //end of first heap.
						last1 = curr2;
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
		return last1;
	}
	
	public HeapNode link(HeapNode root, HeapNode node) {
		/*
		combining two tree of the same rank by changing pointers. O(1).
		 */
		if(root.item.key > node.item.key) {
			HeapNode tmp = node;
			node = root;
			root = tmp;
		}
		if(root.child == null) {
			node.next = node;
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
		return this.size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return this.size==0;
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		// using bitCount function returns the number of one-bits in the two’s complement binary representation of size. O(1).
		return(Integer.bitCount(this.size));
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
			// initialize the node to be circular.
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
			// each HeapItem creates it's own node.
			this.key = key;
			this.info = info;
			this.node = new HeapNode(this);
		}
		
		public HeapNode getNode() {
			return this.node;
		}
	}

}
