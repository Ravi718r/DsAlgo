package com.example.dsalgo.chatbot.data.model

enum class FilterCategory(val displayName: String, val keywords: List<String>) {
    DSA(
        "Data Structures & Algorithms",
        listOf(
            "array", "2d array", "matrix", "linked list", "singly linked list",
            "doubly linked list", "circular linked list", "stack", "queue",
            "priority queue", "deque", "tree", "binary tree", "binary search tree",
            "avl tree", "red black tree", "segment tree", "fenwick tree",
            "heap", "min heap", "max heap", "trie", "graph", "directed graph",
            "undirected graph", "weighted graph", "unweighted graph", "adjacency matrix",
            "adjacency list", "dfs", "bfs", "dijkstra", "floyd warshall", "kruskal",
            "prim", "topological sort", "cycle detection", "connected components",
            "articulation points", "bridges", "sorting", "bubble sort", "selection sort",
            "insertion sort", "merge sort", "quick sort", "heap sort", "counting sort",
            "radix sort", "bucket sort", "searching", "linear search", "binary search",
            "ternary search", "jump search", "exponential search", "interpolation search",
            "dynamic programming", "memoization", "tabulation", "recursion", "backtracking",
            "greedy algorithm", "divide and conquer", "graph algorithms", "hashing",
            "hash map", "hash table", "string algorithms", "pattern matching", "kmp",
            "rabin karp", "manacher", "sliding window", "two pointers", "bit manipulation",
            "disjoint set", "union find", "fibonacci", "knapsack", "matrix chain multiplication",
            "longest common subsequence", "longest increasing subsequence", "max subarray",
            "min coins", "trapping rain water", "stock buy sell", "graph traversal"
        )
    ),
    CN(
        "Computer Networks",
        listOf(
            "tcp", "udp", "ip", "ipv4", "ipv6", "http", "https", "ftp", "smtp",
            "pop3", "imap", "dns", "dhcp", "nat", "subnet", "subnet mask",
            "routing", "switching", "router", "switch", "hub", "bridge", "gateway",
            "osi", "tcp/ip", "ethernet", "lan", "wan", "vpn", "firewall",
            "load balancer", "cdn", "socket", "port", "protocol", "packet", "frame",
            "header", "payload", "ssl", "tls", "ipsec", "arp", "rarp", "icmp", "igmp",
            "routing protocols", "rip", "ospf", "bgp", "eigrp", "mac address",
            "ip address", "bandwidth", "latency", "throughput", "jitter", "congestion control",
            "flow control", "collision detection", "csma/cd", "csma/ca", "dhcp lease",
            "dns resolution", "tcp handshake", "udp characteristics", "network topologies",
            "star topology", "mesh topology", "bus topology", "ring topology"
        )
    ),
    OS(
        "Operating Systems",
        listOf(
            "process", "thread", "multithreading", "concurrency", "parallelism",
            "scheduling", "cpu scheduling", "fcfs", "sjf", "priority scheduling",
            "round robin", "multilevel queue", "multilevel feedback queue",
            "memory management", "virtual memory", "paging", "segmentation",
            "page replacement", "fifo", "lru", "optimal page replacement", "deadlock",
            "semaphore", "mutex", "locks", "condition variable", "synchronization",
            "race condition", "critical section", "ipc", "pipes", "message queues",
            "shared memory", "signals", "system call", "kernel", "monolithic kernel",
            "microkernel", "modules", "interrupt", "context switching", "file system",
            "disk scheduling", "fcfs disk scheduling", "sstf", "scan", "c-scan",
            "file allocation", "directory structure", "inode", "boot process", "linux",
            "windows", "unix", "android", "ios", "shell", "bash", "command line",
            "scheduler", "dispatcher", "swap space", "thrashing", "io management", "paging table"
        )
    ),
    OOP(
        "Object Oriented Programming",
        listOf(
            "class", "object", "inheritance", "single inheritance", "multiple inheritance",
            "polymorphism", "compile time polymorphism", "runtime polymorphism",
            "encapsulation", "abstraction", "interface", "abstract class", "method",
            "constructor", "destructor", "overloading", "overriding", "static", "final",
            "super", "this", "private", "public", "protected", "getter", "setter",
            "lambda", "functional programming", "solid principles", "design patterns",
            "singleton", "factory", "observer", "strategy", "adapter", "decorator",
            "mvc", "mvvm", "mvp", "dependency injection", "composition", "aggregation",
            "association", "instantiation", "virtual", "pure virtual", "friend function",
            "operator overloading", "templates", "generics", "exception handling", "interface segregation",
            "open closed principle", "liskov substitution principle", "command pattern",
            "builder pattern", "prototype pattern", "state pattern", "mediator pattern",
            "chain of responsibility", "bridge pattern", "flyweight pattern"
        )
    );

    companion object {
        fun detectCategory(text: String): FilterCategory? {
            val lowerText = text.lowercase()
            return values().find { category ->
                category.keywords.any { keyword ->
                    lowerText.contains(keyword.lowercase())
                }
            }
        }

        fun isRelevantToCategories(text: String, categories: Set<FilterCategory>): Boolean {
            if (categories.isEmpty()) return true
            val detectedCategory = detectCategory(text)
            return detectedCategory != null && categories.contains(detectedCategory)
        }
    }
}
