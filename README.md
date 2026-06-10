# Smart Food Delivery System (WIA1002)

A high-performance, console-based food delivery simulation engine built in Java. This project replaces standard Java collections with custom-engineered data structures to optimize user management, real-time order processing, priority driver dispatch, and route pathfinding.

---

## 🛠️ System Architecture & Complexity Matrix

| Package / Module | Component | Data Structure / Algorithm | Time Complexity |
| --- | --- | --- | --- |
| **`dataStructure`** | Core Engine | Custom `Node`, `LinkedList`, `Stack`, `Queue`, `PriorityQueue` | Varied |
| **`user`** | Auth & Profiles | Dual `HashMap` (`usersById`, `usersByUsername`) | $O(1)$ Lookups |
| **`user`** | Restaurants | Custom `MyLinkedList<Restaurant>` | $O(n)$ Search |
| **`order`** | Shopping Cart | Custom `MyStack<OrderItem>` (Supports Undo) | $O(1)$ Push/Pop |
| **`order`** | Kitchen Pipeline | Custom `MyQueue<Order>` (FIFO processing) | $O(1)$ Enqueue |
| **`rider`** | Fleet Dispatch | Binary Min-Heap (`MyPriorityQueue<Rider>`) | $O(\log n)$ Dispatch |
| **`navigation`** | Route Optimization | Dijkstra's Algorithm + Sparse Graph (`MapEdge`) | $O((E + V) \log V)$ |
| **`search`** | Sorted Menu | Binary Search Tree (`BST`) via In-Order Traversal | $O(\log n)$ Average |
| **`search`** | Autocomplete Search | Prefix Tree (`Trie`) | $O(m)$ Word Length |

---

## 📂 Project Directory Structure

```text
Smart-Food-Delivery-System-WIA1002/
│
├── data/
│   ├── restaurants.csv       # Persistent restaurant data & menu items storage
│   └── users.txt             # Persistent pipe-delimited user profiles database
│
├── src/
│   └── main/
│       ├── dataStructure/    # Custom engineered low-level data structures
│       │   ├── MyLinkedList.java      # Generic singly linked list for linear sequences
│       │   ├── MyPriorityQueue.java   # Generic min-heap array/list for priority elements
│       │   ├── MyQueue.java           # Generic FIFO head-tail queue for streaming lists
│       │   ├── MyStack.java           # Generic LIFO stack for backtracking and cart states
│       │   └── Node.java              # The structural generic linked node template
│       │
│       ├── model/            # Plain Old Java Objects (POJOs) representing core entities
│       │   ├── FoodItem.java          # Represents a dish with name, price, and ID attributes
│       │   ├── Order.java             # Holds consolidated checkout information and state
│       │   ├── OrderItem.java         # Wraps a FoodItem reference with a selected quantity
│       │   ├── Restaurant.java        # Holds operational records and individual menus
│       │   └── User.java              # Encapsulates profile data and storage save hooks
│       │
│       ├── navigation/       # Graph topology mechanics for physical map routing
│       │   ├── CityGraph.java         # Graph wrapper containing Dijkstra route calculators
│       │   ├── LocationDistance.java  # State object holding unvisited node weight costs
│       │   ├── LocationNode.java      # Vertex element tracking label, indegree, and pointers
│       │   └── MapEdge.java           # Directed edge connector managing weight distances
│       │
│       ├── order/            # Subsystem pipeline handling shopping carts and kitchens
│       │   ├── OrderQueue.java        # FIFO engine sequencing kitchen orders
│       │   ├── OrderService.java      # Orchestrator syncing active checkout steps to histories
│       │   └── OrderStack.java        # LIFO buffer representing active customer carts
│       │
│       ├── rider/            # Logistics dispatcher package for delivery fleets
│       │   ├── DeliveryManager.java   # Controller matching orders to closest couriers
│       │   └── Rider.java             # Courier profile implementing Comparable priority sorting
│       │
│       ├── search/           # Tree data structure indexes for string search lookups
│       │   ├── BST.java               # Lexicographical tree keeping menus alphabetically sorted
│       │   ├── SearchService.java     # Dual-index manager keeping BST and Trie layers synchronized
│       │   ├── TreeNode.java          # Fundamental binary node holding children subtrees
│       │   └── Trie.java              # Character prefix tree handling instant menu searches
│       │
│       ├── user/             # Persistent controller managers handling systemic directories
│       │   ├── RestaurantManager.java # Logic layer for editing active restaurant links
│       │   └── UserManager.java       # High-performance auth engine indexing profiles via HashMaps
│       │
│       ├── AdminMenu.java    # Complete administrative terminal UI loop dashboard
│       ├── App.java          # System initialization gateway and main entry thread loop
│       └── UserMenu.java     # Front-facing consumer interactive portal interface
│
└── .gitignore                # Specifies intentionally untracked build/IDE files to ignore

```

---

## 🔄 Overall System Process Workflow

The lifecycle below outlines how data moves through the program's custom structures during a single runtime transaction:

```text
 1. SYSTEM INITIALIZATION
    └── App loads records from data/ files into memory.
    └── User profiles load into HashMaps; Restaurants parse into MyLinkedList.
    └── Food items index into the BST (alphabetical display) and Trie (instant lookup).

 2. CUSTOMER AUTHENTICATION & BROWSING
    └── User logs in via instantaneous O(1) HashMap username matching.
    └── Queries the menu: Trie handles predictive string searches character-by-character.

 3. CART MANAGEMENT (LIFO)
    └── Selected items wrap into OrderItems and push onto MyStack (cartStack).
    └── Mistakes are handled immediately by popping the top element off the stack.

 4. ORDER CONFIRMATION & PROCESSING (FIFO)
    └── Checkout moves items from the Stack into a finalized Order object.
    └── Order joins the back of MyQueue (OrderQueue) to process kitchen tasks chronologically.
    └── Simultaneously, the order indexes into the orderHistory HashMap for status updates.

 5. LOGISTICAL DISPATCH & ROUTING
    └── Kitchen marks food complete ──> Triggers DeliveryManager assignment.
    └── Program polls the absolute closest courier from the Min-Heap MyPriorityQueue.
    └── CityGraph executes Dijkstra's Algorithm, tracking path weights to generate the
        fastest sequence of nodes back-to-front for the rider's roadmap.

```

---

## ⚙️ Setup & Execution

1. **Compile the Project:**

```bash
javac -d bin src/main/**/*.java src/main/*.java

```

2. **Run the Application:**

```bash
java -cp bin main.App

```
