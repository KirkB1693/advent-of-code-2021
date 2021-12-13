fun main() {
    val start = "start"
    val end = "end"

    fun isUpperCase(s : String) : Boolean
    {
        for (i in s.indices)
        {
            if (Character.isLowerCase(s[i]))
            {
                return false
            }
        }
        return true
    }

    class Graph<T> {
        val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()

        fun addEdge(sourceVertex: T, destinationVertex: T) {
            // Add edge to source vertex / node.
            adjacencyMap
                .computeIfAbsent(sourceVertex) { HashSet() }
                .add(destinationVertex)
            // Add edge to destination vertex / node.
            adjacencyMap
                .computeIfAbsent(destinationVertex) { HashSet() }
                .add(sourceVertex)
        }

        fun getDestinationsFromVertex(sourceVertex: T) : HashSet<T>? {
            return adjacencyMap[sourceVertex]
        }

        override fun toString(): String = StringBuffer().apply {
            for (key in adjacencyMap.keys) {
                append("$key -> ")
                append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
            }
        }.toString()
    }

    fun createCaveGraph(input: List<String>): Graph<String> {

        val caveGraph = Graph<String>()
        input.forEach { line ->
            val (cave1, cave2) = line.split('-')
            caveGraph.addEdge(cave1, cave2)
        }
        return caveGraph
    }

    fun destinationAllowed(currentRoute: MutableList<String>, destination: String, numberOfTimesAllowedToVisit: Int): Boolean {
        if (destination == start || destination == end) return false
        var count = 0
        currentRoute.forEach {
            if (it == destination) {count++}
        }
        return count<numberOfTimesAllowedToVisit
    }

    fun nodeLeadsToEnd(
        node: String,
        currentRoute: MutableList<String>,
        caveGraph: Graph<String>,
        routesList: MutableList<MutableList<String>>,
        numberOfTimesAllowedToVisit: Int
    ) {
        val tempNodeList = caveGraph.getDestinationsFromVertex(node)
        val baseNodeRoute = mutableListOf <String>()
        baseNodeRoute.addAll(currentRoute)
        if (tempNodeList != null) {
            tempNodeList.forEach { destination ->
                val route = mutableListOf<String>()
                route.addAll(baseNodeRoute)
                if (!(!isUpperCase(destination) && destination in route)) {
                    route.add(destination)
                    if (destination == end) {
                        val routeToAdd = mutableListOf<String>()
                        routeToAdd.addAll(route)
                        routesList.add(routeToAdd)
                        currentRoute.remove(end)
                    } else {
                        nodeLeadsToEnd(
                            destination,
                            route,
                            caveGraph,
                            routesList,
                            numberOfTimesAllowedToVisit
                        )
                    }
                    route.remove(destination)
                } else if (destinationAllowed(currentRoute, destination, numberOfTimesAllowedToVisit)) {
                    val maxVisits = 1
                    route.add(destination)
                    if (destination == end) {
                        val routeToAdd = mutableListOf<String>()
                        routeToAdd.addAll(route)
                        routesList.add(routeToAdd)
                        currentRoute.remove(end)
                    } else {
                        nodeLeadsToEnd(
                            destination,
                            route,
                            caveGraph,
                            routesList,
                            maxVisits
                        )
                    }
                    route.remove(destination)
                }
            }
        }
        currentRoute.remove(node)
    }

    fun getAllPossibleRoutes(caveGraph: Graph<String>, numberOfTimesAllowedToVisit: Int): MutableList<MutableList<String>> {
        val routesList = mutableListOf<MutableList<String>>()
        val baseRoute = mutableListOf<String>()
        val  tempList = caveGraph.getDestinationsFromVertex(start)
        baseRoute.add(start)
        if (tempList != null) {
            tempList.forEach{ node ->
                val route = mutableListOf<String>()
                route.addAll(baseRoute)
                if (node == end) {
                    route.add(end)
                    val tempRoute = mutableListOf<String>()
                    tempRoute.addAll(route)
                    routesList.add(tempRoute)
                    route.remove(end)
                } else {
                    route.add(node)
                    nodeLeadsToEnd(node, route, caveGraph, routesList, numberOfTimesAllowedToVisit)
                }
            }
        }
        return routesList
    }

    fun part1(input: List<String>): Int {
        val caveGraph = createCaveGraph(input)
        val traversalLists = getAllPossibleRoutes(caveGraph, 1)
        return traversalLists.size
    }

    fun part2(input: List<String>): Int {
        val caveGraph = createCaveGraph(input)
        val traversalLists = getAllPossibleRoutes(caveGraph, 2)
        return traversalLists.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println("Part 1 Answer : " + part1(input))
    println("Part 2 Answer : " + part2(input))
}
