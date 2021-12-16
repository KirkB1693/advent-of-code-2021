import java.util.*

fun main() {
    class Edge(val v1: String, val v2: String, val dist: Int)

    /** One vertex of the graph, complete with mappings to neighbouring vertices */
    class Vertex(val name: String) : Comparable<Vertex> {

        var dist = Int.MAX_VALUE  //MAX_VALUE assumed to be infinity
        var previous: Vertex? = null
        val neighbours = HashMap<Vertex, Int>()

        fun printPath() {
            if (this == previous) {
                print(name)
            } else if (previous == null) {
                print("$name(unreached)")
            } else {
                previous!!.printPath()
                print(" -> $name($dist)")
            }
        }

        fun altPrintPath() {
            if (this == previous) {
                print(0)
            } else if (previous == null) {
                print("$name(unreached)")
            } else {
                previous!!.altPrintPath()
                val nodeValue = dist - previous!!.dist
                print("$nodeValue")
            }
        }


        override fun compareTo(other: Vertex): Int {
            if (dist == other.dist) return name.compareTo(other.name)
            return dist.compareTo(other.dist)
        }

        override fun toString() = "($name, $dist)"
    }

    class Graph(
        edges: List<Edge>,
        val directed: Boolean,
        val showAllPaths: Boolean = false
    ) {
        //mapping of vertex names to Vertex objects, built from a set of Edges
        private val graph = HashMap<String, Vertex>(edges.size)

        init {
            //one pass to find all vertices
            for (e in edges) {
                if (!graph.containsKey(e.v1)) graph[e.v1] = Vertex(e.v1)
                if (!graph.containsKey(e.v2)) graph[e.v2] = Vertex(e.v2)
            }

            //another pass to set neighbouring vertices
            for (e in edges) {
                graph[e.v1]!!.neighbours[graph[e.v2]!!] = e.dist
                //also do this for an undirected graph if applicable
                if (!directed) graph[e.v2]!!.neighbours[graph[e.v1]!!] = e.dist
            }
        }

        /** Runs dijkstra using a specified source vertex */
        fun dijkstra(startName: String) {
            if (!graph.containsKey(startName)) {
                println("Graph doesn't contain start vertex '$startName'")
                return
            }
            val source = graph[startName]
            val q = TreeSet<Vertex>()

            //set-up vertices
            for (v in graph.values) {
                v.previous = if (v == source) source else null
                v.dist = if (v == source) 0 else Int.MAX_VALUE
                q.add(v)
            }

            dijkstra(q)
        }

        fun getDistanceTraveledToVertex(endName: String): Int {
            if (!graph.containsKey(endName)) {
                println("Graph doesn't contain end vertex '$endName'")
                return 0
            }
            return graph[endName]!!.dist
        }

        /** Implementation of dijkstra's algorithm using a binary heap */
        private fun dijkstra(q: TreeSet<Vertex>) {
            while (!q.isEmpty()) {
                //vertex with shortest distance (first iteration will return source)
                val u = q.pollFirst()
                //if distance is infinite we can ignore 'u' (and any other remaining vertices)
                //since they are unreachable
                if (u!!.dist == Int.MAX_VALUE) break

                //look at distances to each neighbour
                for (a in u.neighbours) {
                    val v = a.key //the neighbour in this iteration

                    val alternateDist = u.dist + a.value
                    if (alternateDist < v.dist) { //shorter path to neighbour found
                        q.remove(v)
                        v.dist = alternateDist
                        v.previous = u
                        q.add(v)
                    }
                }
            }
        }

        /** Prints a path from the source to the specified vertex */
        fun printPath(endName: String) {
            if (!graph.containsKey(endName)) {
                println("Graph doesn't contain end vertex '$endName'")
                return
            }
            print(if (directed) "Directed   : " else "Undirected : ")
            graph[endName]!!.printPath()
            println()
            if (showAllPaths) printAllPaths() else println()
        }

        /** Prints a path from the source to the specified vertex */
        fun printAltPath(endName: String) {
            if (!graph.containsKey(endName)) {
                println("Graph doesn't contain end vertex '$endName'")
                return
            }
            print("Alternate Path : ")
            graph[endName]!!.altPrintPath()
            println()
            if (showAllPaths) printAllPaths() else println()
        }

        /** Prints the path from the source to every vertex (output order is not guaranteed) */
        private fun printAllPaths() {
            for (v in graph.values) {
                v.printPath()
                println()
            }
            println()
        }
    }


    fun createCaveGraph(input: List<String>): Graph {
        val edges = mutableListOf<Edge>()
        for (y in input.indices) {
            for (x in input[0].indices) {
                val currentNode = "($x,$y)"
                val yAbove = y - 1
                if (yAbove in input.indices) {
                    val destinationNode = "($x,$yAbove)"
                    val distance = input[yAbove][x].toString().toInt()
                    edges.add(
                        Edge(
                            currentNode, destinationNode, distance
                        )
                    )
                    // add reverse edge since paths are bidirectional
                    val reverseDistance = input[y][x].toString().toInt()
                    edges.add(Edge(destinationNode, currentNode, reverseDistance))  // add  the reverse path
                }
                val xToLeft = x - 1
                if (xToLeft in input[0].indices) {
                    val destinationNode = "($xToLeft,$y)"
                    val distance = input[y][xToLeft].toString().toInt()
                    edges.add(
                        Edge(
                            currentNode, destinationNode, distance
                        )
                    )
                    // add reverse edge since paths are bidirectional
                    val reverseDistance = input[y][x].toString().toInt()
                    edges.add(Edge(destinationNode, currentNode, reverseDistance))  // add  the reverse path
                }
            }
        }

        return Graph(edges, true)
    }


    fun part1(input: List<String>): Int {
        val caveGraph = createCaveGraph(input)
        caveGraph.dijkstra("(0,0)")
        val x = input[0].indices.last
        val y = input.indices.last
        return caveGraph.getDistanceTraveledToVertex("($x,$y)")
    }

    fun createFullCaveGraph(input: List<String>, initialSize: Int): Graph {
        val edges = mutableListOf<Edge>()
        val initialGridSize = initialSize + 1
        val newGridSize = initialGridSize * 5
        for (a in 0..4) {
            for (b in 0..4) {
                for (y in input.indices) {
                    for (x in input[0].indices) {
                        val newX = x + (initialGridSize * a)
                        val newY = y + (initialGridSize * b)
                        val currentNode = "($newX,$newY)"
                        val yAbove = newY - 1
                        if (yAbove in 0 until newGridSize) {
                            val destinationNode = "($newX,$yAbove)"
                            var distance: Int = if (y - 1 in input.indices) {
                                input[y - 1][x].toString().toInt() + a + b
                            } else {
                                input[initialGridSize - 1][x].toString().toInt() + a + b
                            }
                            if (distance > 9) {
                                distance -= 9
                            }
                            edges.add(
                                Edge(
                                    currentNode, destinationNode, distance
                                )
                            )
                            // add reverse edge since paths are bidirectional
                            var reverseDistance = input[y][x].toString().toInt() + a + b
                            if (reverseDistance > 9) {
                                reverseDistance -= 9
                            }
                            edges.add(Edge(destinationNode, currentNode, reverseDistance))  // add  the reverse path
                        }
                        val xToLeft = newX - 1
                        if (xToLeft in 0 until newGridSize) {
                            val destinationNode = "($xToLeft,$newY)"
                            var distance: Int = if (x - 1 in input[0].indices) {
                                input[y][x - 1].toString().toInt() + a + b
                            } else {
                                input[y][initialGridSize - 1].toString().toInt() + a + b
                            }
                            if (distance > 9) {
                                distance -= 9
                            }
                            edges.add(
                                Edge(
                                    currentNode, destinationNode, distance
                                )
                            )
                            // add reverse edge since paths are bidirectional
                            var reverseDistance = input[y][x].toString().toInt() + a + b
                            if (reverseDistance > 9) {
                                reverseDistance -= 9
                            }
                            edges.add(Edge(destinationNode, currentNode, reverseDistance))  // add  the reverse path
                        }
                    }
                }
            }
        }
        return Graph(edges, true)
    }

    fun part2(input: List<String>): Int {
        var x = input[0].indices.last
        var y = input.indices.last
        val caveGraph = createFullCaveGraph(input, x)
        caveGraph.dijkstra("(0,0)")
        x = (x + 1) * 5 - 1
        y = (y + 1) * 5 - 1
        return caveGraph.getDistanceTraveledToVertex("($x,$y)")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
