import java.util.*

fun main() {
    data class Node(
        val x: Int,
        val y: Int,
        var visited: Boolean = false,
        var distance: Int = Int.MAX_VALUE,
        var parent: Node? = null,
        var value: Int = 0
    ) {

        fun isSameLocation(node: Node): Boolean {
            return (node.x == this.x && node.y == this.y)
        }

        override fun toString() = "($x,$y)"
    }

    data class Edge(
        val initialNode: Node,
        val arrivalNode: Node
    ) {
        fun contains(node: Node): Boolean {
            return (initialNode.isSameLocation(node))
        }
    }

    data class Graph(
        val nodes: MutableList<Node>,
        val edges: MutableList<Edge>
    ) : Iterable<Node> {

        fun getStart(): Node {
            nodes.forEach {
                if (it.x == 0 && it.y == 0) {
                    it.distance = 0
                    return it
                }
            }
            return Node(0, 0)
        }

        fun getEnd(): Node {
            var tempNode = Node(0, 0)
            nodes.forEach {
                if (it.x >= tempNode.x && it.y >= tempNode.y) {
                    tempNode = it
                }
            }
            return tempNode
        }

        fun getEdgesFrom(node: Node): Collection<Edge> {
            val edgesFound = mutableListOf<Edge>()
            edges.forEach {
                if (it.contains(node)) {
                    edgesFound.add(it)
                }
            }
            return edgesFound
        }

        fun getUnvisitedNeighbors(node: Node): Collection<Node> {
            val unvisitedNeighbors = mutableListOf<Node>()
            val edgeList = getEdgesFrom(node)
            edgeList.forEach {
                nodes.forEach { node ->
                    if (node.isSameLocation(it.arrivalNode) && !node.visited) {
                        unvisitedNeighbors.add(node)
                    }
                }
            }
            return unvisitedNeighbors
        }

        fun getEdgeByNodes(from: Node, to: Node): Edge? {
            edges.forEach {
                if (it.initialNode.isSameLocation(from) && it.arrivalNode.isSameLocation(to)) {
                    return it
                }
            }
            return null
        }

        fun getNodeIndex(node: Node): Int {
            for (i in nodes.indices) {
                if (node.isSameLocation(nodes[i])) {
                    return i
                }
            }
            return -1
        }

        override fun iterator(): Iterator<Node> {
            return nodes.iterator()
        }
    }

    fun findMatchingNode(nodes: MutableList<Node>, x: Int, y: Int): Node {
        nodes.forEach {
            if (it.isSameLocation(Node(x, y))) {
                return it
            }
        }
        return Node(x, y)
    }

    fun createCaveGraph(input: List<String>): Graph {
        val map = mutableMapOf<Node, Int>()
        val nodes = mutableListOf<Node>()
        val edges = mutableListOf<Edge>()
        for (y in input.indices) {
            for (x in input[0].indices) {
                val currentNode = Node(x, y)
                nodes.add(currentNode)
                map[currentNode] = input[y][x].toString().toInt()
                currentNode.value = map[currentNode]!!
                if (y - 1 in input.indices) {
                    edges.add(
                        Edge(
                            currentNode,
                            findMatchingNode(nodes, x, y - 1)
                        )
                    )   // can add this edge because nodes above and to left will always have been added
                    edges.add(Edge(findMatchingNode(nodes, x, y - 1), currentNode))  // add  the reverse path
                }
                if (x - 1 in input[0].indices) {
                    edges.add(
                        Edge(
                            currentNode,
                            findMatchingNode(nodes, x - 1, y)
                        )
                    )   // can add this edge because nodes above and to left will always have been added
                    edges.add(Edge(findMatchingNode(nodes, x - 1, y), currentNode))  // add  the reverse path
                }
            }
        }

        return Graph(nodes, edges)
    }


    fun aStarSearch(graph: Graph) {
        val openList: PriorityQueue<Node> = PriorityQueue<Node> { a, b ->
            a.distance - b.distance
        }

        // Init all distances with infinity -- done at graph creation
        val start = graph.getStart()
        val end = graph.getEnd()
        // Distance to the root itself is zero
        start.distance = 0
        // Init queue with the root node
        openList.add(start)
        // Iterate over the priority queue until it is empty.
        while (!openList.isEmpty()) {
            val curNode = openList.remove()  // Fetch next closest node
            curNode.visited = true  // Mark as discovered
            // Iterate over unvisited neighbors
            val neighbors = graph.getUnvisitedNeighbors(curNode)
            for (neighbor in neighbors) {
                // Update minimal distance to neighbor
                // Note: distance between to adjacent node is constant
                val minDistance =
                    neighbor.distance.coerceAtMost(curNode.distance + neighbor.value)

                if (minDistance != neighbor.distance) {
                    neighbor.distance = minDistance
                    neighbor.parent = curNode
                    // Change queue priority of the neighbor

                }
                // Add neighbor to the queue for further visiting.
                if (!openList.contains(neighbor))
                    openList.add(neighbor)
            }
        }
        // Done ! At this point we just have to walk back from the end using the parent
        // If end does not have a parent, it means that it has not been found.
    }

    fun printPath(end: Node) {
        val sb = StringBuilder()
        val values = StringBuilder()
        sb.append(end.toString())
        values.append(end.value.toString())
        var parentNode = end.parent
        while (parentNode != null) {
            sb.insert(0, ", ")
            sb.insert(0, parentNode.toString())
            values.insert(0, ", ")
            values.insert(0, parentNode.value.toString())
            parentNode = parentNode.parent
        }
        println(sb.toString())
        println(values.toString())
    }

    fun part1(input: List<String>): Int {
        val caveGraph = createCaveGraph(input)
        aStarSearch(caveGraph)
        val end = caveGraph.getEnd()
        return end.distance
    }

    fun createFullCaveGraph(initialCaveGraph: Graph): Graph {
        val initialNodes = initialCaveGraph.nodes
        val end = initialCaveGraph.getEnd()
        val nodes = initialNodes.toMutableList()
        val initialEdges = initialCaveGraph.edges
        val edges = initialEdges.toMutableList()
        val gridSize = end.x + 1
        val newGridSize = gridSize * 5
        for (x in 0..4) {
            for (y in 0..4) {
                // expand nodes
                if (!(x == 0 && y == 0)) {
                    print("Assigning values for grid copy ($x,$y)")
                    initialNodes.forEach {
                        // add nodes
                        val newX = it.x + (gridSize * x)
                        val newY = it.y + (gridSize * y)
                        var newValue = it.value + x + y
                        if (newValue > 9) {
                            newValue -= 9
                        }
                        val newNode = Node(newX, newY, value = newValue)
                        nodes.add(newNode)

                        if (newY - 1 in 0 until newGridSize) {
                            edges.add(
                                Edge(
                                    newNode,
                                    findMatchingNode(nodes, newX, newY - 1)
                                )
                            )   // can add this edge because nodes above and to left will always have been added
                            edges.add(Edge(findMatchingNode(nodes, newX, newY - 1), newNode))  // add  the reverse path
                        }
                        if (newX - 1 in 0 until newGridSize) {
                            edges.add(
                                Edge(
                                    newNode,
                                    findMatchingNode(nodes, newX - 1, newY)
                                )
                            )   // can add this edge because nodes above and to left will always have been added
                            edges.add(Edge(findMatchingNode(nodes, newX - 1, newY), newNode))  // add  the reverse path
                        }

                    }
                }
                println()
            }
        }
        return Graph(nodes, edges)
    }

    fun part2(input: List<String>): Int {
        val initialCaveGraph = createCaveGraph(input)
        val trueCaveGraph = createFullCaveGraph(initialCaveGraph)
        aStarSearch(trueCaveGraph)
        val end = trueCaveGraph.getEnd()
        return end.distance
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
